package com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ZombieCrossBowAttackGoal extends AbstractZombieAbilityGoal implements CrossbowAttackMob {
    public static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Zombie.class,
            EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW_VILLAGER = SynchedEntityData.defineId(
            ZombieVillager.class, EntityDataSerializers.BOOLEAN);

    private final float attackRadiusSqr;
    private final float attackRadius;
    protected boolean strafingClockwise = false;
    protected boolean strafingBackwards = false;
    private CrossbowState crossbowState = CrossbowState.UNCHARGED;
    private int seeTime;
    private int attackDelay;
    private int strafingTime = -1;

    public ZombieCrossBowAttackGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
        this.attackRadius = 10.0f;
        this.attackRadiusSqr = attackRadius * attackRadius;
    }

    public boolean canBeUsed() {
        return this.isValidTarget() && this.isHoldingCrossbow();
    }

    private boolean isHoldingCrossbow() {
        return this.zombie.isHolding(is -> is.getItem() instanceof CrossbowItem);
    }

    public boolean canContinueToUse() {
        return this.isValidTarget()
               && (this.canBeUsed() || !this.zombie.getNavigation().isDone())
               && this.isHoldingCrossbow();
    }

    private boolean isValidTarget() {
        return this.zombie.getTarget() != null && this.zombie.getTarget().isAlive();
    }

    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
        this.zombie.setTarget(null);
        this.seeTime = 0;
        if (this.zombie.isUsingItem()) {
            this.zombie.stopUsingItem();
            this.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.zombie.getUseItem(), false);
        }

    }

    protected boolean canMove() {
        if (this.mainGoal.isWalking()) {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
        }
        return !this.mainGoal.isBuilding() && !this.mainGoal.isWalking();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.zombie.getTarget();
        if (livingentity != null) {
            this.zombie.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            boolean flag = this.zombie.getSensing().hasLineOfSight(livingentity);
            double d0 = this.zombie.distanceToSqr(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double) this.attackRadiusSqr * 1.3d) && this.seeTime >= 20) {
                this.zombie.getNavigation().stop();
                ++this.strafingTime;
            } else {
                if (this.canMove()) {
                    Path path = this.zombie.getNavigation().createPath(livingentity, (int) this.attackRadius);
                    if (path != null) {
                        this.zombie.getNavigation().moveTo(path, this.getSpeed());
                    }
                }
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double) this.zombie.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double) this.zombie.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }


            if (this.strafingTime > -1) {
                if (d0 > (double) (this.attackRadiusSqr * 0.8F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.attackRadiusSqr * 0.5F)) {
                    this.strafingBackwards = true;
                }
                float speed = (float) (this.mainGoal.getZombieSpeedBaseSlow() * (
                        this.crossbowState == CrossbowState.CHARGING ? 0.5f : 1.0f));

                if (this.zombie.getNavigation().isDone()) {
                    if (this.canMove()) {
                        this.zombie.getMoveControl().strafe(this.strafingBackwards ? -speed : speed,
                                this.strafingClockwise ? speed : -speed);
                    }
                }
            }

            if (this.crossbowState == CrossbowState.UNCHARGED) {

                this.zombie.startUsingItem(
                        ProjectileUtil.getWeaponHoldingHand(this.zombie, item -> item instanceof CrossbowItem));
                this.crossbowState = CrossbowState.CHARGING;
                this.setChargingCrossbow(true);

            } else if (this.crossbowState == CrossbowState.CHARGING) {
                if (!this.zombie.isUsingItem()) {
                    this.crossbowState = CrossbowState.UNCHARGED;
                }

                int i = this.zombie.getTicksUsingItem();
                ItemStack itemstack = this.zombie.getUseItem();
                if (i >= CrossbowItem.getChargeDuration(itemstack)) {
                    this.zombie.releaseUsingItem();
                    this.crossbowState = CrossbowState.CHARGED;
                    this.attackDelay = 20 + this.zombie.getRandom().nextInt(20);
                    this.setChargingCrossbow(false);
                }
            } else if (this.crossbowState == CrossbowState.CHARGED) {
                --this.attackDelay;
                if (this.attackDelay == 0) {
                    this.crossbowState = CrossbowState.READY_TO_ATTACK;
                }
            } else if (this.crossbowState == CrossbowState.READY_TO_ATTACK && flag) {
                if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.performRangedAttack(livingentity, 1.0F);
                    ItemStack itemstack1 = this.zombie.getItemInHand(
                            ProjectileUtil.getWeaponHoldingHand(this.zombie, item -> item instanceof CrossbowItem));
                    CrossbowItem.setCharged(itemstack1, false);
                    this.crossbowState = CrossbowState.UNCHARGED;
                }
            }

        }
    }


    @Override
    public void setChargingCrossbow(boolean b) {
        try {
            if (this.zombie instanceof ZombieVillager) {
                this.zombie.getEntityData().set(ZombieCrossBowAttackGoal.IS_CHARGING_CROSSBOW_VILLAGER, b);
            } else {
                this.zombie.getEntityData().set(ZombieCrossBowAttackGoal.IS_CHARGING_CROSSBOW, b);
            }
        } catch (Throwable ignored) {
        }
    }


    @Override
    public void shootCrossbowProjectile(@NotNull LivingEntity target, @NotNull ItemStack stack,
                                        @NotNull Projectile projectile, float p_34710_) {
        this.shootCrossbowProjectile(this.zombie, target, projectile, p_34710_, 1.6F);
    }

    protected double getSpeed() {
        if (this.zombie.getTarget() == null) {
            return this.mainGoal.getZombieSpeedBaseSlow();
        }
        return this.zombie.getEyePosition().distanceTo(this.zombie.getTarget().getEyePosition())
               >= this.attackRadius + 3d ? this.mainGoal.getZombieSpeedBaseNormal() :
                this.mainGoal.getZombieSpeedBaseSlow();
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.zombie.getTarget();
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.zombie.setNoActionTime(0);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float f) {
        this.performCrossbowAttack(this.zombie, 1.6F);
    }


    enum CrossbowState {
        UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
    }
}
