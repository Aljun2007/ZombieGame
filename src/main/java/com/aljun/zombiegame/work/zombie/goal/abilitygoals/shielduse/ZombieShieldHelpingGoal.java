package com.aljun.zombiegame.work.zombie.goal.abilitygoals.shielduse;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

public class ZombieShieldHelpingGoal extends AbstractZombieAbilityGoal {
    private int useTime = 0;

    public ZombieShieldHelpingGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
    }

    private boolean checkEnemyItemStackIfThreatening(LivingEntity enemy, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BowItem) {
            if (enemy.isUsingItem()) {
                if (enemy.getUseItem().equals(itemStack)) {
                    this.useTime = 20;
                    return true;
                }
            }
        } else if (itemStack.getItem() instanceof TridentItem) {
            if (enemy.isUsingItem()) {
                if (enemy.getUseItem().equals(itemStack)) {
                    this.useTime = 20;
                    return true;
                }
            }
        } else if (itemStack.getItem() instanceof CrossbowItem) {
            if (CrossbowItem.isCharged(itemStack)) {
                this.useTime = 20;
                return true;
            } else if (enemy.isUsingItem()) {
                if (enemy.getUseItem().equals(itemStack)) {
                    this.useTime = 20;
                    return true;
                }
            }
        } else if (this.zombie.distanceTo(enemy) <= 5d) {
            if (itemStack.getDamageValue() >= 3) {
                this.useTime = 20;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeUsed() {
        return this.mainGoal.zombieShieldUsingGoal.goal.canBeUsed();
    }

    @Override
    public void tick() {
        this.useTime--;
        if (this.zombie.getTarget() != null) {
            if (this.needToUseShield()) {
                if (!this.mainGoal.zombieShieldUsingGoal.goal.isUsingShield()) {
                    if (ZombieUtils.isDangerous(this.zombie.getTarget())) {
                        this.startUse();
                    }
                }
            } else {
                stopUse();
            }
        } else {
            stopUse();
        }
    }

    private void stopUse() {
        if (this.useTime <= 0) {
            this.mainGoal.zombieShieldUsingGoal.goal.stopUsingShied();
        }
    }

    private void startUse() {
        this.mainGoal.zombieShieldUsingGoal.goal.checkAndStartUsingShield();
    }

    private boolean needToUseShield() {
        if (this.zombie.getTarget() == null) return false;
        LivingEntity target = this.zombie.getTarget();
        if (target.hasLineOfSight(this.zombie)) {
            ItemStack mainHandItem = target.getMainHandItem();
            ItemStack offhandItem = target.getOffhandItem();
            return this.checkEnemyItemStackIfThreatening(target, mainHandItem) || this.checkEnemyItemStackIfThreatening(
                    target, offhandItem);
        }
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
