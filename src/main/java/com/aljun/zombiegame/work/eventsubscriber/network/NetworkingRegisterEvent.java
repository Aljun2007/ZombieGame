package com.aljun.zombiegame.work.eventsubscriber.network;

import com.aljun.zombiegame.work.networking.ChattingNetworking;
import com.aljun.zombiegame.work.networking.InformationNetworking;
import com.aljun.zombiegame.work.networking.StartGameNetworking;
import com.aljun.zombiegame.work.networking.OptionSendNetworking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkingRegisterEvent {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ChattingNetworking::registerMessage);
        event.enqueueWork(StartGameNetworking::registerMessage);
        event.enqueueWork(OptionSendNetworking::registerMessage);
        event.enqueueWork(InformationNetworking::registerMessage);
    }
}
