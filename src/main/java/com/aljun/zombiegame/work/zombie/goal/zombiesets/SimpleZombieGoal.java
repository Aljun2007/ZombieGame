package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbilityPack;
import net.minecraft.world.entity.monster.Zombie;

public class SimpleZombieGoal extends ZombieMainGoal {
    public static final String NAME = SimpleZombieGoal.class.getSimpleName();

    public SimpleZombieGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {
        this.startUseGoal(this.zombieShieldHelpingGoal, 3);
        this.startUseGoal(this.zombieBreakGoal, 3);
        this.startUseGoal(this.zombieShieldUsingGoal, 3);
        this.startUseGoal(this.zombieMeleeAttackGoal, 4);
        this.startUseGoal(this.zombieRandomLookGoal, 4);
        this.startUseGoal(this.zombieRandomWalkGoal, 2);
        this.startUseTargetChoosingGoal(this.zombieTargetChoosingGoal, 2);
        this.startUseTargetChoosingGoal(this.hurtTargetAddingGoal, 2);
        this.startUseGoal(this.zombieLookAtPlayerGoal, 3);
        this.startUseGoal(this.zombieFleeSunGoal, 3);
        this.startUseGoal(this.zombieFloatGoal, 1);
    }
}
