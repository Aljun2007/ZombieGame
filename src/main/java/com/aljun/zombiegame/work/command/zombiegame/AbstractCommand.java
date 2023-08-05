package com.aljun.zombiegame.work.command.zombiegame;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public abstract class AbstractCommand implements Command<CommandSourceStack> {

    protected static AbstractCommand instance;
    protected static LiteralArgumentBuilder<CommandSourceStack> command;

    public static AbstractCommand getInstance() {
        return instance;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return command;
    }

    protected static LiteralArgumentBuilder<CommandSourceStack> load(
            LiteralArgumentBuilder<CommandSourceStack> command) {
        return command;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
