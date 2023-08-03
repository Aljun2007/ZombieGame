package com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class ZombieBowAttackGoal extends AbstractZombieAbilityGoal {

    protected final float attackRadiusSqr;
    protected final float attackRadius;
    protected int attackIntervalMin;
    protected int attackTime = -1;
    protected int seeTime;
    protected boolean strafingClockwise;
    protected boolean strafingBackwards;
    protected int strafingTime = -1;

    public ZombieBowAttackGoal(ZombieMainGoal mainGoal, Zombie zombie, int attackIntervalMin_, float attackRadius_) {
        super(mainGoal, zombie);
        this.attackIntervalMin = attackIntervalMin_;
        this.attackRadiusSqr = attackRadius_ * attackRadius_;
        this.attackRadius = attackRadius_;
    }

    protected double getSpeed() {
        if (this.zombie.getTarget() == null) {
            return this.mainGoal.getZombieSpeedBaseSlow();
        }
        return this.zombie.getEyePosition().distanceTo(this.zombie.getTarget().getEyePosition())
               >= this.attackRadius + 3d ? this.mainGoal.getZombieSpeedBaseNormal() :
                this.mainGoal.getZombieSpeedBaseSlow();
    }

    public void setMinAttackInterval(int p_25798_) {
        this.attackIntervalMin = p_25798_;
    }

    public boolean canBeUsed() {
        return this.zombie.getTarget() != null && this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return this.zombie.isHolding(is -> is.getItem() instanceof BowItem);
    }

    public boolean canContinueToUse() {
        return (this.canBeUsed() || !this.zombie.getNavigation().isDone()) && this.isHoldingBow();
    }

    public void start() {
        this.zombie.setAggressive(true);
        super.start();
    }

    public void stop() {
        this.zombie.setAggressive(false);
        super.stop();
        this.seeTime = 0;
        this.attackTime = -1;
        this.zombie.stopUsingItem();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.zombie.getTarget();
        if (livingentity != null) {
            this.zombie.getLookControl().setLookAt(livingentity);
            double d0 = this.zombie.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean flag = this.zombie.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                this.zombie.getNavigation().stop();
                ++this.strafingTime;
            } else {
                if (this.canMove()) {
                    Path path = this.zombie.getNavigation().createPath(livingentity, 15);
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
                if (d0 > (double) (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                if (this.zombie.getNavigation().isDone()) {
                    if (this.canMove()) {
                        float speed = (float) this.mainGoal.getZombieSpeedBaseSlow();
                        this.zombie.getMoveControl().strafe(this.strafingBackwards ? -speed : speed,
                                this.strafingClockwise ? speed : -speed);
                    }
                }
            }

            if (this.zombie.isUsingItem()) {
                if (!flag && this.seeTime < -60) {
                    this.zombie.stopUsingItem();
                } else if (this.zombie.getEyePosition().distanceTo(this.zombie.getTarget().getEyePosition())
                           >= this.attackRadius) {
                    this.zombie.stopUsingItem();
                } else if (flag) {
                    int i = this.zombie.getTicksUsingItem();
                    if (i >= 20) {
                        this.zombie.stopUsingItem();
                        this.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            } else if (--this.attackTime <= 0
                       && this.seeTime >= -60
                       && this.zombie.getEyePosition().distanceTo(this.zombie.getTarget().getEyePosition())
                          <= this.attackRadius) {
                this.zombie.startUsingItem(
                        ProjectileUtil.getWeaponHoldingHand(this.zombie, item -> item instanceof BowItem));
            }

        }
    }

    protected void performRangedAttack(LivingEntity target, float powerForTime) {
        ItemStack itemstack = this.zombie.getProjectile(this.zombie.getItemInHand(
                ProjectileUtil.getWeaponHoldingHand(this.zombie,
                        item -> item instanceof net.minecraft.world.item.BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, powerForTime);
        if (this.zombie.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem)
            abstractarrow = ((net.minecraft.world.item.BowItem) this.zombie.getMainHandItem().getItem()).customArrow(
                    abstractarrow);
        double d0 = target.getX() - this.zombie.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = target.getZ() - this.zombie.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        shoot(abstractarrow, d0, d1 + d3 * (double) 0.2F, d2);
        this.zombie.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (RandomUtils.RANDOM.nextFloat() * 0.4F + 0.8F));
        this.zombie.level.addFreshEntity(abstractarrow);
    }

    protected void shoot(AbstractArrow abstractArrow, double p_37266_, double p_37267_, double p_37268_) {
        Vec3 vec3 = (new Vec3(p_37266_, p_37267_, p_37268_)).normalize().scale((float) 1.6);
        abstractArrow.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        abstractArrow.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        abstractArrow.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
        abstractArrow.yRotO = abstractArrow.getYRot();
        abstractArrow.xRotO = abstractArrow.getXRot();
    }

    protected AbstractArrow getArrow(ItemStack itemstack, float powerForTime) {
        return ProjectileUtil.getMobArrow(this.zombie, itemstack, powerForTime);
    }

    protected boolean canMove() {
        if (this.mainGoal.isWalking()) {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
        }
        return !this.mainGoal.isBuilding() && !this.mainGoal.isWalking();
    }
}
