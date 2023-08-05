package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.datamanager.datamanager.EntityDataManager;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.option.Options;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbilityPack;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieBowAttackGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieCrossBowAttackGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieMeleeAttackGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.blockbreaker.ZombieBreakGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.blockchoosing.ZombieBlockChooserGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.floatgoal.ZombieFloatGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.fluidBlockPlace.FluidBlockPlaceGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.lookatplayergoal.ZombieLookAtPlayerGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.pathbuilder.ZombiePathBuildingGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.randomlookgoal.ZombieRandomLookGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.randomwalkgoal.ZombieRandomWalkGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.shielduse.ZombieShieldHelpingGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.shielduse.ZombieShieldUsingGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.targetchoosing.HurtTargetAddingGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.targetchoosing.ZombieTargetChoosingGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.zombiegotowatergoal.ZombieGoToCertainPlaceGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public abstract class ZombieMainGoal extends Goal {
    public static final String NAME = ZombieMainGoal.class.getSimpleName();
    public static TargetingConditions conditionsEntity = TargetingConditions.forNonCombat().range(75d).selector(
            (LivingEntity::isAlive)).ignoreLineOfSight();
    public final int RANDOM_NUM = RandomUtils.RANDOM.nextInt(-100, 100);
    protected final Zombie zombie;
    private final double SPEED_FIX;
    public AbilityPack<ZombieFloatGoal> zombieFloatGoal;
    public AbilityPack<ZombieBreakGoal> zombieBreakGoal;
    public AbilityPack<ZombieBlockChooserGoal> zombieBlockChooserGoal;
    public AbilityPack<ZombieTargetChoosingGoal> zombieTargetChoosingGoal;
    public AbilityPack<ZombieMeleeAttackGoal> zombieMeleeAttackGoal;
    public AbilityPack<ZombiePathBuildingGoal> zombiePathBuilderGoal;
    public AbilityPack<HurtTargetAddingGoal> hurtTargetAddingGoal;
    public AbilityPack<ZombieRandomWalkGoal> zombieRandomWalkGoal;
    public AbilityPack<ZombieRandomLookGoal> zombieRandomLookGoal;
    public AbilityPack<ZombieBowAttackGoal> zombieBowAttackGoal;
    public AbilityPack<ZombieLookAtPlayerGoal> zombieLookAtPlayerGoal;
    public AbilityPack<ZombieCrossBowAttackGoal> zombieCrossBowAttackGoal;
    public AbilityPack<ZombieGoToCertainPlaceGoal> drownedGoToWaterGoal;
    public AbilityPack<ZombieShieldUsingGoal> zombieShieldUsingGoal;
    public AbilityPack<ZombieGoToCertainPlaceGoal> zombieFleeSunGoal;
    public AbilityPack<ZombieShieldHelpingGoal> zombieShieldHelpingGoal;
    public boolean canAttack = true;
    public Long lastStopBuildingTime = 0L;
    public long banPathFinderTime;
    public Block BLOCK_TO_PLACE = Blocks.DIRT;
    public boolean haveAddedGoal = false;
    public long lastHurtTime;
    public long lastPlaceBlockTime = 0L;
    public AbilityPack<FluidBlockPlaceGoal> fluidPlaceBlockGoal;
    private boolean canUseShield = true;
    private int speedEffectTime;
    private double speedBase = 1.0d;

    public ZombieMainGoal(Zombie zombie) {

        this.zombie = zombie;
        this.SPEED_FIX = EntityDataManager.getOrCreate(zombie, "speed_fix", RandomUtils.RANDOM.nextDouble(-0.2d, 0));
        this.zombieShieldHelpingGoal = AbilityPack.create(new ZombieShieldHelpingGoal(this, this.zombie));
        this.zombieFloatGoal = AbilityPack.create(new ZombieFloatGoal(this, this.zombie));
        this.zombieBreakGoal = AbilityPack.create(new ZombieBreakGoal(this, this.zombie));
        this.zombieBlockChooserGoal = AbilityPack.create(new ZombieBlockChooserGoal(this, this.zombie));
        this.zombieTargetChoosingGoal = AbilityPack.create(new ZombieTargetChoosingGoal(this, this.zombie));
        this.zombieMeleeAttackGoal = AbilityPack.create(new ZombieMeleeAttackGoal(this, this.zombie));
        this.zombiePathBuilderGoal = AbilityPack.create(new ZombiePathBuildingGoal(this, this.zombie));
        this.hurtTargetAddingGoal = AbilityPack.create(new HurtTargetAddingGoal(this, this.zombie));
        this.zombieRandomWalkGoal = AbilityPack.create(new ZombieRandomWalkGoal(this, this.zombie));
        this.zombieRandomLookGoal = AbilityPack.create(new ZombieRandomLookGoal(this, this.zombie));
        this.zombieLookAtPlayerGoal = AbilityPack.create(new ZombieLookAtPlayerGoal(this, this.zombie));
        this.zombieCrossBowAttackGoal = AbilityPack.create(new ZombieCrossBowAttackGoal(this, this.zombie));
        this.zombieShieldUsingGoal = AbilityPack.create(new ZombieShieldUsingGoal(this, this.zombie));
        this.drownedGoToWaterGoal = AbilityPack.create(
                new ZombieGoToCertainPlaceGoal(this, this.zombie, ZombieUtils::getWaterPos,
                        (mainGoal) -> !GameProperty.ZombieProperty.isSunSensitive(mainGoal.zombie)
                                      || ZombieUtils.isNotSunBurnTick(zombie)));
        this.zombieFleeSunGoal = AbilityPack.create(
                new ZombieGoToCertainPlaceGoal(this, this.zombie, ZombieUtils::getDarkPos,
                        (mainGoal) -> !GameProperty.ZombieProperty.isSunSensitive(mainGoal.zombie)
                                      || ZombieUtils.isNotSunBurnTick(zombie)));
        this.zombieBowAttackGoal = AbilityPack.create(new ZombieBowAttackGoal(this, this.zombie, 20, 15f));
        this.fluidPlaceBlockGoal = AbilityPack.create(new FluidBlockPlaceGoal(this, this.zombie));
    }

    public void setCanUseShield(boolean canUseShield) {
        this.canUseShield = canUseShield;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void setCanUseShield() {
        this.setCanUseShield(true);
    }

    public Zombie getZombie() {
        return zombie;
    }

    public long getLastHurtTime() {
        return this.lastHurtTime = 0L;
    }

    public abstract String getName();

    protected void startUseGoal(AbilityPack<?> pack, int importance) {
        pack.isUsable = true;
        this.zombie.goalSelector.addGoal(importance, pack.goal);
    }

    protected void startUseTargetChoosingGoal(AbilityPack<?> pack, int importance) {
        pack.isUsable = true;
        this.zombie.targetSelector.addGoal(importance, pack.goal);
    }

    public void zombieAttack() {
        if (this.canUsePathBuilder()) {

            this.zombiePathBuilderGoal.goal.stopBuild();
            this.zombieMeleeAttackGoal.goal.cancelBuild();
        }
        if (this.canBreakBlock()) {
            this.zombieBreakGoal.goal.failBreak();
        }

        if (this.zombieRandomWalkGoal.goal.isWalking()) {
            this.zombieRandomWalkGoal.goal.stopWalking();
        }
    }

    public void zombieHurt() {
        this.lastHurtTime = this.zombie.getLevel().getGameTime();
        if (this.canUsePathBuilder()) {
            this.zombiePathBuilderGoal.goal.stopBuild();
            this.zombieMeleeAttackGoal.goal.cancelBuild();
        }

        if (this.canBreakBlock()) {
            this.zombieBreakGoal.goal.failBreak();
        }
        if (this.zombieRandomWalkGoal.goal.isWalking()) {
            this.zombieRandomWalkGoal.goal.stopWalking();
        }
        this.hurtTargetAddingGoal.goal.onZombieHurt();
    }

    public boolean isBuilding() {
        return false;
    }

    public boolean canUsePathBuilder() {
        return false;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public void tick() {

        this.speedEffectTime--;
        if (this.speedEffectTime <= 0) {
            this.defaultSpeedBase();
        }

        if ((this.zombie.getLevel().getGameTime() % 5L == RandomUtils.RANDOM.nextInt(0, 5))) {
            if ((this.zombieRandomWalkGoal.isUsable) && (!this.zombieRandomWalkGoal.goal.isWalking())) {
                Zombie zombie_ = this.zombie.getLevel().getNearestEntity(Zombie.class, conditionsEntity, this.zombie,
                        this.zombie.getX(), this.zombie.getY(), this.zombie.getZ(),
                        this.zombie.getBoundingBox().inflate(100D, 60D, 100D));

                if (zombie_ != null) {
                    if (RandomUtils.nextBoolean(0.1d)
                        && zombie_.getEyePosition().distanceTo(this.zombie.getEyePosition()) <= 2d
                        && !this.zombieMeleeAttackGoal.goal.isStartBuilding()
                        && RandomUtils.nextBoolean(0.1d)) {
                        this.zombieRandomWalkGoal.goal.startRandomWalking(5L, !(this.zombie.getTarget() == null));
                    }
                } else if (this.isFree()) {
                    this.zombieRandomWalkGoal.goal.startRandomWalking(5L, false);
                }
            }
        }

        if ((this.zombie.getLevel().getGameTime() + this.RANDOM_NUM) % 10L == 0) {
            if ((this.zombieRandomWalkGoal.isUsable) && (!this.zombieRandomWalkGoal.goal.isWalking())) {
                if (this.isFree()) {
                    this.zombieRandomWalkGoal.goal.startRandomWalking(5L, false);
                }
            }
        }

        if (this.zombie.getTarget() != null) {
            if (!ZombieUtils.canBeAttack(this.zombie.getTarget())) {
                this.zombie.setTarget(null);
            }
            if ((this.zombie.getLevel().getGameTime() - this.getLastHurtTime()) % 400 == 5) {
                this.callToAttack(this.zombie.getTarget());
            }
        }

        this.banPathFinderTime = Math.max(0, this.banPathFinderTime - 1);

    }

    public void callToAttack(LivingEntity target) {
        if (!ZombieUtils.canBeAttack(target)) return;
        this.zombie.getLevel().getNearestEntity(Zombie.class,
                TargetingConditions.forNonCombat().range(75d).selector((entity) -> {
                    if (this.zombie.getSensing().hasLineOfSight(entity)
                        || GameProperty.TimeProperty.getGameStage() >= 0.6d) {
                        if (ZombieUtils.canBeLoaded(entity)) {
                            Zombie zombie1 = (Zombie) entity;
                            if (zombie1.getTarget() == null) {
                                zombie1.setTarget(target);
                            }
                        }
                    }
                    return false;
                }).ignoreLineOfSight(), this.zombie, this.zombie.getX(), this.zombie.getY(), this.zombie.getZ(),
                this.zombie.getBoundingBox().inflate(50D, 10D, 50D));
    }

    public void banPathFinder(long time) {
        this.banPathFinderTime = this.banPathFinderTime + time;
    }


    // Overridable

    public abstract void addGoal();

    public final boolean canPlaceBlock() {
        return this.canPlace() && Options.CAN_ZOMBIE_PLACE_BLOCK.getValue((ServerLevel) this.zombie.getLevel());
    }

    public final boolean canBreakBlock() {
        return this.canBreak() && Options.CAN_ZOMBIE_BREAK_BLOCK.getValue((ServerLevel) this.zombie.getLevel());
    }

    protected boolean canBreak() {
        return false;
    }

    protected boolean canPlace() {
        return false;
    }

    public double getZombieSpeedBaseNormal() {
        return this.speedBase;
    }

    public double getZombieSpeedBaseSlow() {
        return 0.6d * this.speedBase;
    }

    public double getZombieSpeed() {
        return (this.zombie.isBaby() ? 0.8d + this.SPEED_FIX : 1.4d + this.SPEED_FIX)
               * GameProperty.ZombieProperty.getZombieWalkSpeedPlus();
    }

    public boolean isFree() {
        return this.zombie.getTarget() == null && !this.isBuilding();
    }

    public boolean isWalking() {
        return this.zombieRandomWalkGoal.goal.isWalking();
    }

    public double getZombieAttackDamage() {
        return GameProperty.ZombieProperty.getZombieAttackValue();
    }

    public double getZombieArmor() {
        return GameProperty.ZombieProperty.getZombieArmor();
    }

    public void zombieDeath() {
        this.zombieHurt();
    }

    public boolean canUseShield() {
        return this.canUseShield;
    }

    public void setSpeedBase(double speedBase, int speedTime) {
        this.speedEffectTime = speedTime;
        this.speedBase = speedBase;
    }

    public void setSpeedBase(double speedBase) {
        this.speedEffectTime = Integer.MAX_VALUE / 2;
        this.speedBase = speedBase;
    }

    public void addSpeedBaseModifyTime(double speedBase, int speedTime) {
        this.speedEffectTime += speedTime;
        this.speedBase = speedBase;
    }

    public void addSpeedBaseModifyTime(int speedTime) {
        this.speedEffectTime += speedTime;
    }

    public void defaultSpeedBase() {
        this.setSpeedBase(1.0d, 0);
    }
}
