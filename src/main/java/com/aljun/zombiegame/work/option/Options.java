package com.aljun.zombiegame.work.option;

import com.aljun.zombiegame.work.keyset.GameKeySets;
import com.aljun.zombiegame.work.keyset.LevelKeySets;

public class Options {
    public static final Option<Boolean> CAN_ZOMBIE_BREAK_BLOCK = new Option<>(
            new OptionManager.RegisterPack<>(GameKeySets.CAN_ZOMBIE_BREAK_BLOCK, OptionLikes.ZOMBIE));
    public static final Option<Boolean> CAN_ZOMBIE_PLACE_BLOCK = new Option<>(
            new OptionManager.RegisterPack<>(GameKeySets.CAN_ZOMBIE_PLACE_BLOCK, OptionLikes.ZOMBIE));

    public static final Option<Integer> DAY_TOTAL = new Option<>(
            new OptionManager.RegisterPack<>(LevelKeySets.DAY_TOTAL, OptionLikes.GAME, 1000, 10, 10, 1));

    public static final Option<Double> REMAIN_MODE_GAME_STAGE = new Option<>(
            new OptionManager.RegisterPack<>(LevelKeySets.REMAIN_MODE_GAME_STAGE, OptionLikes.GAME, 1d, 0d, 0.1d, 0));

    public static void register() {
        DAY_TOTAL.register();
        REMAIN_MODE_GAME_STAGE.register();
        CAN_ZOMBIE_BREAK_BLOCK.register();
        CAN_ZOMBIE_PLACE_BLOCK.register();
    }
}
