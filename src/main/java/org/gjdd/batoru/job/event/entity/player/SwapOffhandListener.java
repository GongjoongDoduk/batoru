package org.gjdd.batoru.job.event.entity.player;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.Listener;

public interface SwapOffhandListener extends Listener {
    boolean onSwapOffhand(RegistryEntry<Job> job, ServerPlayerEntity player, ItemStack mainHandStack, ItemStack offHandStack);
}
