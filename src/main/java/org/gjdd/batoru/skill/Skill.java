package org.gjdd.batoru.skill;

import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.gjdd.batoru.registry.BatoruRegistries;
import org.gjdd.batoru.skill.action.EmptySkillAction;
import org.gjdd.batoru.skill.action.SkillAction;
import org.gjdd.batoru.skill.condition.GenericSkillCondition;
import org.gjdd.batoru.skill.condition.SkillCondition;
import org.jetbrains.annotations.Nullable;

public final class Skill {
    private final SkillCondition condition;
    private final SkillAction action;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Skill(SkillCondition condition, SkillAction action) {
        this.condition = condition;
        this.action = action;
    }

    public static Builder builder() {
        return new Builder();
    }

    public SkillCondition getCondition() {
        return condition;
    }

    public SkillAction getAction() {
        return action;
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("skill", BatoruRegistries.SKILL.getId(this));
        }

        return translationKey;
    }

    public Text getName() {
        if (name == null) {
            name = Text.translatable(getTranslationKey());
        }

        return name;
    }

    public static final class Builder {
        private SkillCondition condition = GenericSkillCondition.INSTANCE;
        private SkillAction action = EmptySkillAction.INSTANCE;

        private Builder() {
        }

        public SkillCondition getCondition() {
            return condition;
        }

        public Builder setCondition(SkillCondition condition) {
            this.condition = condition;
            return this;
        }

        public SkillAction getAction() {
            return action;
        }

        public Builder setAction(SkillAction action) {
            this.action = action;
            return this;
        }

        public Skill build() {
            return new Skill(condition, action);
        }
    }
}
