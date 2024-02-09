package com.aljun.zombiegame.work.zombie.goal.abilitygoals.randomwalkgoal;

import com.aljun.core.DefaultRandomPos;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.vector.Vector3d;

public class ZombieRandomWalkGoal extends AbstractZombieAbilityGoal {
    public static final int NORMAL = 0;
    public static final int AWAY = 1;
    public static final int TOWARDS = 2;
    private boolean isWalking = false;
    private int state = NORMAL;

    private long walkingTimeLeft = 0L;
    private boolean isMorImportant;

    private boolean isSetPos = false;
    private Vector3d targetPos = new Vector3d(0d, 0d, 0d);

    public ZombieRandomWalkGoal(ZombieMainGoal mainGoal, ZombieEntity zombie) {
        super(mainGoal, zombie);
    }

    @Override
    public boolean canBeUsed() {
        return isWalking;
    }

    @Override
    public boolean canContinueToUse() {
        walkingTimeLeft--;
        boolean shouldStop = walkingTimeLeft == 0L;

        if (!this.mainGoal.isBuilding() && this.zombie.getNavigation().isDone()) {
            if (shouldStop) {
                stopWalking();
            } else {
                if (!isSetPos) {
                    Vector3d vec3;
                    switch (state) {
                        case AWAY:
                            vec3 = DefaultRandomPos.getPosAway(this.getZombie(), 10, 4, this.targetPos);
                            break;
                        case TOWARDS:
                            vec3 = DefaultRandomPos.getPosTowards(this.getZombie(), 10, 4, this.targetPos, (float) Math.PI / 2F);
                            break;
                        default:
                            vec3 = DefaultRandomPos.getPos(this.getZombie(), 10, 4);
                    }
                    ;

                    if (GameProperty.ZombieProperty.isSunSensitive(this.zombie)) {
                        if (vec3 != null && !ZombieUtils.isNotSunBurnTick(this.zombie, vec3)) {
                            vec3 = null;
                        }
                    }

                    if (vec3 != null) {
                        this.zombie.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, isMorImportant ? this.mainGoal.getZombieSpeedBaseNormal() : this.mainGoal.getZombieSpeedBaseSlow());
                        this.isSetPos = true;
                    }
                }
            }
        }
        return shouldStop;
    }

    public void startRandomWalking(long time, boolean isMoreImportant_, int state_) {
        if (mainGoal.isBuilding()) {
            return;
        }
        if (!isWalking) {
            if (isMoreImportant_ || this.mainGoal.isFree()) {
                this.walkingTimeLeft = time;
                this.isMorImportant = isMoreImportant_;
                this.isWalking = true;
                this.isSetPos = false;
                this.state = state_;
            }
        }
    }

    public void setTargetPos(Vector3d targetPos) {
        this.targetPos = targetPos;
    }

    public void startRandomWalking(long time, boolean isMoreImportant_) {
        this.startRandomWalking(time, isMoreImportant_, NORMAL);
    }

    public void callToStopWalking() {
        if (!isMorImportant || (this.walkingTimeLeft <= -100L)) {
            this.stopWalking();
        }
        if (this.zombie.getTarget() != null) {
            if (this.walkingTimeLeft <= -30L && 6d >= this.zombie.getEyePosition(0).distanceTo(this.zombie.getTarget().getEyePosition(0))) {
                this.stopWalking();
            }
        }
    }

    public void stopWalking() {
        if (this.isWalking) {
            this.isWalking = false;
            this.zombie.getNavigation().stop();
        }
    }

    public boolean isWalking() {
        return isWalking;
    }
}
