package com.aljun.zombiegame.work.zombie.goal.tool;

import com.aljun.zombiegame.work.RandomUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;


public class Tools {

    public static Direction randomDirection2() {
        int i = RandomUtils.RANDOM.nextInt(4);
        if (i == 0) {
            return Direction.NORTH;
        } else if (i == 1) {
            return Direction.SOUTH;
        } else if (i == 2) {
            return Direction.WEST;
        } else {
            return Direction.EAST;
        }
    }

    public static Vector3d blockPosToVec3(BlockPos pos) {
        return new Vector3d(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d);
    }
}
