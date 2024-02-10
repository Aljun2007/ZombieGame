package com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal;

import com.aljun.zombiegame.work.keyset.ConfigKeySets;
import com.aljun.zombiegame.work.datamanager.datamanager.ConfigDataManager;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ZombieMeleeAttackGoal extends AbstractZombieAbilityGoal {
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected int ticksUntilNextPathRecalculation;
    protected int ticksUntilNextAttack;
    protected long lastCanUseCheck;
    private boolean startBuilding = false;
    private long startBuildingTime = -1000L;

    public ZombieMeleeAttackGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean isStartBuilding() {
        return startBuilding;
    }

    private boolean canStartAttack() {
        return true;
    }

    @Override
    public boolean canBeUsed() {
        if (this.mainGoal.zombieBowAttackGoal.isUsable) {
            if (this.mainGoal.zombieBowAttackGoal.goal.canBeUsed()) {
                return false;
            }
        }
        if (this.mainGoal.zombieCrossBowAttackGoal.isUsable) {
            if (this.mainGoal.zombieCrossBowAttackGoal.goal.canBeUsed()) {
                return false;
            }
        }
        if (this.canStartAttack()) {
            if (this.mainGoal.canAttack) {
                long i = this.zombie.level().getGameTime();
                if (i - this.lastCanUseCheck < 20L) {
                    return false;
                } else {
                    this.lastCanUseCheck = i;
                    LivingEntity livingentity = this.zombie.getTarget();
                    if (livingentity == null) {
                        return false;
                    } else return livingentity.isAlive();
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mainGoal.zombieBowAttackGoal.isUsable) {
            if (this.mainGoal.zombieBowAttackGoal.goal.canBeUsed()) {
                return false;
            }
        }
        if (this.mainGoal.zombieCrossBowAttackGoal.isUsable) {
            if (this.mainGoal.zombieCrossBowAttackGoal.goal.canBeUsed()) {
                return false;
            }
        }
        if (this.canStartAttack()) {
            if (this.mainGoal.canAttack) {
                LivingEntity livingentity = this.zombie.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else if (!this.zombie.isWithinRestriction(livingentity.blockPosition())) {
                    return false;
                } else {
                    return !(livingentity instanceof Player)
                           || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void start() {
        this.zombie.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        LivingEntity livingentity = this.zombie.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.zombie.setTarget(null);
        }

        this.zombie.setAggressive(false);
        this.zombie.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {

        if (this.zombie.getTarget() != null) {
            if (!ZombieUtils.canBeAttack(this.zombie.getTarget())) {
                this.zombie.setTarget(null);
                this.stop();
            }
        }

        if (this.canStartAttack()) {
            LivingEntity livingentity = this.zombie.getTarget();
            if (livingentity != null) {
                if (this.canMove()) {
                    this.zombie.getNavigation().setSpeedModifier(this.mainGoal.getZombieSpeedBaseNormal());
                }
                if (this.startBuilding) {

                    if (this.zombie.level().getGameTime() - this.startBuildingTime >= 80L) {
                        this.mainGoal.zombiePathBuilderGoal.goal.startBuild(livingentity.blockPosition().below());
                        this.startBuilding = false;
                    }
                }
                this.targetX = livingentity.getX();
                this.targetY = livingentity.getY();
                this.targetZ = livingentity.getZ();
                this.zombie.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = this.zombie.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if (this.ticksUntilNextPathRecalculation == 0 && (this.targetX == 0.0D
                                                                  && this.targetY == 0.0D
                                                                  && this.targetZ == 0.0D
                                                                  || livingentity.distanceToSqr(this.targetX,
                        this.targetY, this.targetZ) >= 1.0D
                                                                  || this.zombie.getRandom().nextFloat() < 0.05F)) {

                    this.ticksUntilNextPathRecalculation = 4 + this.zombie.getRandom().nextInt(7);
                    if (d0 > 1024.0D) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0D) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    Path path = this.zombie.getNavigation().createPath(livingentity, 0);
                    boolean ba1 = false;
                    if (this.canMove()) {

                        if (path != null) {
                            ba1 = this.zombie.getNavigation().moveTo(path, this.mainGoal.getZombieSpeedBaseNormal());
                        }
                    }

                    long i = this.zombie.level().getGameTime();
                    if (canMove() && !this.startBuilding) {
                        if (path != null) {
                            BlockPos targetPos = livingentity.blockPosition();
                            if (Math.sqrt(this.zombie.blockPosition().distSqr(targetPos)) <= 20d) {
                                if (path.getDistToTarget() >= 3d) {
                                    if (this.mainGoal.canUsePathBuilder()
                                        && i - this.mainGoal.lastStopBuildingTime >= 240L) {
                                        this.setBuild();
                                    } else if (GameProperty.TimeProperty.getGameStage()
                                               >= ConfigDataManager.getOrDefault(ConfigKeySets.JUMP_STAGE)
                                               && this.zombie.onGround()
                                               && this.zombie.getEyePosition().distanceTo(
                                            this.zombie.getTarget().getEyePosition()) <= 3d
                                               && this.zombie.getY() < this.zombie.getTarget().getY() - 0.2d) {
                                        this.zombie.getJumpControl().jump();
                                    }
                                }
                            }
                        }


                        if (!this.startBuilding) {
                            boolean a = path != null && ba1;

                            boolean a1 = this.zombie.onGround();
                            boolean a2 = this.zombie.getEyePosition().distanceTo(
                                    this.zombie.getTarget().getEyePosition()) <= 3.5d;
                            boolean a3 = this.zombie.getY() <= this.zombie.getTarget().getY();

                            if (!a) {
                                if (this.mainGoal.canUsePathBuilder()
                                    && i - this.mainGoal.lastStopBuildingTime >= 240L) {
                                    this.setBuild();
                                }
                                this.ticksUntilNextPathRecalculation += 5;

                            }
                            if (GameProperty.TimeProperty.getGameStage() >= ConfigDataManager.getOrDefault(
                                    ConfigKeySets.JUMP_STAGE)
                                && this.zombie.onGround()
                                && this.zombie.getEyePosition().distanceTo(this.zombie.getTarget().getEyePosition())
                                   <= 3.5d
                                && this.zombie.getY() < this.zombie.getTarget().getY() - 0.2d) {
                                this.zombie.getJumpControl().jump();
                            }
                        }

                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity, d0);
            }
        }
    }

    private void setBuild() {
        if (!this.startBuilding) {
            Vec3 vec3 = DefaultRandomPos.getPos(this.getZombie(), 6, 4);
            if (vec3 == null) {
                vec3 = DefaultRandomPos.getPos(this.getZombie(), 8, 4);
            }
            if (vec3 == null) {
                vec3 = DefaultRandomPos.getPos(this.getZombie(), 10, 4);
            }
            if (vec3 != null) {

                this.zombie.getNavigation().moveTo(vec3.x, vec3.y, vec3.z,
                        this.mainGoal.getZombieSpeedBaseNormal() * 0.7);
            }
            this.startBuildingTime = this.zombie.level().getGameTime();
            this.startBuilding = true;
        }
    }

    public void cancelBuild() {
        this.startBuilding = false;
    }

    protected void checkAndPerformAttack(LivingEntity target, double p_25558_) {
        double d0 = this.getAttackReachSqr(target);
        if (p_25558_ <= d0 && this.ticksUntilNextAttack <= 0) {

            if (zombie.isUsingItem()) {
                if (zombie.getUseItem().getItem() instanceof ShieldItem) {
                    this.mainGoal.zombieShieldUsingGoal.goal.stopUsingShied();
                } else {
                    return;
                }
            }
            this.resetAttackCoolDown();
            this.zombie.swing(InteractionHand.MAIN_HAND);
            this.mainGoal.zombieAttack();
            this.zombie.doHurtTarget(target);
        }

    }

    protected boolean canMove() {
        if (this.mainGoal.isWalking()) {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
        }
        return !this.mainGoal.isBuilding() && !this.mainGoal.isWalking();
    }

    protected void resetAttackCoolDown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected double getAttackReachSqr(LivingEntity p_25556_) {
        return this.zombie.getBbWidth() * 2.2F * this.zombie.getBbWidth() * 2.2F + p_25556_.getBbWidth();
    }
}
