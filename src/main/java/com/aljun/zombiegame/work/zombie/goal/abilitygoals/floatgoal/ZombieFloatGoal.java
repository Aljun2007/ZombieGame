package com.aljun.zombiegame.work.zombie.goal.abilitygoals.floatgoal;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.tags.FluidTags;

public class ZombieFloatGoal extends AbstractZombieAbilityGoal {
    public ZombieFloatGoal(ZombieMainGoal mainGoal, ZombieEntity zombie) {
        super(mainGoal, zombie);
    }

    public boolean canBeUsed() {
        return this.zombie.getNavigation().canFloat() && (this.zombie.getTarget() == null
                                                          || this.zombie.getTarget().getY() >= this.zombie.getY()) && (
                       this.zombie.isInWater()
                       && this.zombie.getFluidHeight(FluidTags.WATER) > this.zombie.getFluidJumpThreshold()
                       || this.zombie.isInLava());
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.zombie.getRandom().nextFloat() < 0.8F) {
            this.zombie.getJumpControl().jump();
        }
    }
}
