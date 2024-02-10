package com.aljun.zombiegame.work.command.zombiegame.debug.debugitem.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ItemCommand implements Command<CommandSourceStack> {

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand(String id, Function<Player, ItemStack> stackFunction) {
        return load(Commands.literal(id), stackFunction);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(
            LiteralArgumentBuilder<CommandSourceStack> command, Function<Player, ItemStack> stackFunction) {
        return command.executes((context -> {
            if (context.getSource().getPlayer() != null) {
                ItemStack stack = stackFunction.apply(context.getSource().getPlayer());
                context.getSource().getPlayer().addItem(stack);
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
