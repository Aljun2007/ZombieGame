package com.aljun.zombiegame.work.command.zombiegame.debug.cleanzombie;

import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CleanZombieCommand implements Command<CommandSource> {


    public static CleanZombieCommand instance = new CleanZombieCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("clean_zombie"));
    }

    public static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.executes((context -> {
            int[] a = new int[]{1};
            context.getSource().getLevel().getAllEntities().forEach(((entity -> {
                if (ZombieUtils.canBeLoaded(entity)) {
                    entity.kill();
                    a[0]++;
                }
            })));
            context.getSource().sendSuccess(new TranslationTextComponent("commands.zombiegame.debug.clean_zombie",
                    new StringTextComponent(String.valueOf(a[0]))), true);
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}


