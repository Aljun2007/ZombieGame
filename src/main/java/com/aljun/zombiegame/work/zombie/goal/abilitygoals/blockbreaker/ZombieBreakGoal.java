package com.aljun.zombiegame.work.zombie.goal.abilitygoals.blockbreaker;

import com.aljun.zombiegame.work.config.ZombieGameConfig;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.tool.Tools;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZombieBreakGoal extends AbstractZombieAbilityGoal {

    public static final List<Block> BLACK_LIST = new ArrayList<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    static {
        BLACK_LIST.add(Blocks.BEDROCK);
        BLACK_LIST.add(Blocks.END_PORTAL_FRAME);
        BLACK_LIST.add(Blocks.END_PORTAL);
        BLACK_LIST.add(Blocks.NETHER_PORTAL);
    }

    private float progress = 0;

    private BlockState state = Blocks.AIR.defaultBlockState();

    private Block block = Blocks.AIR;

    private BlockPos pos = new BlockPos(0, 0, 0);

    private boolean isDone = true;

    private ServerLevel level;
    private long startTime = 0L;

    public ZombieBreakGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
        this.level = (ServerLevel) zombie.getLevel();
    }

    private static float getBreakProgress(BlockState state, Zombie zombie, BlockPos pos) {

        ItemStack stack = zombie.getItemBySlot(EquipmentSlot.MAINHAND);

        float f = state.getDestroySpeed(zombie.getLevel(), pos);
        if (f == -1.0F) {
            return 0;
        } else {
            boolean a = !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
            int i = a ? 30 : 100;
            return (float) ((getDigSpeed(state, pos, stack, zombie) / f / (float) i)
                            * GameProperty.ZombieProperty.getZombieBreakSpeedBase());
        }
    }

    // These are methods that can be normally used out of ZombieBreakGoal

    public static float getDigSpeed(BlockState state, @Nullable BlockPos pos, ItemStack stack, Zombie zombie) {
        float f = stack.getDestroySpeed(state);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getBlockEfficiency(zombie);
            if (i > 0 && !stack.isEmpty()) {
                f += (float) (i * i + 1);
            }
        }

        if (MobEffectUtil.hasDigSpeed(zombie)) {
            f *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(zombie) + 1) * 0.2F;
        }

        if (zombie.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1 = switch (Objects.requireNonNull(zombie.getEffect(MobEffects.DIG_SLOWDOWN)).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            f *= f1;
        }

        if (zombie.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) && !EnchantmentHelper.hasAquaAffinity(zombie)) {
            f /= 5.0F;
        }

        if (!zombie.isOnGround()) {
            f /= 5.0F;
        }

        return f;
    }

    private static void destroyBlock(Zombie zombie, BlockPos pos) {
        ItemStack stack = zombie.getItemBySlot(EquipmentSlot.MAINHAND);
        BlockState state = zombie.getLevel().getBlockState(pos);
        Level level = zombie.getLevel();
        zombieDestroy(level, zombie, pos, state, level.getBlockEntity(pos), stack);
        level.destroyBlock(pos, false, zombie);

    }

    public static void zombieDestroy(Level level, Zombie zombie, BlockPos pos, BlockState state,
                                     @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (!state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state)) {
            Block.dropResources(state, level, pos, blockEntity, zombie, stack);
        }
    }

    public boolean isDone() {
        return isDone;
    }

    // These are Overriding methods.

    public boolean checkToStartBreak(BlockPos pos) {
        if (!this.mainGoal.canBreakBlock()) {
            return false;
        }

        if (checkCanStart(pos, this.zombie.getLevel().getBlockState(pos))) {
            this.startBreak(pos);
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        this.failBreak();
    }

    @Override
    public boolean canBeUsed() {
        return !this.isDone && this.mainGoal.canBreakBlock();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canBeUsed();
    }

    @Override
    public void tick() {

        if (isDone()) return;

        if (checkCanContinue(this.pos)) {
            if (this.zombie.getTarget() == null) {
                this.zombie.getLookControl().setLookAt(Tools.blockPosToVec3(pos));
            }
            this.state = this.level.getBlockState(this.pos);
            this.progress += getBreakProgress(this.state, this.zombie, this.pos);
            if (this.progress >= 1f) {
                if (this.checkCanPerformBreak(this.pos, this.state)) {
                    this.succeedBreak();
                }
            } else {
                this.level.destroyBlockProgress(this.zombie.getId(), this.pos, (int) (progress * 10f) - 1);
                if (!this.zombie.swinging) {
                    this.zombie.swing(InteractionHand.MAIN_HAND);
                }
                if ((this.zombie.getLevel().getGameTime() - this.startTime) % 4L == 0) {
                    SoundType soundType = this.state.getSoundType();
                    this.zombie.getLevel().playSound(null, this.pos, soundType.getHitSound(), SoundSource.BLOCKS,
                            (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F);
                }
            }
        } else {
            this.failBreak();
        }
    }

    // Simple Tools

    public void failBreak() {
        if (!this.isDone()) {
            if (!this.mainGoal.zombiePathBuilderGoal.goal.isDone()) {
                this.mainGoal.zombiePathBuilderGoal.goal.stopBuild();
            }
            this.level.destroyBlockProgress(this.zombie.getId(), pos, -1);
            this.isDone = true;
        }
    }

    private void startBreak(BlockPos pos) {
        this.pos = pos;
        this.level = (ServerLevel) zombie.getLevel();
        this.state = this.level.getBlockState(pos);
        this.block = state.getBlock();
        this.progress = 0f;
        this.isDone = false;
        this.startTime = this.zombie.getLevel().getGameTime();
        if (!this.zombie.isSwimming()) {
            this.zombie.swing(InteractionHand.MAIN_HAND);
        }
    }

    private void succeedBreak() {
        if (ForgeHooks.canEntityDestroy(this.zombie.getLevel(), pos, this.zombie)) {
            destroyBlock(this.zombie, this.pos);
        }
        this.level.destroyBlockProgress(this.zombie.getId(), pos, -1);
        this.isDone = true;
    }

    // These are to tentatively check if the block is allowed to break.
    private boolean isPositionCorrect(BlockPos pos) {
        return this.zombie.getLevel().getBlockState(pos).canEntityDestroy(this.zombie.getLevel(), pos, this.zombie)
               && !this.zombie.getLevel().isOutsideBuildHeight(pos)
               && this.zombie.getEyePosition().distanceTo(Tools.blockPosToVec3(pos)) <= 3d;
    }

    private boolean isStateCorrect(BlockState state) {

        Block block2 = state.getBlock();

        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block2);
        String id = "";
        if (key != null) {
            id = key.toString();
        }

        boolean canBreak = !BLACK_LIST.contains(block2) && !ZombieGameConfig.blockBreakingBlackList.get().contains(id);

        if (block2 instanceof LiquidBlock || (!(block2 instanceof PowderSnowCauldronBlock)
                                              && block2 instanceof AbstractCauldronBlock)) {
            canBreak = false;
        } else if (block2 instanceof GameMasterBlock) {
            canBreak = false;
        } else {
            canBreak = canBreak && !(state.isAir());
        }

        return canBreak;
    }

    // Final Checking Blocks
    private boolean checkCanContinue(BlockPos pos) {
        return this.isPositionCorrect(pos)
               && this.zombie.isAlive()
               && !this.isDone()
               && this.block == this.state.getBlock();
    }

    private boolean checkCanStart(BlockPos pos, BlockState state) {
        if (this.mainGoal.zombieRandomWalkGoal.goal.isWalking()) {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
        }
        return this.isPositionCorrect(pos)
               && this.zombie.isAlive()
               && this.isStateCorrect(state)
               && this.isDone()
               && !this.mainGoal.zombieRandomWalkGoal.goal.isWalking();
    }

    private boolean checkCanPerformBreak(BlockPos pos, BlockState state) {
        if (this.mainGoal.zombieRandomWalkGoal.goal.isWalking()) {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
        }
        return this.isPositionCorrect(pos)
               && this.zombie.isAlive()
               && this.isStateCorrect(state)
               && !this.isDone()
               && !this.mainGoal.zombieRandomWalkGoal.goal.isWalking();
    }

}
