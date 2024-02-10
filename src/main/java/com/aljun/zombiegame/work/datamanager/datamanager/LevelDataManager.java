package com.aljun.zombiegame.work.datamanager.datamanager;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.keyset.KeySet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class LevelDataManager {

    private static CompoundTag getTagWithSetDirty(@NotNull ServerLevel serverLevel) {
        LocalDataSaver saver = serverLevel.getDataStorage().computeIfAbsent(LocalDataSaver::load, LocalDataSaver::new,
                LocalDataSaver.NAME);
        saver.setDirty();
        return saver.tag;
    }

    private static CompoundTag getTag(@NotNull ServerLevel serverLevel) {
        LocalDataSaver saver = serverLevel.getDataStorage().computeIfAbsent(LocalDataSaver::load, LocalDataSaver::new,
                LocalDataSaver.NAME);
        return saver.tag;
    }

    public static <V> V getOrDefault(ServerLevel serverLevel, KeySet<V> keySet) {
        return getOrDefault(serverLevel, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> V getOrDefault(ServerLevel serverLevel, String key, V value) {
        CompoundTag tag = getTag(serverLevel);
        if (tag == null) {
            return value;
        }
        return DataManager.getOrDefault(tag, key, value);
    }

    public static <V> V getOrCreate(ServerLevel serverLevel, String key, V value) {
        checkToCreate(serverLevel, key, value);
        return DataManager.getOrCreate(getTag(serverLevel), key, value);
    }

    public static <V> V getOrCreate(ServerLevel serverLevel, KeySet<V> keySet) {
        return getOrCreate(serverLevel, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> void set(ServerLevel serverLevel, String key, V value) {
        DataManager.set(getTagWithSetDirty(serverLevel), key, value);
    }

    public static <V> void set(ServerLevel serverLevel, KeySet<V> keySet, V value) {
        set(serverLevel, keySet.KEY, value);
    }

    public static <V> void checkToCreate(ServerLevel serverLevel, String key, V value) {
        CompoundTag tag = getTag(serverLevel);
        if (!tag.contains(key)) {
            set(serverLevel, key, value);
        }
    }

    public static <V> void checkToCreate(ServerLevel serverLevel, KeySet<V> keySet) {
        checkToCreate(serverLevel, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    private static class LocalDataSaver extends SavedData {

        public static final String NAME = ZombieGame.MOD_ID + "_data_saver";
        public final CompoundTag tag;

        public LocalDataSaver(CompoundTag tag) {
            this.tag = tag;
        }

        public LocalDataSaver() {
            this.tag = new CompoundTag();
        }

        public static LocalDataSaver load(CompoundTag tag) {
            return new LocalDataSaver(tag);
        }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
            return this.tag;
        }
    }

}
