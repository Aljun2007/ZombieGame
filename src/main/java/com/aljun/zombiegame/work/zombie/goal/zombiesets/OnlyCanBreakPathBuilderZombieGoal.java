package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.world.entity.monster.Zombie;

public class OnlyCanBreakPathBuilderZombieGoal extends PathBuilderZombieGoal {
    public static final String NAME = OnlyCanBreakPathBuilderZombieGoal.class.getSimpleName();

    public OnlyCanBreakPathBuilderZombieGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean canPlace() {
        return false;
    }
}
