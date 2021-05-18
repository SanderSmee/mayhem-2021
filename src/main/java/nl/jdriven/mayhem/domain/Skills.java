package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;

public final class Skills {
    public static boolean isPositive(Hero.Skill skill) {
        return
            skill.getEffect() > 0;
    }

    public static boolean isBuff(Hero.Skill skill) {
        return skill.getDuration() > 0;
    }
}
