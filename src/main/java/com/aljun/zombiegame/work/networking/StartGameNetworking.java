package com.aljun.zombiegame.work.networking;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.client.gui.startgame.StartGameScreen;
import com.aljun.zombiegame.work.tool.Chatting;
import com.aljun.zombiegame.work.tool.Information;
import com.aljun.zombiegame.work.tool.InformationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class StartGameNetworking {
    public static final String NAME = "start_game_networking";
    public static final String VERSION = "1.0";
    public static final String NAME_DAY_TIME = "networking_day_time";
    public static final String VERSION_DAY_TIME = "1.1";
    public static final String NAME_GAME_STAGE = "networking_game_stage";
    public static final String VERSION_GAME_STAGE = "1.2";
    public static SimpleChannel INSTANCE;
    public static SimpleChannel INSTANCE_DAY_TIME;
    public static SimpleChannel INSTANCE_GAME_STAGE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ZombieGame.MOD_ID, NAME), () -> VERSION,
                (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
        INSTANCE.messageBuilder(PackStartGame.class, nextID()).encoder(PackStartGame::toBytes).decoder(
                PackStartGame::new).consumer(PackStartGame::handler).add();

        INSTANCE_DAY_TIME = NetworkRegistry.newSimpleChannel(new ResourceLocation(ZombieGame.MOD_ID, NAME_DAY_TIME),
                () -> VERSION_DAY_TIME, (version) -> version.equals(VERSION_DAY_TIME),
                (version) -> version.equals(VERSION_DAY_TIME));
        INSTANCE_DAY_TIME.messageBuilder(PackInt.class, nextID()).encoder(PackInt::toBytes).decoder(
                PackInt::new).consumer(PackInt::handler).add();

        INSTANCE_GAME_STAGE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ZombieGame.MOD_ID, VERSION_GAME_STAGE), () -> VERSION_GAME_STAGE,
                (version) -> version.equals(VERSION_GAME_STAGE), (version) -> version.equals(VERSION_GAME_STAGE));
        INSTANCE_GAME_STAGE.messageBuilder(PackDouble.class, nextID()).encoder(PackDouble::toBytes).decoder(
                PackDouble::new).consumer(PackDouble::handler).add();
    }

    public static PackStartGame createStartGamePack(String mode) {
        return new PackStartGame(mode);
    }

    public static PackInt createGameDayPack(int dayTotal) {
        return new PackInt(dayTotal);
    }

    public static PackDouble createGameStagePack(double gameStage) {
        return new PackDouble(gameStage);
    }

    private static class PackStartGame {

        private final String MODE;

        private PackStartGame( PacketBuffer buffer) {
            this.MODE = buffer.readUtf();
        }

        public PackStartGame(String mode) {
            this.MODE = mode;
        }

        private static void handler(PackStartGame pack,  Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    ServerPlayerEntity player = context.get().getSender();
                    if (player != null) {

                        boolean b = GameProperty.setStartGame(player.getLevel());
                        if (b) {

                            GameProperty.setMode(player.getLevel(), pack.MODE);
                            Chatting.sendMessagePlayerCertain(new TranslationTextComponent("message.zombiegame.successful",
                                            new TranslationTextComponent("message.zombiegame.startgame.started")).getString(),
                                    player);
                            Chatting.sendMessagePlayerAll(new TranslationTextComponent("message.zombiegame.warn",
                                    new TranslationTextComponent("message.zombiegame.startgame.sever_started",
                                            new TranslationTextComponent(
                                                    "message.zombiegame.info." + pack.MODE))).getString());
                            InformationUtils.tellPlayerInformation(player,
                                    Information.ZombieGameInformation.MODIFY_OPTION);
                            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
                            if (pack.MODE.equals("normal")) {
                                InformationUtils.tellPlayerInformation(player,
                                        Information.ZombieGameInformation.DAY_VAR);
                            }


                        } else {
                            Chatting.sendMessagePlayerCertain(new TranslationTextComponent("message.zombiegame.error",
                                            new TranslationTextComponent("message.zombiegame.startgame.has_started")).getString(),
                                    player);
                        }

                    }
                } else {
                    pack.clientReceive();
                }
            });
            context.get().setPacketHandled(true);
        }


        // When receive Pack
        @OnlyIn(Dist.CLIENT)
        private void clientReceive() {
            Minecraft.getInstance().setScreen(
                    new StartGameScreen(new TranslationTextComponent("gui.zombiegame.start_game.name")));
        }

        private void toBytes( PacketBuffer buf) {
            buf.writeUtf(this.MODE);
        }
    }

    private static class PackInt {
        private final int dayTotal;

        public PackInt(int dayTotal) {
            this.dayTotal = dayTotal;
        }

        private PackInt( PacketBuffer buffer) {
            dayTotal = buffer.readInt();
        }

        private static void handler(PackInt pack,  Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    ServerPlayerEntity player = context.get().getSender();
                    if (player != null) {
                        GameProperty.TimeProperty.setDayTotal(player.getLevel(), pack.dayTotal);
                    }
                }
            });
            context.get().setPacketHandled(true);
        }


        private void toBytes( PacketBuffer buf) {
            buf.writeInt(this.dayTotal);
        }
    }

    private static class PackDouble {
        private final double gameStage;

        public PackDouble(double gameStage) {
            this.gameStage = gameStage;
        }

        private PackDouble( PacketBuffer buffer) {
            gameStage = buffer.readDouble();
        }

        private static void handler(PackDouble pack,  Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    ServerPlayerEntity player = context.get().getSender();
                    if (player != null) {
                        GameProperty.TimeProperty.setRemainModeGameStage(player.getLevel(), pack.gameStage);
                    }
                }
            });
            context.get().setPacketHandled(true);
        }


        private void toBytes( PacketBuffer buf) {
            buf.writeDouble(this.gameStage);
        }
    }

}
