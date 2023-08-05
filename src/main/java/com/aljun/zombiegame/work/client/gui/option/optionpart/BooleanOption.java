package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.core.ModButton;
import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class BooleanOption extends AbstractOption<Boolean> {

    protected ModButton trueButton;
    protected ModButton falseButton;
    protected boolean lastValue;

    public BooleanOption(OptionValue<Boolean> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected BooleanOption(OptionManager.RegisterPack<Boolean> registerPack, @Nullable Boolean value,
                            ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }


    @Override
    public void moveTo(int yLine) {
        super.moveTo(yLine);
        int yPos = ZombieGameOptionsScreen.edgeSize + yLine * ZombieGameOptionsScreen.lineSize;
        if (0 <= yLine && yLine <= this.SCREEN.getLineMax()) {
            if (this.trueButton != null) {
                this.trueButton.setX(this.SCREEN.width / 2 - 110);
                this.trueButton.setY(yPos);
            }
            if (this.falseButton != null) {
                this.falseButton.setX(this.SCREEN.width / 2 - 110);
                this.falseButton.setY(yPos);
            }
        }
    }

    @Override
    protected void initOption() {
        super.initOption();
        this.trueButton = new ModButton(0, 0, 60, 15, Component.translatable("gui.zombiegame.true"), this::onPress);
        this.falseButton = new ModButton(0, 0, 60, 15, Component.translatable("gui.zombiegame.false"), this::onPress);
        this.trueButton.visible = false;
        this.falseButton.visible = false;
        this.SCREEN.addRW(this.trueButton);
        this.SCREEN.addRW(this.falseButton);
    }

    private void onPress(Button button) {
        this.OPTION_VALUE.value = !this.OPTION_VALUE.value;
        this.reloadVisible();
    }

    @Override
    public void setModifyMode(boolean b) {
        if (b) {
            this.lastValue = this.OPTION_VALUE.value;
        }
        super.setModifyMode(b);
    }

    @Override
    public void reloadVisible() {
        super.reloadVisible();
        if (this.trueButton != null) {
            this.trueButton.visible = this.visible && this.isChosen && this.modifyMode && this.getValue();
        }
        if (this.falseButton != null) {
            this.falseButton.visible = this.visible && this.isChosen && this.modifyMode && !this.getValue();
        }
    }

    @Override
    protected void save() {
        this.setValue(this.OPTION_VALUE.value);
        super.save();
    }

    @Override
    protected void reset() {
        this.setValue(this.OPTION_VALUE.REGISTER_PACK.KEY_SET.DEFAULT_VALUE);
        super.reset();
    }

    @Override
    public void cancel() {
        this.setValue(this.lastValue);
        super.cancel();
    }

    @Override
    public Boolean getValue() {
        return super.getValue();
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Component getValueComponent() {
        return Component.translatable("gui.zombiegame." + this.getValue());
    }

}
