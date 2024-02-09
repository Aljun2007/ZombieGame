package com.aljun.core;

import net.minecraft.dispenser.Position;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class GoalUtils {
    public GoalUtils() {
    }

    public static boolean hasGroundPathNavigation(MobEntity p_26895_) {
        return p_26895_.getNavigation() instanceof GroundPathNavigator;
    }

    public static boolean mobRestricted(CreatureEntity p_148443_, int p_148444_) {
        BlockPos pos = p_148443_.getRestrictCenter();
        Vector3d p = p_148443_.position();
        double $$3 = (double)pos.getX() + 0.5 - p.x;
        double $$4 = (double)pos.getY() + 0.5 - p.y;
        double $$5 = (double)pos.getZ() + 0.5 - p.z;
        return p_148443_.hasRestriction() && $$3 * $$3 + $$4 * $$4 + $$5 * $$5 < (float) ((p_148443_.getRestrictRadius() + p_148444_) + 1.0);
    }

    public static boolean isOutsideLimits(BlockPos p_148452_, CreatureEntity p_148453_) {
        return p_148452_.getY() < 0 || p_148452_.getY() > p_148453_.level.getMaxBuildHeight();
    }

    public static boolean isRestricted(boolean p_148455_, CreatureEntity p_148456_, BlockPos p_148457_) {
        return p_148455_ && !p_148456_.isWithinRestriction(p_148457_);
    }

    public static boolean isNotStable(PathNavigator p_148449_, BlockPos p_148450_) {
        return !p_148449_.isStableDestination(p_148450_);
    }

    public static boolean isWater(CreatureEntity p_148446_, BlockPos p_148447_) {
        return p_148446_.level.getFluidState(p_148447_).is(FluidTags.WATER);
    }

    public static boolean hasMalus(CreatureEntity p_148459_, BlockPos p_148460_) {
        return p_148459_.getPathfindingMalus(WalkNodeProcessor.getBlockPathTypeStatic(p_148459_.level, p_148460_.mutable())) != 0.0F;
    }

    public static boolean isSolid(CreatureEntity p_148462_, BlockPos p_148463_) {
        return p_148462_.level.getBlockState(p_148463_).getMaterial().isSolid();
    }
}