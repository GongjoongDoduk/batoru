package org.gjdd.batoru.effect;

import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

class StatusEffectImpl extends StatusEffect implements PolymerStatusEffect {
    StatusEffectImpl(StatusEffectCategory category, int color) {
        super(category, color);
    }
}
