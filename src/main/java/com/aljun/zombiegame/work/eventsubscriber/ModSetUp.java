package com.aljun.zombiegame.work.eventsubscriber;

import com.aljun.zombiegame.work.modlinkage.GuardVillagersModLinkage;
import com.aljun.zombiegame.work.modlinkage.ModLinkage;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetUp {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("guardvillagers")) {
            LOGGER.info("Guard Villagers has Loaded!");
            GuardVillagersModLinkage.get().setLoaded();
        } else {
            LOGGER.info("Guard Villagers has not Loaded!");
        }
        ModLinkage.stopFMLSetup();
    }
}
