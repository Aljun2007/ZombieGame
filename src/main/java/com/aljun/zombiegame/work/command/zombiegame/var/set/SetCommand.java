package com.aljun.zombiegame.work.command.zombiegame.var.set;

import com.aljun.zombiegame.work.command.zombiegame.CommandUtils;
import com.aljun.zombiegame.work.command.zombiegame.var.VarCommand;
import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SetCommand implements Command<CommandSource> {

    public static SetCommand instance = new SetCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal(CommandUtils.NAME[1]));
    }

    private static LiteralArgumentBuilder<CommandSource> load(
             LiteralArgumentBuilder<CommandSource> command) {
        return command.requires((player) -> player.hasPermission(2)).then(Commands.literal(VarCommand.DAY).then(
                Commands.argument("time", LongArgumentType.longArg(0L, Long.MAX_VALUE)).executes((context) -> {
                    GameProperty.TimeProperty.setGameTime(context.getSource().getLevel(),
                            (LongArgumentType.getLong(context, "time")));
                    context.getSource().sendSuccess(new TranslationTextComponent("commands.zombiegame.set.day",
                            new StringTextComponent(String.valueOf(LongArgumentType.getLong(context, "time")))), true);
                    return 0;
                })));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}
