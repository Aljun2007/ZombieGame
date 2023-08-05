package com.aljun.zombiegame.work.datamanager.datamanager;

import com.aljun.zombiegame.work.config.ZombieGameConfig;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.keyset.ConfigKeySets;
import com.aljun.zombiegame.work.keyset.KeySet;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;


public class ConfigDataManager {

    public static double getAverageByGameStage(KeySet<Double> initialKeySet, KeySet<Double> finalKeySet) {
        double gameStage = GameProperty.TimeProperty.getGameStage();
        return getOrDefault(finalKeySet) * gameStage + getOrDefault(initialKeySet) * (1 - gameStage);
    }

    @Nonnull
    public static <V> V getOrDefault(KeySet<V> keySet) {
        if (!ZombieGameConfig.contains(keySet)) {
            return keySet.DEFAULT_VALUE;
        }
        V value = ZombieGameConfig.get(keySet);
        if (value == null) {
            return keySet.DEFAULT_VALUE;
        } else {
            return value;
        }
    }

    public static void load(ForgeConfigSpec.Builder builder) {
        builder.comment("Before this, zombies are like vanilla zombies");
        ZombieGameConfig.add(builder, ConfigKeySets.ZOMBIE_START_EVOLUTION_STAGE, "Start EVOLUTION game stage");
        builder.push("zombie_main_ability_weight");
        builder.comment("Integer : Use integers to represent weights, the larger the number, the greater the weight");
        builder.comment("Initial : 0% Game Stage; Final : 100% Game Stage");
        ZombieGameConfig.add(builder, ConfigKeySets.NORMAL_INIT, "Normal Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.NORMAL_FINAL, "Normal Final");
        ZombieGameConfig.add(builder, ConfigKeySets.BUILDER_INIT, "Builder Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.BUILDER_FINAL, "Builder Final");
        ZombieGameConfig.add(builder, ConfigKeySets.BOW_INIT, "Bow Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.BOW_FINAL, "Bow Final");
        ZombieGameConfig.add(builder, ConfigKeySets.CROSSBOW_INIT, "Crossbow Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.CROSSBOW_FINAL, "Crossbow Final");
        builder.pop();

        builder.push("zombie_other_ability_weight");
        ZombieGameConfig.add(builder, ConfigKeySets.JUMP_STAGE, "Zombie Jump Stage");
        ZombieGameConfig.add(builder, ConfigKeySets.BREAK_BLOCK_STAGE, "Zombie Break Block Stage");
        ZombieGameConfig.add(builder, ConfigKeySets.PLACE_BLOCK_STAGE, "Zombie Place Block Stage");
        builder.comment("Initial : 0% Game Stage; Final : 100% Game Stage");
        builder.comment("Only Normal Zombie can use shield");
        ZombieGameConfig.add(builder, ConfigKeySets.SHIELD_INIT, "Shield Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.SHIELD_FINAL, "Shield Final");
        ZombieGameConfig.add(builder, ConfigKeySets.IMMUNE_SUN_INIT, "Immune Sun Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.IMMUNE_SUN_FINAL, "Immune Sun Final");
        ZombieGameConfig.add(builder, ConfigKeySets.SWIM_INIT, "Swim Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.SWIM_FINAL, "Swim Final");
        builder.pop();

        builder.push("zombie_attributes");
        ZombieGameConfig.add(builder, ConfigKeySets.ATTACK_VALUE_INIT, "Attack Value Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.ATTACK_VALUE_FINAL, "Attack Value Final");
        ZombieGameConfig.add(builder, ConfigKeySets.RUN_SPEED_INIT, "Run Speed Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.RUN_SPEED_FINAL, "Run Speed Final");
        ZombieGameConfig.add(builder, ConfigKeySets.ARMOR_INIT, "Armor Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.ARMOR_FINAL, "Armor Final");
        ZombieGameConfig.add(builder, ConfigKeySets.BREAK_SPEED_INIT, "Break Speed Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.BREAK_SPEED_FINAL, "Break Speed Final");
        builder.pop();

        builder.push("game_attributes");
        ZombieGameConfig.add(builder, ConfigKeySets.ZOMBIE_COUNT_INIT, "Zombie Max Count Initial");
        ZombieGameConfig.add(builder, ConfigKeySets.ZOMBIE_COUNT_FINAL, "Zombie Max Count Final");
        builder.pop();

        builder.push("zombie_spawn_chance");
        ZombieGameConfig.add(builder, ConfigKeySets.ZOMBIE_VILLAGER_SPAWNING_CHANCE, "Zombie Villager Spawning Chance");
        builder.pop();
    }
}
