package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;

public final class Action {
    Hero owner;
    Hero.Skill skill;
    Hero target;
    boolean override;

    public Action(Hero owner, Hero.Skill skill, Hero target) {
        this(owner, skill, target, false);
    }

    public Action(Hero owner, Hero.Skill skill, Hero target, boolean override) {
        this.owner = owner;
        this.skill = skill;
        this.target = target;
        this.override = override;
    }

    public Hero getOwner() {
        return owner;
    }

    public Hero.Skill getSkill() {
        return skill;
    }

    public Hero getTarget() {
        return target;
    }

    public boolean isOverride() {
        return override;
    }

    public ActionMessage asActionMessage() {
        return new ActionMessage(owner.getId(), skill.getId(),target.getId(), override);
    }
}
