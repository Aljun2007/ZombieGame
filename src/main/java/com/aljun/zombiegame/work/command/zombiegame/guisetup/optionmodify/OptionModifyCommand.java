package com.aljun.zombiegame.work.command.zombiegame.guisetup.optionmodify;

import com.aljun.zombiegame.work.tool.OptionUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class OptionModifyCommand implements Command<CommandSourceStack> {

    public static OptionModifyCommand instance = new OptionModifyCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("option_modify"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.requires((player) -> player.hasPermission(2)).executes((context -> {
            if (context.getSource().getPlayer() != null) {
                OptionUtils.startModifyOption(context.getSource().getPlayer());
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}