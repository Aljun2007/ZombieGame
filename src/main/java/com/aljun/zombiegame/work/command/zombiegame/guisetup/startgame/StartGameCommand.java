package com.aljun.zombiegame.work.command.zombiegame.guisetup.startgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.networking.StartGameNetworking;
import com.aljun.zombiegame.work.tool.Chatting;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class StartGameCommand implements Command<CommandSourceStack> {

    public static StartGameCommand instance = new StartGameCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("start_game"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.executes((context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player != null) {
                if (!GameProperty.isStartGame(player.getLevel())) {
                    if (player.getServer() != null) {
                        StartGameNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                                StartGameNetworking.createStartGamePack("normal"));
                    }
                } else {
                    context.getSource().sendFailure(Component.translatable("message.zombiegame.error",
                            Component.translatable("message.zombiegame.startgame.has_started")));
                }
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
