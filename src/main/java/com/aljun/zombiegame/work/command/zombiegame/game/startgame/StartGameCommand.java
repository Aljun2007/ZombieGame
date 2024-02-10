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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class StartGameCommand implements Command<CommandSourceStack> {

    public static StartGameCommand instance = new StartGameCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("start_game"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.requires((player) -> player.hasPermission(2)).then(Commands.literal("normal").then(
                Commands.argument("day_total", IntegerArgumentType.integer(0, 1000)).executes((context -> {
                    if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                        GameProperty.setStartGame(context.getSource().getLevel());
                        GameProperty.setMode(context.getSource().getLevel(), "normal");
                        GameProperty.TimeProperty.setDayTotal(context.getSource().getLevel(),
                                IntegerArgumentType.getInteger(context, "day_total"));
                        context.getSource().sendSuccess(
                                ()->Component.translatable("command.zombiegame.start_game.successful"), true);
                        ServerPlayer player = context.getSource().getPlayer();
                        if (player != null) {
                            Chatting.sendMessagePlayerCertain(Component.translatable("message.zombiegame.successful",
                                            Component.translatable("message.zombiegame.startgame.started")).getString(),
                                    player);
                            InformationUtils.tellPlayerInformation(player,
                                    Information.ZombieGameInformation.MODIFY_OPTION);
                            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);

                            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.DAY_VAR);
                        }
                        Chatting.sendMessagePlayerAll(Component.translatable("message.zombiegame.warn",
                                Component.translatable("message.zombiegame.startgame.sever_started",
                                        Component.translatable("message.zombiegame.info.normal"))).getString());
                    } else {
                        context.getSource().sendFailure(Component.translatable("command.zombiegame.start_game.failed"));
                    }
                    return 0;
                }))).executes((context -> {
            if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                GameProperty.setStartGame(context.getSource().getLevel());
                GameProperty.setMode(context.getSource().getLevel(), "normal");

                context.getSource().sendSuccess(()->Component.translatable("command.zombiegame.start_game.successful"),
                        true);
                ServerPlayer player = context.getSource().getPlayer();
                if (player != null) {
                    Chatting.sendMessagePlayerCertain(Component.translatable("message.zombiegame.successful",
                            Component.translatable("message.zombiegame.startgame.started")).getString(), player);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.MODIFY_OPTION);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.DAY_VAR);
                }
                Chatting.sendMessagePlayerAll(Component.translatable("message.zombiegame.warn",
                        Component.translatable("message.zombiegame.startgame.sever_started",
                                Component.translatable("message.zombiegame.info.normal"))).getString());

            } else {
                context.getSource().sendFailure(Component.translatable("command.zombiegame.start_game.failed"));
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
                                ()->Component.translatable("command.zombiegame.start_game.successful"), true);
                        ServerPlayer player = context.getSource().getPlayer();
                        if (player != null) {
                            Chatting.sendMessagePlayerCertain(Component.translatable("message.zombiegame.successful",
                                            Component.translatable("message.zombiegame.startgame.started")).getString(),
                                    player);
                            InformationUtils.tellPlayerInformation(player,
                                    Information.ZombieGameInformation.MODIFY_OPTION);
                            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
                        }
                        Chatting.sendMessagePlayerAll(Component.translatable("message.zombiegame.warn",
                                Component.translatable("message.zombiegame.startgame.sever_started",
                                        Component.translatable("message.zombiegame.info.remain"))).getString());
                    } else {
                        context.getSource().sendFailure(Component.translatable("command.zombiegame.start_game.failed"));
                    }
                    return 0;
                }))).executes((context -> {
            if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                GameProperty.setStartGame(context.getSource().getLevel());
                GameProperty.setMode(context.getSource().getLevel(), "remain");
                context.getSource().sendSuccess(()->Component.translatable("command.zombiegame.start_game.successful"),
                        true);
                ServerPlayer player = context.getSource().getPlayer();
                if (player != null) {
                    Chatting.sendMessagePlayerCertain(Component.translatable("message.zombiegame.successful",
                            Component.translatable("message.zombiegame.startgame.started")).getString(), player);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.MODIFY_OPTION);
                    InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
                }
                Chatting.sendMessagePlayerAll(Component.translatable("message.zombiegame.warn",
                        Component.translatable("message.zombiegame.startgame.sever_started",
                                Component.translatable("message.zombiegame.info.remain"))).getString());
            } else {
                context.getSource().sendFailure(Component.translatable("command.zombiegame.start_game.failed"));
            }
            return 0;

        })));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}