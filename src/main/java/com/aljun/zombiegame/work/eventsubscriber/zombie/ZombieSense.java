package com.aljun.zombiegame.work.eventsubscriber.zombie;

import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ZombieSense {

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()) return;
        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity entity = event.getEntityLiving();
            if (ZombieUtils.canBeTarget(entity)) {
                callToAttack(entity);
            }
        }
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (ZombieUtils.canBeTarget(entity)) {
            callToAttack(entity);
        }

    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level.isClientSide()) return;
        PlayerEntity player = event.getPlayer();
        if (ZombieUtils.canBeTarget(player)) {
            callToAttack(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level.isClientSide()) return;
        PlayerEntity player = event.player;
        if (player.getHealth() <= 5) {
            if (ZombieUtils.canBeTarget(player)) {
                callToAttack(player);
            }
        }
    }

    private static void callToAttack(LivingEntity entity) {
        entity.level.getNearestEntity(ZombieEntity.class,
                (new EntityPredicate()).range(75d).selector((entity1) -> {
                    if (ZombieUtils.canBeLoaded(entity1)) {
                        ZombieEntity zombie1 = (ZombieEntity) entity1;
                        if (zombie1.getTarget() == null) {
                            zombie1.setTarget(entity);
                        }
                    }
                    return false;
                }), entity, entity.getX(), entity.getY(), entity.getZ(),
                entity.getBoundingBox().inflate(20D, 10D, 20D));
    }

}
