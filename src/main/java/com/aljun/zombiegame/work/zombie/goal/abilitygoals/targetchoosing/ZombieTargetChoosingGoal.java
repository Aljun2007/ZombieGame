package com.aljun.zombiegame.work.zombie.goal.abilitygoals.targetchoosing;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import org.slf4j.Logger;


public class ZombieTargetChoosingGoal extends AbstractZombieAbilityGoal {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ZombieTargetChoosingGoal(ZombieMainGoal mainGoal, Zombie zombie) {
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
        ServerLevel level = (ServerLevel) this.zombie.getLevel();
        if (level.getGameTime() % 10 == 0 || level.getGameTime() % 10 == 1) {
            int i = RandomUtils.RANDOM.nextInt(1, 4);

            LivingEntity entity = level.getNearestEntity(LivingEntity.class,
                    TargetingConditions.forNonCombat().range(75d).selector(
                            (e) -> (GameProperty.TimeProperty.getGameStage() >= 0.6d
                                    || this.zombie.getSensing().hasLineOfSight(e)) && ZombieUtils.canBeTarget(
                                    e)).ignoreLineOfSight(), this.zombie, this.zombie.getX(), this.zombie.getY(),
                    this.zombie.getZ(),
                    this.zombie.getBoundingBox().inflate(150D, GameProperty.ZombieProperty.getZombieSenseHeight(),
                            150D));
            if (entity != null) {
                if (this.zombie.getTarget() == null || (this.zombie.getEyePosition().distanceTo(entity.getEyePosition())
                                                        <= 5d)) {
                    this.zombie.setTarget(entity);
                }
            }
        }
    }
}
