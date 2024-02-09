package com.aljun.zombiegame.work.tool;

import com.aljun.zombiegame.work.networking.InformationNetworking;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class InformationUtils {
    public static void tellPlayerInformation(ServerPlayerEntity player, Information information) {
        InformationNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                InformationNetworking.createPack(information.getID()));
    }
}
