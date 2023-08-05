package com.aljun.zombiegame.work.command.zombiegame.guisetup.startgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.networking.StartGameNetworking;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class StartGameCommand implements Command<CommandSourceStack> {

    public static StartGameCommand instance = new StartGameCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("start_game"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.executes((context -> {
            ServerPlayer player = null;
            if (context.getSource().getEntity() instanceof ServerPlayer) {
                player = (ServerPlayer) context.getSource().getEntity();
            }
            if (player != null) {
                if (!GameProperty.isStartGame(player.getLevel())) {
                    if (player.getServer() != null) {
                        ServerPlayer player2 = player;
                        StartGameNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player2),
                                StartGameNetworking.createStartGamePack("normal"));
                    }
                } else {
                    context.getSource().sendFailure(new TranslatableComponent("message.zombiegame.error",
                            new TranslatableComponent("message.zombiegame.startgame.has_started")));
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
