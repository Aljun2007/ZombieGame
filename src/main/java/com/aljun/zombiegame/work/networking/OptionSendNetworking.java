package com.aljun.zombiegame.work.networking;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionSaver;
import com.aljun.zombiegame.work.tool.Chatting;
import com.aljun.zombiegame.work.tool.OptionUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Supplier;

public class OptionSendNetworking {
    public static final String NAME = "option_send_networking";
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

    public static Pack createPack(HashMap<String, String> map) {
        return new Pack(map);
    }

    private static class Pack {
        private final HashMap<String, String> map;

        private Pack(HashMap<String, String> map) {
            this.map = map;
        }

        private Pack(@NotNull FriendlyByteBuf buffer) {
            this.map = (HashMap<String, String>) buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf);
        }

        private static void handler(Pack pack, @NotNull Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    ServerPlayer player = context.get().getSender();
                    if (player != null) {
                        if (player.getServer() != null) {
                            pack.serverReceive(player.getServer().overworld());
                            Chatting.sendMessagePlayerCertain(Component.translatable("message.zombiegame.successful",
                                            Component.translatable("message.zombiegame.option_value.saved")).getString(),
                                    player);
                        }
                    }
                } else {
                    pack.clientReceive();
                }
            });
            context.get().setPacketHandled(true);
        }

        private void serverReceive(ServerLevel level) {
            OptionSaver.save(OptionManager.read(this.map), level);
        }

        // When receive Pack
        @OnlyIn(Dist.CLIENT)
        private void clientReceive() {
            OptionUtils.openGui(OptionManager.readInOrder(this.map));
        }


        private void toBytes(@NotNull FriendlyByteBuf buf) {
            buf.writeMap(this.map, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
        }
    }
}
