package com.aljun.zombiegame.work.game;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.keyset.ConfigKeySets;
import com.aljun.zombiegame.work.datamanager.datamanager.ConfigDataManager;
import com.aljun.zombiegame.work.datamanager.datamanager.EntityDataManager;
import com.aljun.zombiegame.work.keyset.EntityKeySets;
import com.aljun.zombiegame.work.keyset.KeySets;
import com.aljun.zombiegame.work.datamanager.datamanager.LevelDataManager;
import com.aljun.zombiegame.work.keyset.LevelKeySets;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.server.ServerWorld;

public class GameProperty {
    public static boolean startGame = false;

    public static boolean isStartGame(ServerWorld level) {
        level = level.getServer().overworld();
        return LevelDataManager.getOrCreate(level, LevelKeySets.START_GAME);
    }

    public static boolean isStartGame() {
        return startGame;
    }

    public static boolean setStartGame(ServerWorld level) {
        if (isStartGame(level)) {
            return false;
        }
        level = level.getServer().overworld();
        LevelDataManager.set(level, LevelKeySets.START_GAME, true);
        TimeProperty.setGameTime(level, 0L);
        return true;
    }

    public static void cancelGame(ServerWorld level) {
        if (!isStartGame(level)) {
            return;
        }
        level = level.getServer().overworld();
        TimeProperty.setGameTime(level, 0L);
        LevelDataManager.set(level, LevelKeySets.START_GAME, false);
    }

    public static boolean hasGameBeenOn(ServerWorld level) {
        return isStartGame(level) && TimeProperty.getGameTime(level) < TimeProperty.getDayTotal(level);
    }

    public static void setMode(ServerWorld level, String mode) {
        level = level.getServer().overworld();
        LevelDataManager.set(level, LevelKeySets.MODE, mode);
    }

    public static String getMode(ServerWorld level) {
        level = level.getServer().overworld();
        return LevelDataManager.getOrDefault(level, LevelKeySets.MODE);
    }

    public static void tick(ServerWorld overworld) {
        GameProperty.startGame = GameProperty.isStartGame(overworld);
        GameProperty.TimeProperty.gameStage = GameProperty.TimeProperty.getGameStage(overworld);
        GameProperty.ZombieProperty.zombieMaxCount = getZombieCount(overworld);
    }

    private static int getZombieCount(ServerWorld overworld) {
        if (GameProperty.TimeProperty.getGameStage() >= 1.0d) {
            return 0;
        }

        int x1 = (int) (overworld.getDayTime() + 24000 * LevelDataManager.getOrCreate(overworld, KeySets.GAME_DAY_FIX));
        int x2 = Math.floorMod(x1, 24000);
        x1 = Math.max(x1, x2);

        double initialCount = GameProperty.ZombieProperty.getInitialZombieCount();
        double finalCount = GameProperty.ZombieProperty.getFinalZombieCount();

        double g = initialCount / 2400000d * x1 + finalCount - initialCount;
        double h = Math.sin(Math.PI * x2 / 12000);


        return Math.max(0, (int) (g / 2 * (1 - h)));
    }

    public static class TimeProperty {
        public static double gameStage = 0.0d;

        public static int getDayTotal(ServerWorld level) {
            level = level.getServer().overworld();
            return LevelDataManager.getOrDefault(level, LevelKeySets.DAY_TOTAL);
        }


        public static long getGameTime(ServerWorld level) {
            level = level.getServer().overworld();
            return Math.max(0, level.getDayTime() / 24000L + LevelDataManager.getOrCreate(level, KeySets.GAME_DAY_FIX));
        }

        public static void setGameTime(ServerWorld level, long day) {
            level = level.getServer().overworld();
            LevelDataManager.set(level, KeySets.GAME_DAY_FIX,
                    Math.max(-level.getDayTime() / 24000L, -level.getDayTime() / 24000L + day));
        }

        public static double getGameStage(ServerWorld level) {
            return getMode(level).equals("normal") ? Math.min(1d, (double) getGameTime(level) / getDayTotal(level)) :
                    getRemainModeGameStage(level);
        }

        public static double getGameStage() {
            return gameStage;
        }


        public static double getRemainModeGameStage(ServerWorld level) {
            return LevelDataManager.getOrDefault(level, LevelKeySets.REMAIN_MODE_GAME_STAGE);
        }

        public static void setRemainModeGameStage(ServerWorld level, double remainingGameStage) {
            LevelDataManager.set(level, LevelKeySets.REMAIN_MODE_GAME_STAGE, remainingGameStage);
        }

        public static String getMode(ServerWorld level) {
            level = level.getServer().overworld();
            return LevelDataManager.getOrDefault(level, LevelKeySets.MODE);
        }

        public static void setDayTotal(ServerWorld level, int dayTotal) {
            level = level.getServer().overworld();
            LevelDataManager.set(level, LevelKeySets.DAY_TOTAL, dayTotal);
        }
    }

    public static class ZombieProperty {
        public static int zombieMaxCount = 70;

        public static double getZombieWalkSpeedPlus() {
            return ConfigDataManager.getAverageByGameStage(ConfigKeySets.RUN_SPEED_INIT, ConfigKeySets.RUN_SPEED_FINAL);
        }

        public static double getZombieAttackValue() {
            return ConfigDataManager.getAverageByGameStage(ConfigKeySets.ATTACK_VALUE_INIT,
                    ConfigKeySets.ATTACK_VALUE_FINAL);

        }

        public static double getZombieArmor() {
            return ConfigDataManager.getAverageByGameStage(ConfigKeySets.ARMOR_INIT, ConfigKeySets.ARMOR_FINAL);
        }

        public static double getZombieBreakSpeedBase() {
            return ConfigDataManager.getAverageByGameStage(ConfigKeySets.BREAK_SPEED_INIT,
                    ConfigKeySets.BREAK_SPEED_FINAL);
        }

        public static double getZombieHearAndSmellBase() {
            return 1.0d;
        }

        public static boolean isSunSensitive(ZombieEntity zombie) {
            if (ZombieUtils.canBeLoaded(zombie)) {
                return EntityDataManager.getOrDefault(zombie, EntityKeySets.IS_SUN_SENSITIVE);
            }
            return zombie instanceof HuskEntity;
        }

        public static double getArmorImproveSpeed() {
            return 5.3; // 5 <= a <= 5.6
        }

        public static double getInitialArmorLevel() {
            return -0.8; // -1 <= b <= -0.6
        }

        public static double getEnchantChance() {
            return 0.3d + 0.7d * TimeProperty.getGameStage();
        }

        public static int getZombieMaxCount() {
            return zombieMaxCount;
        }

        public static int getInitialZombieCount() {
            return ConfigDataManager.getOrDefault(ConfigKeySets.ZOMBIE_COUNT_INIT);
        }

        public static int getFinalZombieCount() {
            return ConfigDataManager.getOrDefault(ConfigKeySets.ZOMBIE_COUNT_FINAL);
        }

        public static boolean convertsInWater(ZombieEntity zombie) {
            if (ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
                return EntityDataManager.getOrDefault(zombie, EntityKeySets.CONVERTS_IN_WATER);
            } else {
                return false;
            }
        }

        public static double getZombieSenseHeight() {
            return RandomUtils.nextBoolean(0.05d) ? 500d : 30d;
        }
    }


}
