package com.aljun.zombiegame.work.command.zombiegame.debug.debugitem.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

public class ItemCommand implements Command<CommandSource> {

    public static LiteralArgumentBuilder<CommandSource> getCommand(String id, Function<PlayerEntity, ItemStack> stackFunction) {
        return load(Commands.literal(id), stackFunction);
    }

    private static LiteralArgumentBuilder<CommandSource> load(
            LiteralArgumentBuilder<CommandSource> command, Function<PlayerEntity, ItemStack> stackFunction) {
        return command.executes((context -> {
            ServerPlayerEntity player = null;
            if (context.getSource().getEntity() instanceof  ServerPlayerEntity) {
                player = (ServerPlayerEntity) context.getSource().getEntity();
            }
            if (player != null) {
                ItemStack stack = stackFunction.apply(player);
                player.addItem(stack);
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}
