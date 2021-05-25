package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;
import nl.jdriven.mayhem.util.Streams;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparingInt;
import static java.util.function.Predicate.not;
import static nl.jdriven.mayhem.domain.Heroes.getSpecificHeroIfLiving;
import static nl.jdriven.mayhem.domain.Heroes.hasBuff;

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
        return switch (skill.getAllowedTarget()) {
            case self: yield Optional.of(owner);
            case others: yield applicableHero(skill, heroes.stream().filter(hero -> hero.getId() != owner.getId()).toList());
            case all: yield applicableHero(skill, heroes);
        };
    }

    protected static Optional<Hero> applicableHero(Hero.Skill skill, List<Hero> heroes) {
        var heroesWithoutSkillInBuff = heroes.stream()
            .filter(Hero::isAlive)
            .filter(not(hero -> hasBuff(hero, skill)));

        return switch (skill.getType()) {
            case health: yield heroesWithoutSkillInBuff.filter(Heroes::needsHealing).min(comparingInt(Hero::getHealth));
            case resistance: yield heroesWithoutSkillInBuff.min(comparingInt(Hero::getResistance));
            case armor: yield heroesWithoutSkillInBuff.min(comparingInt(Hero::getArmor));
            case power: yield heroesWithoutSkillInBuff.min(comparingInt(Hero::getPower));
        };
    }

    public static Optional<Hero> applicableEnemy(Hero.Skill skill, List<Hero> enemies) {
        var enemiesWithoutSkillInBuff = enemies.stream()
            .filter(Hero::isAlive)
            .filter(not(hero -> hasBuff(hero, skill)));

        return switch (skill.getType()) {
            case health: yield getSpecificHeroIfLiving(enemiesWithoutSkillInBuff.toList());
            case resistance: yield enemiesWithoutSkillInBuff.filter(e -> e.getResistance() > 0).max(comparingInt(Hero::getResistance));
            case armor: yield enemiesWithoutSkillInBuff.filter(e -> e.getArmor() > 0).max(comparingInt(Hero::getArmor));
            case power: yield enemiesWithoutSkillInBuff.filter(e -> e.getPower() > 0).max(comparingInt(Hero::getPower));
        };
    }
}
