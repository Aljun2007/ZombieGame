package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractNumberOption<T extends Number> extends AbstractOption<T> {
    protected ForgeSlider modifySlider;
    protected T lastValue;

    public AbstractNumberOption(OptionValue<T> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected AbstractNumberOption(OptionManager.RegisterPack<T> registerPack, @Nullable T value,
                                   ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }


    @Override
    public void moveTo(int yLine) {
        super.moveTo(yLine);
        int yPos = ZombieGameOptionsScreen.edgeSize + yLine * ZombieGameOptionsScreen.lineSize;
        if (0 <= yLine && yLine <= this.SCREEN.getLineMax()) {

            if (this.modifySlider != null) {
                this.modifySlider.setX(this.SCREEN.width / 2 - 120);
                this.modifySlider.setY(yPos);
            }
        }
    }

    @Override
    protected void initOption() {
        super.initOption();

        this.modifySlider = new ForgeSlider(0, 0, 120, 15, Component.empty(), Component.empty(),
                this.toDouble(this.OPTION_VALUE.REGISTER_PACK.MIN_VALUE),
                this.toDouble(this.OPTION_VALUE.REGISTER_PACK.MAX_VALUE),
                this.toDouble(this.OPTION_VALUE.REGISTER_PACK.KEY_SET.DEFAULT_VALUE),
                this.toDouble(this.OPTION_VALUE.REGISTER_PACK.STEP_VALUE), 0, true);
        this.modifySlider.visible = false;
        this.SCREEN.addRW(this.modifySlider);

    }

    @Override
    protected void save() {
        this.setValue(this.toT(
                Math.round(this.modifySlider.getValue() / this.toDouble(this.OPTION_VALUE.REGISTER_PACK.STEP_VALUE))
                * this.toDouble(this.OPTION_VALUE.REGISTER_PACK.STEP_VALUE)));
        super.save();
    }

    @Override
    public void setModifyMode(boolean b) {
        if (b) {
            this.lastValue = this.OPTION_VALUE.value;
        }
        super.setModifyMode(b);
    }

    protected abstract double toDouble(T v);

    protected abstract T toT(double v);

    @Override
    public void reloadVisible() {
        super.reloadVisible();
        if (modifySlider != null) {
            this.modifySlider.visible = this.visible && this.isChosen && this.modifyMode;
        }
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
    public void setValue(T value) {
        if (value == null) return;
        super.setValue(value);
        this.modifySlider.setValue(this.toDouble(value));
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
        return Component.literal(String.valueOf(this.getValue()));
    }
}

