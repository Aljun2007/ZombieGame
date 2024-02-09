package com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned;

import net.minecraft.entity.monster.ZombieEntity;

public class OnlyCanBreakPathBuilderDrownedGoal extends PathBuilderDrownedGoal {
    public static final String NAME = OnlyCanBreakPathBuilderDrownedGoal.class.getSimpleName();

    public OnlyCanBreakPathBuilderDrownedGoal(ZombieEntity zombie) {
        super(zombie);
    }

    @Override
    public boolean canPlace() {
        return false;
    }
}
