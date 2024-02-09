package com.aljun.zombiegame.work.client.gui.startgame;

import com.aljun.core.ModButton;
import com.aljun.core.ModStringWidgetMod;
import com.aljun.zombiegame.work.networking.StartGameNetworking;
import com.aljun.zombiegame.work.tool.Chatting;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.client.gui.widget.Slider;


public class StartGameScreen extends Screen {

    private Slider dayTime;
    private Slider gameStage;
    private String mode = "normal";
    private Button modeButton;

    public StartGameScreen(ITextComponent component) {
        super(component);
    }

    @Override
    protected void init() {
        ModButton yesButton = new ModButton(this.width / 2 - 80 - 40, this.height / 2 + 30, 80, 20,
                new TranslationTextComponent("gui.zombiegame.button.start"), (b) -> this.yes());
        ModButton noButton = new ModButton(this.width / 2 + 80 - 40, this.height / 2 + 30, 80, 20,
                new TranslationTextComponent("gui.zombiegame.button.cancel"), (b) -> this.no());
        ModStringWidgetMod titleStr = new ModStringWidgetMod(
                new TranslationTextComponent("gui.zombiegame.start_game.title"), this.font);
        titleStr.setX(this.width / 2 - this.font.width(titleStr.getMessage().getString()) / 2);
        titleStr.setY(this.height / 2 - 40);

        this.modeButton = new ModButton(this.width / 2 - 80 - 40, this.height / 2 + 55, 80, 20,
                new TranslationTextComponent("gui.zombiegame.button.start"), (b) -> this.nextMode());
        this.dayTime = new Slider(this.width / 2 - 20, this.height / 2 + 55, 120, 20,
                new TranslationTextComponent("gui.zombiegame.start_game.day_time_pr"),
                new TranslationTextComponent("gui.zombiegame.start_game.day_time_sr"), 10, 1000, 100, true,true,  (r)->{});
        this.gameStage = new Slider(this.width / 2 - 20, this.height / 2 + 55, 120, 20,
                new TranslationTextComponent("gui.zombiegame.start_game.game_stage_pr"),
                new TranslationTextComponent("gui.zombiegame.start_game.game_stage_sr"), 0, 100, 10, true, true,(r)->{} );

        this.addButton(yesButton);
        this.addButton(noButton);
        this.addButton(titleStr);
        this.addButton(dayTime);
        this.addButton(modeButton);
        this.addButton(gameStage);
    }

    private void nextMode() {
        if (this.mode.equals("normal")) {
            this.mode = "remain";
        } else if (this.mode.equals("remain")) {
            this.mode = "normal";
        }
    }

    private void yes() {
        Chatting.sendMessageLocalPlayerOnly(new TranslationTextComponent("message.zombiegame.warn",
                new TranslationTextComponent("message.zombiegame.startgame.starting")));
        StartGameNetworking.INSTANCE.sendToServer(StartGameNetworking.createStartGamePack(this.mode));
        if (this.mode.equals("normal")) {
            StartGameNetworking.INSTANCE_DAY_TIME.sendToServer(
                    StartGameNetworking.createGameDayPack(this.dayTime.getValueInt()));
        } else if (this.mode.equals("remain")) {
            StartGameNetworking.INSTANCE_GAME_STAGE.sendToServer(
                    StartGameNetworking.createGameStagePack(this.gameStage.getValueInt() / 100d));
        }
        super.onClose();
    }

    private void no() {
        Chatting.sendMessageLocalPlayerOnly(new TranslationTextComponent("message.zombiegame.warn",
                new TranslationTextComponent("message.zombiegame.startgame.canceled")));
        super.onClose();
    }

    @Override
    public void onClose() {
        Chatting.sendMessageLocalPlayerOnly(new TranslationTextComponent("message.zombiegame.error",
                new TranslationTextComponent("message.zombiegame.common_screen.unexpected_shutdown")));
        super.onClose();
    }

    @Override
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float f) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, f);
    }

    @Override
    public void tick() {

        if (this.mode.equals("normal")) {
            if (this.modeButton != null) {
                this.modeButton.setMessage(new TranslationTextComponent("gui.zombiegame.start_game.mode_button.normal"));
            }
            if (this.gameStage != null) {
                this.gameStage.visible = false;
            }
            if (this.dayTime != null) {
                this.dayTime.visible = true;
            }
        } else if (this.mode.equals("remain")) {
            if (this.modeButton != null) {
                this.modeButton.setMessage(
                        new TranslationTextComponent("gui.zombiegame.start_game.mode_button.remain").withStyle(
                                ((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new TranslationTextComponent("message.zombiegame.info.remain"))))));
            }
            if (this.gameStage != null) {
                this.gameStage.visible = true;
            }
            if (this.dayTime != null) {
                this.dayTime.visible = false;
            }

        }
    }
}
