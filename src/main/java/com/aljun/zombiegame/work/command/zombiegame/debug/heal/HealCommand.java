package com.aljun.zombiegame.work.command.zombiegame.debug.heal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;


public class HealCommand implements Command<CommandSourceStack> {

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return load(Commands.literal("heal"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> load(
            LiteralArgumentBuilder<CommandSourceStack> command) {
        return command.executes((context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player != null) {
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.HEAL, 10, 20));
                player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 10, 20));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10, 20));
            }
            return 0;
        }));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}