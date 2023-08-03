package com.aljun.zombiegame.work.datamanager.datamanager;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.keyset.KeySet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackDataManager {

    public static <V> V getOrDefault(ItemStack stack, KeySet<V> keySet) {
        return getOrDefault(stack, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> V getOrDefault(ItemStack stack, String key, V value) {
        if (stack.getTag() == null) {
            return value;
        }

        if (!stack.getTag().contains(ZombieGame.MOD_ID)) {
            return value;
        }
        return DataManager.getOrDefault(stack.getTag().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> V getOrCreate(ItemStack stack, KeySet<V> keySet) {
        return getOrCreate(stack, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> V getOrCreate(ItemStack stack, String key, V value) {
        if (!stack.getOrCreateTag().contains(ZombieGame.MOD_ID)) {
            stack.getOrCreateTag().put(ZombieGame.MOD_ID, new CompoundTag());
        }
        return DataManager.getOrCreate(stack.getOrCreateTag().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> void set(ItemStack stack, String key, V value) {
        if (!stack.getOrCreateTag().contains(ZombieGame.MOD_ID)) {
            stack.getOrCreateTag().put(ZombieGame.MOD_ID, new CompoundTag());
        }
        DataManager.set(stack.getOrCreateTag().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> void set(ItemStack stack, KeySet<V> keySet, V value) {
        set(stack, keySet.KEY, value);
    }

    public static <V> void checkToCreate(ItemStack stack, String key, V value) {
        if (!stack.getOrCreateTag().contains(ZombieGame.MOD_ID)) {
            stack.getOrCreateTag().put(ZombieGame.MOD_ID, new CompoundTag());
        }
        DataManager.checkToCreate(stack.getOrCreateTag().getCompound(ZombieGame.MOD_ID), key, value);
    }

    public static <V> void checkToCreate(ItemStack stack, KeySet<V> keySet) {
        checkToCreate(stack, keySet.KEY, keySet.DEFAULT_VALUE);
    }

}
