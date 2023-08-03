package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.world.entity.monster.Zombie;

public class OnlyCanBreakZombieGoal extends SimpleZombieGoal {
    public static final String NAME = OnlyCanBreakZombieGoal.class.getSimpleName();

    public OnlyCanBreakZombieGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {
        super.addGoal();
        this.startUseGoal(this.zombieBlockChooserGoal, 2);
        this.startUseGoal(this.zombieBreakGoal, 3);

    }

    @Override
    public boolean canBreak() {
        return true;
    }

    @Override
    public boolean isBuilding() {
        return !this.zombiePathBuilderGoal.goal.isDone() || !this.zombieBreakGoal.goal.isDone();
    }
}
