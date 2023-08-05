package com.aljun.zombiegame.work;

import com.aljun.zombiegame.work.config.ZombieGameConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.Predicate;

@Mod(ZombieGame.MOD_ID)
public class ZombieGame {
    public static final String MOD_ID = "zombiegame";
    public static final boolean DEBUG_MODE = false;

    public ZombieGame() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ZombieGameConfig.commonConfig);
    }
}

