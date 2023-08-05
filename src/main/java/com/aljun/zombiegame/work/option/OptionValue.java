package com.aljun.zombiegame.work.option;

import javax.annotation.Nullable;

public class OptionValue <E> {
    public static final OptionValue<Void> ERROR_VALUE = new OptionValue<>(OptionManager.RegisterPack.ERROR_PACK,
            null);
    public final OptionManager.RegisterPack<E> REGISTER_PACK;
    public E value;


    public OptionValue(OptionManager.RegisterPack<E> registerPack,@Nullable E value) {
        this.REGISTER_PACK = registerPack;
        this.value = value;
    }


}