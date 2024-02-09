package com.aljun.core;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;


public class ModButton extends Button {
    public ModButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, ITextComponent p_93725_, IPressable p_93726_) {
        super(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
    }

    public ModButton(int p_93728_, int p_93729_, int p_93730_, int p_93731_, ITextComponent p_93732_, IPressable p_93733_,
                     ITooltip p_93734_) {
        super(p_93728_, p_93729_, p_93730_, p_93731_, p_93732_, p_93733_, p_93734_);
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
