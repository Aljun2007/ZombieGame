package com.aljun.zombiegame.work.tool;

import com.aljun.zombiegame.work.client.gui.option.ZombieGameOptionsScreen;
import com.aljun.zombiegame.work.networking.OptionSendNetworking;
import com.aljun.zombiegame.work.option.OptionManager;
import com.aljun.zombiegame.work.option.OptionSaver;
import com.aljun.zombiegame.work.option.OptionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;

public class OptionUtils {

    // Only in Server
    public static void startModifyOption(ServerPlayer player) {
        if (player.getServer() != null) {
            OptionSendNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                    OptionSendNetworking.createPack(
                            OptionManager.write(OptionSaver.get(player.getServer().overworld()))));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void saveOptions(HashMap<String, OptionValue<?>> map) {
        OptionSendNetworking.INSTANCE.sendToServer(OptionSendNetworking.createPack(OptionManager.write(map)));
    }

    @OnlyIn(Dist.CLIENT)
    public static void openGui(HashMap<Integer, OptionValue<?>> optionValueHashMap) {
        Minecraft.getInstance().setScreen(
                new ZombieGameOptionsScreen(Component.translatable("gui.zombiegame.option_modify.name"),
                        optionValueHashMap));
    }
}
