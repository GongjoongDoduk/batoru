package org.gjdd.batoru.internal;

import net.minecraft.entity.effect.StatusEffect;
import org.jetbrains.annotations.ApiStatus;

public interface StatusEffectExtensions {
    default boolean isPushed() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default boolean isSilenced() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default StatusEffect setPushed(boolean pushed) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default StatusEffect setSilenced(boolean silenced) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @ApiStatus.Experimental
    default StatusEffect addDisarmedAttributeModifiers() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @ApiStatus.Experimental
    default StatusEffect addRootedAttributeModifiers() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}
