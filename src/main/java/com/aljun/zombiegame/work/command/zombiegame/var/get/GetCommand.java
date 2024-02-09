package com.aljun.zombiegame.work.command.zombiegame.var.get;

import com.aljun.zombiegame.work.command.zombiegame.CommandUtils;
import com.aljun.zombiegame.work.command.zombiegame.var.VarCommand;
import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GetCommand implements Command<CommandSource> {
    public static GetCommand instance = new GetCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal(CommandUtils.NAME[2]));
    }

    public static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.then(Commands.literal(VarCommand.DAY).executes((context -> {
            context.getSource().sendSuccess(new TranslationTextComponent("commands.zombiegame.get.day", new StringTextComponent(
                    String.valueOf(GameProperty.TimeProperty.getGameTime(context.getSource().getLevel())))), true);
            return 0;
        })));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }

}
