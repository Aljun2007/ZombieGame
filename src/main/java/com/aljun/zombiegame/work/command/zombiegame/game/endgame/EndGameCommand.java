package com.aljun.zombiegame.work.command.zombiegame.game.endgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;


public class EndGameCommand implements Command<CommandSource> {

    public static EndGameCommand instance = new EndGameCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("end_game"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.requires((player) -> player.hasPermission(2)).executes((context -> {

            if (GameProperty.hasGameBeenOn(context.getSource().getLevel())) {
                GameProperty.TimeProperty.setGameTime(context.getSource().getLevel(),
                        GameProperty.TimeProperty.getDayTotal(context.getSource().getLevel()));
                context.getSource().sendSuccess(new TranslationTextComponent("command.zombiegame.end_game.successful"), true);
            } else {
                if (GameProperty.isStartGame(context.getSource().getLevel())) {
                    context.getSource().sendFailure(
                            new TranslationTextComponent("command.zombiegame.end_game.failed.already_ended"));
                } else if (!GameProperty.isStartGame(context.getSource().getLevel())) {
                    context.getSource().sendFailure(
                            new TranslationTextComponent("command.zombiegame.end_game.failed.did_not_start"));
                }
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}
