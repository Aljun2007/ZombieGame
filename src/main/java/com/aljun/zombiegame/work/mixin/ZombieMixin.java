package com.aljun.zombiegame.work.mixin;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieCrossBowAttackGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieMixin extends MonsterEntity {

    protected ZombieMixin(EntityType<? extends MonsterEntity> p_33002_, World p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Shadow
    protected abstract void registerGoals();

    @Shadow
    public abstract boolean isBaby();

    @Shadow
    public abstract void setBaby(boolean p_34309_);

    @Inject(method = "isSunSensitive", at = @At("RETURN"), cancellable = true)
    public void isSunSensitiveMixin(CallbackInfoReturnable<Boolean> cir) {
        if (this.level.isClientSide()) {
            return;
        }
        if (GameProperty.isStartGame((ServerWorld) this.level)) {
            ZombieEntity zombie = (ZombieEntity) (Object) this;
            if (ZombieUtils.canBeLoaded(zombie)) {
                cir.setReturnValue(GameProperty.ZombieProperty.isSunSensitive(zombie));
            }
        }
    }

    @Inject(method = "convertsInWater", at = @At("RETURN"), cancellable = true)
    public void convertsInWaterMixin(CallbackInfoReturnable<Boolean> cir) {
        if (this.level.isClientSide()) {
            return;
        }
        if (GameProperty.isStartGame((ServerWorld) this.level)) {
            ZombieEntity zombie = (ZombieEntity) (Object) this;
            if (ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
                cir.setReturnValue(GameProperty.ZombieProperty.convertsInWater(zombie));
            }
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSynchedDataMixin(CallbackInfo ci) {
        ZombieEntity zombie = (ZombieEntity) (Object) this;
        if (!(zombie instanceof ZombieVillagerEntity)) {
            this.entityData.define(ZombieCrossBowAttackGoal.IS_CHARGING_CROSSBOW, false);
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    protected void getHurtSoundMixin(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
        if (this.level.isClientSide()) return;
        if (GameProperty.isStartGame()) {
            ZombieEntity zombie = (ZombieEntity) (Object) this;
            ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);
            if (mainGoal != null) {
                if (mainGoal.zombieShieldUsingGoal.goal.blockSound()) {
                    cir.setReturnValue(null);
                }
            }
        }
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    protected void populateDefaultEquipmentSlotsMixin(DifficultyInstance p_34286_, CallbackInfo ci) {
        if (GameProperty.isStartGame((ServerWorld) this.level)) {
            ZombieEntity zombie = (ZombieEntity) (Object) this;
            if (ZombieUtils.canBeLoaded(zombie)) {
                this.setCanPickUpLoot(false);
                if (RandomUtils.nextBoolean(0.1d + 0.2d * GameProperty.TimeProperty.getGameStage())) {
                    this.setCanPickUpLoot(true);
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "registerGoals", at = @At("HEAD"), cancellable = true)
    protected void registerGoalsMixin(CallbackInfo ci) {
        if (GameProperty.isStartGame((ServerWorld) this.level)) {
            if (ZombieUtils.canBeLoaded(this)) {
                ci.cancel();
            }
        }
    }

    @Override
    protected void populateDefaultEquipmentEnchantments( DifficultyInstance difficultyInstance) {
        if (GameProperty.isStartGame((ServerWorld) this.level)) {
            ZombieEntity zombie = (ZombieEntity) (Object) this;
            if (ZombieUtils.canBeLoaded(zombie)) {
                return;
            }
        }
        super.populateDefaultEquipmentEnchantments(difficultyInstance);
    }
}
