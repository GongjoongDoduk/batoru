package org.gjdd.batoru.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;

public record SkillContext(RegistryEntry<Skill> skill, LivingEntity source) {
}
