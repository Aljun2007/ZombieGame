package com.aljun.zombiegame.work.command.zombiegame.guisetup.optionmodify;

import com.aljun.zombiegame.work.tool.OptionUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;


public class OptionModifyCommand implements Command<CommandSourceStack> {

    public static OptionModifyCommand instance = new OptionModifyCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("option_modify"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.requires((player) -> player.hasPermission(2)).executes((context -> {
            ServerPlayer player = null;
            if (context.getSource().getEntity() instanceof  ServerPlayer) {
                player = (ServerPlayer) context.getSource().getEntity();
            }
            if (player != null) {
                OptionUtils.startModifyOption(player);
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}