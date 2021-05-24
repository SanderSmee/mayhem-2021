package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;
import nl.jdriven.mayhem.util.Randoms;
import nl.jdriven.mayhem.util.Streams;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    public static Hero.Skill get(String name, Hero hero) {
        return get(name, hero.getSkills());
    }

    public static Optional<Hero> applicableHero(Hero.Skill skill, Hero owner, List<Hero> heroes) {
        return Hero.Skill.AllowedTarget.self == skill.getAllowedTarget()
            ? Optional.of(owner)
            : applicableHero(skill, heroes);
    }

    protected static Optional<Hero> applicableHero(Hero.Skill skill, List<Hero> heroes) {
        return switch (skill.getType()) {
            case health: yield Optional.ofNullable(Heroes.getSpecificAliveHero(heroes, skill));
            case resistance: yield heroes.stream().filter(Hero::isAlive).min(Comparator.comparingInt(Hero::getResistance));
            case armor: yield heroes.stream().filter(Hero::isAlive).min(Comparator.comparingInt(Hero::getArmor));
            case power: yield heroes.stream().filter(Hero::isAlive).min(Comparator.comparingInt(Hero::getPower));
        };
    }

    public static Optional<Hero> applicableEnemy(Hero.Skill skill, List<Hero> enemies) {
        return switch (skill.getType()) {
            case health: yield Optional.ofNullable(Heroes.getSpecificAliveHero(enemies, skill));
            case resistance: yield enemies.stream().filter(e -> e.isAlive() && e.getResistance() > 0).max(Comparator.comparingInt(Hero::getResistance));
            case armor: yield enemies.stream().filter(e -> e.isAlive() && e.getArmor() > 0).max(Comparator.comparingInt(Hero::getArmor));
            case power: yield enemies.stream().filter(e -> e.isAlive() && e.getPower() > 0).max(Comparator.comparingInt(Hero::getPower));
        };
    }
}
