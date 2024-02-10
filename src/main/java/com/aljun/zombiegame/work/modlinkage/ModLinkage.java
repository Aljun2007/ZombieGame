package com.aljun.zombiegame.work.modlinkage;

public abstract class ModLinkage {
    private static boolean isFMLSetup = true;
    protected boolean loaded = false;

    public static void stopFMLSetup() {
        ModLinkage.isFMLSetup = false;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded() {
        if (isFMLSetup) this.loaded = true;
    }
}
