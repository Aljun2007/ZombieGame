package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.entity.monster.ZombieEntity;

public class OnlyCanBreakPathBuilderZombieGoal extends PathBuilderZombieGoal {
    public static final String NAME = OnlyCanBreakPathBuilderZombieGoal.class.getSimpleName();

    public OnlyCanBreakPathBuilderZombieGoal(ZombieEntity zombie) {
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
