package org.gjdd.batoru.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.skill.Skill;

public final class BatoruRegistryKeys {
    public static final RegistryKey<Registry<Job>> JOB = RegistryKey.ofRegistry(Identifier.of("batoru", "job"));
    public static final RegistryKey<Registry<Skill>> SKILL = RegistryKey.ofRegistry(Identifier.of("batoru", "skill"));

    public static void register() {
    }
}
