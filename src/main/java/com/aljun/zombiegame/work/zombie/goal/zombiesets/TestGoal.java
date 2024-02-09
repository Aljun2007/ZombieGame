package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.entity.monster.ZombieEntity;

public class TestGoal extends ZombieMainGoal {
    public static final String NAME = TestGoal.class.getSimpleName();

    public TestGoal(ZombieEntity zombie) {
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
