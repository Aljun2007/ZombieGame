package com.aljun.zombiegame.work.zombie.goal.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.apache.logging.log4j.util.BiConsumer;

import java.util.HashMap;
import java.util.function.Predicate;

public class PathFinder {

    private final BiConsumer<BlockPos, BlockPos> changeSelfPos;
    private final Predicate<HashMap<Integer, BlockPacket>> checkIfDone;
    private boolean firstStructure = true;

    private Structure structure = Structure.NORTH;
    private int progress = -1;
    private BlockPos selfPos;
    private BlockPos targetPos;
    private boolean isDone = true;
    private boolean isLeftHand = false;

    public PathFinder(BiConsumer<BlockPos, BlockPos> changeSelfPos,
                      Predicate<HashMap<Integer, BlockPacket>> checkIfDone) {
        this.changeSelfPos = changeSelfPos;
        this.checkIfDone = checkIfDone;
    }

    private static BlockPos getEndBlockPos(BlockPos pos, Structure structure) {
        return structure.getHeight() == Height.NONE ? structure.getBlockPos(6, pos) : structure.getBlockPos(8, pos);
    }

    private static BlockPos getStartBlockPos(BlockPos pos, Structure structure) {
        return pos;
    }

    public BlockPacket getBlock() {
        return isDone ? BlockPacket.EMPTY : new BlockPacket(this.structure.getNextBlockKind(this.progress),
                this.structure.getBlockPos(this.progress, this.selfPos));
    }

    public void start(BlockPos selfPos, BlockPos targetPos) {
        if (this.isDone) {
            this.load(selfPos, targetPos);
            this.isDone = false;
            this.firstStructure = true;
            this.newStructure();
            this.firstStructure = false;
        }
    }

    public void stop() {
        if (!this.isDone) {
            this.isDone = true;
            this.progress = -1;
        }
    }

    public void setLeftHand(boolean leftHand) {
        isLeftHand = leftHand;
    }

    public boolean isDone() {
        return isDone;
    }

    private void load(BlockPos selfPos, BlockPos targetPos) {
        if (this.isDone) {
            this.selfPos = selfPos;
            this.targetPos = targetPos;
        }
    }

    public void next() {
        if (!this.isDone) {
            if (this.progress < this.structure.getTotalBlockNum()) {
                this.progress++;
            } else {
                HashMap<Integer, BlockPacket> hash = new HashMap<>();
                for (int i = 1; i <= this.structure.getTotalBlockNum(); i++) {
                    hash.put(i, new BlockPacket(this.structure.getNextBlockKind(this.progress),
                            this.structure.getBlockPos(this.progress, this.selfPos)));
                }
                if (this.checkIfDone.test(hash)) {
                    this.newStructure();
                } else {
                    this.progress = 1;
                }
            }
        }
    }

    private void newStructure() {
        if (!this.isDone) {
            this.progress = 1;

            Structure newStructure;

            if (!this.firstStructure) {
                this.changeSelfPos.accept(this.selfPos, getEndBlockPos(this.selfPos, this.structure));
                this.selfPos = getEndBlockPos(this.selfPos, this.structure);
            }

            int x = this.selfPos.getX() - this.targetPos.getX();
            int y = this.selfPos.getY() - this.targetPos.getY();
            int z = this.selfPos.getZ() - this.targetPos.getZ();

            Direction direction;
            Height height;

            if (-1 <= x && x <= 1 && -1 <= z && z <= 1 && !(-1 <= y && y <= 1)) {
                if ((x == 0 && z == -1) || (x == -1 && z == -1)) {
                    direction = Direction.EAST;
                } else if (x == -1) {
                    direction = Direction.NORTH;
                } else if ((x == 1 && z == 1) || (x == 0 && z == 1)) {
                    direction = Direction.WEST;
                } else if (x == 1) {
                    direction = Direction.SOUTH;
                } else {
                    direction = Tools.randomDirection2();
                }
            } else if (x == 0 && -1 <= y && y <= 1 && z == 0) {
                this.isDone = true;
                return;
            } else if (z <= x && z < -x) {
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

            if (y > 0) {
                height = Height.DOWN;
            } else if (y < 0) {
                height = Height.UP;
            } else {
                height = Height.NONE;
            }
            newStructure = Structure.change(direction, height);

            this.structure = newStructure;

        }
    }


}
