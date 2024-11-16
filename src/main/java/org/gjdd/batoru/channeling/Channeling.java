package org.gjdd.batoru.channeling;

import java.util.function.Predicate;

public interface Channeling {
    void onTick(ChannelingContext context);

    default Channeling stopIfPushed() {
        return stopIf(context -> context.source().hasPushedStatusEffect());
    }

    default Channeling stopIfSilenced() {
        return stopIf(context -> context.source().hasSilencedStatusEffect());
    }

    default Channeling stopIf(Predicate<ChannelingContext> predicate) {
        return context -> {
            if (predicate.test(context)) {
                context.source().stopChanneling();
            } else {
                onTick(context);
            }
        };
    }
}
