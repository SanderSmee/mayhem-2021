package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;
import nl.jdriven.mayhem.util.Randoms;
import nl.jdriven.mayhem.util.Streams;

import java.util.List;
import java.util.stream.Collectors;

public final class Skills {
    public static boolean isPositive(Hero.Skill skill) {
        return
            skill.getEffect() > 0;
    }

    public static boolean isNegative(Hero.Skill skill) {
        return !isPositive(skill);
    }

    public static boolean isBuff(Hero.Skill skill) {
        return skill.getDuration() > 0;
    }

    public static boolean isNegativeBuff(Hero.Skill skill) {
        return isBuff(skill) && isNegative(skill);
    }

    public static boolean isPositiveBuff(Hero.Skill skill) {
        return isBuff(skill) && isPositive(skill);
    }

    public static Hero.Skill get(String name, List<Hero.Skill> skills) {
        return skills.stream()
            .filter(skill -> name.equals(skill.getName()))
            .collect(Streams.onlyOne());

    }

    public static Hero pickTarget(Hero.Skill skill, Hero owner, List<Hero> heroes) {
        return Hero.Skill.AllowedTarget.self == skill.getAllowedTarget()
            ? owner
            : Randoms.randomFrom(heroes.stream()
                .filter(hero -> hero.getId() != owner.getId())
                .collect(Collectors.toList())
              );
    }
}
