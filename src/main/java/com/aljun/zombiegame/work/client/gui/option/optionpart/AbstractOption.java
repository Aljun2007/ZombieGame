package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.core.ModButton;
import com.aljun.core.ModStringWidgetMod;
import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.keyset.KeySet;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractOption<T> {

    protected final OptionValue<T> OPTION_VALUE;
    protected final ZombieGameOptionsScreen SCREEN;
    private final OptionLike father;
    protected ModStringWidgetMod nameText;
    protected ModStringWidgetMod valueText;
    protected ModButton saveButton;
    protected ModButton resetButton;
    protected ModButton modifyButton;
    protected ModButton cancelButton;
    protected boolean modifyMode = false;
    protected boolean visible = true;
    protected boolean isChosen = false;

    protected AbstractOption(OptionValue<T> optionValue, ZombieGameOptionsScreen screen, OptionLike father) {
        this.SCREEN = screen;
        this.father = father;
        this.OPTION_VALUE = optionValue;
    }

    protected AbstractOption(OptionManager.RegisterPack<T> registerPack, @Nullable T value,
                             ZombieGameOptionsScreen screen, OptionLike father) {
        this(new OptionValue<>(registerPack, value), screen, father);
    }

    public boolean equals(Object object) {
        if (object instanceof AbstractOption<?> option) {
            return option.getKeySet().KEY.equals(this.getKeySet().KEY);
        }
        return false;
    }

    public boolean isModifyMode() {
        return modifyMode;
    }

    public void setModifyMode(boolean b) {
        this.modifyMode = b;
        this.reloadVisible();
    }

    public OptionValue<?> toOptionValue() {
        return this.OPTION_VALUE;
    }

    public T getValue() {
        return this.OPTION_VALUE.value == null ? this.OPTION_VALUE.REGISTER_PACK.KEY_SET.DEFAULT_VALUE :
                this.OPTION_VALUE.value;
    }

    public void setValue(T value) {
        this.OPTION_VALUE.value = value;
        this.valueText.setMessage(this.getValueComponent());
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.reloadVisible();
    }

    public void setChosen(boolean b) {
        if (b) {
            this.SCREEN.yCheckLine = 0;
        } else {
            if (this.modifyMode) {
                this.cancel();
            }
        }
        isChosen = b;
        this.reloadVisible();
    }

    public void moveTo(int yLine) {
        int xBasePos = this.SCREEN.width / 2;
        if (0 <= yLine && yLine <= this.SCREEN.getLineMax()) {

            int yPos = ZombieGameOptionsScreen.edgeSize + yLine * ZombieGameOptionsScreen.lineSize;

            this.setVisible(true);

            if (this.saveButton != null) {

                this.saveButton.setX(xBasePos + 30);
                this.saveButton.setY(yPos);

            }
            if (this.modifyButton != null) {
                if (this.saveButton != null) {
                    this.modifyButton.setX(this.saveButton.getX());
                }
                this.modifyButton.setY(yPos);
            }
            if (this.cancelButton != null) {
                if (this.saveButton != null) {
                    this.cancelButton.setX(this.saveButton.getX() + 65);
                }
                this.cancelButton.setY(yPos);
            }

            if (this.resetButton != null) {
                if (this.cancelButton != null) {
                    this.resetButton.setX(this.cancelButton.getX() + 65);
                }
                this.resetButton.setY(yPos);
            }
            if (this.valueText != null) {
                if (this.saveButton != null) {
                    this.valueText.setX(xBasePos - 140);
                }
                this.valueText.setY(yPos);
            }
            if (this.nameText != null) {

                if (this.valueText != null) {
                    this.nameText.setX(xBasePos - 200);
                }
                this.nameText.setY(yPos);
            }
        } else {
            this.setVisible(false);
        }

    }

    public final void init() {
        this.initOption();
    }

    protected void initOption() {
        this.nameText = new ModStringWidgetMod(0, 0, 30, 15, this.OPTION_VALUE.REGISTER_PACK.KEY_SET.translateToComponent(),
                this.SCREEN.getFont());
        this.valueText = new ModStringWidgetMod(0, 0, 120, 15, this.getValueComponent(), this.SCREEN.getFont());
        this.saveButton = new ModButton(0, 0, 60, 15, Component.translatable("gui.zombiegame.button.save"),
                (button) -> this.save());
        this.resetButton = new ModButton(0, 0, 60, 15, Component.translatable("gui.zombiegame.button.reset"),
                (button) -> this.reset());
        this.modifyButton = new ModButton(0, 0, 60, 15, Component.translatable("gui.zombiegame.button.modify"),
                (button) -> this.father.startModify(this));
        this.cancelButton = new ModButton(0, 0, 60, 15, Component.translatable("gui.zombiegame.button.cancel"),
                (button) -> this.cancel());

        this.saveButton.visible = false;
        this.resetButton.visible = false;
        this.cancelButton.visible = false;

        this.valueText.setMessage(this.getValueComponent());

        this.SCREEN.addRW(this.modifyButton);
        this.SCREEN.addRW(this.saveButton);
        this.SCREEN.addRW(this.cancelButton);
        this.SCREEN.addRW(this.resetButton);
        this.SCREEN.addRW(this.nameText);
        this.SCREEN.addRW(this.valueText);

    }

    public void reloadVisible() {

        if (this.saveButton != null) {
            this.saveButton.visible = this.visible && this.isChosen && this.modifyMode;
        }
        if (this.resetButton != null) {
            this.resetButton.visible = this.visible && this.isChosen && this.modifyMode;
        }
        if (this.cancelButton != null) {
            this.cancelButton.visible = this.visible && this.isChosen && this.modifyMode;
        }
        if (this.modifyButton != null) {
            this.modifyButton.visible = this.visible && this.isChosen && !this.modifyMode;
        }
        if (this.valueText != null) {
            this.valueText.visible = this.visible && this.isChosen && !this.modifyMode;
        }
        if (this.nameText != null) {
            this.nameText.visible = this.visible && this.isChosen;
        }
    }

    public Component getValueComponent() {
        return Component.literal(String.valueOf(this.OPTION_VALUE.value));
    }

    public void modify() {
        this.SCREEN.setDirty();
        this.setModifyMode(true);
    }

    protected void save() {
        this.setModifyMode(false);
    }

    protected void reset() {
        this.setModifyMode(false);
    }

    public void cancel() {
        this.setModifyMode(false);
    }

    public void render() {

    }

    public void tick() {

    }


    public final KeySet<T> getKeySet() {
        return this.OPTION_VALUE.REGISTER_PACK.KEY_SET;
    }


}
