package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import org.jetbrains.annotations.Nullable;

public class FloatOption extends AbstractNumberOption<Float> {
    public FloatOption(OptionValue<Float> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected FloatOption(OptionManager.RegisterPack<Float> registerPack, @Nullable Float value,
                          ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }

    @Override
    protected double toDouble(Float v) {
        return (double) v;
    }

    @Override
    protected Float toT(double v) {
        return (float) v;
    }

}
