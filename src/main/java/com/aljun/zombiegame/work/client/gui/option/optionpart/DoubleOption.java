package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import org.jetbrains.annotations.Nullable;

public class DoubleOption extends AbstractNumberOption<Double> {
    public DoubleOption(OptionValue<Double> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected DoubleOption(OptionManager.RegisterPack<Double> registerPack, @Nullable Double value,
                           ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }

    @Override
    protected double toDouble(Double v) {
        return v;
    }

    @Override
    protected Double toT(double v) {
        return v;
    }


}
