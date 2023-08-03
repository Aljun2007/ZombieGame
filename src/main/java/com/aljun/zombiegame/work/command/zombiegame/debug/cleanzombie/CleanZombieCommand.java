package com.aljun.zombiegame.work.command.zombiegame.debug.cleanzombie;

import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CleanZombieCommand implements Command<CommandSourceStack> {


    public static CleanZombieCommand instance = new CleanZombieCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("clean_zombie"));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.executes((context -> {
            int[] a = new int[]{1};
            context.getSource().getLevel().getAllEntities().forEach(((entity -> {
                if (ZombieUtils.canBeLoaded(entity)) {
                    entity.kill();
                    a[0]++;
                }
            })));
            context.getSource().sendSuccess(Component.translatable("commands.zombiegame.debug.clean_zombie",
                    Component.literal(String.valueOf(a[0]))), true);
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}


