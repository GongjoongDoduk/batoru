package org.gjdd.batoru.skill.condition;

import org.gjdd.batoru.skill.SkillContext;
import org.gjdd.batoru.skill.SkillResult;

public enum GenericSkillCondition implements SkillCondition {
    INSTANCE;

    @Override
    public SkillResult canUse(SkillContext context) {
        if (context.source().isSpectator() || context.source().isDead()) {
            return SkillResult.unavailable();
        }

        if (context.source().hasSkillCooldown(context.skill())) {
            return SkillResult.cooldown();
        }

        if (context.source().isChanneling()) {
            return SkillResult.channeling();
        }

        if (context.source().hasSilencedStatusEffect()) {
            return SkillResult.silenced();
        }

        return SkillResult.success();
    }
}
