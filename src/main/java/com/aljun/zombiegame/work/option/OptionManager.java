package com.aljun.zombiegame.work.option;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.client.gui.option.optionpart.*;
import com.aljun.zombiegame.work.keyset.KeySet;
import com.aljun.zombiegame.work.keyset.KeySets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;

public class OptionManager {
    private static final HashMap<String, Integer> REGISTER_CHECK_LIST = new HashMap<>();
    private static final HashMap<Integer, RegisterPack<?>> REGISTER_LIST = new HashMap<>();
    private static int i = 0;

    @OnlyIn(Dist.CLIENT)
    public static AbstractOption<?> buildOption(OptionValue<?> optionValue, ZombieGameOptionsScreen screen,
                                                OptionLike optionLike) {
        if (optionValue.value instanceof String)
            return new StringOption((OptionValue<String>) optionValue, screen, optionLike);
        else if (optionValue.value instanceof Boolean)
            return new BooleanOption((OptionValue<Boolean>) optionValue, screen, optionLike);
        else if (optionValue.value instanceof Integer)
            return new IntegerOption((OptionValue<Integer>) optionValue, screen, optionLike);
        else if (optionValue.value instanceof Double)
            return new DoubleOption((OptionValue<Double>) optionValue, screen, optionLike);
        else if (optionValue.value instanceof Float)
            return new FloatOption((OptionValue<Float>) optionValue, screen, optionLike);
        else if (optionValue.value instanceof Short)
            return new ShortOption((OptionValue<Short>) optionValue, screen, optionLike);
        else if (optionValue.value instanceof Long)
            return new LongOption((OptionValue<Long>) optionValue, screen, optionLike);
        else return new DummyOption(screen, optionLike);

    }

    private static String transformToString(OptionValue<?> optionValue) {
        return String.valueOf(optionValue.value);
    }

    private static OptionValue<?> transformToValue(String key, String value) {
        RegisterPack<?> registerPack = getRegisterPack(key);

        if (registerPack.KEY_SET.DEFAULT_VALUE instanceof String)
            return new OptionValue<>((RegisterPack<String>) registerPack, String.valueOf(value));
        if (registerPack.KEY_SET.DEFAULT_VALUE instanceof Boolean)
            return new OptionValue<>((RegisterPack<Boolean>) registerPack, Boolean.valueOf(value));
        if (registerPack.KEY_SET.DEFAULT_VALUE instanceof Integer)
            return new OptionValue<>((RegisterPack<Integer>) registerPack, Integer.valueOf(value));
        if (registerPack.KEY_SET.DEFAULT_VALUE instanceof Double)
            return new OptionValue<>((RegisterPack<Double>) registerPack, Double.valueOf(value));
        if (registerPack.KEY_SET.DEFAULT_VALUE instanceof Float)
            return new OptionValue<>((RegisterPack<Float>) registerPack, Float.valueOf(value));
        if (registerPack.KEY_SET.DEFAULT_VALUE instanceof Long)
            return new OptionValue<>((RegisterPack<Long>) registerPack, Long.valueOf(value));
        else return OptionValue.ERROR_VALUE;
    }

    public static RegisterPack<?> getRegisterPack(AbstractOption<?> option) {
        return REGISTER_LIST.getOrDefault(REGISTER_CHECK_LIST.get(option.getKeySet().KEY), RegisterPack.ERROR_PACK);
    }

    public static RegisterPack<?> getRegisterPack(String key) {
        return REGISTER_LIST.getOrDefault(REGISTER_CHECK_LIST.get(key), RegisterPack.ERROR_PACK);
    }

    public static HashMap<Integer, OptionValue<?>> readInOrder(HashMap<String, String> map) {
        HashMap<Integer, OptionValue<?>> result = createOptionDefaultValueMapInOrder();
        map.forEach((key, optionValue) -> {
            result.put(REGISTER_CHECK_LIST.get(key), transformToValue(key, optionValue));
        });
        return result;
    }

    public static HashMap<String, OptionValue<?>> read(HashMap<String, String> map) {
        HashMap<String, OptionValue<?>> result = createOptionDefaultValueMap();
        map.forEach((key, optionValue) -> {
            result.put(key, transformToValue(key, optionValue));
        });
        return result;
    }

    public static HashMap<String, String> write(HashMap<String, OptionValue<?>> map) {
        HashMap<String, String> result = new HashMap<>();
        map.forEach((key, optionValue) -> {
            result.put(key, transformToString(optionValue));
        });
        return result;
    }

    public static void register(KeySet<?> keySet, String option_like) {
        i++;
        REGISTER_LIST.put(i, new RegisterPack<>(keySet, option_like));
        REGISTER_CHECK_LIST.put(keySet.KEY, i);
    }

    public static void register(RegisterPack<?> registerPack) {
        i++;
        REGISTER_LIST.put(i, registerPack);
        REGISTER_CHECK_LIST.put(registerPack.KEY_SET.KEY, i);
    }

    public static <T extends Number> void registerSlider(KeySet<T> keySet, String option_like, T maxValue, T minValue,
                                                         T stepValue, int kind) {
        i++;
        REGISTER_LIST.put(i, new RegisterPack<>(keySet, option_like, maxValue, minValue, stepValue, kind));
        REGISTER_CHECK_LIST.put(keySet.KEY, i);
    }

    public static HashMap<String, OptionValue<?>> createOptionDefaultValueMap() {

        HashMap<String, OptionValue<?>> result = new HashMap<>();

        REGISTER_LIST.forEach((key, pack) -> OptionManager.createSingleOption(key, pack, result));

        return result;
    }

    private static <T> void createSingleOption(Integer key, RegisterPack<T> pack,
                                               HashMap<String, OptionValue<?>> result) {
        result.put(pack.KEY_SET.KEY, new OptionValue<>(pack, pack.KEY_SET.DEFAULT_VALUE));

    }

    public static HashMap<Integer, OptionValue<?>> createOptionDefaultValueMapInOrder() {

        HashMap<Integer, OptionValue<?>> result = new HashMap<>();

        REGISTER_LIST.forEach((key, pack) -> OptionManager.createSingleOptionInOrder(key, pack, result));

        return result;
    }

    private static <T> void createSingleOptionInOrder(Integer key, RegisterPack<T> pack,
                                                      HashMap<Integer, OptionValue<?>> result) {
        result.put(key, new OptionValue<>(pack, pack.KEY_SET.DEFAULT_VALUE));

    }

    public static class RegisterPack<T> {


        public static final RegisterPack<Void> ERROR_PACK = new RegisterPack<>(KeySets.ERROR_KEY_SET, "error");
        public final KeySet<T> KEY_SET;
        public final String OPTION_LIKE;

        public final T MAX_VALUE;
        public final T MIN_VALUE;
        public final T STEP_VALUE;
        public final int KIND;

        public RegisterPack(KeySet<T> keySet, String optionLike) {
            this.KEY_SET = keySet;
            this.OPTION_LIKE = optionLike;
            this.MAX_VALUE = keySet.DEFAULT_VALUE;
            this.MIN_VALUE = keySet.DEFAULT_VALUE;
            this.STEP_VALUE = keySet.DEFAULT_VALUE;
            this.KIND = 0;
        }

        public RegisterPack(KeySet<T> keySet, String optionLike, T maxValue, T minValue, T stepValue, int kind) {
            this.KEY_SET = keySet;
            this.OPTION_LIKE = optionLike;
            this.MAX_VALUE = maxValue;
            this.MIN_VALUE = minValue;
            this.STEP_VALUE = stepValue;
            this.KIND = kind;
        }
    }


}
