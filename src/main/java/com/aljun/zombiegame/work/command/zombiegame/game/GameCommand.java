package com.aljun.zombiegame.work.command.zombiegame.game;

import com.aljun.zombiegame.work.command.zombiegame.game.cancelgame.CancelGameCommand;
import com.aljun.zombiegame.work.command.zombiegame.game.endgame.EndGameCommand;
import com.aljun.zombiegame.work.command.zombiegame.game.startgame.StartGameCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class GameCommand implements Command<CommandSourceStack> {

    public static GameCommand instance = new GameCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("game"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.requires((player) -> player.hasPermission(2)).then(EndGameCommand.getCommand()).then(
                CancelGameCommand.getCommand()).then(StartGameCommand.getCommand());
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}