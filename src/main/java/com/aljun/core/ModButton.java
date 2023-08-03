package com.aljun.core;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ModButton extends Button {
    public ModButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_) {
        super(Button.builder(p_93725_, p_93726_).bounds(p_93721_, p_93722_, p_93723_, p_93724_));
    }
}
