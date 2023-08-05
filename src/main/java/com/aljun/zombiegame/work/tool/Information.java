package com.aljun.zombiegame.work.tool;

import net.minecraft.network.chat.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Information {
    private static final HashMap<Integer, Information> REGISTER_MAP = new HashMap<>();
    public static Information EMPTY = register(TextComponent.EMPTY);
    private static int id = -2;
    private final int ID;
    private final Component component;

    private Information(Component component) {
        id++;
        this.ID = id;
        this.component = component;
    }

    public static Information register(Component component) {
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

    public Component getComponent() {
        return component.copy();
    }

    public static class ZombieGameInformation {
        public static Information WELCOME = register(new TranslatableComponent("message.zombiegame.join_game.welcome"));
        public static Information START_GAME = register(
                new TranslatableComponent("message.zombiegame.join_game.start_game",
                        new TranslatableComponent("message.zombiegame.click_here").withStyle(
                                ((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                        "/zombiegame gui_set_up start_game")).withHoverEvent(
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent(
                                                "message.zombiegame.join_game.hover.start_game")))))));
        public static Information MODIFY_OPTION = register(new TranslatableComponent("message.zombiegame.join_game.option",
                new TranslatableComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame gui_set_up option_modify")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslatableComponent("message.zombiegame.join_game.hover.option_modify")))))));
        public static Information DAY_VAR = register(new TranslatableComponent("message.zombiegame.join_game.day",
                new TranslatableComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zombiegame var get day")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslatableComponent("message.zombiegame.join_game.hover.get_day"))))),
                new TranslatableComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame var set day <time>")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslatableComponent("message.zombiegame.join_game.hover.set_day")))))));

        public static Information OTHER = register(new TranslatableComponent("message.zombiegame.join_game.other",
                new TranslatableComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame game cancel_game")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslatableComponent("message.zombiegame.join_game.hover.cancel_game"))))),
                new TranslatableComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zombiegame game end_game")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslatableComponent("message.zombiegame.join_game.hover.end_game")))))));
    }


}
