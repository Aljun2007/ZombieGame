package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import org.jetbrains.annotations.Nullable;

public class ShortOption extends AbstractNumberOption<Short> {
    public ShortOption(OptionValue<Short> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected ShortOption(OptionManager.RegisterPack<Short> registerPack, @Nullable Short value,
                          ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }

    @Override
    protected double toDouble(Short v) {
        return (double) v;
    }

    @Override
    protected Short toT(double v) {
        return (short) v;
    }
}
