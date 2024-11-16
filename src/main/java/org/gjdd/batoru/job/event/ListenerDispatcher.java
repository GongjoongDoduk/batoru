package org.gjdd.batoru.job.event;

import java.util.*;

public final class ListenerDispatcher {
    private final List<? extends Listener> listeners;
    private final Map<Class<? extends Listener>, List<? extends Listener>> cache = new HashMap<>();

    private ListenerDispatcher(Collection<? extends Listener> listeners) {
        this.listeners = List.copyOf(listeners);
    }

    public static ListenerDispatcher empty() {
        return new ListenerDispatcher(List.of());
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<? extends Listener> getAllListeners() {
        return listeners;
    }

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

    public static final class Builder {
        private final List<Listener> listeners = new ArrayList<>();

        public Builder addListener(Listener listener) {
            this.listeners.add(listener);
            return this;
        }

        public Builder addListeners(Collection<? extends Listener> listeners) {
            this.listeners.addAll(listeners);
            return this;
        }

        public ListenerDispatcher build() {
            return new ListenerDispatcher(listeners);
        }
    }
}
