package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;

public class LongOption extends AbstractNumberOption<Long> {

    public LongOption(OptionValue<Long> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected LongOption(OptionManager.RegisterPack<Long> registerPack, Long value,
                         ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }

    @Override
    protected double toDouble(Long v) {
        return (double) v;
    }

    @Override
    protected Long toT(double v) {
        return (long) v;
    }
}
