package com.aljun.zombiegame.work.option;

import com.aljun.zombiegame.work.client.gui.option.optionpart.AbstractOption;
import net.minecraft.client.gui.components.Button;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class OptionLike {
    public final String ID;
    private final HashMap<Integer, AbstractOption<?>> OPTIONS = new HashMap<>();
    private int num = 0;
    private Button button;

    public OptionLike(String id) {
        ID = id;
    }

    public void add(AbstractOption<?> option) {
        this.OPTIONS.put(this.nextInt(), option);
    }

    private int nextInt() {
        num++;
        return num;
    }

    public void remove(AbstractOption<?> option) {
        this.OPTIONS.remove(this.getKey(option));
    }

    private int getKey(AbstractOption<?> option) {
        final int[] key = {-1};
        this.forEach((i, o) -> {
            if (o.equals(option)) {
                key[0] = i;
            }
        });
        return key[0];
    }

    public boolean contains(AbstractOption<?> option) {
        return this.getKey(option) != -1;
    }

    public void forEach(BiConsumer<Integer, AbstractOption<?>> action) {
        this.OPTIONS.forEach(action);
    }

    public void startModify(AbstractOption<?> option) {
        this.forEach((key, abstractOption) -> {

            if (option.equals(abstractOption)) {
                if (!option.isModifyMode()) {
                    abstractOption.modify();
                }
            } else {
                if (abstractOption.isModifyMode()) {
                    abstractOption.cancel();
                }
            }

        });
    }

    public int length() {
        return this.OPTIONS.size();
    }

    public Button getButton() {
        return this.button;
    }

    public void setButton(Button optionLikeButton) {
        this.button = optionLikeButton;
    }
}
