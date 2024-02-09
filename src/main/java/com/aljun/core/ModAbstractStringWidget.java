package com.aljun.core;


import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ModAbstractStringWidget extends Widget {
    private final FontRenderer font;
    private int color = 16777215;

    public ModAbstractStringWidget(int p_270910_, int p_270297_, int p_270088_, int p_270842_, ITextComponent p_270063_,
                                   FontRenderer p_270327_) {
        super(p_270910_, p_270297_, p_270088_, p_270842_, p_270063_);
        this.font = p_270327_;
    }

    protected final FontRenderer getFont() {
        return this.font;
    }

    protected final int getColor() {
        return this.color;
    }

    public void setColor(int p_270638_) {
        this.color = p_270638_;
    }
}
