package com.aljun.zombiegame.work.zombie.goal.abilitygoals.fluidBlockPlace;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.tool.Tools;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.block.SoundType;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;


import javax.annotation.Nullable;

public class FluidBlockPlaceGoal extends AbstractZombieAbilityGoal {

    public FluidBlockPlaceGoal(ZombieMainGoal mainGoal, ZombieEntity zombie) {
        super(mainGoal, zombie);
    }

    private static Direction getDirection(BlockPos selfPos, BlockPos targetPos) {
        double x = selfPos.getX() - targetPos.getX();
        double z = selfPos.getZ() - targetPos.getZ();
        Direction direction;
        if (z <= x && z < -x) {
            direction = Direction.SOUTH;
        } else if (z > x && z <= -x) {
            direction = Direction.EAST;
        } else if (z >= x && z > -x) {
            direction = Direction.NORTH;
        } else if (z < x && z >= -x) {
            direction = Direction.WEST;
        } else {
            direction = Tools.randomDirection2();
        }
        return direction;
    }

    @Override
    public boolean canBeUsed() {
        boolean b = true;
        if (this.zombie instanceof DrownedEntity) {
            b = this.zombie.getNavigation().canFloat();
        }
        return this.mainGoal.canPlaceBlock() && b;
    }

    @Override
    public void tick() {
        if ((this.zombie.level.getGameTime() - this.mainGoal.lastPlaceBlockTime) > 12L) {
            BlockPos blockPos = this.chooseBlockPos();
            if (blockPos != null) {
                this.mainGoal.lastPlaceBlockTime = this.zombie.level.getGameTime();
                SoundType soundType = this.mainGoal.BLOCK_TO_PLACE.defaultBlockState().getSoundType();
                this.zombie.level.playSound(null, blockPos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
                        (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                this.zombie.level.setBlock(blockPos, this.mainGoal.BLOCK_TO_PLACE.defaultBlockState(), 11);
                if (!this.zombie.isSwimming()) {
                    this.zombie.swing(Hand.OFF_HAND);
                }
                this.mainGoal.setSpeedBase(0.6d, 30);
                this.mainGoal.banPathFinder(20);
            }

        }
    }

    @Nullable
    private BlockPos chooseBlockPos() {
        Direction direction;
        BlockPos target;
        if (this.zombie.getTarget() == null) {
            target = new BlockPos((int) (this.zombie.getLookControl().getWantedX() - 0.5d),
                    (int) (this.zombie.getLookControl().getWantedY()),
                    (int) (this.zombie.getLookControl().getWantedZ() - 0.5d));
        } else {
            target = this.zombie.getTarget().blockPosition();
        }
        BlockPos pos = this.zombie.blockPosition();

        pos = pos.below();
        if (this.isPosOk(pos)) {
            return pos;
        }
        pos = pos.above();

        for (int i = 1; i <= 3; i++) {
            direction = getDirection(pos, target);
            pos = pos.relative(direction);

            if (isPosOk(pos)) {
                return pos;
            }
        }
        pos = zombie.blockPosition().below();
        for (int i = 1; i <= 3; i++) {
            direction = getDirection(pos, target);
            pos = pos.relative(direction);
            if (isPosOk(pos)) {
                return pos;
            }
        }
        pos = zombie.blockPosition().above();
        for (int i = 1; i <= 3; i++) {
            direction = getDirection(pos, target);
            pos = pos.relative(direction);
            if (isPosOk(pos)) {
                return pos;
            }
        }
        pos = zombie.blockPosition().above(2);
        for (int i = 1; i <= 3; i++) {
            direction = getDirection(pos, target);
            pos = pos.relative(direction);
            if (isPosOk(pos)) {
                return pos;
            }
        }
        return null;
    }

    private boolean isPosOk(BlockPos pos) {
        FluidState state = this.zombie.level.getBlockState(pos).getFluidState();
        return !state.isEmpty();
    }
}
