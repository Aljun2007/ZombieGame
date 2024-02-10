package com.aljun.zombiegame.work.tool;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Information {
    private static final HashMap<Integer, Information> REGISTER_MAP = new HashMap<>();
    public static Information EMPTY = register(Component.empty());
    private static int id = -2;
    private final int ID;
    private final MutableComponent component;

    private Information(MutableComponent component) {
        id++;
        this.ID = id;
        this.component = component;
    }

    public static Information register(MutableComponent component) {
        Information information = new Information(component);
        REGISTER_MAP.put(information.getID(), information);
        return information;
    }

    @NotNull
    public static Information getInformation(int id) {
        Information information = REGISTER_MAP.get(id);
        return information == null ? Information.EMPTY : information;
    }

    public int getID() {
        return ID;
    }

    public MutableComponent getComponent() {
        return component.copy();
    }

    public static class ZombieGameInformation {
        public static Information WELCOME = register(Component.translatable("message.zombiegame.join_game.welcome"));
        public static Information START_GAME = register(
                Component.translatable("message.zombiegame.join_game.start_game",
                        Component.translatable("message.zombiegame.click_here").withStyle(
                                ((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                        "/zombiegame gui_set_up start_game")).withHoverEvent(
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable(
                                                "message.zombiegame.join_game.hover.start_game")))))));
        public static Information MODIFY_OPTION = register(Component.translatable("message.zombiegame.join_game.option",
                Component.translatable("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame gui_set_up option_modify")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.translatable("message.zombiegame.join_game.hover.option_modify")))))));
        public static Information DAY_VAR = register(Component.translatable("message.zombiegame.join_game.day",
                Component.translatable("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zombiegame var get day")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.translatable("message.zombiegame.join_game.hover.get_day"))))),
                Component.translatable("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame var set day <time>")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.translatable("message.zombiegame.join_game.hover.set_day")))))));

        public static Information OTHER = register(Component.translatable("message.zombiegame.join_game.other",
                Component.translatable("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame game cancel_game")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.translatable("message.zombiegame.join_game.hover.cancel_game"))))),
                Component.translatable("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zombiegame game end_game")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.translatable("message.zombiegame.join_game.hover.end_game")))))));
    }


}
