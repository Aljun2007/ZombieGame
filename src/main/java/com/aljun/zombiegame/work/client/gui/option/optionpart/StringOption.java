package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.core.ModEditBox;
import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class StringOption extends AbstractOption<String> {

    protected ModEditBox modifyEditBox;
    protected String lastValue;

    public StringOption(OptionValue<String> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        super(optionValue, screen, father);
    }

    protected StringOption(OptionManager.RegisterPack<String> registerPack, @Nullable String value,
                           ZombieGameOptionsScreen screen, OptionLike father) {
        super(registerPack, value, screen, father);
    }


    @Override
    public void moveTo(int yLine) {
        super.moveTo(yLine);
        int yPos = ZombieGameOptionsScreen.edgeSize + yLine * ZombieGameOptionsScreen.lineSize;
        if (0 <= yLine && yLine <= this.SCREEN.getLineMax()) {
            if (this.modifyEditBox != null) {
                this.modifyEditBox.setX(this.SCREEN.width / 2 - 120);
                this.modifyEditBox.setY(yPos);
            }
        }
    }

    @Override
    protected void initOption() {
        super.initOption();
        this.modifyEditBox = new ModEditBox(this.SCREEN.getFont(), 0, 0, 120, 15, this.getValueComponent());
        this.modifyEditBox.visible = false;
        this.modifyEditBox.setValue(this.getValue());
        this.SCREEN.addRW(this.modifyEditBox);

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
        if (this.modifyEditBox != null) {
            this.modifyEditBox.visible = this.visible && this.isChosen && this.modifyMode;
        }
    }

    @Override
    protected void save() {
        this.setValue(this.modifyEditBox.getValue());
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
    public String getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(String value) {
        if (value == null) return;
        super.setValue(value);
        this.modifyEditBox.setValue(value);
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
        return Component.literal(this.getValue());
    }
}
