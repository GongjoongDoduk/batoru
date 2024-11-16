package org.gjdd.batoru.mixin;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import org.gjdd.batoru.internal.StatusEffectExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = StatusEffect.class)
public abstract class StatusEffectMixin implements StatusEffectExtensions {
    @Unique
    private boolean batoru$isPushed = false;
    @Unique
    private boolean batoru$isSilenced = false;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean isPushed() {
        return batoru$isPushed;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean isSilenced() {
        return batoru$isSilenced;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public StatusEffect setPushed(boolean pushed) {
        batoru$isPushed = pushed;
        return (StatusEffect) (Object) this;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public StatusEffect setSilenced(boolean silenced) {
        batoru$isSilenced = silenced;
        return (StatusEffect) (Object) this;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public StatusEffect addDisarmedAttributeModifiers() {
        return ((StatusEffect) (Object) this)
                .addAttributeModifier(
                        EntityAttributes.ATTACK_DAMAGE,
                        Identifier.of("batoru", "disarmed"),
                        -16.0,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public StatusEffect addRootedAttributeModifiers() {
        return ((StatusEffect) (Object) this)
                .addAttributeModifier(
                        EntityAttributes.MOVEMENT_SPEED,
                        Identifier.of("batoru", "rooted"),
                        -16.0,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                )
                .addAttributeModifier(
                        EntityAttributes.JUMP_STRENGTH,
                        Identifier.of("batoru", "rooted"),
                        -16.0,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
    }
}
