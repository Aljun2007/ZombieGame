package com.aljun.zombiegame.work.zombie.goal.abilitygoals.fluidBlockPlace;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.tool.Tools;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class FluidBlockPlaceGoal extends AbstractZombieAbilityGoal {

    public FluidBlockPlaceGoal(ZombieMainGoal mainGoal, Zombie zombie) {
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
        if (this.zombie instanceof Drowned) {
            b = this.zombie.getNavigation().canFloat();
        }
        return this.mainGoal.canPlaceBlock() && b;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if ((this.zombie.getLevel().getGameTime() - this.mainGoal.lastPlaceBlockTime) > 12L) {
            BlockPos blockPos = this.chooseBlockPos();
            if (blockPos != null) {
                this.mainGoal.lastPlaceBlockTime = this.zombie.getLevel().getGameTime();
                SoundType soundType = this.mainGoal.BLOCK_TO_PLACE.defaultBlockState().getSoundType();
                this.zombie.level.playSound(null, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                        (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                this.zombie.level.setBlock(blockPos, this.mainGoal.BLOCK_TO_PLACE.defaultBlockState(), 11);
                if (!this.zombie.isSwimming()) {
                    this.zombie.swing(InteractionHand.OFF_HAND);
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
        FluidState state = this.zombie.getLevel().getBlockState(pos).getFluidState();
        return !state.is(Fluids.EMPTY);
    }
}
