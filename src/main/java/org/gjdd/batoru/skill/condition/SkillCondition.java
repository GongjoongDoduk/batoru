package org.gjdd.batoru.skill.condition;

import org.gjdd.batoru.skill.SkillContext;
import org.gjdd.batoru.skill.SkillResult;

public interface SkillCondition {
    SkillResult canUse(SkillContext context);

    default SkillCondition and(SkillCondition other) {
        return context -> canUse(context) instanceof SkillResult.Failure failure ?
                failure :
                other.canUse(context);
    }

    default SkillCondition or(SkillCondition other) {
        return context -> canUse(context) instanceof SkillResult.Success success ?
                success :
                other.canUse(context);
    }
}
