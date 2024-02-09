package com.aljun.zombiegame.work.zombie.goal.abilitygoals.lookatplayergoal;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.AbstractZombieAbilityGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ZombieLookAtPlayerGoal extends AbstractZombieAbilityGoal {

    public static final float DEFAULT_PROBABILITY = 0.02F;
    protected final float lookDistance;
    protected final float probability;
    protected final Class<? extends LivingEntity> lookAtType;
    protected final EntityPredicate lookAtContext;
    private final boolean onlyHorizontal;
    @Nullable
    protected Entity lookAt;
    private int lookTime;

    public ZombieLookAtPlayerGoal(ZombieMainGoal mainGoal, ZombieEntity zombie) {
        super(mainGoal, zombie);
        this.lookAtType = PlayerEntity.class;
        this.lookDistance = 8.0F;
        this.probability = 0.02F;
        this.onlyHorizontal = false;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        this.lookAtContext = (new EntityPredicate()).range(this.lookDistance).allowSameTeam().allowInvulnerable()
                .allowNonAttackable().selector((e) -> EntityPredicates.notRiding(this.getZombie()).test(e));

    }

    public boolean canBeUsed() {
        if (this.zombie.getTarget() != null) {
            return false;
        }
        if (this.zombie.getRandom().nextFloat() >= this.probability) {
            return false;
        } else {
            if (this.zombie.getTarget() != null) {
                this.lookAt = this.zombie.getTarget();
            }

            if (this.lookAtType == PlayerEntity.class) {
                this.lookAt = this.zombie.level.getNearestPlayer(this.lookAtContext, this.zombie, this.zombie.getX(), this.zombie.getEyeY(), this.zombie.getZ());
            } else {
                this.lookAt = this.zombie.level.getNearestEntity(this.zombie.level.getEntitiesOfClass(this.lookAtType, this.zombie.getBoundingBox().inflate((double) this.lookDistance, 3.0D, (double) this.lookDistance), (p_148124_) -> {
                    return true;
                }), this.lookAtContext, this.zombie, this.zombie.getX(), this.zombie.getEyeY(), this.zombie.getZ());
            }

            return this.lookAt != null;
        }
    }

    public boolean canContinueToUse() {
        if (this.lookAt==null) {
            return false;
        }
        if (!this.lookAt.isAlive()) {
            return false;
        } else if (this.zombie.distanceToSqr(this.lookAt) > (double)(this.lookDistance * this.lookDistance)) {
            return false;
        } else {
            return this.lookTime > 0;
        }
    }

    public void start() {
        this.lookTime = 40 + this.zombie.getRandom().nextInt(40);
    }

    public void stop() {
        this.lookAt = null;
    }

    public void tick() {
        if (this.lookAt != null && this.lookAt.isAlive()) {
            double d0 = this.onlyHorizontal ? this.zombie.getEyeY() : this.lookAt.getEyeY();
            this.zombie.getLookControl().setLookAt(this.lookAt.getX(), d0, this.lookAt.getZ());
            --this.lookTime;
        }
    }
}
