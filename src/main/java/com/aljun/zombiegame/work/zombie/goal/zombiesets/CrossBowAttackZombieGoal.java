package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.entity.monster.ZombieEntity;

public class CrossBowAttackZombieGoal extends ZombieMainGoal {

    public static final String NAME = CrossBowAttackZombieGoal.class.getSimpleName();

    public CrossBowAttackZombieGoal(ZombieEntity zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {
        this.startUseGoal(this.zombieFleeSunGoal, 3);
        this.startUseGoal(this.zombieRandomLookGoal, 4);
        this.startUseGoal(this.zombieFloatGoal, 1);
        this.startUseGoal(this.zombieRandomWalkGoal, 1);
        this.startUseGoal(this.zombieCrossBowAttackGoal, 1);
        this.startUseGoal(this.zombieMeleeAttackGoal, 2);
        this.startUseTargetChoosingGoal(this.zombieTargetChoosingGoal, 1);
        this.startUseTargetChoosingGoal(this.hurtTargetAddingGoal, 1);
        this.startUseGoal(this.zombieLookAtPlayerGoal, 3);
    }


}