package com.aljun.zombiegame.work.command.zombiegame.game.endgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class EndGameCommand implements Command<CommandSourceStack> {

    public static EndGameCommand instance = new EndGameCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("end_game"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.requires((player) -> player.hasPermission(2)).executes((context -> {

            if (GameProperty.hasGameBeenOn(context.getSource().getLevel())) {
                GameProperty.TimeProperty.setGameTime(context.getSource().getLevel(),
                        GameProperty.TimeProperty.getDayTotal(context.getSource().getLevel()));
                context.getSource().sendSuccess(()->Component.translatable("command.zombiegame.end_game.successful"), true);
            } else {
                if (GameProperty.isStartGame(context.getSource().getLevel())) {
                    context.getSource().sendFailure(
                            Component.translatable("command.zombiegame.end_game.failed.already_ended"));
                } else if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                    context.getSource().sendFailure(
                            Component.translatable("command.zombiegame.end_game.failed.did_not_start"));
                }
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
