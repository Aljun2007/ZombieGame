package com.aljun.zombiegame.work.eventsubscriber.zombie;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.datamanager.datamanager.EntityDataManager;
import com.aljun.zombiegame.work.keyset.EntityKeySets;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.BowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.CrossBowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class ZombieMainEvent {

    @SubscribeEvent
    public static void onZombieLoad(EntityJoinLevelEvent event) {
        if (event.getEntity().getLevel().isClientSide()) return;
        if (!GameProperty.isStartGame((ServerLevel) event.getLevel())) {
            return;
        }
        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            Zombie zombie = (Zombie) event.getEntity();
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
        if (event.getEntity().getLevel().getServer() != null) {
            GameProperty.tick(event.getEntity().getLevel().getServer().overworld());
        }
    }

    @SubscribeEvent
    public static void zombieHurtEvent(LivingHurtEvent event) {
        if (event.getEntity().getLevel().isClientSide()) return;
        if (!GameProperty.isStartGame()) {
            return;
        }
        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            Zombie zombie = (Zombie) event.getEntity();
            ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);
            if (mainGoal != null) {
                mainGoal.zombieHurt();
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(ShieldBlockEvent event) {
        if (event.getEntity().getLevel().isClientSide()) return;
        if (event.getDamageSource().getEntity() == null) return;
        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            Zombie zombie = (Zombie) event.getEntity();
            ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);
            if (mainGoal != null) {
                if (mainGoal.zombieShieldUsingGoal.isUsable) {
                    if (mainGoal.zombieShieldUsingGoal.goal.isUsingShield()) {
                        mainGoal.zombieShieldUsingGoal.goal.onShieldBlock(
                                ((LivingEntity) event.getDamageSource().getEntity()).getMainHandItem(),
                                (LivingEntity) event.getDamageSource().getEntity());
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {

        if (event.getEntity().getLevel().isClientSide()) {
            return;
        }
        if (!GameProperty.isStartGame()) {
            return;
        }

        if (ZombieUtils.canBeLoaded(event.getEntity())) {
            Zombie zombie = (Zombie) event.getEntity();

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
                    if (event.getSource().getEntity() instanceof LivingEntity entity) {
                        level = entity.getMainHandItem().getEnchantmentLevel(Enchantments.MOB_LOOTING);
                    }
                }

                zombie.spawnAtLocation(new ItemStack(Items.ARROW, RandomUtils.RANDOM.nextInt(1, level + 3)));
            }
        }
    }
}
