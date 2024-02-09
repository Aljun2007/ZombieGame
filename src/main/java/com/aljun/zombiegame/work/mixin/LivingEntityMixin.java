package com.aljun.zombiegame.work.mixin;

import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"))
    public void hurtMixin(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.getEntity() == null) return;
        if (damageSource.getEntity().level.isClientSide()) return;
        if (damageSource.getEntity() == null) return;
        if (ZombieUtils.canBeLoaded((LivingEntity) (Object) this)) {
            if ((Object)this instanceof ZombieEntity) {
                ZombieEntity zombie = (ZombieEntity) (Object) this;
                ZombieMainGoal mainGoal = ZombieUtils.getOrLoadMainGoal(zombie);
                if (mainGoal != null) {
                    if (mainGoal.zombieShieldUsingGoal.isUsable) {
                        if (mainGoal.zombieShieldUsingGoal.goal.isUsingShield()) {
                            mainGoal.zombieShieldUsingGoal.goal.onShieldBlock(((LivingEntity) damageSource.getEntity()).getMainHandItem(), (LivingEntity) damageSource.getEntity());
                        }
                    }
                }
            }
        }
    }
}
