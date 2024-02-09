package com.aljun.zombiegame.work.command.zombiegame;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;

public abstract class AbstractCommand implements Command<CommandSource> {

    protected static AbstractCommand instance;
    protected static LiteralArgumentBuilder<CommandSource> command;

    public static AbstractCommand getInstance() {
        return instance;
    }

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return command;
    }

    protected static LiteralArgumentBuilder<CommandSource> load(
            LiteralArgumentBuilder<CommandSource> command) {
        return command;
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}
