package com.aljun.zombiegame.work.zombie.goal.tool;

public enum Height {
    DOWN(8), UP(8), NONE(6);
    public final int limit;

    Height(int limit) {
        this.limit = limit;
    }
}
