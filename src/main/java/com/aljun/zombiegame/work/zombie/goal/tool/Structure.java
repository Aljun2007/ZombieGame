package com.aljun.zombiegame.work.zombie.goal.tool;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public enum Structure {

    NORTH(Direction.NORTH, Height.NONE), SOUTH(Direction.SOUTH, Height.NONE),
    WEST(Direction.WEST, Height.NONE), EAST(Direction.EAST, Height.NONE),
    NORTH_UP(Direction.NORTH, Height.UP), SOUTH_UP(Direction.SOUTH, Height.UP),
    WEST_UP(Direction.WEST, Height.UP), EAST_UP(Direction.EAST, Height.UP),

    NORTH_DOWN(Direction.NORTH, Height.DOWN), SOUTH_DOWN(Direction.SOUTH, Height.DOWN),
    WEST_DOWN(Direction.WEST, Height.DOWN), EAST_DOWN(Direction.EAST, Height.DOWN),
    ;

    private final Direction direction;
    private final Height height;

    Structure(Direction direction, Height height) {
        this.direction = direction;
        this.height = height;
    }

    public static Structure change(Direction direction, Height height) {
        if (direction == Direction.SOUTH) {
            if (height == Height.UP) {
                return Structure.SOUTH_UP;
            } else if (height == Height.DOWN) {
                return Structure.SOUTH_DOWN;
            } else {
                return Structure.SOUTH;
            }
        } else if (direction == Direction.EAST) {
            if (height == Height.UP) {
                return Structure.EAST_UP;
            } else if (height == Height.DOWN) {
                return Structure.EAST_DOWN;
            } else {
                return Structure.EAST;
            }
        } else if (direction == Direction.WEST) {
            if (height == Height.UP) {
                return Structure.WEST_UP;
            } else if (height == Height.DOWN) {
                return Structure.WEST_DOWN;
            } else {
                return Structure.WEST;
            }
        } else {
            if (height == Height.UP) {
                return Structure.NORTH_UP;
            } else if (height == Height.DOWN) {
                return Structure.NORTH_DOWN;
            } else {
                return Structure.NORTH;
            }
        }
    }

    private BlockPos addNum(BlockPos pos, int x, int y) {
        return new BlockPos(pos).relative(this.direction, x).above(y);
    }

    public BlockPos getBlockPos(int i, BlockPos stand) {
        if (this.height == Height.NONE) {
            switch (i) {
                case 1:
                    return this.addNum(stand, 0, -1);
                case 2:
                    return this.addNum(stand, 0, 1);
                case 3:
                    return this.addNum(stand, 0, 0);
                case 4:
                    return this.addNum(stand, 1, -1);
                case 5:
                    return this.addNum(stand, 1, 1);
                case 6:
                    return this.addNum(stand, 1, 0);
                default:
                    return stand;
            }
        } else if (this.height == Height.DOWN) {
            switch (i) {
                case 1:
                    return this.addNum(stand, 0, -1);
                case 2:
                    return this.addNum(stand, 0, 1);
                case 3:
                    return this.addNum(stand, 0, 0);
                case 4:
                    return this.addNum(stand, 0, -2);
                case 5:
                    return this.addNum(stand, 1, -2);
                case 6:
                    return this.addNum(stand, 1, 1);
                case 7:
                    return this.addNum(stand, 1, 0);
                case 8:
                    return this.addNum(stand, 1, -1);
                default:
                    return stand;
            }
        } else if (this.height == Height.UP) {
            switch (i) {
                case 1:
                    return this.addNum(stand, 0, -1);
                case 2:
                    return this.addNum(stand, 0, 1);
                case 3:
                    return this.addNum(stand, 0, 0);
                case 4:
                    return this.addNum(stand, 0, 2);
                case 5:
                    return this.addNum(stand, 1, -1);
                case 6:
                    return this.addNum(stand, 1, 0);
                case 7:
                    return this.addNum(stand, 1, 2);
                case 8:
                    return this.addNum(stand, 1, 1);
                default:
                    return stand;
            }
        } else {
            return stand;
        }
    }

    public BlockKind getNextBlockKind(int i) {
        if (this.height == Height.NONE) {
            switch (i) {
                case 1: case 4:
                    return BlockKind.BLOCK;
                case 2: case 3: case 5: case 6:
                    return BlockKind.AIR;
                default:
                    throw new IllegalArgumentException(
                        "getNextBlockKind(int BlockPos): int \"" + String.valueOf(i)
                                + "\" is too big!");
            }
        } else if (this.height == Height.DOWN) {
            switch (i) {
                case 1:
                case 4:
                case 5:
                    return BlockKind.BLOCK;
                case 2:
                case 3:
                case 6:
                case 7:
                case 8:
                    return BlockKind.AIR;
                default:
                    throw new IllegalArgumentException(
                            "getNextBlockKind(int BlockPos): int \"" + String.valueOf(i)
                                    + "\" is too big!");
            }
        } else if (this.height == Height.UP) {
            switch (i) {
                case 1: case 5: case 6:
                    return BlockKind.BLOCK;
                case 2: case 3: case 4: case 7: case 8:
                    return BlockKind.AIR;
                default:
                    throw new IllegalArgumentException(
                        "getNextBlockKind(int BlockPos): int \"" + String.valueOf(i)
                                + "\" is too big!");
            }
        } else {
            return null;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public Height getHeight() {
        return height;
    }

    public int getTotalBlockNum() {
        return this.height.limit;
    }
}
