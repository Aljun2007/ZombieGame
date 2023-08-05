package com.aljun.zombiegame.work.command.zombiegame.debug;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.command.zombiegame.debug.cleanzombie.CleanZombieCommand;
import com.aljun.zombiegame.work.command.zombiegame.debug.debugitem.DEBUGItemCommand;
import com.aljun.zombiegame.work.command.zombiegame.debug.heal.HealCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class DEBUGCommand implements Command<CommandSourceStack> {

    public static DEBUGCommand instance = new DEBUGCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("debug"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.then(CleanZombieCommand.getCommand()).then(DEBUGItemCommand.getCommand()).then(
                HealCommand.getCommand()).requires((p) -> ZombieGame.DEBUG_MODE && p.hasPermission(2));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
