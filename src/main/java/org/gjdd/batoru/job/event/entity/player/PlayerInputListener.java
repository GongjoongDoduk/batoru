package org.gjdd.batoru.job.event.entity.player;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PlayerInput;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.Listener;

public interface PlayerInputListener extends Listener {
    void onPlayerInput(RegistryEntry<Job> job, ServerPlayerEntity player, PlayerInput input);
}
