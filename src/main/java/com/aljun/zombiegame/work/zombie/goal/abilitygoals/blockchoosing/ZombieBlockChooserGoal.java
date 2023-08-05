package com.aljun.zombiegame.work.zombie.goal.abilitygoals.blockchoosing;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.config.ZombieGameConfig;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.blockbreaker.ZombieBreakGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ZombieBlockChooserGoal extends AbstractZombieAbilityGoal {
    private static final List<Block> blockList = new ArrayList<>();

    static {
        blockList.add(Blocks.STONECUTTER);
        blockList.add(Blocks.CARTOGRAPHY_TABLE);
        blockList.add(Blocks.REDSTONE_WIRE);
        blockList.add(Blocks.REDSTONE_LAMP);
        blockList.add(Blocks.COMPOSTER);
        blockList.add(Blocks.LADDER);
        blockList.add(Blocks.BOOKSHELF);
        blockList.add(Blocks.SCAFFOLDING);

    }

    private BlockPos targetPos;

    public ZombieBlockChooserGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
    }

    private static boolean isTypeCorrect(BlockState state) {
        if (state == null) {
            return false;
        }
        if (firstCheck(state)) {
            return true;
        }

        Block block2 = state.getBlock();

        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block2);
        String id = "";
        if (key != null) {
            id = key.toString();
        }

        return (blockList.contains(state.getBlock()) || ZombieGameConfig.blockChoosingWhiteList.get().contains(id))
               && !ZombieGameConfig.blockBreakingBlackList.get().contains(id);
    }

    private static boolean firstCheck(BlockState state) {
        Block block = state.getBlock();
        return (block instanceof CropBlock) || ((block instanceof IronBarsBlock
                                                 || block instanceof AbstractGlassBlock
                                                 || block instanceof TorchBlock
                                                 || block instanceof LanternBlock
                                                 || block instanceof CandleBlock
                                                 || block instanceof CraftingTableBlock
                                                 || block instanceof DoorBlock
                                                 || block instanceof AnvilBlock
                                                 || block instanceof HorizontalDirectionalBlock
                                                 || block instanceof BaseEntityBlock
                                                 || block instanceof BaseRailBlock)
                                                && !(block instanceof GameMasterBlock
                                                     || block instanceof EndPortalBlock
                                                     || block instanceof EndGatewayBlock
                                                     || block instanceof GlazedTerracottaBlock
                                                     || block instanceof SpawnerBlock
                                                     || block instanceof BonemealableBlock));
    }

    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public void tick() {
        if (this.mainGoal.canBreakBlock()) {
            ZombieBreakGoal zombieBreakGoal = this.mainGoal.zombieBreakGoal.goal;
            if (zombieBreakGoal.isDone() && this.mainGoal.isFree()) {

                if (this.targetPos == null) {
                    BlockPos pos = null;
                    for (int i = 1; i <= 20; i++) {
                        pos = this.tryToGetPos();
                        if (pos != null) {
                            break;
                        }
                    }
                    this.targetPos = pos;
                } else {
                    if (!isTypeCorrect(this.zombie.getLevel().getBlockState(this.targetPos))) {
                        this.targetPos = null;
                    }
                }

                if (this.targetPos != null) {

                    if (this.mainGoal.zombieRandomWalkGoal.goal.isWalking()) {
                        this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
                    }
                    if (this.zombie.getNavigation().isDone()) {
                        Path path = this.zombie.getNavigation().createPath(this.targetPos, 3);
                        if (path != null) {
                            this.zombie.getNavigation().moveTo(path, this.mainGoal.getZombieSpeedBaseNormal() * 0.7d);
                        }
                    }
                    zombieBreakGoal.checkToStartBreak(this.targetPos);
                }
            }
        }
    }

    private @Nullable BlockPos tryToGetPos() {
        int a = RandomUtils.RANDOM.nextInt(-8, 8);
        int b = RandomUtils.RANDOM.nextInt(-8, 8);
        int c = RandomUtils.RANDOM.nextInt(-2, 4);
        BlockPos posZombie = this.zombie.getOnPos();
        BlockPos pos = new BlockPos(posZombie.getX() + a, posZombie.getY() + c, posZombie.getZ() + b);
        BlockState state = this.zombie.getLevel().getBlockState(pos);
        if (ForgeHooks.canEntityDestroy(this.zombie.getLevel(), pos, this.zombie) && isTypeCorrect(state)) {
            return pos;
        }
        return null;
    }
}
