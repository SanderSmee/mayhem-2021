package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;
import nl.jdriven.mayhem.util.Streams;

import java.util.List;

public final class Heroes {

    public static Hero getHero(String name, List<Hero> heroes) {
        return heroes.stream()
            .filter(hero -> Heroes.is(hero, name))
            .collect(Streams.onlyOne());
    }

    public static Hero getJHipster(List<Hero> heroes) {
        return getHero("JHipster", heroes);
    }

    public static Hero getCiCdGod(List<Hero> heroes) {
        return getHero("CI/CD god", heroes);
    }

    public static Hero getLegacyDuster(List<Hero> heroes) {
        return getHero("Legacy Duster", heroes);
    }

    public static Hero getSpecificAliveHero(List<Hero> heroes, Hero.Skill skill) {
        var jhipster = getJHipster(heroes);
        var ciCdGod = getCiCdGod(heroes);
        var legacyDuster = getLegacyDuster(heroes);

        return jhipster.isAlive() && !hasBuff(jhipster, skill)
            ? jhipster
            : (ciCdGod.isAlive() && !hasBuff(ciCdGod, skill)
                ? ciCdGod
                : legacyDuster.isAlive() && !hasBuff(legacyDuster, skill)
                    ? legacyDuster
                    : null);
    }

    public static boolean canExecute(Hero hero, Hero.Skill theSkill) {
        return
            !hero.isBusy()
            && !hero.getCooldowns().containsKey(theSkill.getId())
            && (theSkill.getPower() > 0 || -theSkill.getPower() < hero.getPower());
    }

    public static List<Hero> living(List<Hero> heroes) {
        return heroes.stream().filter(Hero::isAlive).toList();
    }

    public static boolean is(Hero hero, String name) {
        return name.equals(hero.getName());
    }

    public static boolean hasBuff(Hero hero, Hero.Skill skill) {
        return hero.getBuffs().containsKey(skill.getName());
    }
}
