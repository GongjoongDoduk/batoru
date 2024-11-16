package org.gjdd.batoru.job.event.entity.player;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.Listener;

public interface DropItemListener extends Listener {
    boolean onDropItem(RegistryEntry<Job> job, ServerPlayerEntity player, boolean entireStack);
}
