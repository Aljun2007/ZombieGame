package com.aljun.zombiegame.work.command.zombiegame.game.cancelgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;


public class CancelGameCommand implements Command<CommandSource> {

    public static CancelGameCommand instance = new CancelGameCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("cancel_game"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.requires((player) -> player.hasPermission(2)).executes((context -> {
            if (GameProperty.isStartGame(context.getSource().getLevel())) {
                GameProperty.cancelGame(context.getSource().getLevel());
                context.getSource().sendSuccess(new TranslationTextComponent("command.zombiegame.cancel_game.successful"),
                        true);
            } else {
                context.getSource().sendFailure(new TranslationTextComponent("command.zombiegame.cancel_game.failed"));
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }

}