package com.aljun.zombiegame.work.tool;

import com.aljun.zombiegame.work.networking.InformationNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class InformationUtils {
    public static void tellPlayerInformation(ServerPlayer player, Information information) {
        InformationNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                InformationNetworking.createPack(information.getID()));
    }
}
