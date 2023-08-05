package com.aljun.zombiegame.work.zombie.goal.abilitygoals;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieBowAttackGoal;

public class AbilityPack<T extends AbstractZombieAbilityGoal> {
    public final T goal;
    public boolean isUsable = false;

    private AbilityPack(T goal) {
        this.goal = goal;
    }

    public static <G extends AbstractZombieAbilityGoal> AbilityPack<G> create(G goal) {
        return goal.connect(new AbilityPack<>(goal));
    }
}
