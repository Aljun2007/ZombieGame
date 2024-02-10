package com.aljun.zombiegame.work.zombie.goal.tool;

import net.minecraft.core.BlockPos;

public class BlockPacket {
    public static final BlockPacket EMPTY = (new BlockPacket(BlockKind.AIR,
            new BlockPos(0, 0, 0))).setEmpty();
    public final BlockKind blockKind;
    public final BlockPos blockPos;
    private boolean isEmpty;

    public BlockPacket(BlockKind blockKind, BlockPos blockPos) {
        this.blockKind = blockKind;
        this.blockPos = blockPos;
        this.isEmpty = false;
    }

    private BlockPacket setEmpty() {
        this.isEmpty = true;
        return this;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
