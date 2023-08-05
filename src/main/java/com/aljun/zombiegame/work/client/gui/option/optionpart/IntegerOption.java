package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import org.jetbrains.annotations.Nullable;

public class IntegerOption extends AbstractNumberOption<Integer> {
    public IntegerOption(OptionValue<Integer> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected IntegerOption(OptionManager.RegisterPack<Integer> registerPack, @Nullable Integer value,
                            ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }

    @Override
    protected double toDouble(Integer v) {
        return (double) v;
    }

    @Override
    protected Integer toT(double v) {
        return (int) v;
    }


}
