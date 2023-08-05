package com.aljun.zombiegame.work.client.gui.option.optionpart;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionValue;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DummyOption extends AbstractOption<Void> {
    public DummyOption(ZombieGameOptionsScreen screen, OptionLike father) {
        super(OptionValue.ERROR_VALUE, screen, father);
    }

    @Override
    protected void initOption() {
    }

    @Override
    public void moveTo(int yLine) {
    }

    @Override
    public void modify() {

    }

    @Override
    protected void save() {

    }

    @Override
    public void cancel() {
    }

    @Override
    protected void reset() {
        super.reset();
    }

    @Override
    public void reloadVisible() {
    }

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public void setValue(Void value) {
    }

    @Override
    public Component getValueComponent() {
        return Component.empty();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void setChosen(boolean b) {
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public void setModifyMode(boolean b) {
    }

}
