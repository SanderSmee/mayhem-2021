package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;
import nl.jdriven.mayhem.util.Streams;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

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
            case health: yield Optional.ofNullable(Heroes.getSpecificAliveHero(heroes.stream().filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).collect(toList()), skill));
            case resistance: yield heroes.stream().filter(Hero::isAlive).filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).min(comparingInt(Hero::getResistance));
            case armor: yield heroes.stream().filter(Hero::isAlive).filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).min(comparingInt(Hero::getArmor));
            case power: yield heroes.stream().filter(Hero::isAlive).filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).min(comparingInt(Hero::getPower));
        };
    }

    public static Optional<Hero> applicableEnemy(Hero.Skill skill, List<Hero> enemies) {
        return switch (skill.getType()) {
            case health: yield Optional.ofNullable(Heroes.getSpecificAliveHero(enemies.stream().filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).collect(toList()), skill));
            case resistance: yield enemies.stream().filter(e -> e.isAlive() && e.getResistance() > 0).filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).max(comparingInt(Hero::getResistance));
            case armor: yield enemies.stream().filter(e -> e.isAlive() && e.getArmor() > 0).filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).max(comparingInt(Hero::getArmor));
            case power: yield enemies.stream().filter(e -> e.isAlive() && e.getPower() > 0).filter(hero -> Heroes.doesNotHaveBuff(hero, skill)).max(comparingInt(Hero::getPower));
        };
    }
}
