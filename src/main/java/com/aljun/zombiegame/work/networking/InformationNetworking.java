package com.aljun.zombiegame.work.networking;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.tool.Chatting;
import com.aljun.zombiegame.work.tool.Information;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class InformationNetworking {


    public static final String NAME = "player_join_game_networking";
    public static final String VERSION = "1.0";
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ZombieGame.MOD_ID, NAME), () -> VERSION,
                (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
        INSTANCE.messageBuilder(Pack.class, nextID()).encoder(Pack::toBytes).decoder(Pack::new).consumerNetworkThread(
                Pack::handler).add();
    }

    public static Pack createPack(int i) {
        return new Pack(i);
    }

    private static class Pack {
        private final int ID;

        public Pack(int i) {
            this.ID = i;
        }

        private Pack(@NotNull FriendlyByteBuf buffer) {
            this.ID = buffer.readInt();
        }

        private static void handler(Pack pack, @NotNull Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    pack.clientReceive();
                }
            });
            context.get().setPacketHandled(true);
        }


        // When receive Pack
        @OnlyIn(Dist.CLIENT)
        private void clientReceive() {
            Chatting.sendMessageLocalPlayerOnly(Information.getInformation(this.ID).getComponent());
        }

        private void toBytes(@NotNull FriendlyByteBuf buf) {
            buf.writeInt(this.ID);
        }
    }
}
