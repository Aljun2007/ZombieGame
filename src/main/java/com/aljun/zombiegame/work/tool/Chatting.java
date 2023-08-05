package com.aljun.zombiegame.work.tool;

import com.aljun.zombiegame.work.networking.ChattingNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class Chatting {

    @OnlyIn(Dist.CLIENT)
    public static void sendMessageLocalPlayerOnly(String message) {
        if (Minecraft.getInstance().player != null) {
            sendMessageLocalPlayerOnly(Component.literal(message));
        }
    }

    public static void sendMessageLocalPlayerOnly(MutableComponent message) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(message);
        }
    }

    // Only in Server
    public static void sendMessagePlayerAll(String message) {
        ChattingNetworking.INSTANCE.send(PacketDistributor.ALL.noArg(), ChattingNetworking.createPack(message));
    }

    public static void sendMessagePlayerCertain(String message, ServerPlayer player) {
        ChattingNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                ChattingNetworking.createPack(message));
    }

}
