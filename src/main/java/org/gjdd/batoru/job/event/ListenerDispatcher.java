package org.gjdd.batoru.job.event;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.job.event.entity.JobAssignedListener;
import org.gjdd.batoru.job.event.entity.player.UpdateSelectedSlotListener;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Dispatches event listeners associated with a job.
 */
public final class ListenerDispatcher {
    private final List<? extends Listener> listeners;
    private final Map<Class<? extends Listener>, List<? extends Listener>> cache = new HashMap<>();

    private ListenerDispatcher(Collection<? extends Listener> listeners) {
        this.listeners = List.copyOf(listeners);
    }

    /**
     * Returns an empty {@link ListenerDispatcher} with no listeners.
     *
     * @return an empty ListenerDispatcher instance
     */
    public static ListenerDispatcher empty() {
        return new ListenerDispatcher(List.of());
    }

    /**
     * Creates a new {@link Builder} instance for building a {@link ListenerDispatcher}.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns all the listeners in this dispatcher.
     *
     * @return an immutable list of listeners
     */
    public List<? extends Listener> getAllListeners() {
        return listeners;
    }

    /**
     * Retrieves listeners of a specific type.
     *
     * @param listenerClass the class of the listener type
     * @param <T>           the type of the listener
     * @return an immutable list of listeners of the specified type
     */
    @SuppressWarnings("unchecked")
    public <T extends Listener> List<T> getListeners(Class<T> listenerClass) {
        return (List<T>) cache.computeIfAbsent(
                listenerClass,
                key -> listeners.stream()
                        .filter(key::isInstance)
                        .map(key::cast)
                        .toList()
        );
    }

    /**
     * Builder class for constructing {@link ListenerDispatcher} instances.
     */
    public static final class Builder {
        private final List<Listener> listeners = new ArrayList<>();

        /**
         * Adds a listener to the dispatcher.
         *
         * @param listener the listener to add
         * @return this builder instance
         */
        public Builder addListener(Listener listener) {
            this.listeners.add(listener);
            return this;
        }

        /**
         * Adds multiple listeners to the dispatcher.
         *
         * @param listeners a collection of listeners to add
         * @return this builder instance
         */
        public Builder addListeners(Collection<? extends Listener> listeners) {
            this.listeners.addAll(listeners);
            return this;
        }

        /**
         * Assigns a skill to be activated when a player selects a specific hotbar slot,
         * if the given condition is met.
         *
         * @param slot      the hotbar slot index
         * @param predicate a condition that takes the job and player as input
         * @param id        the identifier of the skill in the job's skill map
         * @return this builder instance
         */
        public Builder assignSkillToHotbarSlot(
                int slot,
                BiPredicate<RegistryEntry<Job>, ServerPlayerEntity> predicate,
                Identifier id
        ) {
            return addListener((UpdateSelectedSlotListener) (job, player, selectedSlot) -> {
                if (slot != selectedSlot) {
                    return false;
                }

                if (predicate.test(job, player)) {
                    var skill = job.value().getSkillMap().get(id);
                    if (skill != null) {
                        player.useSkill(skill);
                    }
                }

                return true;
            });
        }

        /**
         * Assigns a skill to be activated when a player selects a specific hotbar slot,
         * if the item in the player's main hand satisfies the given predicate.
         *
         * @param slot      the hotbar slot index
         * @param predicate a condition that tests the item stack in the player's main hand
         * @param id        the identifier of the skill in the job's skill map
         * @return this builder instance
         */
        public Builder assignSkillToHotbarSlot(int slot, Predicate<ItemStack> predicate, Identifier id) {
            return assignSkillToHotbarSlot(slot, (job, player) -> predicate.test(player.getMainHandStack()), id);
        }

        /**
         * Assigns a skill to be activated when a player selects a specific hotbar slot,
         * if the player is holding the specified item in their main hand.
         *
         * @param slot the hotbar slot index
         * @param item the item that must be held in the player's main hand
         * @param id   the identifier of the skill in the job's skill map
         * @return this builder instance
         */
        public Builder assignSkillToHotbarSlot(int slot, Item item, Identifier id) {
            return assignSkillToHotbarSlot(slot, itemStack -> itemStack.isOf(item), id);
        }

        /**
         * Adds a listener that equips the job's equipment when the job is assigned.
         * Existing equipment in the relevant slots will be dropped.
         *
         * @return this builder instance
         */
        public Builder enableAutoEquip() {
            return addListener((JobAssignedListener) (job, entity) -> {
                if (!(entity.getWorld() instanceof ServerWorld world)) {
                    return;
                }

                job.value()
                        .getEquipmentMap()
                        .forEach((slot, supplier) -> {
                            entity.dropStack(world, entity.getEquippedStack(slot));
                            entity.equipStack(slot, supplier.get());
                        });
            });
        }

        /**
         * Builds the {@link ListenerDispatcher} instance.
         *
         * @return a new ListenerDispatcher instance
         */
        public ListenerDispatcher build() {
            return new ListenerDispatcher(listeners);
        }
    }
}
