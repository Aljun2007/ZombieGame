package com.aljun.zombiegame.work.zombie.goal.abilitygoals.pathbuilder;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.tool.BlockKind;
import com.aljun.zombiegame.work.zombie.goal.tool.BlockPacket;
import com.aljun.zombiegame.work.zombie.goal.tool.PathFinder;
import com.aljun.zombiegame.work.zombie.goal.tool.Tools;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

public class ZombiePathBuildingGoal extends AbstractZombieAbilityGoal {

    private boolean isDone = true;
    private BlockPos selfPos = new BlockPos(0, 0, 0);
    private final PathFinder PATH_FINDER = new PathFinder((oldStartPos, newStartPos) -> {
        if (this.zombie.isAlive()) {
            this.selfPos = newStartPos;
            Vec3 vec3 = Tools.blockPosToVec3(newStartPos);
            Path path = this.zombie.getNavigation().createPath(vec3.x(), vec3.y(), vec3.z(), 0);
            if (path != null) {
                this.zombie.getNavigation().moveTo(path, this.mainGoal.getZombieSpeedBaseNormal() * 0.7d);
            }
        }
    }, (hash) -> {
        final boolean[] a = {true};
        hash.forEach((i, packet) -> {
            a[0] = (isBlockDone(this.zombie.getLevel().getBlockState(packet.blockPos), packet.blockKind, this,
                    packet.blockPos)) && a[0];
        });
        return a[0];
    });

    public ZombiePathBuildingGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
    }

    private static boolean canBeABlock(BlockState state, ZombiePathBuildingGoal goal, BlockPos pos) {
        return Block.isShapeFullBlock(state.getCollisionShape(goal.getZombie().level, pos));
    }

    private static boolean canBeAAir(BlockState state) {
        return (state.isAir()
                || state.getBlock() instanceof LiquidBlock
                || state.getBlock() instanceof AbstractCauldronBlock)
               && !(state.getBlock() instanceof PowderSnowCauldronBlock);
    }

    private static boolean isBlockDone(BlockState state, BlockKind kind, ZombiePathBuildingGoal goal, BlockPos pos) {
        return kind == BlockKind.AIR ? canBeAAir(state) : canBeABlock(state, goal, pos);
    }

    @Override
    public boolean canBeUsed() {
        boolean b = true;
        if (this.zombie instanceof Drowned) {
            b = this.zombie.getNavigation().canFloat();
        }
        return !this.isDone && this.mainGoal.canUsePathBuilder() && b;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canBeUsed();
    }

    @Override
    public void tick() {
        if (!this.isDone && this.mainGoal.canUsePathBuilder()) {
            if (this.zombie.getEyePosition().distanceTo(Tools.blockPosToVec3(this.selfPos)) <= 10d) {
                for (int i = 1; i <= 10; i++) {
                    BlockPacket packet = this.PATH_FINDER.getBlock();
                    if (packet.isEmpty()) {
                        this.stopBuild();
                    } else {
                        if (isBlockDone(this.zombie.getLevel().getBlockState(packet.blockPos), packet.blockKind, this,
                                packet.blockPos)) {
                            if (this.zombie.getEyePosition().distanceTo(Tools.blockPosToVec3(this.selfPos)) <= 2.5d) {
                                this.PATH_FINDER.next();
                            } else {
                                if (this.zombie.getNavigation().isDone()) {
                                    Vec3 vec3 = Tools.blockPosToVec3(this.selfPos);
                                    Path path = this.zombie.getNavigation().createPath(vec3.x(), vec3.y(), vec3.z(), 0);
                                    if (path != null) {
                                        this.zombie.getNavigation().moveTo(path,
                                                this.mainGoal.getZombieSpeedBaseNormal() * 0.7d);
                                    }
                                }
                                break;
                            }
                        } else {
                            this.tryToFinishBlock(packet);
                            break;
                        }

                    }
                }
            } else {
                this.stopBuild();
            }
        }
    }

    private void tryToFinishBlock(BlockPacket packet) {
        if (!this.isDone && !packet.isEmpty() && this.zombie.blockPosition().distSqr(this.selfPos) <= 15d) {
            BlockState state = this.zombie.getLevel().getBlockState(packet.blockPos);
            if (packet.blockKind == BlockKind.BLOCK) {
                if (state.isAir()
                    || state.getBlock() instanceof LiquidBlock
                    || state.getBlock() instanceof AbstractCauldronBlock) {
                    if (state.getBlock() instanceof PowderSnowCauldronBlock) {
                        this.destroyBlock(packet.blockPos);
                    } else {
                        this.placeBlock(packet.blockPos);
                    }
                } else {
                    this.destroyBlock(packet.blockPos);
                }
            } else {
                this.destroyBlock(packet.blockPos);
            }
        }
    }

    private void placeBlock(BlockPos blockPos) {
        if (this.mainGoal.canPlaceBlock() && !this.isDone) {
            if (this.zombie.blockPosition().distSqr(blockPos) <= 16) {
                if ((this.zombie.getLevel().getGameTime() - this.mainGoal.lastPlaceBlockTime) > 12L) {
                    if (this.zombie.getLevel().isOutsideBuildHeight(blockPos)) {
                        this.stopBuild();
                        return;
                    }
                    if (!this.zombie.isSwimming()) {
                        this.zombie.swing(InteractionHand.OFF_HAND);
                    }
                    this.mainGoal.lastPlaceBlockTime = this.zombie.getLevel().getGameTime();
                    SoundType soundType = this.getBlockToPlace().defaultBlockState().getSoundType();
                    this.zombie.level.playSound(null, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                            (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    this.zombie.level.setBlock(blockPos, this.getBlockToPlace().defaultBlockState(), 11);

                }
            } else if (this.zombie.blockPosition().distSqr(blockPos) <= 36) {
                this.stopBuild();
            }
        }

    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private void destroyBlock(BlockPos blockPos) {
        if (this.mainGoal.canBreakBlock() && !this.isDone) {
            if (this.zombie.getLevel().isOutsideBuildHeight(blockPos)) {
                this.stopBuild();
                return;
            }
            //this.zombie.level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 1);
            if (!this.mainGoal.zombieBreakGoal.goal.checkToStartBreak(blockPos) && ForgeHooks.canEntityDestroy(
                    this.zombie.getLevel(), blockPos, this.zombie)) {

                if (this.mainGoal.zombieBreakGoal.goal.isDone()) {
                    this.stopBuild();
                }
            }
        }
    }

    private Block getBlockToPlace() {
        return this.mainGoal.BLOCK_TO_PLACE;
    }

    public void stopBuild() {
        stopBuild(true);
    }

    public void stopBuild(boolean reStart) {
        if (!this.isDone) {
            this.PATH_FINDER.stop();
            this.isDone = true;
            this.mainGoal.lastStopBuildingTime = this.zombie.getLevel().getGameTime();
            if (reStart) {
                this.mainGoal.zombieRandomWalkGoal.goal.startRandomWalking(5L, true);
            }
        }
    }

    public void startBuild(BlockPos targetPos) {
        if (this.mainGoal.isWalking()) {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
        }
        if (this.mainGoal.canUsePathBuilder()
            && this.isDone
            && !this.mainGoal.isWalking()
            && (this.zombie.getLevel().getGameTime() - this.mainGoal.lastHurtTime) >= 30L) {
            this.selfPos = this.zombie.getOnPos().above();
            this.PATH_FINDER.start(this.selfPos, targetPos.above());
            this.isDone = false;
        }
    }


    public boolean isDone() {
        return this.isDone;
    }

}
