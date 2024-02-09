package com.aljun.zombiegame.work.command.zombiegame.guisetup.optionmodify;

import com.aljun.zombiegame.work.tool.OptionUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;


public class OptionModifyCommand implements Command<CommandSource> {

    public static OptionModifyCommand instance = new OptionModifyCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("option_modify"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.requires((player) -> player.hasPermission(2)).executes((context -> {
            ServerPlayerEntity player = null;
            if (context.getSource().getEntity() instanceof  ServerPlayerEntity) {
                player = (ServerPlayerEntity) context.getSource().getEntity();
            }
            if (player != null) {
                OptionUtils.startModifyOption(player);
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}