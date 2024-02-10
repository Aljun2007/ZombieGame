package com.aljun.core;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public abstract class ModAbstractStringWidget extends AbstractWidget {
    private final Font font;
    private int color = 16777215;

    public ModAbstractStringWidget(int p_270910_, int p_270297_, int p_270088_, int p_270842_, Component p_270063_,
                                   Font p_270327_) {
        super(p_270910_, p_270297_, p_270088_, p_270842_, p_270063_);
        this.font = p_270327_;
    }

    protected final Font getFont() {
        return this.font;
    }

    protected final int getColor() {
        return this.color;
    }

    public void setColor(int p_270638_) {
        this.color = p_270638_;
    }
}
