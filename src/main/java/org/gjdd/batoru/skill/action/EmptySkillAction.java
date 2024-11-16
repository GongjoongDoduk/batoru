package org.gjdd.batoru.skill.action;

import org.gjdd.batoru.skill.SkillContext;
import org.gjdd.batoru.skill.SkillResult;

public enum EmptySkillAction implements SkillAction {
    INSTANCE;

    @Override
    public SkillResult use(SkillContext context) {
        return SkillResult.success();
    }
}
