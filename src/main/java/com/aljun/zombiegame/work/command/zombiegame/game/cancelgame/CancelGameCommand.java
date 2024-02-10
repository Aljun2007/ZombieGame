package com.aljun.zombiegame.work.command.zombiegame.game.cancelgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;


public class CancelGameCommand implements Command<CommandSourceStack> {

    public static CancelGameCommand instance = new CancelGameCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("cancel_game"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.requires((player) -> player.hasPermission(2)).executes((context -> {
            if (GameProperty.isStartGame(context.getSource().getLevel())) {
                GameProperty.cancelGame(context.getSource().getLevel());
                context.getSource().sendSuccess(()->Component.translatable("command.zombiegame.cancel_game.successful"),
                        true);
            } else {
                context.getSource().sendFailure(Component.translatable("command.zombiegame.cancel_game.failed"));
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}