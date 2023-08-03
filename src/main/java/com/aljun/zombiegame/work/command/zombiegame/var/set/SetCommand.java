package com.aljun.zombiegame.work.command.zombiegame.var.set;

import com.aljun.zombiegame.work.command.zombiegame.CommandUtils;
import com.aljun.zombiegame.work.command.zombiegame.var.VarCommand;
import com.aljun.zombiegame.work.game.GameProperty;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class SetCommand implements Command<CommandSourceStack> {

    public static SetCommand instance = new SetCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal(CommandUtils.NAME[1]));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(
            @NotNull LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.requires((player) -> player.hasPermission(2)).then(Commands.literal(VarCommand.DAY).then(
                Commands.argument("time", LongArgumentType.longArg(0L, Long.MAX_VALUE)).executes((context) -> {
                    GameProperty.TimeProperty.setGameTime(context.getSource().getLevel(),
                            (LongArgumentType.getLong(context, "time")));
                    context.getSource().sendSuccess(Component.translatable("commands.zombiegame.set.day",
                            Component.literal(String.valueOf(LongArgumentType.getLong(context, "time")))), true);
                    return 0;
                })));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
