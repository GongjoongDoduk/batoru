package org.gjdd.batoru.job.event.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.Listener;

public interface JobAssignedListener extends Listener {
    void onJobAssigned(RegistryEntry<Job> job, LivingEntity entity);
}
