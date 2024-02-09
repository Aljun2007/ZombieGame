package com.aljun.zombiegame.work.eventsubscriber.zombie;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.datamanager.datamanager.EntityDataManager;
import com.aljun.zombiegame.work.keyset.EntityKeySets;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.BowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.CrossBowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class ZombieMainEvent {

    @SubscribeEvent
    public static void onZombieLoad(EntityJoinWorldEvent event) {
        if (event.getEntity().level.isClientSide()) return;
        if (!GameProperty.isStartGame((ServerWorld) event.getWorld())) {
            return;
        }
        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            ZombieEntity zombie = (ZombieEntity) event.getEntity();
            ZombieUtils.loadZombieGoal(zombie);
            if (ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
                if (EntityDataManager.getOrDefault(zombie, EntityKeySets.CAN_FLOAT)) {
                    zombie.getNavigation().setCanFloat(true);
                }
            }
            if (!EntityDataManager.getOrDefault(zombie, EntityKeySets.LOADED_ITEMS)) {
                ZombieUtils.loadZombieItems(zombie);
                EntityDataManager.set(zombie, EntityKeySets.LOADED_ITEMS, true);
            }
        }
        if (event.getEntity().level.getServer() != null) {
            GameProperty.tick(event.getEntity().level.getServer().overworld());
        }
    }

    @SubscribeEvent
    public static void zombieHurtEvent(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()) return;
        if (!GameProperty.isStartGame()) {
            return;
        }
        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            ZombieEntity zombie = (ZombieEntity) event.getEntity();
            ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);
            if (mainGoal != null) {
                mainGoal.zombieHurt();
            }
        }
    }

//    @SubscribeEvent
//    public static void onAttack(ShieldBlockEvent event) {
//        if (event.getEntity().getLevel().isClientSide()) return;
//        if (event.getDamageSource().getEntity() == null) return;
//        if (ZombieUtils.canBeLoaded(event.getEntity())) {
//            ZombieEntity zombie = (ZombieEntity) event.getEntity();
//            ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);
//            if (mainGoal != null) {
//                if (mainGoal.zombieShieldUsingGoal.isUsable) {
//                    if (mainGoal.zombieShieldUsingGoal.goal.isUsingShield()) {
//                        mainGoal.zombieShieldUsingGoal.goal.onShieldBlock(
//                                ((LivingEntity) event.getDamageSource().getEntity()).getMainHandItem(),
//                                (LivingEntity) event.getDamageSource().getEntity());
//                    }
//                }
//            }
//        }
//    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {

        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!GameProperty.isStartGame()) {
            return;
        }

        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            ZombieEntity zombie = (ZombieEntity) event.getEntity();

            String mainGoalName = "";
            ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);

            if (mainGoal != null) {
                mainGoalName = mainGoal.getName();
            }

            if (mainGoal != null) {
                mainGoal.zombieDeath();
            }
            if (mainGoalName.equals(BowAttackZombieGoal.NAME) || mainGoalName.equals(CrossBowAttackZombieGoal.NAME)) {
                int level = 0;
                if (event.getSource().getEntity() != null) {
                    if (event.getSource().getEntity() instanceof LivingEntity) {
                        LivingEntity entity = event.getEntityLiving();
                        level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING,entity.getMainHandItem());
                    }
                }

                zombie.spawnAtLocation(new ItemStack(Items.ARROW, RandomUtils.nextInt(1, level + 3)));
            }
        }
    }
}
