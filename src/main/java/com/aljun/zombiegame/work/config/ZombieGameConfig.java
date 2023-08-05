package com.aljun.zombiegame.work.config;

import com.aljun.zombiegame.work.datamanager.datamanager.ConfigDataManager;
import com.aljun.zombiegame.work.keyset.KeySet;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ZombieGameConfig {
    private static final HashMap<KeySet<?>, ForgeConfigSpec.ConfigValue<?>> configMap = new HashMap<>();
    public static ForgeConfigSpec commonConfig;
    public static ForgeConfigSpec.ConfigValue<List<String>> blockChoosingWhiteList;
    public static ForgeConfigSpec.ConfigValue<List<String>> blockChoosingBlackList;
    public static ForgeConfigSpec.ConfigValue<List<String>> blockBreakingBlackList;
    public static ForgeConfigSpec.ConfigValue<List<String>> targetChoosingWhiteList;
    public static ForgeConfigSpec.ConfigValue<List<String>> targetChoosingBlackList;
    public static ForgeConfigSpec.ConfigValue<List<String>> replaceEntityWhiteList;
    public static ForgeConfigSpec.ConfigValue<List<String>> replaceEntityBlackList;
    public static ForgeConfigSpec.ConfigValue<List<String>> removeEntityWhiteList;
    public static ForgeConfigSpec.ConfigValue<List<String>> removeEntityBlackList;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();


        builder.push("general");
        ConfigDataManager.load(builder);
        builder.pop();

        builder.push("choosing_blocks");

        builder.comment("Zombie will try to break beds, chests, doors, machines and so on!");
        List<String> ar1 = new ArrayList<>();
        blockChoosingWhiteList = builder.define("ChoosingBlock WhiteList", ar1);

        List<String> ar2 = new ArrayList<>();
        blockChoosingBlackList = builder.define("ChoosingBlock BlackList", ar2);

        builder.pop();
        builder.push("breaking_blocks");

        List<String> ar3 = new ArrayList<>();
        blockBreakingBlackList = builder.define("blockBreaking BlackList", ar3);

        builder.pop();
        builder.push("target_choosing");

        List<String> ar4 = new ArrayList<>();
        ar4.add("guardvillagers:guard");
        targetChoosingWhiteList = builder.define("TargetChoosing WhiteList", ar4);
        List<String> ar5 = new ArrayList<>();
        targetChoosingBlackList = builder.define("TargetChoosing BlackList", ar5);

        builder.pop();
        builder.push("zombie_spawn");

        builder.comment("The following creatures will be replaced with zombies");
        List<String> ar6 = new ArrayList<>();
        replaceEntityWhiteList = builder.define("ReplaceEntity WhiteList", ar6);
        List<String> ar7 = new ArrayList<>();
        replaceEntityBlackList = builder.define("ReplaceEntity BlackList", ar7);
        builder.comment("The following creatures will be removed");
        List<String> ar8 = new ArrayList<>();
        removeEntityWhiteList = builder.define("RemoveEntity WhiteList", ar8);
        List<String> ar9 = new ArrayList<>();
        removeEntityBlackList = builder.define("RemoveEntity BlackList", ar9);

        builder.pop();

        commonConfig = builder.build();
    }

    public static <T> T get(KeySet<T> keySet) {
        if (configMap.containsKey(keySet)) {
            return keySet.DEFAULT_VALUE;
        } else {
            return (T) configMap.get(keySet).get();
        }
    }

    public static <T> void add(ForgeConfigSpec.Builder builder, KeySet<T> keySet, @Nullable String name) {
        if (!configMap.containsKey(keySet)) {
            if (name == null) {
                name = keySet.KEY;
            }
            configMap.put(keySet, builder.define(name, keySet.DEFAULT_VALUE));
        }
    }

    public static <V> boolean contains(KeySet<V> keySet) {
        return configMap.containsKey(keySet);
    }
}