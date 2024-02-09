package com.aljun.zombiegame.work.zombie.goal.abilitygoals.zombiegotowatergoal;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.function.Function;

public class ZombieGoToCertainPlaceGoal extends AbstractZombieAbilityGoal {
    private final Function<ZombieMainGoal, Vector3d> getPlacePos;
    private final Function<ZombieMainGoal, Boolean> isPlaceSuitable;
    private double wantedX;
    private double wantedY;
    private double wantedZ;

    public ZombieGoToCertainPlaceGoal(ZombieMainGoal mainGoal, ZombieEntity zombie,
                                      Function<ZombieMainGoal, Vector3d> getPlacePos,
                                      Function<ZombieMainGoal, Boolean> isPlaceSuitable) {
        super(mainGoal, zombie);
        this.getPlacePos = getPlacePos;
        this.isPlaceSuitable = isPlaceSuitable;
    }

    public boolean canBeUsed() {
        if (!this.mainGoal.isFree()) {
            return false;
        } else if (!this.zombie.level.isDay()) {
            return false;
        } else if (this.isPlaceSuitable.apply(this.mainGoal)) {
            return false;
        } else {
            this.mainGoal.zombieRandomWalkGoal.goal.callToStopWalking();
            if (this.mainGoal.zombieRandomWalkGoal.goal.isWalking()) {
                return false;
            }
            Vector3d vec3 = this.getPlacePos.apply(this.mainGoal);
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
