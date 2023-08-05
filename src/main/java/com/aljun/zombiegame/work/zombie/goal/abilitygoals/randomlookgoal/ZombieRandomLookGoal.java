package com.aljun.zombiegame.work.zombie.goal.abilitygoals.randomlookgoal;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;

import java.util.EnumSet;

public class ZombieRandomLookGoal extends AbstractZombieAbilityGoal {
    private final Mob mob;
    private double relX;
    private double relZ;
    private int lookTime;

    public ZombieRandomLookGoal(ZombieMainGoal zombieMainGoal, Zombie zombie) {
        super(zombieMainGoal, zombie);
        this.mob = zombie;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    public boolean canBeUsed() {
        return this.mainGoal.isFree() && this.mob.getRandom().nextFloat() < 0.02F;
    }

    public boolean canContinueToUse() {
        return this.lookTime >= 0 && this.mainGoal.isFree();
    }

    public void start() {
        double d0 = (Math.PI * 2D) * this.mob.getRandom().nextDouble();
        this.relX = Math.cos(d0);
        this.relZ = Math.sin(d0);
        this.lookTime = 20 + this.mob.getRandom().nextInt(20);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        --this.lookTime;
        if (this.mainGoal.isFree() && this.mainGoal.zombieBreakGoal.goal.isDone()) {
            this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX, this.mob.getEyeY(),
                    this.mob.getZ() + this.relZ);
        }
    }


}
