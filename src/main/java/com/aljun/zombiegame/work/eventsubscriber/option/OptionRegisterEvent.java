package com.aljun.zombiegame.work.eventsubscriber.option;

import com.aljun.zombiegame.work.option.Options;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OptionRegisterEvent {
    private static boolean registered = false;

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        Options.register();
        registered = true;
        ZombieUtils.reloadMainGoalWeight();
    }

    public static boolean isRegistered() {
        return registered;
    }
}

