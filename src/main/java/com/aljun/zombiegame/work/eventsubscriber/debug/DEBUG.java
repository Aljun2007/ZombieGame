package com.aljun.zombiegame.work.eventsubscriber.debug;

import com.aljun.zombiegame.work.keyset.KeySet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DEBUG {

    public static final KeySet<Boolean> DEBUG_THROW_ITEM = new KeySet<>("debug_throw_item", false);
    public static final KeySet<Boolean> DEBUG_KILLER = new KeySet<>("debug_item", false);
    public static final KeySet<Boolean> DEBUG_INFO = new KeySet<>("debug_info", false);

    private static void throwItem(LivingEntity entity, EquipmentSlotType slot) {
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