package com.aljun.zombiegame.work.command.zombiegame.var.get;

import com.aljun.zombiegame.work.command.zombiegame.CommandUtils;
import com.aljun.zombiegame.work.command.zombiegame.var.VarCommand;
import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class GetCommand implements Command<CommandSourceStack> {
    public static GetCommand instance = new GetCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal(CommandUtils.NAME[2]));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.then(Commands.literal(VarCommand.DAY).executes((context -> {
            context.getSource().sendSuccess(Component.translatable("commands.zombiegame.get.day", Component.literal(
                    String.valueOf(GameProperty.TimeProperty.getGameTime(context.getSource().getLevel())))), true);
            return 0;
        })));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }

}
