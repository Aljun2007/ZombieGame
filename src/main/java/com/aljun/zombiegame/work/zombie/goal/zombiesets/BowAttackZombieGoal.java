package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.world.entity.monster.Zombie;

public class BowAttackZombieGoal extends ZombieMainGoal {
    public static final String NAME = BowAttackZombieGoal.class.getSimpleName();

    public BowAttackZombieGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {
        this.startUseGoal(this.zombieFleeSunGoal, 3);
        this.startUseGoal(this.zombieFloatGoal, 1);
        this.startUseGoal(this.zombieRandomLookGoal, 4);
        this.startUseGoal(this.zombieRandomWalkGoal, 1);
        this.startUseGoal(this.zombieBowAttackGoal, 1);
        this.startUseGoal(this.zombieMeleeAttackGoal, 2);
        this.startUseTargetChoosingGoal(this.zombieTargetChoosingGoal, 1);
        this.startUseTargetChoosingGoal(this.hurtTargetAddingGoal, 1);
        this.startUseGoal(this.zombieLookAtPlayerGoal, 3);
    }


}