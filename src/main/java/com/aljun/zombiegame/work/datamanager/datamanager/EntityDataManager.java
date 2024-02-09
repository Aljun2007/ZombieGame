package com.aljun.zombiegame.work.datamanager.datamanager;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.keyset.KeySet;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;

public class EntityDataManager {

    public static <V> V getOrDefault(Entity entity, KeySet<V> keySet) {
        return getOrDefault(entity, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> V getOrDefault(Entity entity, String key, V value) {
        if (!entity.getPersistentData().contains(ZombieGame.MOD_ID)) {
            return value;
        }
        return DataManager.getOrDefault(entity.getPersistentData().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> V getOrCreate(Entity entity, String key, V value) {
        if (!entity.getPersistentData().contains(ZombieGame.MOD_ID)) {
            entity.getPersistentData().put(ZombieGame.MOD_ID, new CompoundNBT());
        }
        return DataManager.getOrCreate(entity.getPersistentData().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> V getOrCreate(Entity entity, KeySet<V> keySet) {
        return getOrCreate(entity, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> void set(Entity entity, String key, V value) {
        if (!entity.getPersistentData().contains(ZombieGame.MOD_ID)) {
            entity.getPersistentData().put(ZombieGame.MOD_ID, new CompoundNBT());
        }
        DataManager.set(entity.getPersistentData().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> void set(Entity entity, KeySet<V> keySet, V value) {
        set(entity, keySet.KEY, value);
    }

    public static <V> void checkToCreate(Entity entity, String key, V value) {
        if (!entity.getPersistentData().contains(ZombieGame.MOD_ID)) {
            entity.getPersistentData().put(ZombieGame.MOD_ID, new CompoundNBT());
        }
        DataManager.checkToCreate(entity.getPersistentData().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> void checkToCreate(Entity entity, KeySet<V> keySet) {
        checkToCreate(entity, keySet.KEY, keySet.DEFAULT_VALUE);
    }

}
