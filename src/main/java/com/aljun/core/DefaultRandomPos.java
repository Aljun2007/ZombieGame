package com.aljun.core;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class DefaultRandomPos {
    public DefaultRandomPos() {
    }

    @Nullable
    public static Vector3d getPos(CreatureEntity p_148404_, int p_148405_, int p_148406_) {
        boolean $$3 = GoalUtils.mobRestricted(p_148404_, p_148405_);
        return RandomPos.generateRandomPos(p_148404_, () -> {
            BlockPos $$4 = RandomPos.generateRandomDirection(p_148405_, p_148406_);
            return generateRandomPosTowardDirection(p_148404_, p_148405_, $$3, $$4);
        });
    }

    @Nullable
    public static Vector3d getPosTowards(CreatureEntity p_148413_, int p_148414_, int p_148415_, Vector3d p_148416_, double p_148417_) {
        Vector3d $$5 = p_148416_.subtract(p_148413_.getX(), p_148413_.getY(), p_148413_.getZ());
        boolean $$6 = GoalUtils.mobRestricted(p_148413_, p_148414_);
        return RandomPos.generateRandomPos(p_148413_, () -> {
            BlockPos $$6x = RandomPos.generateRandomDirectionWithinRadians(p_148414_, p_148415_, 0, $$5.x, $$5.z, p_148417_);
            return $$6x == null ? null : generateRandomPosTowardDirection(p_148413_, p_148414_, $$6, $$6x);
        });
    }

    @Nullable
    public static Vector3d getPosAway(CreatureEntity p_148408_, int p_148409_, int p_148410_, Vector3d p_148411_) {
        Vector3d $$4 = p_148408_.position().subtract(p_148411_);
        boolean $$5 = GoalUtils.mobRestricted(p_148408_, p_148409_);
        return RandomPos.generateRandomPos(p_148408_, () -> {
            BlockPos $$5x = RandomPos.generateRandomDirectionWithinRadians(p_148409_, p_148410_, 0, $$4.x, $$4.z, 1.5707963705062866);
            return $$5x == null ? null : generateRandomPosTowardDirection(p_148408_, p_148409_, $$5, $$5x);
        });
    }

    @Nullable
    private static BlockPos generateRandomPosTowardDirection(CreatureEntity p_148437_, int p_148438_, boolean p_148439_, BlockPos p_148440_) {
        BlockPos $$4 = RandomPos.generateRandomPosTowardDirection(p_148437_, p_148438_, p_148440_);
        return !GoalUtils.isOutsideLimits($$4, p_148437_) && !GoalUtils.isRestricted(p_148439_, p_148437_, $$4) && !GoalUtils.isNotStable(p_148437_.getNavigation(), $$4) && !GoalUtils.hasMalus(p_148437_, $$4) ? $$4 : null;
    }
}