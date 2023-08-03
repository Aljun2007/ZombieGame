package com.aljun.zombiegame.work.keyset;

import com.aljun.zombiegame.work.keyset.KeySet;

public class ConfigKeySets {
    public static final KeySet<Double> ZOMBIE_VILLAGER_SPAWNING_CHANCE = new KeySet<>("zombie_villager_spawning_chance",
            0.05d);

    public static final KeySet<Double> ZOMBIE_START_EVOLUTION_STAGE = new KeySet<>("zombie_start_evolution_stage",
            0.2d);
    public static final KeySet<Integer> NORMAL_INIT = new KeySet<>("normal_init", 12);
    public static final KeySet<Integer> NORMAL_FINAL = new KeySet<>("normal_final", 12);
    public static final KeySet<Integer> BUILDER_INIT = new KeySet<>("builder_init", 5);
    public static final KeySet<Integer> BUILDER_FINAL = new KeySet<>("builder_final", 10);
    public static final KeySet<Integer> BOW_INIT = new KeySet<>("bow_init", 1);
    public static final KeySet<Integer> BOW_FINAL = new KeySet<>("bow_final", 3);
    public static final KeySet<Integer> CROSSBOW_INIT = new KeySet<>("crossbow_init", 1);
    public static final KeySet<Integer> CROSSBOW_FINAL = new KeySet<>("crossbow_final", 3);

    public static final KeySet<Double> SHIELD_INIT = new KeySet<>("shield_init", 0d);
    public static final KeySet<Double> SHIELD_FINAL = new KeySet<>("shield_final", 0.2d);

    public static final KeySet<Double> IMMUNE_SUN_INIT = new KeySet<>("immune_sun_init", 0d);
    public static final KeySet<Double> IMMUNE_SUN_FINAL = new KeySet<>("immune_sun_final", 1d);

    public static final KeySet<Double> SWIM_INIT = new KeySet<>("swim_init", 0d);
    public static final KeySet<Double> SWIM_FINAL = new KeySet<>("swim_final", 0.75d);
    public static final KeySet<Double> BREAK_BLOCK_STAGE = new KeySet<>("break_block_stage", 0.2d);
    public static final KeySet<Double> PLACE_BLOCK_STAGE = new KeySet<>("place_block_stage", 0.6d);

    public static final KeySet<Double> ARMOR_INIT = new KeySet<>("armor_init", 2d);
    public static final KeySet<Double> ARMOR_FINAL = new KeySet<>("armor_final", 4d);
    public static final KeySet<Double> RUN_SPEED_INIT = new KeySet<>("run_speed_init", 1d);
    public static final KeySet<Double> RUN_SPEED_FINAL = new KeySet<>("run_speed_final", 1.2d);
    public static final KeySet<Double> BREAK_SPEED_INIT = new KeySet<>("break_speed_init", 1d);
    public static final KeySet<Double> BREAK_SPEED_FINAL = new KeySet<>("break_speed_final", 2d);
    public static final KeySet<Double> ATTACK_VALUE_INIT = new KeySet<>("attack_value_init", 1.5d);
    public static final KeySet<Double> ATTACK_VALUE_FINAL = new KeySet<>("attack_value_final", 3d);
    public static final KeySet<Integer> ZOMBIE_COUNT_INIT = new KeySet<>("zombie_count_init", 50);
    public static final KeySet<Integer> ZOMBIE_COUNT_FINAL = new KeySet<>("zombie_count_final", 140);
    public static final KeySet<Double> JUMP_STAGE = new KeySet<>("jump_stage", 0.35d);
}
