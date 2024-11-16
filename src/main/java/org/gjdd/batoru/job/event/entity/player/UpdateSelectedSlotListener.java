package org.gjdd.batoru.job.event.entity.player;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.Listener;

public interface UpdateSelectedSlotListener extends Listener {
    boolean onUpdateSelectedSlot(RegistryEntry<Job> job, ServerPlayerEntity player, int selectedSlot);
}
