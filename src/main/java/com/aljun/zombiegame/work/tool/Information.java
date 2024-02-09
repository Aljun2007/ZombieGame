package com.aljun.zombiegame.work.tool;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import java.util.HashMap;

public class Information {
    private static final HashMap<Integer, Information> REGISTER_MAP = new HashMap<>();
    public static Information EMPTY = register(Chatting.EMPTY);
    private static int id = -2;
    private final int ID;
    private final ITextComponent component;

    private Information(ITextComponent component) {
        id++;
        this.ID = id;
        this.component = component;
    }

    public static Information register(ITextComponent component) {
        Information information = new Information(component);
        REGISTER_MAP.put(information.getID(), information);
        return information;
    }
    
    public static Information getInformation(int id) {
        Information information = REGISTER_MAP.get(id);
        return information == null ? Information.EMPTY : information;
    }

    public int getID() {
        return ID;
    }

    public ITextComponent getComponent() {
        return component.copy();
    }

    public static class ZombieGameInformation {
        public static Information WELCOME = register(new TranslationTextComponent("message.zombiegame.join_game.welcome"));
        public static Information START_GAME = register(
                new TranslationTextComponent("message.zombiegame.join_game.start_game",
                        new TranslationTextComponent("message.zombiegame.click_here").withStyle(
                                ((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                        "/zombiegame gui_set_up start_game")).withHoverEvent(
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent(
                                                "message.zombiegame.join_game.hover.start_game")))))));
        public static Information MODIFY_OPTION = register(new TranslationTextComponent("message.zombiegame.join_game.option",
                new TranslationTextComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame gui_set_up option_modify")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslationTextComponent("message.zombiegame.join_game.hover.option_modify")))))));
        public static Information DAY_VAR = register(new TranslationTextComponent("message.zombiegame.join_game.day",
                new TranslationTextComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zombiegame var get day")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslationTextComponent("message.zombiegame.join_game.hover.get_day"))))),
                new TranslationTextComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame var set day <time>")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslationTextComponent("message.zombiegame.join_game.hover.set_day")))))));

        public static Information OTHER = register(new TranslationTextComponent("message.zombiegame.join_game.other",
                new TranslationTextComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/zombiegame game cancel_game")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslationTextComponent("message.zombiegame.join_game.hover.cancel_game"))))),
                new TranslationTextComponent("message.zombiegame.click_here").withStyle(((style) -> style.withClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/zombiegame game end_game")).withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TranslationTextComponent("message.zombiegame.join_game.hover.end_game")))))));
    }


}
