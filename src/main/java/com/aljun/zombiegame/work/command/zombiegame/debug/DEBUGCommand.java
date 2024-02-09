package com.aljun.zombiegame.work.command.zombiegame.debug;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.command.zombiegame.debug.cleanzombie.CleanZombieCommand;
import com.aljun.zombiegame.work.command.zombiegame.debug.debugitem.DEBUGItemCommand;
import com.aljun.zombiegame.work.command.zombiegame.debug.heal.HealCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;


public class DEBUGCommand implements Command<CommandSource> {

    public static DEBUGCommand instance = new DEBUGCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("debug"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.then(CleanZombieCommand.getCommand()).then(DEBUGItemCommand.getCommand()).then(
                HealCommand.getCommand()).requires((p) -> ZombieGame.DEBUG_MODE && p.hasPermission(2));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}
