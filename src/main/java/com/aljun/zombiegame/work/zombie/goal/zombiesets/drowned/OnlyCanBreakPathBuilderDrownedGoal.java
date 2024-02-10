package com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned;

import net.minecraft.world.entity.monster.Zombie;

public class OnlyCanBreakPathBuilderDrownedGoal extends PathBuilderDrownedGoal {
    public static final String NAME = OnlyCanBreakPathBuilderDrownedGoal.class.getSimpleName();

    public OnlyCanBreakPathBuilderDrownedGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public boolean canPlace() {
        return false;
    }
}
