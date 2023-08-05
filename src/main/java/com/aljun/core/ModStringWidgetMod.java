package com.aljun.core;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ModStringWidgetMod extends ModAbstractStringWidget {
    private float alignX = 0.5F;

    public ModStringWidgetMod(Component p_268211_, Font p_267963_) {
        this(0, 0, p_267963_.width(p_268211_.getVisualOrderText()), 9, p_268211_, p_267963_);
    }

    public ModStringWidgetMod(int p_268183_, int p_268082_, Component p_268069_, Font p_268121_) {
        this(0, 0, p_268183_, p_268082_, p_268069_, p_268121_);
    }

    public ModStringWidgetMod(int p_268199_, int p_268137_, int p_268178_, int p_268169_, Component p_268285_, Font p_268047_) {
        super(p_268199_, p_268137_, p_268178_, p_268169_, p_268285_, p_268047_);
        this.active = false;
    }

    public void setColor(int p_270680_) {
        super.setColor(p_270680_);
    }

    private ModStringWidgetMod horizontalAlignment(float p_267947_) {
        this.alignX = p_267947_;
        return this;
    }

    public ModStringWidgetMod alignLeft() {
        return this.horizontalAlignment(0.0F);
    }

    public ModStringWidgetMod alignCenter() {
        return this.horizontalAlignment(0.5F);
    }

    public ModStringWidgetMod alignRight() {
        return this.horizontalAlignment(1.0F);
    }

    public void renderWidget(PoseStack p_268177_, int p_268221_, int p_268001_, float p_268214_) {
        Component component = this.getMessage();
        Font font = this.getFont();
        int i = this.x + Math.round(this.alignX * (float)(this.getWidth() - font.width(component)));
        int j = this.y + (this.getHeight() - 9) / 2;
        drawString(p_268177_, font, component, i, j, this.getColor());
    }

    @Override
    public void render(PoseStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
        super.render(p_93657_, p_93658_, p_93659_, p_93660_);
        if (this.visible) {
            this.renderWidget(p_93657_, p_93658_, p_93659_, p_93660_);
        }
    }

    @Override
    public void renderButton(@NotNull PoseStack p_93676_, int p_93677_, int p_93678_, float p_93679_) {
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
