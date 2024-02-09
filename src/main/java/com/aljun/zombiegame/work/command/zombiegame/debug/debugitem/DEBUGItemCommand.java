package com.aljun.zombiegame.work.command.zombiegame.debug.debugitem;

import com.aljun.zombiegame.work.command.zombiegame.debug.debugitem.item.ItemCommand;
import com.aljun.zombiegame.work.datamanager.datamanager.ItemStackDataManager;
import com.aljun.zombiegame.work.eventsubscriber.debug.DEBUG;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;


public class DEBUGItemCommand implements Command<CommandSource> {

    public static DEBUGItemCommand instance = new DEBUGItemCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("debug_item"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(
            LiteralArgumentBuilder<CommandSource> command) {
        return command.then(
                ItemCommand.getCommand("throw_item", player -> {
                    ItemStack stack = new ItemStack(Items.STICK);
                    ItemStackDataManager.set(stack, DEBUG.DEBUG_THROW_ITEM, true);
                    stack.setHoverName(new StringTextComponent("Throw Item"));
                    return stack;
                })).then(
                ItemCommand.getCommand("killer", player -> {
                    ItemStack stack = new ItemStack(Items.FIRE_CHARGE);
                    ItemStackDataManager.set(stack, DEBUG.DEBUG_KILLER, true);
                    stack.setHoverName(new StringTextComponent("Killer"));
                    return stack;
                })).then(
                ItemCommand.getCommand("info", player -> {
                    ItemStack stack = new ItemStack(Items.GOLD_INGOT);
                    ItemStackDataManager.set(stack, DEBUG.DEBUG_INFO, true);
                    stack.setHoverName(new StringTextComponent("Info"));
                    return stack;
                }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}
