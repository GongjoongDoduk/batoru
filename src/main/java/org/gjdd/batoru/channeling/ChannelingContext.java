package org.gjdd.batoru.channeling;

import net.minecraft.entity.LivingEntity;

public record ChannelingContext(Channeling channeling, LivingEntity source, int time) {
}
