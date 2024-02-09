package com.aljun.zombiegame.work.zombie.goal.abilitygoals;

import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.ZombieEntity;

public abstract class AbstractZombieAbilityGoal extends Goal {
    protected final ZombieMainGoal mainGoal;
    protected final ZombieEntity zombie;
    private AbilityPack<?> abilityPack;

    protected AbstractZombieAbilityGoal(ZombieMainGoal mainGoal, ZombieEntity zombie) {
        this.mainGoal = mainGoal;
        this.zombie = zombie;
    }

    @Override
    public boolean canUse() {
        return this.canBeUsed() && this.abilityPack.isUsable;
    }

    public abstract boolean canBeUsed();

    public ZombieEntity getZombie() {
        return zombie;
    }

    public ZombieMainGoal getMainGoal() {
        return mainGoal;
    }

    public <G extends AbstractZombieAbilityGoal> AbilityPack<G> connect(AbilityPack<G> pack) {
        this.abilityPack = pack;
        return pack;
    }
}
