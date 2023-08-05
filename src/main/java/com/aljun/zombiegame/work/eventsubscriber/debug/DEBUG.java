package com.aljun.zombiegame.work.eventsubscriber.debug;

import com.aljun.zombiegame.work.ZombieGame;
import com.aljun.zombiegame.work.datamanager.datamanager.ItemStackDataManager;
import com.aljun.zombiegame.work.keyset.KeySet;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DEBUG {

    public static final KeySet<Boolean> DEBUG_THROW_ITEM = new KeySet<>("debug_throw_item", false);
    public static final KeySet<Boolean> DEBUG_KILLER = new KeySet<>("debug_item", false);
    public static final KeySet<Boolean> DEBUG_INFO = new KeySet<>("debug_info", false);

    private static void throwItem(LivingEntity entity, EquipmentSlot slot) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (!stack.isEmpty()) {
            entity.spawnAtLocation(stack);
            entity.setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    private static void killed(LivingEntity entity) {
        if (entity.isAlive()) {
            entity.kill();
        }
    }
}