package com.aljun.zombiegame.work.mixin;


import com.aljun.zombiegame.work.game.GameProperty;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobCategory.class)
public abstract class MobCategoryMixin {
    @Shadow
    @Final
    private int max;

    @Shadow
    public abstract String getName();

    /**
     * @author aljun
     * @reason to add more zombies
     */
    @Overwrite
    public int getMaxInstancesPerChunk() {
        if (GameProperty.isStartGame()) {
            if (this.getName().equals(MobCategory.MONSTER.getName())) {
                return GameProperty.ZombieProperty.getZombieMaxCount();
            }
        }
        return this.max;
    }
}
