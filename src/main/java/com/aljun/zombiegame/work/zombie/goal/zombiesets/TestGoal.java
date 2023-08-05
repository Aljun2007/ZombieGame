package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.world.entity.monster.Zombie;

public class TestGoal extends ZombieMainGoal {
    public static final String NAME = TestGoal.class.getSimpleName();

    public TestGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {

    }
}
