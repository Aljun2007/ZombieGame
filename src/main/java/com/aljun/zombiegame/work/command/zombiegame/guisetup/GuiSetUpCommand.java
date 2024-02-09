package com.aljun.zombiegame.work.command.zombiegame.guisetup;

import com.aljun.zombiegame.work.command.zombiegame.guisetup.optionmodify.OptionModifyCommand;
import com.aljun.zombiegame.work.command.zombiegame.guisetup.startgame.StartGameCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;


public class GuiSetUpCommand implements Command<CommandSource> {

    public static GuiSetUpCommand instance = new GuiSetUpCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("gui_set_up"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.then(StartGameCommand.getCommand()).then(OptionModifyCommand.getCommand());
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}