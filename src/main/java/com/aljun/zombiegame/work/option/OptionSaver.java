package com.aljun.zombiegame.work.option;

import com.aljun.zombiegame.work.datamanager.datamanager.LevelDataManager;
import net.minecraft.server.level.ServerLevel;

import java.util.HashMap;

public class OptionSaver {


    public static void save(HashMap<String, OptionValue<?>> map, ServerLevel level) {
        map.forEach((key, optionValue) -> saveSingleOption(key, optionValue, level));
    }

    public static HashMap<String, OptionValue<?>> get(ServerLevel level) {

        HashMap<String, OptionValue<?>> result = OptionManager.createOptionDefaultValueMap();
        result.forEach((key, optionValue) -> loadSingleOptionValue(optionValue, level));
        return result;

    }

    private static <T> void loadSingleOptionValue(OptionValue<T> optionValue, ServerLevel level) {
        optionValue.value = LevelDataManager.getOrCreate(level, optionValue.REGISTER_PACK.KEY_SET);
    }


    private static <T> void saveSingleOption(String key, OptionValue<T> optionValue, ServerLevel level) {
        LevelDataManager.set(level, optionValue.REGISTER_PACK.KEY_SET, optionValue.value);
    }

}
