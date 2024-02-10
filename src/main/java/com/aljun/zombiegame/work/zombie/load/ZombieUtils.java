package com.aljun.zombiegame.work.zombie.load;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.keyset.ConfigKeySets;
import com.aljun.zombiegame.work.config.ZombieGameConfig;
import com.aljun.zombiegame.work.datamanager.datamanager.ConfigDataManager;
import com.aljun.zombiegame.work.datamanager.datamanager.EntityDataManager;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.keyset.EntityKeySets;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.*;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned.OnlyCanBreakDrownedGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned.OnlyCanBreakPathBuilderDrownedGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned.PathBuilderDrownedGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned.SimpleDrownedGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public class ZombieUtils {
    private static final RandomUtils.RandomHelper<MobEffect> EFFECT_RANDOM = RandomUtils.RandomHelper.create();
    private static final RandomUtils.RandomHelper<String> RANDOM_LAND_BLOCK = RandomUtils.RandomHelper.create();
    private static final RandomUtils.RandomHelper<String> RANDOM_WATER_BLOCK = RandomUtils.RandomHelper.create();
    private static final RandomUtils.RandomHelper<Item> DROWNED_ITEM = RandomUtils.RandomHelper.create();
    public static RandomUtils.RandomHelper<Function<Zombie, ZombieMainGoal>> initCommonZombieWeight;
    public static RandomUtils.RandomHelper<Function<Zombie, ZombieMainGoal>> finalCommonZombieWeight;
    public static RandomUtils.RandomHelper<Function<Zombie, ZombieMainGoal>> initDrownedWeight;
    public static RandomUtils.RandomHelper<Function<Zombie, ZombieMainGoal>> finalDrownedWeight;

    static {
        RANDOM_LAND_BLOCK.add("minecraft:cobblestone", 5.0d).add("minecraft:dirt", 6.0d).add("minecraft:oak_planks",
                1.0d).done();
        RANDOM_WATER_BLOCK.add("minecraft:cobblestone", 5.0d).add("minecraft:dirt", 6.0d).add("minecraft:prismarine",
                0.3d).add("minecraft:dark_prismarine", 0.3d).add("minecraft:prismarine_bricks", 0.3d).done();
        EFFECT_RANDOM.add(MobEffects.POISON, 5d).add(MobEffects.DAMAGE_BOOST, 5d).add(MobEffects.MOVEMENT_SPEED,
                5d).add(MobEffects.INVISIBILITY, 0.5d).add(MobEffects.HEALTH_BOOST, 2d).add(MobEffects.INVISIBILITY,
                3d).done();
        DROWNED_ITEM.add(Items.AIR, 76.15d).add(Items.FISHING_ROD, 0.85d).add(Items.TRIDENT, 15d).add(
                Items.NAUTILUS_SHELL, 3d).done();
    }

    public static <T> T decideByGameStage(T initialOne, T finalOne) {
        double gameStage = GameProperty.TimeProperty.getGameStage();
        double a = RandomUtils.RANDOM.nextDouble(0d, 1d);
        if (a < gameStage) {
            return finalOne;
        } else {
            return initialOne;
        }
    }

    public static void reloadMainGoalWeight() {
        initCommonZombieWeight = RandomUtils.RandomHelper.create();
        finalCommonZombieWeight = RandomUtils.RandomHelper.create();

        initCommonZombieWeight.add(ZombieUtils::createNormal,
                ConfigDataManager.getOrDefault(ConfigKeySets.NORMAL_INIT)).add(BowAttackZombieGoal::new,
                ConfigDataManager.getOrDefault(ConfigKeySets.BOW_INIT)).add(CrossBowAttackZombieGoal::new,
                ConfigDataManager.getOrDefault(ConfigKeySets.CROSSBOW_INIT)).add(ZombieUtils::createBuilder,
                ConfigDataManager.getOrDefault(ConfigKeySets.BUILDER_INIT)).done();

        finalCommonZombieWeight.add(ZombieUtils::createNormal,
                ConfigDataManager.getOrDefault(ConfigKeySets.NORMAL_FINAL)).add(BowAttackZombieGoal::new,
                ConfigDataManager.getOrDefault(ConfigKeySets.BOW_FINAL)).add(CrossBowAttackZombieGoal::new,
                ConfigDataManager.getOrDefault(ConfigKeySets.CROSSBOW_FINAL)).add(ZombieUtils::createBuilder,
                ConfigDataManager.getOrDefault(ConfigKeySets.BUILDER_FINAL)).done();

        initDrownedWeight = RandomUtils.RandomHelper.create();
        finalDrownedWeight = RandomUtils.RandomHelper.create();

        initDrownedWeight.add(ZombieUtils::createNormal, ConfigDataManager.getOrDefault(ConfigKeySets.NORMAL_INIT)).add(
                ZombieUtils::createBuilder, ConfigDataManager.getOrDefault(ConfigKeySets.BUILDER_INIT)).done();

        finalDrownedWeight.add(ZombieUtils::createNormal,
                ConfigDataManager.getOrDefault(ConfigKeySets.NORMAL_FINAL)).add(ZombieUtils::createBuilder,
                ConfigDataManager.getOrDefault(ConfigKeySets.BUILDER_FINAL)).done();
    }

    private static ZombieMainGoal createBuilder(Zombie zombie) {
        double gameStage = GameProperty.TimeProperty.getGameStage();
        double breakStage = ConfigDataManager.getOrDefault(ConfigKeySets.BREAK_BLOCK_STAGE);
        double placeStage = ConfigDataManager.getOrDefault(ConfigKeySets.PLACE_BLOCK_STAGE);
        if (canBeLoadedAsWaterZombie(zombie)) {
            if (breakStage > placeStage) return new SimpleDrownedGoal(zombie);
            else if (breakStage > gameStage) return new SimpleDrownedGoal(zombie);
            else if (placeStage > gameStage) return new OnlyCanBreakPathBuilderDrownedGoal(zombie);
            else return new PathBuilderDrownedGoal(zombie);
        } else {
            if (breakStage > placeStage) return new SimpleZombieGoal(zombie);
            else if (breakStage > gameStage) return new SimpleZombieGoal(zombie);
            else if (placeStage > gameStage) return new OnlyCanBreakPathBuilderZombieGoal(zombie);
            else return new PathBuilderZombieGoal(zombie);
        }
    }

    private static ZombieMainGoal createNormal(Zombie zombie) {
        double gameStage = GameProperty.TimeProperty.getGameStage();
        double breakStage = ConfigDataManager.getOrDefault(ConfigKeySets.BREAK_BLOCK_STAGE);
        if (canBeLoadedAsWaterZombie(zombie)) {
            if (breakStage > gameStage) return new SimpleDrownedGoal(zombie);
            else return new OnlyCanBreakDrownedGoal(zombie);
        } else {
            if (breakStage > gameStage) return new SimpleZombieGoal(zombie);
            else return new OnlyCanBreakZombieGoal(zombie);
        }
    }

    public static void loadZombieItems(Zombie zombie) {

        boolean flag = zombie.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()
                       && zombie.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()
                       && zombie.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                       && zombie.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                       && zombie.getItemBySlot(EquipmentSlot.LEGS).isEmpty()
                       && zombie.getItemBySlot(EquipmentSlot.FEET).isEmpty();

        if (flag) {
            ServerLevel level = (ServerLevel) zombie.level();
            double k = GameProperty.TimeProperty.getGameStage();
            double a = GameProperty.ZombieProperty.getArmorImproveSpeed();
            double b = GameProperty.ZombieProperty.getInitialArmorLevel();
            double x = RandomUtils.RANDOM.nextDouble(0d, 25d);
            double weaponLevel = ((6 - b) / ((6 - a * k) * x + 1) + b);

            loadArmorItems(zombie, weaponLevel);
            loadItemsOnHand(zombie, weaponLevel);
            loadEnchantment(zombie);
        }

    }

    private static void loadEnchantment(Zombie zombie) {
        enchantItem(zombie.getItemBySlot(EquipmentSlot.HEAD));
        enchantItem(zombie.getItemBySlot(EquipmentSlot.CHEST));
        enchantItem(zombie.getItemBySlot(EquipmentSlot.LEGS));
        enchantItem(zombie.getItemBySlot(EquipmentSlot.FEET));
        enchantItem(zombie.getItemBySlot(EquipmentSlot.MAINHAND));
        enchantItem(zombie.getItemBySlot(EquipmentSlot.OFFHAND));

    }

    private static void loadArmorItems(Zombie zombie, double armorLevel) {

        if (RandomUtils.nextBoolean(0.1d + 0.4d * (1d - GameProperty.TimeProperty.getGameStage()))) {
            return;
        }

        if (canBeLoadedAsWaterZombie(zombie)) {
            if (RandomUtils.nextBoolean(0.1d + 0.4d * (1d - GameProperty.TimeProperty.getGameStage()))) {
                return;
            }
        }

        int[] level = new int[4];

        level[0] = Math.max(0, Math.min(6, (int) Math.ceil(armorLevel + RandomUtils.RANDOM.nextDouble(-0.5d, 0.5d))));
        level[1] = Math.max(0, Math.min(6, (int) Math.ceil(armorLevel + RandomUtils.RANDOM.nextDouble(-0.5d, 0.5d))));
        level[2] = Math.max(0, Math.min(6, (int) Math.ceil(armorLevel + RandomUtils.RANDOM.nextDouble(-0.5d, 0.5d))));
        level[3] = Math.max(0, Math.min(6, (int) Math.ceil(armorLevel + RandomUtils.RANDOM.nextDouble(-0.5d, 0.5d))));

        ItemStack[] itemStacks = new ItemStack[4];

        switch (level[0]) {
            case 6 -> itemStacks[0] = new ItemStack(Items.NETHERITE_HELMET);
            case 5 -> itemStacks[0] = new ItemStack(Items.DIAMOND_HELMET);
            case 4 -> itemStacks[0] = new ItemStack(Items.IRON_HELMET);
            case 3 -> itemStacks[0] = new ItemStack(Items.GOLDEN_HELMET);
            case 2 -> itemStacks[0] = new ItemStack(Items.CHAINMAIL_HELMET);
            case 1 -> itemStacks[0] = new ItemStack(Items.LEATHER_HELMET);
            default -> itemStacks[0] = ItemStack.EMPTY;
        }

        switch (level[1]) {
            case 6 -> itemStacks[1] = new ItemStack(Items.NETHERITE_CHESTPLATE);
            case 5 -> itemStacks[1] = new ItemStack(Items.DIAMOND_CHESTPLATE);
            case 4 -> itemStacks[1] = new ItemStack(Items.IRON_CHESTPLATE);
            case 3 -> itemStacks[1] = new ItemStack(Items.GOLDEN_CHESTPLATE);
            case 2 -> itemStacks[1] = new ItemStack(Items.CHAINMAIL_CHESTPLATE);
            case 1 -> itemStacks[1] = new ItemStack(Items.LEATHER_CHESTPLATE);
            default -> itemStacks[1] = ItemStack.EMPTY;
        }

        switch (level[2]) {
            case 6 -> itemStacks[2] = new ItemStack(Items.NETHERITE_LEGGINGS);
            case 5 -> itemStacks[2] = new ItemStack(Items.DIAMOND_LEGGINGS);
            case 4 -> itemStacks[2] = new ItemStack(Items.IRON_LEGGINGS);
            case 3 -> itemStacks[2] = new ItemStack(Items.GOLDEN_LEGGINGS);
            case 2 -> itemStacks[2] = new ItemStack(Items.CHAINMAIL_LEGGINGS);
            case 1 -> itemStacks[2] = new ItemStack(Items.LEATHER_LEGGINGS);
            default -> itemStacks[2] = ItemStack.EMPTY;
        }

        switch (level[3]) {
            case 6 -> itemStacks[3] = new ItemStack(Items.NETHERITE_BOOTS);
            case 5 -> itemStacks[3] = new ItemStack(Items.DIAMOND_BOOTS);
            case 4 -> itemStacks[3] = new ItemStack(Items.IRON_BOOTS);
            case 3 -> itemStacks[3] = new ItemStack(Items.GOLDEN_BOOTS);
            case 2 -> itemStacks[3] = new ItemStack(Items.CHAINMAIL_BOOTS);
            case 1 -> itemStacks[3] = new ItemStack(Items.LEATHER_BOOTS);
            default -> itemStacks[3] = ItemStack.EMPTY;
        }

        zombie.setItemSlot(EquipmentSlot.HEAD, itemStacks[0]);
        zombie.setItemSlot(EquipmentSlot.CHEST, itemStacks[1]);
        zombie.setItemSlot(EquipmentSlot.LEGS, itemStacks[2]);
        zombie.setItemSlot(EquipmentSlot.FEET, itemStacks[3]);

    }

    private static void loadItemsOnHand(Zombie zombie, double weaponLevel) {

        ZombieMainGoal mainGoal = getOrLoadMainGoal(zombie);
        String mainGoalName = "";
        if (mainGoal != null) {
            mainGoalName = mainGoal.getName();
        }
        double stage = GameProperty.TimeProperty.getGameStage();
        if (canBeLoadedAsLandZombie(zombie)) {

            if (mainGoalName.equals(PathBuilderZombieGoal.NAME)) {
                if (mainGoal != null) {
                    zombie.setItemSlot(EquipmentSlot.OFFHAND,
                            new ItemStack(mainGoal.BLOCK_TO_PLACE, RandomUtils.RANDOM.nextInt(5, 15)));
                }
            } else if (mainGoalName.equals(OnlyCanBreakZombieGoal.NAME)) {
                if (RandomUtils.nextBoolean(ConfigDataManager.getAverageByGameStage(ConfigKeySets.SHIELD_INIT,
                        ConfigKeySets.SHIELD_FINAL))) {
                    zombie.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                }
            }

            if (mainGoalName.equals(BowAttackZombieGoal.NAME)) {
                zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            } else if (mainGoalName.equals(CrossBowAttackZombieGoal.NAME)) {
                zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
            } else if (RandomUtils.nextBoolean(0.5d)) {
                ItemStack mainItemStack;
                int handLevel = Math.max(0,
                        Math.min(6, (int) Math.ceil(weaponLevel + RandomUtils.RANDOM.nextDouble(1d, 2d))));
                int type = RandomUtils.RANDOM.nextInt(0, 15);
                switch (type) {
                    case 0, 1, 9 -> {
                        switch (handLevel) {
                            case 6 -> mainItemStack = new ItemStack(Items.NETHERITE_SWORD);
                            case 5 -> mainItemStack = new ItemStack(Items.DIAMOND_SWORD);
                            case 4 -> mainItemStack = new ItemStack(Items.IRON_SWORD);
                            case 3 -> mainItemStack = new ItemStack(Items.GOLDEN_SWORD);
                            case 2 -> mainItemStack = new ItemStack(Items.STONE_SWORD);
                            case 1 -> mainItemStack = new ItemStack(Items.WOODEN_SWORD);
                            default -> mainItemStack = ItemStack.EMPTY;
                        }
                    }
                    case 3, 8, 10, 11, 12, 13, 14 -> {
                        switch (handLevel) {
                            case 6 -> mainItemStack = new ItemStack(Items.NETHERITE_PICKAXE);
                            case 5 -> mainItemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                            case 4 -> mainItemStack = new ItemStack(Items.IRON_PICKAXE);
                            case 3 -> mainItemStack = new ItemStack(Items.GOLDEN_PICKAXE);
                            case 2 -> mainItemStack = new ItemStack(Items.STONE_PICKAXE);
                            case 1 -> mainItemStack = new ItemStack(Items.WOODEN_PICKAXE);
                            default -> mainItemStack = ItemStack.EMPTY;
                        }
                    }
                    case 4, 5 -> {
                        switch (handLevel) {
                            case 6 -> mainItemStack = new ItemStack(Items.NETHERITE_AXE);
                            case 5 -> mainItemStack = new ItemStack(Items.DIAMOND_AXE);
                            case 4 -> mainItemStack = new ItemStack(Items.IRON_AXE);
                            case 3 -> mainItemStack = new ItemStack(Items.GOLDEN_AXE);
                            case 2 -> mainItemStack = new ItemStack(Items.STONE_AXE);
                            case 1 -> mainItemStack = new ItemStack(Items.WOODEN_AXE);
                            default -> mainItemStack = ItemStack.EMPTY;
                        }
                    }
                    case 6, 7 -> {
                        switch (handLevel) {
                            case 6 -> mainItemStack = new ItemStack(Items.NETHERITE_SHOVEL);
                            case 5 -> mainItemStack = new ItemStack(Items.DIAMOND_SHOVEL);
                            case 4 -> mainItemStack = new ItemStack(Items.IRON_SHOVEL);
                            case 3 -> mainItemStack = new ItemStack(Items.GOLDEN_SHOVEL);
                            case 2 -> mainItemStack = new ItemStack(Items.STONE_SHOVEL);
                            case 1 -> mainItemStack = new ItemStack(Items.WOODEN_SHOVEL);
                            default -> mainItemStack = ItemStack.EMPTY;
                        }
                    }
                    case 2 -> {
                        switch (handLevel) {
                            case 6 -> mainItemStack = new ItemStack(Items.NETHERITE_HOE);
                            case 5 -> mainItemStack = new ItemStack(Items.DIAMOND_HOE);
                            case 4 -> mainItemStack = new ItemStack(Items.IRON_HOE);
                            case 3 -> mainItemStack = new ItemStack(Items.GOLDEN_HOE);
                            case 2 -> mainItemStack = new ItemStack(Items.STONE_HOE);
                            case 1 -> mainItemStack = new ItemStack(Items.WOODEN_HOE);
                            default -> mainItemStack = ItemStack.EMPTY;
                        }
                    }
                    default -> mainItemStack = ItemStack.EMPTY;
                }
                zombie.setItemSlot(EquipmentSlot.MAINHAND, mainItemStack);
            }
        } else if (canBeLoadedAsWaterZombie(zombie)) {
            if (mainGoalName.equals(PathBuilderDrownedGoal.NAME)) {
                if (mainGoal != null) {
                    zombie.setItemSlot(EquipmentSlot.OFFHAND,
                            new ItemStack(mainGoal.BLOCK_TO_PLACE, RandomUtils.RANDOM.nextInt(5, 10)));
                }
            }
            ItemStack itemStack = new ItemStack(DROWNED_ITEM.nextValue());
            if (itemStack.isEmpty()) {
                if (RandomUtils.nextBoolean(stage * 0.2)) {
                    itemStack = new ItemStack(Items.TRIDENT);
                }
            }
            zombie.setItemSlot(EquipmentSlot.MAINHAND, itemStack);
        }
    }


    private static void enchantItem(ItemStack stack) {

        if (RandomUtils.nextBoolean(1d - GameProperty.ZombieProperty.getEnchantChance())) {
            return;
        }

        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

        if (Enchantments.SHARPNESS.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.5d)) {
                int protectionType = RandomUtils.RANDOM.nextInt(0, 6);
                int i = createEnchantmentLevel(Enchantments.SHARPNESS.getMaxLevel());
                if (i >= 1) {
                    switch (protectionType) {
                        case 0, 3, 5 -> map.putIfAbsent(Enchantments.SHARPNESS, i);
                        case 1, 4 -> map.putIfAbsent(Enchantments.SMITE, i);
                        case 2 -> map.putIfAbsent(Enchantments.BANE_OF_ARTHROPODS, i);
                    }
                }
            }
        }

        if (Enchantments.FIRE_ASPECT.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                map.putIfAbsent(Enchantments.FIRE_ASPECT, RandomUtils.RANDOM.nextInt(1, 3));
            }
        }

        if (Enchantments.PUNCH_ARROWS.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.2d)) {
                map.putIfAbsent(Enchantments.PUNCH_ARROWS, RandomUtils.RANDOM.nextInt(1, 3));
            }
        }


        if (Enchantments.ALL_DAMAGE_PROTECTION.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.4d)) {
                int protectionType = RandomUtils.RANDOM.nextInt(0, 4);
                int i = createEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION.getMaxLevel());
                if (i >= 1) {
                    switch (protectionType) {
                        case 0 -> map.putIfAbsent(Enchantments.ALL_DAMAGE_PROTECTION, i);
                        case 1 -> map.putIfAbsent(Enchantments.FIRE_PROTECTION, i);
                        case 2 -> map.putIfAbsent(Enchantments.BLAST_PROTECTION, i);
                        case 3 -> map.putIfAbsent(Enchantments.PROJECTILE_PROTECTION, i);
                    }
                }
            }
        }

        if (Enchantments.AQUA_AFFINITY.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                map.putIfAbsent(Enchantments.AQUA_AFFINITY, 1);
            }
        }


        if (Enchantments.RESPIRATION.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                int i = createEnchantmentLevel(Enchantments.RESPIRATION.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.RESPIRATION, i);
                }
            }
        }

        if (Enchantments.PIERCING.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.5d)) {
                int i = createEnchantmentLevel(Enchantments.PIERCING.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.RESPIRATION, i);
                }
            }
        } else if (Enchantments.MULTISHOT.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                map.putIfAbsent(Enchantments.MULTISHOT, 1);
            }
        }

        if (Enchantments.THORNS.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                int i = createEnchantmentLevel(Enchantments.THORNS.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.THORNS, i);
                }
            }
        }

        if (Enchantments.MOB_LOOTING.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                int i = createEnchantmentLevel(Enchantments.MOB_LOOTING.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.MOB_LOOTING, i);
                }
            }
        }

        if (Enchantments.POWER_ARROWS.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.5d)) {
                int i = createEnchantmentLevel(Enchantments.POWER_ARROWS.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.POWER_ARROWS, i);
                }
            }
        }

        if (Enchantments.BLOCK_EFFICIENCY.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.3d)) {
                int i = createEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.BLOCK_EFFICIENCY, i);
                }
            }
        }

        if (Enchantments.BLOCK_FORTUNE.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.2d)) {
                int i = createEnchantmentLevel(Enchantments.BLOCK_FORTUNE.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.BLOCK_FORTUNE, i);
                }
            }
        } else if (Enchantments.SILK_TOUCH.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                map.putIfAbsent(Enchantments.SILK_TOUCH, 1);
            }
        }


        if (Enchantments.UNBREAKING.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.4d)) {
                int i = createEnchantmentLevel(Enchantments.UNBREAKING.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.UNBREAKING, i);
                }
            }
        }

        if (Enchantments.QUICK_CHARGE.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.4d)) {
                int i = createEnchantmentLevel(Enchantments.QUICK_CHARGE.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.QUICK_CHARGE, i);
                }
            }
        }

        if (Enchantments.FALL_PROTECTION.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.4d)) {
                int i = createEnchantmentLevel(Enchantments.FALL_PROTECTION.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.FALL_PROTECTION, i);
                }
            }
        }

        if (Enchantments.SOUL_SPEED.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                int i = createEnchantmentLevel(Enchantments.SOUL_SPEED.getMaxLevel());
                if (i >= 1) {
                    map.putIfAbsent(Enchantments.SOUL_SPEED, i);
                }
            }
        }

        if (Enchantments.FROST_WALKER.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.05d)) {
                map.putIfAbsent(Enchantments.FROST_WALKER, RandomUtils.RANDOM.nextInt(1, 3));
            }
        } else if (Enchantments.DEPTH_STRIDER.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {

                int i = createEnchantmentLevel(Enchantments.DEPTH_STRIDER.getMaxLevel());

                if (i >= 1) {
                    map.putIfAbsent(Enchantments.DEPTH_STRIDER, i);
                }

            }
        }

        if (Enchantments.MENDING.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.05d)) {
                map.putIfAbsent(Enchantments.MENDING, 1);
            }
        } else if (Enchantments.INFINITY_ARROWS.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.2d)) {
                map.putIfAbsent(Enchantments.INFINITY_ARROWS, 1);
            }
        }

        if (Enchantments.FLAMING_ARROWS.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.1d)) {
                map.putIfAbsent(Enchantments.FLAMING_ARROWS, 1);
            }
        }

        if (Enchantments.BINDING_CURSE.canEnchant(stack)) {
            if (RandomUtils.nextBoolean(0.05d)) {
                map.putIfAbsent(Enchantments.BINDING_CURSE, 1);
            }
        }


        EnchantmentHelper.setEnchantments(map, stack);
    }

    public static void loadZombieGoal(Zombie zombie) {

        if (getMainGoal(zombie) != null) {
            return;
        }

        ZombieMainGoal mainGoal = getOrLoadMainGoal(zombie);

        if (mainGoal == null) {
            if (!zombie.getMainHandItem().isEmpty() || !zombie.getOffhandItem().isEmpty()) {
                if (GameProperty.TimeProperty.getGameStage() <= 0.2d) {
                    mainGoal = new SimpleZombieGoal(zombie);
                } else if (zombie.getMainHandItem().getItem() instanceof BowItem) {
                    mainGoal = new BowAttackZombieGoal(zombie);
                } else if (zombie.getMainHandItem().getItem() instanceof CrossbowItem) {
                    mainGoal = new CrossBowAttackZombieGoal(zombie);
                } else {
                    mainGoal = new OnlyCanBreakZombieGoal(zombie);
                }
            } else {
                mainGoal = createMainGoal(zombie);
            }
        }

        String blockToPlace;
        if (canBeLoadedAsWaterZombie(zombie)) {
            blockToPlace = EntityDataManager.getOrCreate(zombie, EntityKeySets.BLOCK_TO_PLACE.KEY,
                    RANDOM_WATER_BLOCK.nextValue());
        } else {
            blockToPlace = EntityDataManager.getOrCreate(zombie, EntityKeySets.BLOCK_TO_PLACE.KEY,
                    RANDOM_LAND_BLOCK.nextValue());
        }

        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockToPlace));
        block = block == null ? Blocks.DIRT : block;
        block = block.defaultBlockState().isAir() ? Blocks.DIRT : block;
        mainGoal.BLOCK_TO_PLACE = block;

        zombie.goalSelector.addGoal(1, mainGoal);

        if (!mainGoal.haveAddedGoal) {
            mainGoal.addGoal();
            mainGoal.haveAddedGoal = true;
        }

        AttributeInstance a = zombie.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
        if (a != null) {
            a.setBaseValue(mainGoal.getZombieAttackDamage());
        }

        AttributeInstance b = zombie.getAttributes().getInstance(Attributes.ARMOR);
        if (b != null) {
            b.setBaseValue(mainGoal.getZombieArmor());
        }

        AttributeInstance c = zombie.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (c != null) {
            c.setBaseValue(0.23d * mainGoal.getZombieSpeed());
        }

        AttributeInstance d = zombie.getAttributes().getInstance(ForgeMod.SWIM_SPEED.get());
        if (d != null) {
            d.setBaseValue((zombie instanceof Drowned ? 2.0d : 1.2d) * mainGoal.getZombieSpeed());
        }


    }

    private static ZombieMainGoal createMainGoal(Zombie zombie) {
        ZombieMainGoal mainGoal;
        if (GameProperty.TimeProperty.getGameStage() >= ConfigDataManager.getOrDefault(
                ConfigKeySets.ZOMBIE_START_EVOLUTION_STAGE)) {
            if (canBeLoadedAsWaterZombie(zombie)) {
                mainGoal = decideByGameStage(initDrownedWeight, finalDrownedWeight).nextValue().apply(zombie);
            } else {
                mainGoal = decideByGameStage(initCommonZombieWeight, finalCommonZombieWeight).nextValue().apply(zombie);
            }
        } else {
            if (canBeLoadedAsWaterZombie(zombie)) {
                mainGoal = new SimpleDrownedGoal(zombie);
            } else {
                mainGoal = new SimpleZombieGoal(zombie);
            }
        }
        EntityDataManager.set(zombie, EntityKeySets.MAIN_GOAL_NAME, mainGoal.getName());
        return mainGoal;
    }

    public static boolean canBeLoadedAsLandZombie(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        if (!(entity instanceof Zombie)) {
            return false;
        }
        return entity.getType() == EntityType.ZOMBIE
               || entity.getType() == EntityType.ZOMBIE_VILLAGER
               || entity.getType() == EntityType.HUSK;
    }

    public static boolean canBeLoadedAsWaterZombie(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        if (!(entity instanceof Zombie)) {
            return false;
        }
        return entity.getType() == EntityType.DROWNED;
    }

    public static boolean canBeLoaded(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        if (!(entity instanceof Zombie)) {
            return false;
        }
        return canBeLoadedAsWaterZombie(entity) || canBeLoadedAsLandZombie(entity);
    }

    @Nullable
    public static ZombieMainGoal getOrLoadMainGoal(Zombie zombie) {
        ZombieMainGoal mainGoal = getMainGoal(zombie);
        if (mainGoal == null) {
            String string = EntityDataManager.getOrDefault(zombie, EntityKeySets.MAIN_GOAL_NAME);
            if (string.equals(PathBuilderZombieGoal.NAME)) mainGoal = new PathBuilderZombieGoal(zombie);
            else if (string.equals(PathBuilderDrownedGoal.NAME)) mainGoal = new PathBuilderDrownedGoal(zombie);
            else if (string.equals(SimpleZombieGoal.NAME)) mainGoal = new SimpleZombieGoal(zombie);
            else if (string.equals(SimpleDrownedGoal.NAME)) mainGoal = new SimpleDrownedGoal(zombie);
            else if (string.equals(OnlyCanBreakZombieGoal.NAME)) mainGoal = new OnlyCanBreakZombieGoal(zombie);
            else if (string.equals(OnlyCanBreakDrownedGoal.NAME)) mainGoal = new OnlyCanBreakDrownedGoal(zombie);
            else if (string.equals(OnlyCanBreakPathBuilderZombieGoal.NAME))
                mainGoal = new OnlyCanBreakPathBuilderZombieGoal(zombie);
            else if (string.equals(OnlyCanBreakPathBuilderDrownedGoal.NAME))
                mainGoal = new OnlyCanBreakPathBuilderDrownedGoal(zombie);
            else if (string.equals(BowAttackZombieGoal.NAME)) mainGoal = new BowAttackZombieGoal(zombie);
            else if (string.equals(CrossBowAttackZombieGoal.NAME)) mainGoal = new CrossBowAttackZombieGoal(zombie);
            else if (string.equals(TestGoal.NAME)) mainGoal = new TestGoal(zombie);
        }
        return mainGoal;
    }

    @Nullable
    private static ZombieMainGoal getMainGoal(Zombie zombie) {
        ZombieMainGoal[] mainGoal = new ZombieMainGoal[1];
        zombie.goalSelector.getAvailableGoals().forEach((action) -> {
            if (action.getGoal() instanceof ZombieMainGoal) {
                mainGoal[0] = (ZombieMainGoal) action.getGoal();
            }
        });
        return mainGoal[0];
    }

    public static Zombie randomZombie(Level level, boolean isDesert) {
        int i = RandomUtils.RANDOM.nextInt(0, 21);
        Zombie zombie = createType(level, isDesert);
        if (i == 1) {
            zombie.setBaby(true);
        }
        return zombie;
    }

    private static Zombie createType(Level level, boolean isDesert) {
        if (RandomUtils.nextBoolean(ConfigDataManager.getOrDefault(ConfigKeySets.ZOMBIE_VILLAGER_SPAWNING_CHANCE))) {
            return EntityType.ZOMBIE_VILLAGER.create(level);
        } else {
            return isDesert ? EntityType.HUSK.create(level) : EntityType.ZOMBIE.create(level);
        }
    }

    private static int createEnchantmentLevel(int boundLevel) {
        return (int) Math.round(boundLevel * RandomUtils.nextDouble(10d, 1d));
    }

    public static boolean canBeTarget(@Nullable LivingEntity livingEntity) {
        if (livingEntity == null) {
            return false;
        }

        boolean a = false;

        if (livingEntity instanceof AbstractVillager) {
            a = true;
        } else if (livingEntity instanceof IronGolem) {
            a = true;
        } else if (livingEntity instanceof Player) {
            a = true;
        }

        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType());

        if (key != null) {
            String id = key.toString();
            a = (a || checkTargetWhiteList(id)) && !checkTargetBlackList(id);
        }

        a = a && canBeAttack(livingEntity);

        return a;
    }

    private static boolean checkTargetWhiteList(@Nullable String id) {
        if (id != null) {
            return ZombieGameConfig.targetChoosingWhiteList.get().contains(id);
        } else {
            return false;
        }
    }

    private static boolean checkTargetBlackList(String id) {
        if (id != null) {
            return ZombieGameConfig.targetChoosingBlackList.get().contains(id);
        } else {
            return false;
        }
    }

    public static boolean canBeAttack(LivingEntity livingentity) {
        if (livingentity == null) return false;
        if (!livingentity.isAlive()) {
            return false;
        }
        if (ZombieUtils.canBeLoaded(livingentity)) {
            return false;
        }
        if (livingentity instanceof Player player) {
            return !player.isSpectator() && !player.isCreative();
        }
        return true;

    }

    public static void addRandomEffect(Zombie zombie) {
        zombie.addEffect(createRandomEffect());
    }

    private static MobEffectInstance createRandomEffect() {
        return new MobEffectInstance(EFFECT_RANDOM.nextValue(), 48000, 1);
    }

    @Nullable
    public static Vec3 getWaterPos(ZombieMainGoal mainGoal) {
        BlockPos blockpos = mainGoal.getZombie().blockPosition();
        for (int i = 0; i < 10; ++i) {

            BlockPos pos = blockpos.offset(RandomUtils.RANDOM.nextInt(20) - 10, 2 - RandomUtils.RANDOM.nextInt(8),
                    RandomUtils.RANDOM.nextInt(20) - 10);
            if (mainGoal.getZombie().level().getBlockState(pos).is(Blocks.WATER)) {
                return Vec3.atBottomCenterOf(pos);
            }
        }
        return null;
    }

    @Nullable
    public static Vec3 getSunlessPlace(ZombieMainGoal mainGoal) {

        BlockPos blockpos = mainGoal.getZombie().blockPosition();
        for (int i = 0; i < 10; ++i) {
            BlockPos pos = blockpos.offset(RandomUtils.RANDOM.nextInt(20) - 10, RandomUtils.RANDOM.nextInt(6) - 3,
                    RandomUtils.RANDOM.nextInt(20) - 10);
            if (!mainGoal.getZombie().level().canSeeSky(pos)
                && mainGoal.getZombie().getWalkTargetValue(pos) < 0.0F) {
                return Vec3.atBottomCenterOf(pos);
            }
        }
        return null;
    }

    public static Vec3 getDarkPos(ZombieMainGoal mainGoal) {
        if (mainGoal.getZombie().isOnFire()) {

            Vec3 vec3 = getWaterPos(mainGoal);
            if (vec3 != null) {
                return vec3;
            }

        }

        Vec3 vec3 = getSunlessPlace(mainGoal);
        if (vec3 != null) {
            return vec3;
        }

        if (!mainGoal.getZombie().isOnFire()) {
            vec3 = getWaterPos(mainGoal);
        }

        return vec3;
    }

    public static boolean isDangerous(LivingEntity livingEntity) {
        if (livingEntity instanceof Enemy) return true;
        else if (livingEntity instanceof Player) return true;
        else if (livingEntity instanceof Mob mob) return mob.getTarget() != null;
        return false;
    }

    public static boolean isNotSunBurnTick(Zombie zombie) {
        if (zombie.level().isDay() && !zombie.level().isClientSide) {
            float f = zombie.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(zombie.getX(), zombie.getEyeY(), zombie.getZ());
            boolean flag = zombie.isInWaterRainOrBubble() || zombie.isInPowderSnow || zombie.wasInPowderSnow;
            return !(f > 0.5F) || flag || !zombie.level().canSeeSky(blockpos);
        }
        return true;
    }

    public static boolean isNotSunBurnTick(Zombie zombie1, @Nonnull Vec3 vec3) {
        if (zombie1.level().isDay() && !zombie1.level().isClientSide) {
            float f = zombie1.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(vec3.x(), vec3.y(), vec3.z());
            boolean flag = zombie1.isInWaterRainOrBubble() || zombie1.isInPowderSnow || zombie1.wasInPowderSnow;
            return !(f > 0.5F) || flag || !zombie1.level().canSeeSky(blockpos);
        }
        return true;
    }

}
