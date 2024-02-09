package com.aljun.zombiegame.work.eventsubscriber.zombie;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.keyset.ConfigKeySets;
import com.aljun.zombiegame.work.config.ZombieGameConfig;
import com.aljun.zombiegame.work.datamanager.datamanager.ConfigDataManager;
import com.aljun.zombiegame.work.datamanager.datamanager.EntityDataManager;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.keyset.EntityKeySets;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
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
    public static void onMobSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity().level.isClientSide()) {
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
                ZombieUtils.addRandomEffect((ZombieEntity) event.getEntity());
            }
        } else if (event.getSpawnReason().equals(SpawnReason.NATURAL)) {
            EntityType<?> type = event.getEntity().getType();
            ResourceLocation key = ForgeRegistries.ENTITIES.getKey(type);
            String id = "";
            if (key != null) {
                id = key.toString();
            }
            if ((replaceType.contains(type) || ZombieGameConfig.replaceEntityWhiteList.get().contains(id))
                && !ZombieGameConfig.replaceEntityBlackList.get().contains(id)) {
                if (!event.getEntity().level.dimension().equals(World.END)) {
                    ZombieEntity zombie = ZombieUtils.randomZombie(event.getEntity().level,
                            event.getWorld().getBiome(event.getEntity().blockPosition()).getBiomeCategory() == Biome.Category.DESERT);
                    zombie.moveTo(event.getEntity().position());
                    zombie.finalizeSpawn((IServerWorld) event.getWorld(),
                            event.getWorld().getCurrentDifficultyAt(zombie.blockPosition()), event.getSpawnReason(), null,
                            null);
                    zombie.level.addFreshEntity(zombie);
                }
                event.getEntity().remove(false);
                event.setCanceled(true);
            } else if ((removeType.contains(type) || ZombieGameConfig.removeEntityWhiteList.get().contains(id))
                       && !ZombieGameConfig.removeEntityBlackList.get().contains(id)) {
                event.getEntity().remove(false);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        if (event.world.isClientSide()) return;
        if (event.world.getServer()!=null) {
            GameProperty.tick(event.world.getServer().overworld());
        }
    }
}
