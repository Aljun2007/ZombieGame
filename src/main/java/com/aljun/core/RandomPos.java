package com.aljun.core;

import com.aljun.zombiegame.work.RandomUtils;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;


public class RandomPos {
    private static final int RANDOM_POS_ATTEMPTS = 10;

    public RandomPos() {
    }

    public static BlockPos generateRandomDirection(int p_217853_, int p_217854_) {
        int $$3 = RandomUtils.RANDOM.nextInt(2 * p_217853_ + 1) - p_217853_;
        int $$4 = RandomUtils.RANDOM.nextInt(2 * p_217854_ + 1) - p_217854_;
        int $$5 = RandomUtils.RANDOM.nextInt(2 * p_217853_ + 1) - p_217853_;
        return new BlockPos($$3, $$4, $$5);
    }

    @Nullable
    public static BlockPos generateRandomDirectionWithinRadians( int p_217857_, int p_217858_, int p_217859_, double p_217860_, double p_217861_, double p_217862_) {
        double $$7 = MathHelper.atan2(p_217861_, p_217860_) - 1.5707963705062866;
        double $$8 = $$7 + (double)(2.0F * RandomUtils.RANDOM.nextFloat() - 1.0F) * p_217862_;
        double $$9 = Math.sqrt(RandomUtils.RANDOM.nextDouble()) * (double)MathHelper.SQRT_OF_TWO * (double)p_217857_;
        double $$10 = -$$9 * Math.sin($$8);
        double $$11 = $$9 * Math.cos($$8);
        if (!(Math.abs($$10) > (double)p_217857_) && !(Math.abs($$11) > (double)p_217857_)) {
            int $$12 = RandomUtils.RANDOM.nextInt(2 * p_217858_ + 1) - p_217858_ + p_217859_;
            return containing($$10, (double)$$12, $$11);
        } else {
            return null;
        }
    }

    @VisibleForTesting
    public static BlockPos moveUpOutOfSolid(BlockPos p_148546_, int p_148547_, Predicate<BlockPos> p_148548_) {
        if (!p_148548_.test(p_148546_)) {
            return p_148546_;
        } else {
            BlockPos $$3;
            for($$3 = p_148546_.above(); $$3.getY() < p_148547_ && p_148548_.test($$3); $$3 = $$3.above()) {
            }

            return $$3;
        }
    }

    @VisibleForTesting
    public static BlockPos moveUpToAboveSolid(BlockPos p_26948_, int p_26949_, int p_26950_, Predicate<BlockPos> p_26951_) {
        if (p_26949_ < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + p_26949_ + ", expected >= 0");
        } else if (!p_26951_.test(p_26948_)) {
            return p_26948_;
        } else {
            BlockPos $$4;
            for($$4 = p_26948_.above(); $$4.getY() < p_26950_ && p_26951_.test($$4); $$4 = $$4.above()) {
            }

            BlockPos $$5;
            BlockPos $$6;
            for($$5 = $$4; $$5.getY() < p_26950_ && $$5.getY() - $$4.getY() < p_26949_; $$5 = $$6) {
                $$6 = $$5.above();
                if (p_26951_.test($$6)) {
                    break;
                }
            }

            return $$5;
        }
    }

    @Nullable
    public static Vector3d generateRandomPos(CreatureEntity p_148543_, Supplier<BlockPos> p_148544_) {
        Objects.requireNonNull(p_148543_);
        return generateRandomPos(p_148544_, p_148543_::getWalkTargetValue);
    }

    @Nullable
    public static Vector3d generateRandomPos(Supplier<BlockPos> p_148562_, ToDoubleFunction<BlockPos> p_148563_) {
        double $$2 = Double.NEGATIVE_INFINITY;
        BlockPos $$3 = null;

        for(int $$4 = 0; $$4 < 10; ++$$4) {
            BlockPos $$5 = (BlockPos)p_148562_.get();
            if ($$5 != null) {
                double $$6 = p_148563_.applyAsDouble($$5);
                if ($$6 > $$2) {
                    $$2 = $$6;
                    $$3 = $$5;
                }
            }
        }

        return $$3 != null ? Vector3d.atBottomCenterOf($$3) : null;
    }

    public static BlockPos generateRandomPosTowardDirection(CreatureEntity p_217864_, int p_217865_, BlockPos p_217867_) {
        int $$4 = p_217867_.getX();
        int $$5 = p_217867_.getZ();
        if (p_217864_.hasRestriction() && p_217865_ > 1) {
            BlockPos $$6 = p_217864_.getRestrictCenter();
            if (p_217864_.getX() > (double)$$6.getX()) {
                $$4 -= RandomUtils.RANDOM.nextInt(p_217865_ / 2);
            } else {
                $$4 += RandomUtils.RANDOM.nextInt(p_217865_ / 2);
            }

            if (p_217864_.getZ() > (double)$$6.getZ()) {
                $$5 -= RandomUtils.RANDOM.nextInt(p_217865_ / 2);
            } else {
                $$5 += RandomUtils.RANDOM.nextInt(p_217865_ / 2);
            }
        }

        return containing((double)$$4 + p_217864_.getX(), (double)p_217867_.getY() + p_217864_.getY(), (double)$$5 + p_217864_.getZ());
    }

    public static BlockPos containing(double p_275310_, double p_275414_, double p_275737_) {
        return new BlockPos(MathHelper.floor(p_275310_), MathHelper.floor(p_275414_), MathHelper.floor(p_275737_));
    }
}