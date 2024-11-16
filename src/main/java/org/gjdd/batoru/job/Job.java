package org.gjdd.batoru.job;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.gjdd.batoru.job.event.ListenerDispatcher;
import org.gjdd.batoru.registry.BatoruRegistries;
import org.gjdd.batoru.skill.Skill;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

/**
 * Represents a job that a {@link net.minecraft.entity.LivingEntity} can have.
 */
public final class Job {
    private final Map<EquipmentSlot, Supplier<ItemStack>> equipmentMap;
    private final Map<Identifier, RegistryEntry<Skill>> skillMap;
    private final Set<Integer> disabledHotbarSlots;
    private final ListenerDispatcher listenerDispatcher;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Job(
            Map<EquipmentSlot, Supplier<ItemStack>> equipmentMap,
            Map<Identifier, RegistryEntry<Skill>> skillMap,
            Set<Integer> disabledHotbarSlots,
            ListenerDispatcher listenerDispatcher
    ) {
        this.equipmentMap = Map.copyOf(equipmentMap);
        this.skillMap = Map.copyOf(skillMap);
        this.disabledHotbarSlots = Set.copyOf(disabledHotbarSlots);
        this.listenerDispatcher = listenerDispatcher;
    }

    /**
     * Creates a new {@link Builder} instance for building a {@link Job}.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns the equipment map of this job.
     *
     * @return an immutable map of equipment slots to item stack suppliers
     */
    public Map<EquipmentSlot, Supplier<ItemStack>> getEquipmentMap() {
        return equipmentMap;
    }

    /**
     * Returns the skill map of this job.
     *
     * @return an immutable map of identifiers to skill registry entries
     */
    public Map<Identifier, RegistryEntry<Skill>> getSkillMap() {
        return skillMap;
    }

    /**
     * Returns the set of disabled hotbar slots for this job.
     *
     * @return an immutable set of hotbar slot indices
     */
    public Set<Integer> getDisabledHotbarSlots() {
        return disabledHotbarSlots;
    }

    /**
     * Returns the listener dispatcher associated with this job.
     *
     * @return the listener dispatcher
     */
    public ListenerDispatcher getListenerDispatcher() {
        return listenerDispatcher;
    }

    /**
     * Returns the translation key for this job.
     *
     * @return the translation key
     */
    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("job", BatoruRegistries.JOB.getId(this));
        }

        return translationKey;
    }

    /**
     * Returns the name of this job.
     *
     * @return the name as a {@link Text} object
     */
    public Text getName() {
        if (name == null) {
            name = Text.translatable(getTranslationKey());
        }

        return name;
    }

    /**
     * Builder class for constructing {@link Job} instances.
     */
    public static final class Builder {
        private final Map<EquipmentSlot, Supplier<ItemStack>> equipmentMap = new HashMap<>();
        private final Map<Identifier, RegistryEntry<Skill>> skillMap = new HashMap<>();
        private final Set<Integer> disabledHotbarSlots = new HashSet<>();

        private ListenerDispatcher listenerDispatcher = ListenerDispatcher.empty();

        private Builder() {
        }

        /**
         * Adds an equipment supplier for a specific equipment slot.
         *
         * @param slot     the equipment slot
         * @param supplier a supplier that provides the item stack for the slot
         * @return this builder instance
         */
        public Builder addEquipment(EquipmentSlot slot, Supplier<ItemStack> supplier) {
            this.equipmentMap.put(slot, supplier);
            return this;
        }

        /**
         * Adds multiple equipment suppliers.
         *
         * @param equipmentMap a map of equipment slots to item stack suppliers
         * @return this builder instance
         */
        public Builder addEquipments(Map<? extends EquipmentSlot, ? extends Supplier<ItemStack>> equipmentMap) {
            this.equipmentMap.putAll(equipmentMap);
            return this;
        }

        /**
         * Adds a skill to the job's skill map.
         *
         * @param id    the identifier used within this job for the skill
         * @param skill the skill registry entry
         * @return this builder instance
         */
        public Builder addSkill(Identifier id, RegistryEntry<Skill> skill) {
            this.skillMap.put(id, skill);
            return this;
        }

        /**
         * Adds multiple skills to the job's skill map.
         *
         * @param skillMap a map of identifiers to skill registry entries
         * @return this builder instance
         */
        public Builder addSkills(Map<? extends Identifier, ? extends RegistryEntry<Skill>> skillMap) {
            this.skillMap.putAll(skillMap);
            return this;
        }

        /**
         * Adds a hotbar slot to the set of disabled hotbar slots.
         *
         * @param slot the index of the hotbar slot to disable
         * @return this builder instance
         */
        public Builder addDisabledHotbarSlot(int slot) {
            this.disabledHotbarSlots.add(slot);
            return this;
        }

        /**
         * Adds multiple hotbar slots to the set of disabled hotbar slots.
         *
         * @param slots the indices of the hotbar slots to disable
         * @return this builder instance
         */
        public Builder addDisabledHotbarSlots(int... slots) {
            for (var slot : slots) {
                this.disabledHotbarSlots.add(slot);
            }

            return this;
        }

        /**
         * Sets the listener dispatcher for this job.
         *
         * @param listenerDispatcher the listener dispatcher to set
         * @return this builder instance
         */
        public Builder setListenerDispatcher(ListenerDispatcher listenerDispatcher) {
            this.listenerDispatcher = listenerDispatcher;
            return this;
        }

        /**
         * Builds the {@link Job} instance.
         *
         * @return a new Job instance
         */
        public Job build() {
            return new Job(equipmentMap, skillMap, disabledHotbarSlots, listenerDispatcher);
        }
    }
}
