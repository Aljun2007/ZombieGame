package com.aljun.zombiegame.work.client.gui.option;

import com.aljun.core.ModButton;
import com.aljun.zombiegame.work.option.OptionLike;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionValue;
import com.aljun.zombiegame.work.tool.Chatting;
import com.aljun.zombiegame.work.tool.OptionUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.HashMap;


@OnlyIn(Dist.CLIENT)
public class ZombieGameOptionsScreen extends Screen {

    public static int edgeSize = 50;
    public static int lineSize = 20;

    private final HashMap<Integer, OptionLike> OPTION_LIKES = new HashMap<>();
    private final HashMap<String, Integer> OPTION_KEY = new HashMap<>();
    public int mouseScrollingValue = 0;
    public int yCheckLine = 0;

    private String chosenOptionLike = "";
    private int i;
    private ModButton saveButton;
    private boolean isDirty = false;

    public ZombieGameOptionsScreen(ITextComponent component, HashMap<Integer, OptionValue<?>> optionValueHashMap) {
        super(component);
        this.loadOption(optionValueHashMap);
    }

    private void addOptionLikeIfAbsent(String string) {
        if (!this.OPTION_KEY.containsKey(string)) {
            int k = this.nextInt();
            this.OPTION_LIKES.put(k, new OptionLike(string));
            this.OPTION_KEY.put(string, k);
        }
        if (this.chosenOptionLike.equals("")) {
            this.chosenOptionLike = string;
        }
    }

    private int nextInt() {
        i++;
        return i;
    }

    public OptionLike getOptionLike(String string) {
        return this.OPTION_LIKES.get(this.OPTION_KEY.get(string));
    }

    private <T> void addOption(OptionValue<T> value) {
        this.addOptionLikeIfAbsent(value.REGISTER_PACK.OPTION_LIKE);
        OptionLike optionLike = this.getOptionLike(value.REGISTER_PACK.OPTION_LIKE);
        optionLike.add(OptionManager.buildOption(value, this, optionLike));
    }

    public HashMap<String, OptionValue<?>> toMap() {
        HashMap<String, OptionValue<?>> map = new HashMap<>();
        this.OPTION_LIKES.forEach((k, o) -> o.forEach((k2, option) -> map.put(option.getKeySet().KEY, option.toOptionValue())));
        return map;
    }

    private void loadOption(HashMap<Integer, OptionValue<?>> optionValueHashMap) {
        optionValueHashMap.forEach((k, optionValue) -> this.addOption(optionValue));
    }

    @Override
    protected void init() {

        final int[] i = {0};


        this.OPTION_LIKES.forEach((key, optionLike) -> {
            optionLike.forEach((k, o) -> o.init());
            if (!this.chosenOptionLike.equals("") && this.getOptionLike(this.chosenOptionLike).equals(optionLike)) {
                optionLike.forEach((k, o) -> o.setChosen(true));
            } else {
                optionLike.forEach((k, o) -> o.setChosen(false));
            }

            ModButton optionLikeButton = new ModButton(
                    Math.max(0, this.width / 2 - 230) + 60 * i[0], edgeSize / 2 - 5, 50, 15,
                    new TranslationTextComponent("gui.zombiegame.option_like." + optionLike.ID), (b) -> {
                this.chosenOptionLike = optionLike.ID;
                this.moveAllOption(yCheckLine);
                b.active = false;
            });
            i[0]++;

            optionLike.setButton(optionLikeButton);
            this.addButton(optionLikeButton);
        });
        this.moveAllOption(yCheckLine);
        this.saveButton = new ModButton(this.width - 120, lineSize / 3, 50, 15,
                new TranslationTextComponent("gui.zombiegame.button.save"),
                (button) -> this.onSave());
        ModButton cancelButton = new ModButton(this.width - 65, lineSize / 3, 50, 15,
                new TranslationTextComponent("gui.zombiegame.button.cancel"),
                (button) -> this.onCancel());
        this.addButton(this.saveButton);
        this.addButton(cancelButton);

    }

    public void setDirty() {
        this.isDirty = true;
    }

    private void onSave() {
        Chatting.sendMessageLocalPlayerOnly(new TranslationTextComponent("message.zombiegame.warn",
                new TranslationTextComponent("message.zombiegame.option_value.saving")));
        OptionUtils.saveOptions(this.toMap());
        this.onSafeClose();
    }

    private void onCancel() {
        Chatting.sendMessageLocalPlayerOnly(new TranslationTextComponent("message.zombiegame.warn",
                new TranslationTextComponent("message.zombiegame.option_value.canceled")));
        this.onSafeClose();
    }

    private void onSafeClose() {
        super.onClose();
    }

    @Override
    public void onClose() {
        Chatting.sendMessageLocalPlayerOnly(new TranslationTextComponent("message.zombiegame.error",
                new TranslationTextComponent("message.zombiegame.common_screen.unexpected_shutdown")));
        super.onClose();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.mouseScrollingValue = (int) delta;
        return true;
    }

    @Override
    public void tick() {
        this.yCheckLine += this.mouseScrollingValue;
        int total = this.getLineTotal();
        int max = this.getLineMax();
        int lineToAdd = Math.max(total - max, 0);
        this.yCheckLine = Math.max(-lineToAdd, Math.min(0, this.yCheckLine));

        if (this.mouseScrollingValue != 0) {
            this.moveAllOption(yCheckLine);
            this.mouseScrollingValue = 0;
        }
        this.OPTION_LIKES.forEach((key, optionLike) -> {
            if (!this.chosenOptionLike.equals("") && this.getOptionLike(this.chosenOptionLike).equals(optionLike)) {
                optionLike.forEach((k, o) -> o.setChosen(true));
                if (optionLike.getButton() != null) optionLike.getButton().active = false;

            } else {
                if (optionLike.getButton() != null) optionLike.getButton().active = true;
                optionLike.forEach((k, o) -> o.setChosen(false));
            }

        });

        if (this.saveButton != null) {
            this.saveButton.active = this.isDirty;
        }
    }

    private int getLineTotal() {
        return this.OPTION_KEY.containsKey(this.chosenOptionLike) ? this.getOptionLike(this.chosenOptionLike).length() :
                0;
    }



    public final FontRenderer getFont() {
        return this.font;
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float f) {
        this.renderBackground(poseStack);
        this.OPTION_LIKES.forEach((key, optionLike) -> optionLike.forEach((k, o) -> o.render()));
        super.render(poseStack, mouseX, mouseY, f);
    }

    public void moveAllOption(int yLine) {
        this.OPTION_LIKES.forEach((k1, optionLike) -> {
            if (!this.chosenOptionLike.equals("") && this.getOptionLike(this.chosenOptionLike).equals(optionLike)) {
                final int[] i = {0};
                optionLike.forEach((k2, o) -> {
                    o.moveTo(yLine + i[0]);
                    i[0]++;
                });
            }
        });
    }


    public int getLineMax() {
        return (int) (Math.ceil(
                (double) (this.height - ZombieGameOptionsScreen.edgeSize * 2) / ZombieGameOptionsScreen.lineSize));
    }

    public void addW(Widget widget) {
        this.addButton(widget);
    }

    public void addB(Widget button) {
        this.addButton(button);
    }
}
