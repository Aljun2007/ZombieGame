package com.aljun.zombiegame.work.command.zombiegame.guisetup.startgame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.networking.StartGameNetworking;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class StartGameCommand implements Command<CommandSource> {

    public static StartGameCommand instance = new StartGameCommand();

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("start_game"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(LiteralArgumentBuilder<CommandSource> command) {
        return command.executes((context -> {
            ServerPlayerEntity player = null;
            if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
                player = (ServerPlayerEntity) context.getSource().getEntity();
            }
            if (player != null) {
                if (!GameProperty.isStartGame(player.getLevel())) {
                    if (player.getServer() != null) {
                        ServerPlayerEntity player2 = player;
                        StartGameNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player2),
                                StartGameNetworking.createStartGamePack("normal"));
                    }
                } else {
                    context.getSource().sendFailure(new TranslationTextComponent("message.zombiegame.error",
                            new TranslationTextComponent("message.zombiegame.startgame.has_started")));
                }
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}
