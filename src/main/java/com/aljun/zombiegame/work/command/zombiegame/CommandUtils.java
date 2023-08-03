package com.aljun.zombiegame.work.command.zombiegame;

import com.aljun.zombiegame.work.command.zombiegame.debug.DEBUGCommand;
import com.aljun.zombiegame.work.command.zombiegame.game.GameCommand;
import com.aljun.zombiegame.work.command.zombiegame.guisetup.GuiSetUpCommand;
import com.aljun.zombiegame.work.command.zombiegame.var.VarCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandUtils {
    public static String[] NAME = {"zombiegame", "set", "get", "var"};

    public static LiteralArgumentBuilder<CommandSourceStack> ROOT = Commands.literal(NAME[0]);

    public static LiteralCommandNode<CommandSourceStack> registry(CommandDispatcher<CommandSourceStack> dispatcher) {
        return dispatcher.register(ROOT.then(GuiSetUpCommand.getCommand()).then(GameCommand.getCommand()).then(
                VarCommand.getCommand()).then(DEBUGCommand.getCommand()));
    }
}
