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

    public static Builder builder() {
        return new Builder();
    }

    public Map<EquipmentSlot, Supplier<ItemStack>> getEquipmentMap() {
        return equipmentMap;
    }

    public Map<Identifier, RegistryEntry<Skill>> getSkillMap() {
        return skillMap;
    }

    public Set<Integer> getDisabledHotbarSlots() {
        return disabledHotbarSlots;
    }

    public ListenerDispatcher getListenerDispatcher() {
        return listenerDispatcher;
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("job", BatoruRegistries.JOB.getId(this));
        }

        return translationKey;
    }

    public Text getName() {
        if (name == null) {
            name = Text.translatable(getTranslationKey());
        }

        return name;
    }

    public static final class Builder {
        private final Map<EquipmentSlot, Supplier<ItemStack>> equipmentMap = new HashMap<>();
        private final Map<Identifier, RegistryEntry<Skill>> skillMap = new HashMap<>();
        private final Set<Integer> disabledHotbarSlots = new HashSet<>();

        private ListenerDispatcher listenerDispatcher = ListenerDispatcher.empty();

        private Builder() {
        }

        public Builder addEquipment(EquipmentSlot slot, Supplier<ItemStack> supplier) {
            this.equipmentMap.put(slot, supplier);
            return this;
        }

        public Builder addEquipments(Map<? extends EquipmentSlot, ? extends Supplier<ItemStack>> equipmentMap) {
            this.equipmentMap.putAll(equipmentMap);
            return this;
        }

        public Builder addSkill(Identifier id, RegistryEntry<Skill> skill) {
            this.skillMap.put(id, skill);
            return this;
        }

        public Builder addSkills(Map<? extends Identifier, ? extends RegistryEntry<Skill>> skillMap) {
            this.skillMap.putAll(skillMap);
            return this;
        }

        public Builder addDisabledHotbarSlot(int slot) {
            this.disabledHotbarSlots.add(slot);
            return this;
        }

        public Builder addDisabledHotbarSlots(int... slots) {
            for (var slot : slots) {
                this.disabledHotbarSlots.add(slot);
            }

            return this;
        }

        public Builder setListenerDispatcher(ListenerDispatcher listenerDispatcher) {
            this.listenerDispatcher = listenerDispatcher;
            return this;
        }

        public Job build() {
            return new Job(equipmentMap, skillMap, disabledHotbarSlots, listenerDispatcher);
        }
    }
}
