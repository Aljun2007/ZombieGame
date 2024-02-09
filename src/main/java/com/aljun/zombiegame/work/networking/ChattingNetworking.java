package com.aljun.zombiegame.work.networking;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.tool.Chatting;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ChattingNetworking {
    public static final String NAME = "chatting_networking";
    public static final String VERSION = "1.0";
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ZombieGame.MOD_ID, NAME), () -> VERSION,
                (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
        INSTANCE.messageBuilder(PackString.class, nextID()).encoder(PackString::toBytes).decoder(
                PackString::new).consumer(PackString::handler).add();
    }

    public static PackString createPack(String message) {
        return new PackString(message);
    }

    private static class PackString {
        private final String message;

        public PackString(String message) {
            this.message = message;
        }

        private PackString( PacketBuffer buffer) {
            message = buffer.readUtf(Short.MAX_VALUE);
        }

        private static void handler(PackString pack, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(pack::receive);
            ctx.get().setPacketHandled(true);
        }

        // When receive Pack
        private void receive() {
            Chatting.sendMessageLocalPlayerOnly(this.message);
        }

        private void toBytes( PacketBuffer  buf) {
            buf.writeUtf(this.message);
        }
    }
}
