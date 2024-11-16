package org.gjdd.batoru.job.event.entity.player;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.Listener;

public interface HandSwingListener extends Listener {
    void onHandSwing(RegistryEntry<Job> job, ServerPlayerEntity player, Hand hand);
}
