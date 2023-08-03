package com.aljun.zombiegame.work.zombie.goal.zombiesets.drowned;

import net.minecraft.world.entity.monster.Zombie;

public class PathBuilderDrownedGoal extends SimpleDrownedGoal {

    public static final String NAME = PathBuilderDrownedGoal.class.getSimpleName();

    public PathBuilderDrownedGoal(Zombie zombie) {
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
        this.startUseTargetChoosingGoal(this.zombieBlockChooserGoal, 3);
        this.startUseTargetChoosingGoal(this.fluidPlaceBlockGoal, 2);
    }

    @Override
    public boolean canBreak() {
        return true;
    }

    @Override
    public boolean canPlace() {
        return true;
    }
}
