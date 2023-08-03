package com.aljun.zombiegame.work.eventsubscriber.zombie;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.keyset.ConfigKeySets;
import com.aljun.zombiegame.work.config.ZombieGameConfig;
import com.aljun.zombiegame.work.datamanager.datamanager.ConfigDataManager;
import com.aljun.zombiegame.work.datamanager.datamanager.EntityDataManager;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.keyset.EntityKeySets;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber()
public class ZombieSpawn {
    private static final List<EntityType<?>> replaceType = new ArrayList<>();
    private static final List<EntityType<?>> removeType = new ArrayList<>();

    static {
        replaceType.add(EntityType.SKELETON);
        replaceType.add(EntityType.CREEPER);
        replaceType.add(EntityType.SPIDER);
        replaceType.add(EntityType.ENDERMAN);
        replaceType.add(EntityType.WITCH);
        replaceType.add(EntityType.ENDERMAN);
        replaceType.add(EntityType.ZOMBIFIED_PIGLIN);
        replaceType.add(EntityType.STRAY);
        replaceType.add(EntityType.ZOGLIN);

        removeType.add(EntityType.SLIME);
        removeType.add(EntityType.PIGLIN);
        removeType.add(EntityType.DROWNED);
    }

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getEntity().getLevel().isClientSide()) {
            return;
        }
        if (!GameProperty.isStartGame()) {
            return;
        }
        if (RandomUtils.nextBoolean(Math.max(0.02, 0.2d - GameProperty.TimeProperty.getGameStage()))) {
            return;
        }

        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            if (RandomUtils.nextBoolean(ConfigDataManager.getAverageByGameStage(ConfigKeySets.IMMUNE_SUN_INIT,
                    ConfigKeySets.IMMUNE_SUN_FINAL))) {
                EntityDataManager.set(event.getEntity(), EntityKeySets.IS_SUN_SENSITIVE, false);
            }
            if (RandomUtils.nextBoolean(
                    ConfigDataManager.getAverageByGameStage(ConfigKeySets.SWIM_INIT, ConfigKeySets.SWIM_FINAL))) {
                EntityDataManager.set(event.getEntity(), EntityKeySets.CAN_FLOAT, true);
                EntityDataManager.set(event.getEntity(), EntityKeySets.CONVERTS_IN_WATER, false);
            }
            if (RandomUtils.nextBoolean(0.2d * GameProperty.TimeProperty.getGameStage())) {
                ZombieUtils.addRandomEffect((Zombie) event.getEntity());
            }
        } else if (event.getSpawnType().equals(MobSpawnType.NATURAL)) {
            EntityType<?> type = event.getEntity().getType();
            ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(type);
            String id = "";
            if (key != null) {
                id = key.toString();
            }
            if ((replaceType.contains(type) || ZombieGameConfig.replaceEntityWhiteList.get().contains(id))
                && !ZombieGameConfig.replaceEntityBlackList.get().contains(id)) {
                if (!event.getEntity().getLevel().dimension().equals(Level.END)) {
                    Zombie zombie = ZombieUtils.randomZombie(event.getEntity().getLevel(),
                            event.getLevel().getBiome(event.getEntity().blockPosition()).is(Biomes.DESERT));
                    zombie.moveTo(event.getEntity().position());
                    zombie.finalizeSpawn(event.getLevel(),
                            event.getLevel().getCurrentDifficultyAt(zombie.blockPosition()), event.getSpawnType(), null,
                            null);
                    zombie.getLevel().addFreshEntity(zombie);
                }
                event.setSpawnCancelled(true);
            } else if ((removeType.contains(type) || ZombieGameConfig.removeEntityWhiteList.get().contains(id))
                       && !ZombieGameConfig.removeEntityBlackList.get().contains(id)) {
                event.setSpawnCancelled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        GameProperty.tick(event.getServer().overworld());
    }
}
