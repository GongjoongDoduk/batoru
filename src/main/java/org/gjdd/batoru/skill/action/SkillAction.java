package org.gjdd.batoru.skill.action;

import org.gjdd.batoru.skill.SkillContext;
import org.gjdd.batoru.skill.SkillResult;

import java.util.function.Consumer;

public interface SkillAction {
    SkillResult use(SkillContext context);

    default SkillAction onSuccess(Consumer<SkillContext> consumer) {
        return context -> {
            var skillResult = use(context);
            if (skillResult instanceof SkillResult.Success) {
                consumer.accept(context);
            }

            return skillResult;
        };
    }

    default SkillAction onFailure(Consumer<SkillContext> consumer) {
        return context -> {
            var skillResult = use(context);
            if (skillResult instanceof SkillResult.Failure) {
                consumer.accept(context);
            }

            return skillResult;
        };
    }
}
