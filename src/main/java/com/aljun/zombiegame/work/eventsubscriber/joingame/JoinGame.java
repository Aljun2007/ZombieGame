package com.aljun.zombiegame.work.eventsubscriber.joingame;

import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.tool.Information;
import com.aljun.zombiegame.work.tool.InformationUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber()
public class JoinGame {

    @SubscribeEvent
    public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (!GameProperty.isStartGame((ServerLevel) event.getEntity().level())) {
            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.WELCOME);
            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.START_GAME);
        } else {
            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.MODIFY_OPTION);
            InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.OTHER);
            if (GameProperty.getMode((ServerLevel) player.level()).equals("normal")) {
                InformationUtils.tellPlayerInformation(player, Information.ZombieGameInformation.DAY_VAR);
            }
        }
    }
}



