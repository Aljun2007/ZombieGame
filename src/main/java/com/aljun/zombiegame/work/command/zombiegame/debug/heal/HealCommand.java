package com.aljun.zombiegame.work.command.zombiegame.debug.heal;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class HealCommand implements Command<CommandSource> {

    public static LiteralArgumentBuilder<CommandSource> getCommand() {
        return load(Commands.literal("heal"));
    }

    private static LiteralArgumentBuilder<CommandSource> load(
            LiteralArgumentBuilder<CommandSource> command) {
        return command.executes((context -> {
            ServerPlayerEntity player = null;
            if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
                player = (ServerPlayerEntity) context.getSource().getEntity();
            }

            if (player != null) {
                player.removeAllEffects();
                player.addEffect(new EffectInstance(Effects.HEAL, 10, 20));
                player.addEffect(new EffectInstance(Effects.SATURATION, 10, 20));
                player.addEffect(new EffectInstance(Effects.REGENERATION, 10, 20));
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        return 0;
    }
}