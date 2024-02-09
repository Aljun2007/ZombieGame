package com.aljun.zombiegame.work.command.zombiegame.game.startgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.tool.Chatting;
import com.aljun.zombiegame.work.tool.Information;
import com.aljun.zombiegame.work.tool.InformationUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class StartGameCommand implements Command<CommandSource> {

    public static StartGameCommand instance = new StartGameCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("start_game"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.requires((player) -> player.hasPermission(2)).then(Commands.literal("normal").then(
                Commands.argument("day_total", IntegerArgumentType.integer(0, 1000)).executes((context -> {
                    if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                        GameProperty.setStartGame(context.getSource().getLevel());
                        GameProperty.setMode(context.getSource().getLevel(), "normal");
                        GameProperty.TimeProperty.setDayTotal(context.getSource().getLevel(),
                                IntegerArgumentType.getInteger(context, "day_total"));
                        context.getSource().sendSuccess(
                                new TranslationTextComponent("command.zombiegame.start_game.successful"), true);
                        ServerPlayerEntity player = null;
                        if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
                            player = (ServerPlayerEntity) context.getSource().getEntity();
                        }
                        if (player != null) {
                            Chatting.sendMessagePlayerCertain(new TranslationTextComponent("message.zombiegame.successful",
                                            new TranslationTextComponent("message.zombiegame.startgame.started")).getString(),
                                    player);
                            InformationUtils.tellPlayerInformation(player,
                                    Information.ZombieGameInformation.MODIFY_OPTION);
                            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
                            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.DAY_VAR);
                        }
                        Chatting.sendMessagePlayerAll(new TranslationTextComponent("message.zombiegame.warn",
                                new TranslationTextComponent("message.zombiegame.startgame.sever_started",
                                        new TranslationTextComponent("message.zombiegame.info.normal"))).getString());
                    } else {
                        context.getSource().sendFailure(
                                new TranslationTextComponent("command.zombiegame.start_game.failed"));
                    }
                    return 0;
                }))).executes((context -> {
            if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                GameProperty.setStartGame(context.getSource().getLevel());
                GameProperty.setMode(context.getSource().getLevel(), "normal");

                context.getSource().sendSuccess(new TranslationTextComponent("command.zombiegame.start_game.successful"),
                        true);
                ServerPlayerEntity player = null;
                if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
                    player = (ServerPlayerEntity) context.getSource().getEntity();
                }
                if (player != null) {
                    Chatting.sendMessagePlayerCertain(new TranslationTextComponent("message.zombiegame.successful",
                            new TranslationTextComponent("message.zombiegame.startgame.started")).getString(), player);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.MODIFY_OPTION);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);

                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.DAY_VAR);
                }
                Chatting.sendMessagePlayerAll(new TranslationTextComponent("message.zombiegame.warn",
                        new TranslationTextComponent("message.zombiegame.startgame.sever_started",
                                new TranslationTextComponent("message.zombiegame.info.normal"))).getString());


            } else {
                context.getSource().sendFailure(new TranslationTextComponent("command.zombiegame.start_game.failed"));
            }
            return 0;
        }))).then(Commands.literal("remain").then(
                Commands.argument("game_stage", DoubleArgumentType.doubleArg(0d, 1d)).executes((context -> {
                    if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                        GameProperty.setStartGame(context.getSource().getLevel());
                        GameProperty.setMode(context.getSource().getLevel(), "remain");
                        GameProperty.TimeProperty.setDayTotal(context.getSource().getLevel(),
                                (int) DoubleArgumentType.getDouble(context, "game_stage"));
                        context.getSource().sendSuccess(
                                new TranslationTextComponent("command.zombiegame.start_game.successful"), true);
                        ServerPlayerEntity player = null;
                        if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
                            player = (ServerPlayerEntity) context.getSource().getEntity();
                        }
                        if (player != null) {
                            Chatting.sendMessagePlayerCertain(new TranslationTextComponent("message.zombiegame.successful",
                                            new TranslationTextComponent("message.zombiegame.startgame.started")).getString(),
                                    player);
                            InformationUtils.tellPlayerInformation(player,
                                    Information.ZombieGameInformation.MODIFY_OPTION);
                            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
                        }
                        Chatting.sendMessagePlayerAll(new TranslationTextComponent("message.zombiegame.warn",
                                new TranslationTextComponent("message.zombiegame.startgame.sever_started",
                                        new TranslationTextComponent("message.zombiegame.info.remain"))).getString());
                    } else {
                        context.getSource().sendFailure(
                                new TranslationTextComponent("command.zombiegame.start_game.failed"));
                    }
                    return 0;
                }))).executes((context -> {
            if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                GameProperty.setStartGame(context.getSource().getLevel());
                GameProperty.setMode(context.getSource().getLevel(), "remain");
                context.getSource().sendSuccess(new TranslationTextComponent("command.zombiegame.start_game.successful"),
                        true);
                ServerPlayerEntity player = null;
                if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
                    player = (ServerPlayerEntity) context.getSource().getEntity();
                }
                if (player != null) {
                    Chatting.sendMessagePlayerCertain(new TranslationTextComponent("message.zombiegame.successful",
                            new TranslationTextComponent("message.zombiegame.startgame.started")).getString(), player);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.MODIFY_OPTION);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
                }
                Chatting.sendMessagePlayerAll(new TranslationTextComponent("message.zombiegame.warn",
                        new TranslationTextComponent("message.zombiegame.startgame.sever_started",
                                new TranslationTextComponent("message.zombiegame.info.remain"))).getString());

            } else {
                context.getSource().sendFailure(new TranslationTextComponent("command.zombiegame.start_game.failed"));
            }
            return 0;

        })));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}