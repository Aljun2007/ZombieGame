package com.aljun.zombiegame.work.command.zombiegame.debug.debugitem;

import com.aljun.zombiegame.work.command.zombiegame.debug.debugitem.item.ItemCommand;
import com.aljun.zombiegame.work.datamanager.datamanager.ItemStackDataManager;
import com.aljun.zombiegame.work.eventsubscriber.debug.DEBUG;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DEBUGItemCommand implements Command<CommandSourceStack> {

    public static DEBUGItemCommand instance = new DEBUGItemCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("debug_item"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(
            LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.then(
                ItemCommand.getCommand("throw_item", player -> {
                    ItemStack stack = new ItemStack(Items.STICK);
                    ItemStackDataManager.set(stack, DEBUG.DEBUG_THROW_ITEM, true);
                    stack.setHoverName(Component.literal("Throw Item"));
                    return stack;
                })).then(
                ItemCommand.getCommand("killer", player -> {
                    ItemStack stack = new ItemStack(Items.FIRE_CHARGE);
                    ItemStackDataManager.set(stack, DEBUG.DEBUG_KILLER, true);
                    stack.setHoverName(Component.literal("Killer"));
                    return stack;
                })).then(
                ItemCommand.getCommand("info", player -> {
                    ItemStack stack = new ItemStack(Items.GOLD_INGOT);
                    ItemStackDataManager.set(stack, DEBUG.DEBUG_INFO, true);
                    stack.setHoverName(Component.literal("Info"));
                    return stack;
                }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
