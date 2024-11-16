package org.gjdd.batoru.job.event.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.Listener;

public interface JobUnassignedListener extends Listener {
    void onJobUnassigned(RegistryEntry<Job> job, LivingEntity entity);
}
