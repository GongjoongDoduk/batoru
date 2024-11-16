package org.gjdd.batoru.internal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import org.gjdd.batoru.channeling.Channeling;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.skill.Skill;
import org.gjdd.batoru.skill.SkillResult;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityExtensions {
    default Vec3d getPushedVelocity() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default void setPushedVelocity(Vec3d pushedVelocity) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean hasPushedStatusEffect() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean hasSilencedStatusEffect() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean isChanneling() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean startChanneling(Channeling channeling) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean stopChanneling() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default @Nullable RegistryEntry<Job> getJob() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default void setJob(@Nullable RegistryEntry<Job> job) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean hasSkillCooldown(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default int getSkillCooldown(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default void setSkillCooldown(RegistryEntry<Skill> skill, int cooldown) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default SkillResult canUseSkill(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default SkillResult useSkill(RegistryEntry<Skill> skill) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}
