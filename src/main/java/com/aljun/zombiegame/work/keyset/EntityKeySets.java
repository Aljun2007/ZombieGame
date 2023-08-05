package com.aljun.zombiegame.work.keyset;

import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;

public class EntityKeySets {
    public static final KeySet<String> MAIN_GOAL_NAME = new KeySet<>("main_goal_name", ZombieMainGoal.NAME);
    public static final KeySet<Boolean> LOADED_ITEMS = new KeySet<>("loaded_items", false);
    public static final KeySet<Boolean> IS_SUN_SENSITIVE = new KeySet<>("is_sun_sensitive", true);
    public static final KeySet<String> BLOCK_TO_PLACE = new KeySet<>("block_to_place", "minecraft:dirt");
    public static final KeySet<Boolean> CAN_FLOAT = new KeySet<>("can_float", false);
    public static final KeySet<Boolean> CONVERTS_IN_WATER = new KeySet<>("converts_in_water", true);
    ;
}
