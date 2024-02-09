package com.aljun.zombiegame.work.networking;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionSaver;
import com.aljun.zombiegame.work.tool.Chatting;
import com.aljun.zombiegame.work.tool.OptionUtils;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
        INSTANCE.messageBuilder(Pack.class, nextID()).encoder(Pack::toBytes).decoder(Pack::new).consumer(
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

        private Pack( PacketBuffer buffer) {
            int i = buffer.readVarInt();
            HashMap<String,String> output = Maps.newHashMapWithExpectedSize(i);
            for(int j = 0; j < i; ++j) {
                String k = buffer.readUtf();
                String v = buffer.readUtf();
                output.put(k, v);
            }
            this.map = output;
        }

        private static void handler(Pack pack,  Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    ServerPlayerEntity player = context.get().getSender();
                    if (player != null) {
                        if (player.getServer() != null) {
                            pack.serverReceive(player.getServer().overworld());
                            Chatting.sendMessagePlayerCertain(new TranslationTextComponent("message.zombiegame.successful",
                                            new TranslationTextComponent("message.zombiegame.option_value.saved")).getString(),
                                    player);
                        }
                    }
                } else {
                    pack.clientReceive();
                }
            });
            context.get().setPacketHandled(true);
        }

        private void serverReceive(ServerWorld level) {
            OptionSaver.save(OptionManager.read(this.map), level);
        }

        // When receive Pack
        @OnlyIn(Dist.CLIENT)
        private void clientReceive() {
            OptionUtils.openGui(OptionManager.readInOrder(this.map));
        }


        private void toBytes( PacketBuffer buf) {
            buf.writeVarInt(this.map.size());
            map.forEach((k, v) -> {
                buf.writeUtf(k);
                buf.writeUtf(v);
            });
        }
    }
}
