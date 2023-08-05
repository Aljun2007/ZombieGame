package com.aljun.zombiegame.work.zombie.goal.abilitygoals.zombiegotowatergoal;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class ZombieGoToCertainPlaceGoal extends AbstractZombieAbilityGoal {
    private final Function<ZombieMainGoal, Vec3> getPlacePos;
    private final Function<ZombieMainGoal, Boolean> isPlaceSuitable;
    private double wantedX;
    private double wantedY;
    private double wantedZ;

    public ZombieGoToCertainPlaceGoal(ZombieMainGoal mainGoal, Zombie zombie,
                                      Function<ZombieMainGoal, Vec3> getPlacePos,
                                      Function<ZombieMainGoal, Boolean> isPlaceSuitable) {
        super(mainGoal, zombie);
        this.getPlacePos = getPlacePos;
        this.isPlaceSuitable = isPlaceSuitable;
    }

    public boolean canBeUsed() {
        if (!this.mainGoal.isFree()) {
            return false;
        } else if (!this.zombie.getLevel().isDay()) {
            return false;
        } else if (this.isPlaceSuitable.apply(this.mainGoal)) {
            return false;
        } else {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
            if (this.mainGoal.zombieRandomWalkGoal.goal.isWalking()) {
                return false;
            }
            Vec3 vec3 = this.getPlacePos.apply(this.mainGoal);
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                return true;
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.zombie.getNavigation().isDone() && this.mainGoal.isFree();
    }

    public void start() {
        this.zombie.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ,
                this.mainGoal.getZombieSpeedBaseNormal());
    }

}
