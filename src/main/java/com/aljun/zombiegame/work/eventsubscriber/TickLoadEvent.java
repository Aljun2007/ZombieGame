package com.aljun.zombiegame.work.eventsubscriber;

import com.mojang.logging.LogUtils;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber()
public class TickLoadEvent {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void tickServer(TickEvent.ServerTickEvent event) {

    }


}
