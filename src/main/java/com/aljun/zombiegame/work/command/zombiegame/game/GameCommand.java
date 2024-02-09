package com.aljun.zombiegame.work.command.zombiegame.game;

import com.aljun.zombiegame.work.command.zombiegame.game.cancelgame.CancelGameCommand;
import com.aljun.zombiegame.work.command.zombiegame.game.endgame.EndGameCommand;
import com.aljun.zombiegame.work.command.zombiegame.game.startgame.StartGameCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;


public class GameCommand implements Command<CommandSource> {

    public static GameCommand instance = new GameCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("game"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.requires((player) -> player.hasPermission(2)).then(EndGameCommand.getCommand()).then(
                CancelGameCommand.getCommand()).then(StartGameCommand.getCommand());
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}