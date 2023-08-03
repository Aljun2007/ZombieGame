package com.aljun.zombiegame.work.mixin;

import com.aljun.zombiegame.work.RandomUtils;
import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieCrossBowAttackGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {

    protected ZombieMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
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
        if (this.getLevel().isClientSide()) {
            return;
        }
        if (GameProperty.isStartGame((ServerLevel) this.getLevel())) {
            Zombie zombie = (Zombie) (Object) this;
            if (ZombieUtils.canBeLoaded(zombie)) {
                cir.setReturnValue(GameProperty.ZombieProperty.isSunSensitive(zombie));
            }
        }
    }

    @Inject(method = "convertsInWater", at = @At("RETURN"), cancellable = true)
    public void convertsInWaterMixin(CallbackInfoReturnable<Boolean> cir) {
        if (this.getLevel().isClientSide()) {
            return;
        }
        if (GameProperty.isStartGame((ServerLevel) this.getLevel())) {
            Zombie zombie = (Zombie) (Object) this;
            if (ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
                cir.setReturnValue(GameProperty.ZombieProperty.convertsInWater(zombie));
            }
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSynchedDataMixin(CallbackInfo ci) {
        Zombie zombie = (Zombie) (Object) this;
        if (!(zombie instanceof ZombieVillager)) {
            this.entityData.define(ZombieCrossBowAttackGoal.IS_CHARGING_CROSSBOW, false);
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    protected void getHurtSoundMixin(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
        if (this.getLevel().isClientSide()) return;
        if (GameProperty.isStartGame()) {
            Zombie zombie = (Zombie) (Object) this;
            ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);
            if (mainGoal != null) {
                if (mainGoal.zombieShieldUsingGoal.goal.blockSound()) {
                    cir.setReturnValue(null);
                }
            }
        }
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    protected void populateDefaultEquipmentSlotsMixin(RandomSource p_219165_, DifficultyInstance p_219166_,
                                                      CallbackInfo ci) {

        if (GameProperty.isStartGame((ServerLevel) this.getLevel())) {
            Zombie zombie = (Zombie) (Object) this;
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
        if (GameProperty.isStartGame((ServerLevel) this.getLevel())) {
            if (ZombieUtils.canBeLoaded(this)) {
                ci.cancel();
            }
        }
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(@NotNull RandomSource randomSource,
                                                        @NotNull DifficultyInstance difficultyInstance) {
        if (GameProperty.isStartGame((ServerLevel) this.getLevel())) {
            Zombie zombie = (Zombie) (Object) this;
            if (ZombieUtils.canBeLoaded(zombie)) {
                return;
            }
        }
        super.populateDefaultEquipmentEnchantments(randomSource, difficultyInstance);
    }
}
