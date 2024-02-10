package com.aljun.zombiegame.work.command.zombiegame.guisetup;

import com.aljun.zombiegame.work.command.zombiegame.guisetup.optionmodify.OptionModifyCommand;
import com.aljun.zombiegame.work.command.zombiegame.guisetup.startgame.StartGameCommand;
import com.aljun.zombiegame.work.tool.OptionUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class GuiSetUpCommand implements Command<CommandSourceStack> {

    public static GuiSetUpCommand instance = new GuiSetUpCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("gui_set_up"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.then(StartGameCommand.getCommand()).then(OptionModifyCommand.getCommand()).requires(
                (p) -> p.getPlayer() != null);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}