package com.aljun.zombiegame.work.mixin;

import com.aljun.zombiegame.work.game.GameProperty;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HuskEntity.class)
public class HuskMixin extends ZombieEntity {
    public HuskMixin(EntityType<? extends ZombieEntity> p_34271_, World p_34272_) {
        super(p_34271_, p_34272_);
    }

    public HuskMixin(World p_34274_) {
        super(p_34274_);
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
}
