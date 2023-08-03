package com.aljun.zombiegame.work.mixin;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieCrossBowAttackGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.BowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.CrossBowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(AbstractZombieModel.class)
public abstract class ZombieModelMixin<T extends Monster> extends HumanoidModel<T> {

    @Unique
    private String zombieGame_1_19_4$type = ZombieMainGoal.NAME;

    public ZombieModelMixin(ModelPart p_170677_) {
        super(p_170677_);
    }

    public ZombieModelMixin(ModelPart p_170679_, Function<ResourceLocation, RenderType> p_170680_) {
        super(p_170679_, p_170680_);
    }


    @Shadow
    public abstract boolean isAggressive(T p_101999_);

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/Monster;FFFFF)V", at = @At("HEAD"), cancellable =
            true)
    public void setupAnimMixin(T zombie, float p_102867_, float p_102868_, float p_102869_, float p_102870_,
                               float p_102871_, CallbackInfo ci) {
        if (!ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
            super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
            AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(zombie), this.attackTime,
                    p_102869_);
            return;
        }
        if (zombie.isUsingItem()) {
            if (zombie.getUseItem().getItem() instanceof ShieldItem) {
                if (zombie.getUsedItemHand() == InteractionHand.OFF_HAND) {
                    this.leftArmPose = ArmPose.BLOCK;
                    super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
                    ci.cancel();
                    return;
                } else if (zombie.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                    this.rightArmPose = ArmPose.BLOCK;
                    super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
                    ci.cancel();
                    return;
                }
            }
        }
        super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
        ItemStack itemstack = zombie.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemstack.is(Items.BOW)) {
            this.zombieGame_1_19_4$type = BowAttackZombieGoal.NAME;
        } else if (itemstack.is(Items.CROSSBOW)) {
            this.zombieGame_1_19_4$type = CrossBowAttackZombieGoal.NAME;
        } else {
            this.zombieGame_1_19_4$type = ZombieMainGoal.NAME;
        }
        if (this.zombieGame_1_19_4$type.equals(BowAttackZombieGoal.NAME)) {
            if (zombie.isAggressive() && (itemstack.isEmpty() || !itemstack.is(Items.BOW))) {
                float f = Mth.sin(this.attackTime * (float) Math.PI);
                float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
                this.rightArm.zRot = 0.0F;
                this.leftArm.zRot = 0.0F;
                this.rightArm.yRot = -(0.1F - f * 0.6F);
                this.leftArm.yRot = 0.1F - f * 0.6F;
                this.rightArm.xRot = (-(float) Math.PI / 2F);
                this.leftArm.xRot = (-(float) Math.PI / 2F);
                this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
                this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
                AnimationUtils.bobArms(this.rightArm, this.leftArm, p_102869_);
            }
            if (!this.isAggressive(zombie)) {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(zombie),
                        this.attackTime, p_102869_);
            }
        } else if (this.zombieGame_1_19_4$type.equals(CrossBowAttackZombieGoal.NAME)) {
            this.head.yRot = p_102870_ * ((float) Math.PI / 180F);
            this.head.xRot = p_102871_ * ((float) Math.PI / 180F);
            if (this.riding) {
                this.rightArm.xRot = (-(float) Math.PI / 5F);
                this.rightArm.yRot = 0.0F;
                this.rightArm.zRot = 0.0F;
                this.leftArm.xRot = (-(float) Math.PI / 5F);
                this.leftArm.yRot = 0.0F;
                this.leftArm.zRot = 0.0F;
                this.rightLeg.xRot = -1.4137167F;
                this.rightLeg.yRot = ((float) Math.PI / 10F);
                this.rightLeg.zRot = 0.07853982F;
                this.leftLeg.xRot = -1.4137167F;
                this.leftLeg.yRot = (-(float) Math.PI / 10F);
                this.leftLeg.zRot = -0.07853982F;
            } else {
                this.rightArm.xRot = Mth.cos(p_102867_ * 0.6662F + (float) Math.PI) * 2.0F * p_102868_ * 0.5F;
                this.rightArm.yRot = 0.0F;
                this.rightArm.zRot = 0.0F;
                this.leftArm.xRot = Mth.cos(p_102867_ * 0.6662F) * 2.0F * p_102868_ * 0.5F;
                this.leftArm.yRot = 0.0F;
                this.leftArm.zRot = 0.0F;
                this.rightLeg.xRot = Mth.cos(p_102867_ * 0.6662F) * 1.4F * p_102868_ * 0.5F;
                this.rightLeg.yRot = 0.0F;
                this.rightLeg.zRot = 0.0F;
                this.leftLeg.xRot = Mth.cos(p_102867_ * 0.6662F + (float) Math.PI) * 1.4F * p_102868_ * 0.5F;
                this.leftLeg.yRot = 0.0F;
                this.leftLeg.zRot = 0.0F;
            }

            AbstractIllager.IllagerArmPose abstractillager$illagerarmpose = this.zombieGame_1_19_4$getArmPose(zombie);
            if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.ATTACKING) {
                if (zombie.getMainHandItem().isEmpty()) {
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, p_102869_);
                } else {
                    AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, zombie, this.attackTime, p_102869_);
                }
            } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.rightArm.xRot = Mth.cos(p_102869_ * 0.6662F) * 0.25F;
                this.leftArm.xRot = Mth.cos(p_102869_ * 0.6662F) * 0.25F;
                this.rightArm.zRot = 2.3561945F;
                this.leftArm.zRot = -2.3561945F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
            } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = -0.9424779F + this.head.xRot;
                this.leftArm.yRot = this.head.yRot - 0.4F;
                this.leftArm.zRot = ((float) Math.PI / 2F);
            } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CROSSBOW_HOLD) {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
            } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE) {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, zombie, true);
            } else if (abstractillager$illagerarmpose == AbstractIllager.IllagerArmPose.CELEBRATING) {
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.rightArm.xRot = Mth.cos(p_102869_ * 0.6662F) * 0.05F;
                this.rightArm.zRot = 2.670354F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.leftArm.xRot = Mth.cos(p_102869_ * 0.6662F) * 0.05F;
                this.leftArm.zRot = -2.3561945F;
                this.leftArm.yRot = 0.0F;
            }
        } else {
            AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(zombie), this.attackTime,
                    p_102869_);
        }
        ci.cancel();
    }

    @Unique
    private AbstractIllager.IllagerArmPose zombieGame_1_19_4$getArmPose(T zombie) {
        if (this.zombieGame_1_19_4$isChargingCrossbow(zombie)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
        } else if (zombie.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive(zombie) ? AbstractIllager.IllagerArmPose.ATTACKING :
                    AbstractIllager.IllagerArmPose.NEUTRAL;
        }
    }

    @Unique
    private boolean zombieGame_1_19_4$isChargingCrossbow(T zombie) {
        boolean b = false;
        try {
            b = zombie.getEntityData().get(ZombieCrossBowAttackGoal.IS_CHARGING_CROSSBOW);
        } catch (Throwable ignored) {
        }
        return b;
    }


    @Override
    public void prepareMobModel(@NotNull T zombie, float p_103794_, float p_103795_, float p_103796_) {
        if (!ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
            super.prepareMobModel(zombie, p_103794_, p_103795_, p_103796_);
            return;
        }
        ItemStack itemstack = zombie.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemstack.is(Items.BOW)) {
            this.zombieGame_1_19_4$type = BowAttackZombieGoal.NAME;
        } else if (itemstack.is(Items.CROSSBOW)) {
            this.zombieGame_1_19_4$type = CrossBowAttackZombieGoal.NAME;
        } else {
            this.zombieGame_1_19_4$type = ZombieMainGoal.NAME;
        }
        if (this.zombieGame_1_19_4$type.equals(BowAttackZombieGoal.NAME)) {
            this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
            this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
            if (itemstack.is(Items.BOW) && zombie.isAggressive()) {
                if (zombie.getMainArm() == HumanoidArm.RIGHT) {
                    this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
                } else {
                    this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
            }
        }
        super.prepareMobModel(zombie, p_103794_, p_103795_, p_103796_);
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm p_103778_, @NotNull PoseStack p_103779_) {
        if (zombieGame_1_19_4$type.equals(BowAttackZombieGoal.NAME)) {
            float f = p_103778_ == HumanoidArm.RIGHT ? 1.0F : -1.0F;
            ModelPart modelpart = this.getArm(p_103778_);
            modelpart.x += f;
            modelpart.translateAndRotate(p_103779_);
            modelpart.x -= f;
        } else {
            this.getArm(p_103778_).translateAndRotate(p_103779_);
        }
    }
}
