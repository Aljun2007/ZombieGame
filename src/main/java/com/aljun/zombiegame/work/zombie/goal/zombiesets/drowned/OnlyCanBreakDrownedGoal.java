package com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned;

import net.minecraft.entity.monster.ZombieEntity;

public class OnlyCanBreakDrownedGoal extends SimpleDrownedGoal {

    public static final String NAME = OnlyCanBreakDrownedGoal.class.getSimpleName();

    public OnlyCanBreakDrownedGoal(ZombieEntity zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {
        super.addGoal();
        this.startUseTargetChoosingGoal(this.zombiePathBuilderGoal, 2);
    }

    @Override
    public boolean canBreak() {
        return true;
    }
}
