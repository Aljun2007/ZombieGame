package com.aljun.zombiegame.work.modlinkage;

public class GuardVillagersModLinkage extends ModLinkage {

    private static final GuardVillagersModLinkage INSTANCE = new GuardVillagersModLinkage();

    public static GuardVillagersModLinkage get() {
        return INSTANCE;
    }
}
