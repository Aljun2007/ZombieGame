package com.aljun.zombiegame.work.eventsubscriber.zombie;

import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ZombieSense {

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity().getLevel().isClientSide()) return;
        if (event.getSource().getEntity() instanceof LivingEntity entity) {
            if (ZombieUtils.canBeTarget(entity)) {
                callToAttack(entity);
            }
        }
        LivingEntity entity = event.getEntity();
        if (ZombieUtils.canBeTarget(entity)) {
            callToAttack(entity);
        }

    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().getLevel().isClientSide()) return;
        Player player = event.getPlayer();
        if (ZombieUtils.canBeTarget(player)) {
            callToAttack(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.getLevel().isClientSide()) return;
        Player player = event.player;
        if (player.getHealth() <= 5) {
            if (ZombieUtils.canBeTarget(player)) {
                callToAttack(player);
            }
        }
    }

    private static void callToAttack(LivingEntity entity) {
        entity.getLevel().getNearestEntity(Zombie.class,
                TargetingConditions.forNonCombat().range(75d).selector((entity1) -> {
                    if (ZombieUtils.canBeLoaded(entity1)) {
                        Zombie zombie1 = (Zombie) entity1;
                        if (zombie1.getTarget() == null) {
                            zombie1.setTarget(entity);
                        }
                    }
                    return false;
                }).ignoreLineOfSight(), entity, entity.getX(), entity.getY(), entity.getZ(),
                entity.getBoundingBox().inflate(20D, 10D, 20D));
    }

}
