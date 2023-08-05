package com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned;

import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;


public class SimpleDrownedGoal extends ZombieMainGoal {
    public static final String NAME = SimpleDrownedGoal.class.getSimpleName();

    public SimpleDrownedGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {
        assert this.zombie instanceof Drowned;
        this.startUseGoal(this.zombieBreakGoal, 3);
        this.startUseGoal(this.zombieMeleeAttackGoal, 4);
        this.startUseGoal(this.zombieFloatGoal, 2);
        this.startUseGoal(this.zombieRandomLookGoal, 4);
        this.startUseTargetChoosingGoal(this.zombieTargetChoosingGoal, 2);
        this.startUseTargetChoosingGoal(this.hurtTargetAddingGoal, 2);
        this.startUseGoal(this.zombieLookAtPlayerGoal, 3);
        this.startUseGoal(this.drownedGoToWaterGoal, 3);
        this.zombie.goalSelector.addGoal(2,
                new DrownedTridentAttackGoal((RangedAttackMob) this.zombie, 1.0D, 40, 10.0F));
        this.zombie.goalSelector.addGoal(6,
                new DrownedSwimUpGoal(this.zombie, this.getZombieSpeed(), this.zombie.level.getSeaLevel()));
        this.zombie.goalSelector.addGoal(7, new RandomStrollGoal(this.zombie, 1.0D));
        this.zombie.goalSelector.addGoal(5, new DrownedGoToBeachGoal((Drowned) this.zombie, 1.0D));

    }

    static class DrownedSwimUpGoal extends Goal {
        private final Drowned drowned;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;

        public DrownedSwimUpGoal(Zombie p_32440_, double p_32441_, int p_32442_) {
            this.drowned = (Drowned) p_32440_;
            this.speedModifier = p_32441_;
            this.seaLevel = p_32442_;
        }

        public boolean canUse() {
            boolean b = true;
            if (this.drowned.getTarget() != null) {
                b = this.drowned.getTarget().isInWater();
            }
            this.drowned.getNavigation().setCanFloat(!b);

            return !this.drowned.level.isDay()
                   && b
                   && this.drowned.isInWater()
                   && this.drowned.getY() < (double) (this.seaLevel - 2);
        }

        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }

        public void tick() {
            if (this.drowned.getY() < (double) (this.seaLevel - 1) && (this.drowned.getNavigation().isDone()
                                                                       || this.closeToNextPos())) {
                Vec3 vec3 = DefaultRandomPos.getPosTowards(this.drowned, 4, 8,
                        new Vec3(this.drowned.getX(), this.seaLevel, this.drowned.getZ()), (float) Math.PI / 2F);
                if (vec3 == null) {
                    this.stuck = true;
                    return;
                }
                this.drowned.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
            }

        }

        protected boolean closeToNextPos() {
            Path path = this.drowned.getNavigation().getPath();
            if (path != null) {
                BlockPos blockpos = path.getTarget();
                double d0 = this.drowned.distanceToSqr((double) blockpos.getX(), (double) blockpos.getY(),
                        blockpos.getZ());
                return d0 < 4.0D;
            }
            return false;
        }

        public void start() {
            this.drowned.setSearchingForLand(true);
            this.stuck = false;
        }

        public void stop() {
            this.drowned.setSearchingForLand(false);
        }
    }

    static class DrownedGoToBeachGoal extends MoveToBlockGoal {
        private final Drowned drowned;

        public DrownedGoToBeachGoal(Drowned p_32409_, double p_32410_) {
            super(p_32409_, p_32410_, 8, 2);
            this.drowned = p_32409_;
        }

        public boolean canUse() {
            return super.canUse() && (!GameProperty.ZombieProperty.isSunSensitive(this.drowned)
                                      || !this.drowned.getLevel().isDay()) && this.drowned.isInWater();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected boolean isValidTarget(LevelReader p_32413_, BlockPos p_32414_) {
            BlockPos blockpos = p_32414_.above();
            return p_32413_.isEmptyBlock(blockpos) && p_32413_.isEmptyBlock(blockpos.above()) && p_32413_.getBlockState(
                    p_32414_).entityCanStandOn(p_32413_, p_32414_, this.drowned);
        }

        public void start() {
            this.drowned.setSearchingForLand(false);
            super.start();
        }

        public void stop() {
            super.stop();
        }
    }

    static class DrownedTridentAttackGoal extends RangedAttackGoal {
        private final Drowned drowned;

        public DrownedTridentAttackGoal(RangedAttackMob p_32450_, double p_32451_, int p_32452_, float p_32453_) {
            super(p_32450_, p_32451_, p_32452_, p_32453_);
            this.drowned = (Drowned) p_32450_;
        }

        public boolean canUse() {
            return super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT);
        }

        public void start() {
            super.start();
            this.drowned.setAggressive(true);
            this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.drowned.stopUsingItem();
            this.drowned.setAggressive(false);
        }
    }
}