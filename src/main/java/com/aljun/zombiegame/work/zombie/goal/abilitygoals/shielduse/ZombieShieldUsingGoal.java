package com.aljun.zombiegame.work.zombie.goal.abilitygoals.shielduse;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;

public class ZombieShieldUsingGoal extends AbstractZombieAbilityGoal {
    private int coolTime = 0;
    private boolean isUsingShield = false;
    private boolean isBlocked = false;
    private long lastBlockTime = 0L;

    public ZombieShieldUsingGoal(ZombieMainGoal mainGoal, ZombieEntity zombie) {
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
        if (this.isBlocked && (this.zombie.level.getGameTime() - this.lastBlockTime) > 1) {
            this.isBlocked = false;
        }
    }

    private void startUsingShield() {
        if (this.zombie.getMainHandItem().getItem() instanceof ShieldItem) {
            this.zombie.startUsingItem(Hand.MAIN_HAND);
        } else if (this.zombie.getOffhandItem().getItem() instanceof ShieldItem) {
            this.zombie.startUsingItem(Hand.OFF_HAND);
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
            this.lastBlockTime = this.zombie.level.getGameTime();
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

    public boolean blockSound() {
        boolean b = this.isBlocked;
        this.isBlocked = false;
        return b;
    }
}
