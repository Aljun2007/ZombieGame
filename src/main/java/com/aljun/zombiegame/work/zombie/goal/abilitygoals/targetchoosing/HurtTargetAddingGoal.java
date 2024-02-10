package com.aljun.zombiegame.work.zombie.goal.abilitygoals.targetchoosing;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;

public class HurtTargetAddingGoal extends AbstractZombieAbilityGoal {

    public HurtTargetAddingGoal(ZombieMainGoal mainGoal, Zombie zombie) {
        super(mainGoal, zombie);
    }

    public void onZombieHurt() {
        LivingEntity livingentity = this.zombie.getLastHurtByMob();
        if (ZombieUtils.canBeAttack(livingentity)) {
            this.mainGoal.callToAttack(livingentity);
        }
    }

    @Override
    public boolean canBeUsed() {
        return false;
    }

}
