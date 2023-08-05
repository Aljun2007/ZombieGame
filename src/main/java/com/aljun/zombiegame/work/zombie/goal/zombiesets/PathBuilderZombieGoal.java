package com.aljun.zombiegame.work.zombie.goal.zombiesets;

import net.minecraft.world.entity.monster.Zombie;

public class PathBuilderZombieGoal extends ZombieMainGoal {
    public static final String NAME = PathBuilderZombieGoal.class.getSimpleName();

    public PathBuilderZombieGoal(Zombie zombie) {
        super(zombie);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addGoal() {

        // Start the goals
        this.startUseGoal(this.zombieFleeSunGoal, 3);
        this.startUseGoal(this.zombieFloatGoal, 1);
        this.startUseGoal(this.zombiePathBuilderGoal, 2);
        this.startUseGoal(this.zombieBlockChooserGoal, 2);
        this.startUseGoal(this.zombieBreakGoal, 3);
        this.startUseGoal(this.zombieMeleeAttackGoal, 4);
        this.startUseGoal(this.zombieRandomLookGoal, 4);
        this.startUseGoal(this.zombieRandomWalkGoal, 2);
        this.startUseTargetChoosingGoal(this.zombieTargetChoosingGoal, 2);
        this.startUseTargetChoosingGoal(this.hurtTargetAddingGoal, 2);
        this.startUseGoal(this.zombieLookAtPlayerGoal, 3);
        this.startUseGoal(this.fluidPlaceBlockGoal, 3);

    }

    @Override
    public boolean isBuilding() {
        return !this.zombiePathBuilderGoal.goal.isDone() || !this.zombieBreakGoal.goal.isDone();
    }

    @Override
    public boolean canUsePathBuilder() {
        return this.banPathFinderTime <= 0L;
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
