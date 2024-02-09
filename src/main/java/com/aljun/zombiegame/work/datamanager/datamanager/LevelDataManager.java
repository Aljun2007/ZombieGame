package com.aljun.zombiegame.work.datamanager.datamanager;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.keyset.KeySet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class LevelDataManager {

    private static CompoundNBT getTagWithSetDirty(ServerWorld serverLevel) {
        LocalDataSaver saver = serverLevel.getDataStorage().computeIfAbsent(LocalDataSaver::new,
                LocalDataSaver.NAME);
        saver.setDirty();
        return saver.tag;
    }

    private static CompoundNBT getTag(ServerWorld serverLevel) {
        LocalDataSaver saver = serverLevel.getDataStorage().computeIfAbsent(LocalDataSaver::new,
                LocalDataSaver.NAME);
        return saver.tag;
    }

    public static <V> V getOrDefault(ServerWorld serverLevel, KeySet<V> keySet) {
        return getOrDefault(serverLevel, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> V getOrDefault(ServerWorld serverLevel, String key, V value) {
        CompoundNBT tag = getTag(serverLevel);
        if (tag == null) {
            return value;
        }
        return DataManager.getOrDefault(tag, key, value);
    }

    public static <V> V getOrCreate(ServerWorld serverLevel, String key, V value) {
        checkToCreate(serverLevel, key, value);
        return DataManager.getOrCreate(getTag(serverLevel), key, value);
    }

    public static <V> V getOrCreate(ServerWorld serverLevel, KeySet<V> keySet) {
        return getOrCreate(serverLevel, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> void set(ServerWorld serverLevel, String key, V value) {
        DataManager.set(getTagWithSetDirty(serverLevel), key, value);
    }

    public static <V> void set(ServerWorld serverLevel, KeySet<V> keySet, V value) {
        set(serverLevel, keySet.KEY, value);
    }

    public static <V> void checkToCreate(ServerWorld serverLevel, String key, V value) {
        CompoundNBT tag = getTag(serverLevel);
        if (!tag.contains(key)) {
            set(serverLevel, key, value);
        }
    }

    public static <V> void checkToCreate(ServerWorld serverLevel, KeySet<V> keySet) {
        checkToCreate(serverLevel, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    private static class LocalDataSaver extends WorldSavedData {

        public static final String NAME = ZombieGame.MOD_ID + "_data_saver";
        public CompoundNBT tag;

        public LocalDataSaver(CompoundNBT tag) {
            super(NAME);
            this.tag = tag;
        }

        public LocalDataSaver() {
            super(NAME);
            this.tag = new CompoundNBT();
        }

        @Override
        public void load(CompoundNBT tag) {
            this.tag = tag;
        }

        @Override
        public CompoundNBT save(CompoundNBT tag) {
            return this.tag;
        }
    }

}
