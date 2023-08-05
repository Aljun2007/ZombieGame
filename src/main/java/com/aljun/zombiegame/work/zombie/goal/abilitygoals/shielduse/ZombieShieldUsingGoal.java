package com.aljun.zombiegame.work.zombie.goal.abilitygoals.shielduse;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ZombieShieldUsingGoal extends AbstractZombieAbilityGoal {
    private int coolTime = 0;
    private boolean isUsingShield = false;
    private boolean isBlocked = false;
    private long lastBlockTime = 0L;

    public ZombieShieldUsingGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
    }

    @Override
    public boolean canBeUsed() {
        return this.canUseShield();
    }

    private boolean canUseShield() {
        return this.mainGoal.canUseShield() && (this.zombie.getMainHandItem().getItem() instanceof ShieldItem
                                                || this.zombie.getOffhandItem().getItem() instanceof ShieldItem);
    }

    @Override
    public void tick() {
        coolTime--;
        if (this.isUsingShield()) {
            this.mainGoal.setSpeedBase(0.6d / this.mainGoal.getZombieSpeed(), 2);
        }
        if (this.isBlocked && (this.zombie.getLevel().getGameTime() - this.lastBlockTime) > 1) {
            this.isBlocked = false;
        }
    }

    private void startUsingShield() {
        if (this.zombie.getMainHandItem().getItem() instanceof ShieldItem) {
            this.zombie.startUsingItem(InteractionHand.MAIN_HAND);
        } else if (this.zombie.getOffhandItem().getItem() instanceof ShieldItem) {
            this.zombie.startUsingItem(InteractionHand.OFF_HAND);
        }
        this.isUsingShield = true;
    }

    public void checkAndStartUsingShield() {
        if (this.canUseShield()) {
            if (!this.isUsingShield()) {
                if (!this.zombie.isUsingItem() && this.coolTime <= 0) {
                    this.startUsingShield();
                }
            }
        }
    }

    public void stopUsingShied() {
        if (this.canUseShield()) {
            if (this.isUsingShield()) {
                this.zombie.stopUsingItem();
                this.coolTime = 5;
                this.isUsingShield = false;
            }
        }
    }

    public void onShieldBlock(ItemStack weapon, LivingEntity attacker) {
        if (this.isUsingShield()) {
            zombie.handleEntityEvent((byte) 29);
            this.isBlocked = true;
            this.lastBlockTime = this.zombie.getLevel().getGameTime();
            if (weapon == null) return;
            if (!weapon.isEmpty() && weapon.getItem().canDisableShield(weapon, this.zombie.getUseItem(), this.zombie,
                    attacker)) {
                this.stopUsingShied();
                this.zombie.handleEntityEvent((byte) 30);
                this.coolTime = 100;
            }
        }
    }

    public boolean isUsingShield() {
        if (!(this.zombie.getUseItem().getItem() instanceof ShieldItem)) {
            this.isUsingShield = false;
        }
        return this.isUsingShield;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean blockSound() {
        boolean b = this.isBlocked;
        this.isBlocked = false;
        return b;
    }
}
