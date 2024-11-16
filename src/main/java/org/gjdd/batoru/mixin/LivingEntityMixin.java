package org.gjdd.batoru.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import org.gjdd.batoru.channeling.Channeling;
import org.gjdd.batoru.channeling.ChannelingContext;
import org.gjdd.batoru.internal.LivingEntityExtensions;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.entity.JobAssignedListener;
import org.gjdd.batoru.job.event.entity.JobUnassignedListener;
import org.gjdd.batoru.skill.Skill;
import org.gjdd.batoru.skill.SkillResult;
import org.gjdd.batoru.skill.SkillContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityExtensions {
    @Unique
    private final Map<RegistryEntry<Skill>, Integer> batoru$skillCooldowns = new HashMap<>();
    @Unique
    private @Nullable RegistryEntry<Job> batoru$job;
    @Unique
    private @Nullable Channeling batoru$channeling;
    @Unique
    private int batoru$channelingTime = -1;
    @Unique
    private Vec3d batoru$pushedVelocity = Vec3d.ZERO;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public Vec3d getPushedVelocity() {
        return batoru$pushedVelocity;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setPushedVelocity(Vec3d pushedVelocity) {
        batoru$pushedVelocity = pushedVelocity;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean hasPushedStatusEffect() {
        return batoru$hasStatusEffect(StatusEffect::isPushed);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean hasSilencedStatusEffect() {
        return batoru$hasStatusEffect(StatusEffect::isSilenced);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean isChanneling() {
        return batoru$channeling != null;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean startChanneling(Channeling channeling) {
        if (batoru$channeling != null || hasSilencedStatusEffect()) {
            return false;
        }

        batoru$channelingTime = 0;
        batoru$channeling = channeling;
        return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean stopChanneling() {
        if (batoru$channeling == null) {
            return false;
        }

        batoru$channelingTime = -1;
        batoru$channeling = null;
        return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public @Nullable RegistryEntry<Job> getJob() {
        return batoru$job;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setJob(@Nullable RegistryEntry<Job> job) {
        if (batoru$job == job) {
            return;
        }

        var oldJob = batoru$job;
        batoru$job = job;
        if (job == null) {
            batoru$onJobRemoved(oldJob);
        } else {
            batoru$onJobSet(job);
        }
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean hasSkillCooldown(RegistryEntry<Skill> skill) {
        return getSkillCooldown(skill) > 0;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public int getSkillCooldown(RegistryEntry<Skill> skill) {
        return batoru$skillCooldowns.getOrDefault(skill, 0);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setSkillCooldown(RegistryEntry<Skill> skill, int cooldown) {
        batoru$skillCooldowns.put(skill, cooldown);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillResult canUseSkill(RegistryEntry<Skill> skill) {
        return skill.value().getCondition().canUse(skillContext(skill));
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillResult useSkill(RegistryEntry<Skill> skill) {
        var skillResult = canUseSkill(skill);
        if (skillResult instanceof SkillResult.Failure) {
            return skillResult;
        }

        return skill.value().getAction().use(skillContext(skill));
    }

    @Inject(method = "tick()V", at = @At(value = "TAIL"))
    private void batoru$injectTick(CallbackInfo info) {
        var entity = (LivingEntity) (Object) this;
        if (entity.getWorld().isClient() || entity.isDead() || entity.isSpectator()) {
            return;
        }

        batoru$skillCooldownsTick();
        batoru$channelingTick();
        batoru$pushedStatusEffectTick();
    }

    @Unique
    private void batoru$skillCooldownsTick() {
        if (batoru$skillCooldowns.isEmpty()) {
            return;
        }

        batoru$skillCooldowns.replaceAll((skill, cooldown) -> cooldown - 1);
        batoru$skillCooldowns.values().removeIf(cooldown -> cooldown <= 0);
    }

    @Unique
    private void batoru$channelingTick() {
        if (batoru$channeling == null) {
            return;
        }

        batoru$channeling.onTick(channelingContext(batoru$channeling));
        batoru$channelingTime++;
    }

    @Unique
    private void batoru$pushedStatusEffectTick() {
        if (!hasPushedStatusEffect()) {
            return;
        }

        var entity = (LivingEntity) (Object) this;
        entity.setVelocity(getPushedVelocity());
        entity.velocityDirty = true;
        entity.velocityModified = true;
    }

    @Unique
    private void batoru$onJobSet(RegistryEntry<Job> job) {
        job.value()
                .getListenerDispatcher()
                .getListeners(JobAssignedListener.class)
                .forEach(listener ->
                        listener.onJobAssigned(
                                job,
                                (LivingEntity) (Object) this
                        )
                );
    }

    @Unique
    private void batoru$onJobRemoved(RegistryEntry<Job> oldJob) {
        oldJob.value()
                .getListenerDispatcher()
                .getListeners(JobUnassignedListener.class)
                .forEach(listener ->
                        listener.onJobUnassigned(
                                oldJob,
                                (LivingEntity) (Object) this
                        )
                );
    }

    @Unique
    private boolean batoru$hasStatusEffect(Predicate<StatusEffect> predicate) {
        return ((LivingEntity) (Object) this).getActiveStatusEffects()
                .keySet()
                .stream()
                .map(RegistryEntry::value)
                .anyMatch(predicate);
    }

    @Unique
    private ChannelingContext channelingContext(Channeling channeling) {
        return new ChannelingContext(channeling, (LivingEntity) (Object) this, batoru$channelingTime);
    }

    @Unique
    private SkillContext skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext(skill, (LivingEntity) (Object) this);
    }
}
