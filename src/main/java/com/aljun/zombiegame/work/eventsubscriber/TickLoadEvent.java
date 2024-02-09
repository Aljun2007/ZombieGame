package com.aljun.zombiegame.work.eventsubscriber;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class TickLoadEvent {

    @SubscribeEvent
    public static void tickServer(TickEvent.ServerTickEvent event) {

    }


}
