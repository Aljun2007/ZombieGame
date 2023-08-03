package com.aljun.zombiegame.work.command.zombiegame.var;

import com.aljun.zombiegame.work.command.zombiegame.CommandUtils;
import com.aljun.zombiegame.work.command.zombiegame.var.get.GetCommand;
import com.aljun.zombiegame.work.command.zombiegame.var.set.SetCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class VarCommand implements Command<CommandSourceStack> {

    public static VarCommand instance = new VarCommand();
    public static String DAY = "day";

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal(CommandUtils.NAME[3]));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.then(SetCommand.getCommand()).then(GetCommand.getCommand());
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
