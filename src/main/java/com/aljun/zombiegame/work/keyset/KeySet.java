package com.aljun.zombiegame.work.keyset;


import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class KeySet<V> {
    public final String KEY;
    public final V DEFAULT_VALUE;

    public KeySet(String key, V value) {
        this.KEY = key;
        this.DEFAULT_VALUE = value;
    }

    public String translateToString() {
        return "key_set.zombiegame." + this.KEY;
    }

    public ITextComponent translateToComponent() {
        return new TranslationTextComponent(this.translateToString());
    }
}
