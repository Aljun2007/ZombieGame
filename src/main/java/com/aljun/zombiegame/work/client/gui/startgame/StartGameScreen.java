package com.aljun.zombiegame.work.client.gui.startgame;

import com.aljun.core.ModButton;
import com.aljun.core.ModStringWidgetMod;
import com.aljun.zombiegame.work.networking.StartGameNetworking;
import com.aljun.zombiegame.work.tool.Chatting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;

public class StartGameScreen extends Screen {

    private ForgeSlider dayTime;
    private ForgeSlider gameStage;
    private String mode = "normal";
    private Button modeButton;

    public StartGameScreen(Component component) {
        super(component);
    }

    @Override
    protected void init() {
        ModButton yesButton = new ModButton(this.width / 2 - 80 - 40, this.height / 2 + 30, 80, 20,
                Component.translatable("gui.zombiegame.button.start"), (b) -> this.yes());
        ModButton noButton = new ModButton(this.width / 2 + 80 - 40, this.height / 2 + 30, 80, 20,
                Component.translatable("gui.zombiegame.button.cancel"), (b) -> this.no());
        ModStringWidgetMod titleStr = new ModStringWidgetMod(Component.translatable("gui.zombiegame.start_game.title"), this.font);
        titleStr.setX(this.width / 2 - this.font.width(titleStr.getMessage().getString()) / 2);
        titleStr.setY(this.height / 2 - 40);

        this.modeButton = new ModButton(this.width / 2 - 80 - 40, this.height / 2 + 55, 80, 20,
                Component.translatable("gui.zombiegame.button.start"), (b) -> this.nextMode());
        this.dayTime = new ForgeSlider(this.width / 2 - 20, this.height / 2 + 55, 120, 20,
                Component.translatable("gui.zombiegame.start_game.day_time_pr"),
                Component.translatable("gui.zombiegame.start_game.day_time_sr"), 10, 1000, 100, 10, 0, true);
        this.gameStage = new ForgeSlider(this.width / 2 - 20, this.height / 2 + 55, 120, 20,
                Component.translatable("gui.zombiegame.start_game.game_stage_pr"),
                Component.translatable("gui.zombiegame.start_game.game_stage_sr"), 0, 100, 60, 10, 0, true);

        this.addRenderableWidget(yesButton);
        this.addRenderableWidget(noButton);
        this.addRenderableWidget(titleStr);
        this.addRenderableWidget(dayTime);
        this.addRenderableWidget(modeButton);
        this.addRenderableWidget(gameStage);
    }

    private void nextMode() {
        if (this.mode.equals("normal")) {
            this.mode = "remain";
        } else if (this.mode.equals("remain")) {
            this.mode = "normal";
        }
    }

    private void yes() {
        Chatting.sendMessageLocalPlayerOnly(Component.translatable("message.zombiegame.warn",
                Component.translatable("message.zombiegame.startgame.starting")));
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
        Chatting.sendMessageLocalPlayerOnly(Component.translatable("message.zombiegame.warn",
                Component.translatable("message.zombiegame.startgame.canceled")));
        super.onClose();
    }

    @Override
    public void onClose() {
        Chatting.sendMessageLocalPlayerOnly(Component.translatable("message.zombiegame.error",
                Component.translatable("message.zombiegame.common_screen.unexpected_shutdown")));
        super.onClose();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float f) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, f);
    }

    @Override
    public void tick() {

        if (this.mode.equals("normal")) {
            if (this.modeButton != null) {
                this.modeButton.setMessage(
                        Component.translatable("gui.zombiegame.start_game.mode_button.normal").withStyle(
                                ((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.translatable("message.zombiegame.info.normal",
                                                Component.literal(String.valueOf(dayTime.getValue()))))))));
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
                        Component.translatable("gui.zombiegame.start_game.mode_button.remain").withStyle(
                                ((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Component.translatable("message.zombiegame.info.remain"))))));
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
