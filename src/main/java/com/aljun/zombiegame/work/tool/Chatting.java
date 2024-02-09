package com.aljun.zombiegame.work.tool;

import com.aljun.zombiegame.work.networking.ChattingNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;


public class Chatting {

    public static final ITextComponent EMPTY = new StringTextComponent("");

    @OnlyIn(Dist.CLIENT)
    public static void sendMessageLocalPlayerOnly(String message) {
        if (Minecraft.getInstance().player != null) {
            sendMessageLocalPlayerOnly(new TranslationTextComponent(message));
        }
    }

    public static void sendMessageLocalPlayerOnly(ITextComponent message) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendMessage(message,Minecraft.getInstance().player.getUUID());
        }
    }

    // Only in Server
    public static void sendMessagePlayerAll(String message) {
        ChattingNetworking.INSTANCE.send(PacketDistributor.ALL.noArg(), ChattingNetworking.createPack(message));
    }

    public static void sendMessagePlayerCertain(String message, ServerPlayerEntity player) {
        ChattingNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                ChattingNetworking.createPack(message));
    }

}
