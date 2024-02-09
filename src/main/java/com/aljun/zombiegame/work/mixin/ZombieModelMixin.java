package com.aljun.zombiegame.work.mixin;

import com.aljun.zombiegame.work.zombie.goal.abilitygoals.attackgoal.ZombieCrossBowAttackGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.BowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.CrossBowAttackZombieGoal;
import com.aljun.zombiegame.work.zombie.goal.zombiesets.ZombieMainGoal;
import com.aljun.zombiegame.work.zombie.load.ZombieUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(AbstractZombieModel.class)
public abstract class ZombieModelMixin<Z extends MonsterEntity> extends BipedModel<Z> {

    @Unique
    private String zombieGame_1_19_4$type = ZombieMainGoal.NAME;

    public ZombieModelMixin(float p_i1148_1_) {
        super(p_i1148_1_);
    }

    protected ZombieModelMixin(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_, int p_i1149_4_) {
        super(p_i1149_1_, p_i1149_2_, p_i1149_3_, p_i1149_4_);
    }

    public ZombieModelMixin(Function<ResourceLocation, RenderType> p_i225946_1_, float p_i225946_2_, float p_i225946_3_, int p_i225946_4_, int p_i225946_5_) {
        super(p_i225946_1_, p_i225946_2_, p_i225946_3_, p_i225946_4_, p_i225946_5_);
    }


    @Unique
    public boolean zombieGame_1_19_4$isAggressive(Z p_101999_) {
        return p_101999_.isAggressive();
    }

    @Inject(method = "setupAnim(Lnet/minecraft/entity/monster/MonsterEntity;FFFFF)V", at = @At("HEAD"), cancellable =
            true)
    public void setupAnimMixin(Z zombie, float p_102867_, float p_102868_, float p_102869_, float p_102870_,
                               float p_102871_, CallbackInfo ci) {
        if (!ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
            super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
            ModelHelper.animateZombieArms(this.leftArm, this.rightArm, this.zombieGame_1_19_4$isAggressive(zombie),
                    this.attackTime, p_102869_);
            return;
        }
        if (zombie.isUsingItem()) {
            if (zombie.getUseItem().getItem() instanceof ShieldItem) {
                if (zombie.getUsedItemHand() == Hand.OFF_HAND) {
                    this.leftArmPose = ArmPose.BLOCK;
                    super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
                    ci.cancel();
                    return;
                } else if (zombie.getUsedItemHand() == Hand.MAIN_HAND) {
                    this.rightArmPose = ArmPose.BLOCK;
                    super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
                    ci.cancel();
                    return;
                }
            }
        }
        super.setupAnim(zombie, p_102867_, p_102868_, p_102869_, p_102870_, p_102871_);
        ItemStack itemstack = zombie.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem()==(Items.BOW)) {
            this.zombieGame_1_19_4$type = BowAttackZombieGoal.NAME;
        } else if (itemstack.getItem()==(Items.CROSSBOW)) {
            this.zombieGame_1_19_4$type = CrossBowAttackZombieGoal.NAME;
        } else {
            this.zombieGame_1_19_4$type = ZombieMainGoal.NAME;
        }
        if (this.zombieGame_1_19_4$type.equals(BowAttackZombieGoal.NAME)) {
            if (zombie.isAggressive() && (itemstack.isEmpty() || !(itemstack.getItem()==(Items.BOW)))) {
                float f = MathHelper.sin(this.attackTime * (float) Math.PI);
                float f1 = MathHelper.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float) Math.PI);
                this.rightArm.zRot = 0.0F;
                this.leftArm.zRot = 0.0F;
                this.rightArm.yRot = -(0.1F - f * 0.6F);
                this.leftArm.yRot = 0.1F - f * 0.6F;
                this.rightArm.xRot = (-(float) Math.PI / 2F);
                this.leftArm.xRot = (-(float) Math.PI / 2F);
                this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
                this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
                ModelHelper.bobArms(this.rightArm, this.leftArm, p_102869_);
            }
            if (!this.zombieGame_1_19_4$isAggressive(zombie)) {
                ModelHelper.animateZombieArms(this.leftArm, this.rightArm,
                        this.zombieGame_1_19_4$isAggressive(zombie), this.attackTime, p_102869_);
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
                this.rightArm.xRot = MathHelper.cos(p_102867_ * 0.6662F + (float) Math.PI) * 2.0F * p_102868_ * 0.5F;
                this.rightArm.yRot = 0.0F;
                this.rightArm.zRot = 0.0F;
                this.leftArm.xRot = MathHelper.cos(p_102867_ * 0.6662F) * 2.0F * p_102868_ * 0.5F;
                this.leftArm.yRot = 0.0F;
                this.leftArm.zRot = 0.0F;
                this.rightLeg.xRot = MathHelper.cos(p_102867_ * 0.6662F) * 1.4F * p_102868_ * 0.5F;
                this.rightLeg.yRot = 0.0F;
                this.rightLeg.zRot = 0.0F;
                this.leftLeg.xRot = MathHelper.cos(p_102867_ * 0.6662F + (float) Math.PI) * 1.4F * p_102868_ * 0.5F;
                this.leftLeg.yRot = 0.0F;
                this.leftLeg.zRot = 0.0F;
            }

            AbstractIllagerEntity.ArmPose abstractillager$illagerarmpose = this.zombieGame_1_19_4$getArmPose(zombie);
            if (abstractillager$illagerarmpose == AbstractIllagerEntity.ArmPose.ATTACKING) {
                if (zombie.getMainHandItem().isEmpty()) {
                    ModelHelper.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, p_102869_);
                } else {
                    ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, zombie, this.attackTime, p_102869_);
                }
            } else if (abstractillager$illagerarmpose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.rightArm.xRot = MathHelper.cos(p_102869_ * 0.6662F) * 0.25F;
                this.leftArm.xRot = MathHelper.cos(p_102869_ * 0.6662F) * 0.25F;
                this.rightArm.zRot = 2.3561945F;
                this.leftArm.zRot = -2.3561945F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
            } else if (abstractillager$illagerarmpose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = -0.9424779F + this.head.xRot;
                this.leftArm.yRot = this.head.yRot - 0.4F;
                this.leftArm.zRot = ((float) Math.PI / 2F);
            } else if (abstractillager$illagerarmpose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
                ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
            } else if (abstractillager$illagerarmpose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
                ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, zombie, true);
            } else if (abstractillager$illagerarmpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.rightArm.xRot = MathHelper.cos(p_102869_ * 0.6662F) * 0.05F;
                this.rightArm.zRot = 2.670354F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.leftArm.xRot = MathHelper.cos(p_102869_ * 0.6662F) * 0.05F;
                this.leftArm.zRot = -2.3561945F;
                this.leftArm.yRot = 0.0F;
            }
        } else {
            ModelHelper.animateZombieArms(this.leftArm, this.rightArm, this.zombieGame_1_19_4$isAggressive(zombie),
                    this.attackTime, p_102869_);
        }
        ci.cancel();
    }
    @Unique
    private AbstractIllagerEntity.ArmPose zombieGame_1_19_4$getArmPose(Z zombie) {
        if (this.zombieGame_1_19_4$isChargingCrossbow(zombie)) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE;
        } else if (zombie.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD;
        } else {
            return this.zombieGame_1_19_4$isAggressive(zombie) ? AbstractIllagerEntity.ArmPose.ATTACKING :
                    AbstractIllagerEntity.ArmPose.NEUTRAL;
        }
    }

    @Unique
    private boolean zombieGame_1_19_4$isChargingCrossbow(Z zombie) {
        boolean b = false;
        try {
            b = zombie.getEntityData().get(ZombieCrossBowAttackGoal.IS_CHARGING_CROSSBOW);
        } catch (Throwable ignored) {
        }
        return b;
    }


    @Override
    public void prepareMobModel( Z zombie, float p_103794_, float p_103795_, float p_103796_) {
        if (!ZombieUtils.canBeLoadedAsLandZombie(zombie)) {
            super.prepareMobModel(zombie, p_103794_, p_103795_, p_103796_);
            return;
        }
        ItemStack itemstack = zombie.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem()==(Items.BOW)) {
            this.zombieGame_1_19_4$type = BowAttackZombieGoal.NAME;
        } else if (itemstack.getItem()==(Items.CROSSBOW)) {
            this.zombieGame_1_19_4$type = CrossBowAttackZombieGoal.NAME;
        } else {
            this.zombieGame_1_19_4$type = ZombieMainGoal.NAME;
        }
        if (this.zombieGame_1_19_4$type.equals(BowAttackZombieGoal.NAME)) {
            this.rightArmPose = BipedModel.ArmPose.EMPTY;
            this.leftArmPose = BipedModel.ArmPose.EMPTY;
            if (itemstack.getItem()==(Items.BOW) && zombie.isAggressive()) {
                if (zombie.getMainArm() == HandSide.RIGHT) {
                    this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                } else {
                    this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
                }
            }
        }
        super.prepareMobModel(zombie, p_103794_, p_103795_, p_103796_);
    }


    @Override
    public void translateToHand( HandSide p_103778_,  MatrixStack p_103779_) {
        if (zombieGame_1_19_4$type.equals(BowAttackZombieGoal.NAME)) {
            float f = p_103778_ == HandSide.RIGHT ? 1.0F : -1.0F;
            ModelRenderer modelrenderer = this.getArm(p_103778_);
            modelrenderer.x += f;
            modelrenderer.translateAndRotate(p_103779_);
            modelrenderer.x -= f;
        } else {
            this.getArm(p_103778_).translateAndRotate(p_103779_);
        }
    }
}
