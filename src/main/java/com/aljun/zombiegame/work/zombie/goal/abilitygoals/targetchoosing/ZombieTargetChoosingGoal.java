package com.aljun.zombiegame.work.zombie.goal.abilitygoals.targetchoosing;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.server.ServerWorld;


public class ZombieTargetChoosingGoal extends AbstractZombieAbilityGoal {


    public ZombieTargetChoosingGoal(ZombieMainGoal mainGoal, ZombieEntity zombie) {
        super(mainGoal, zombie);
    }


    @Override
    public boolean canBeUsed() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public void tick() {
        ServerWorld level = (ServerWorld) this.zombie.level;
        if (level.getGameTime() % 10 == 0 || level.getGameTime() % 10 == 1) {
            int i = RandomUtils.nextInt(1, 4);

            LivingEntity entity = level.getNearestEntity(LivingEntity.class,
                    (new EntityPredicate()).range(75d).selector(
                            (e) -> (GameProperty.TimeProperty.getGameStage() >= 0.6d
                                    || this.zombie.getSensing().canSee(e)) &&
                                    ZombieUtils.canBeTarget(e)), this.zombie, this.zombie.getX(), this.zombie.getY(),
                    this.zombie.getZ(),
                    this.zombie.getBoundingBox().inflate(150D, GameProperty.ZombieProperty.getZombieSenseHeight(),
                            150D));
            if (entity != null) {
                if (this.zombie.getTarget() == null || (this.zombie.getEyePosition(0).distanceTo(entity.getEyePosition(0))
                                                        <= 5d)) {
                    this.zombie.setTarget(entity);
                }
            }
        }
    }
}
