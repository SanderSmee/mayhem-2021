package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;
import nl.jdriven.mayhem.util.Streams;

import java.util.List;
import java.util.Optional;

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

    /**
     * Get living hero in order JHipster > CiCd God > Legacy Duster.
     * @param heroes
     * @return
     */
    public static Optional<Hero> getSpecificHeroIfLiving(List<Hero> heroes) {
        var jhipster = getJHipster(heroes);
        var ciCdGod = getCiCdGod(heroes);
        var legacyDuster = getLegacyDuster(heroes);

        return jhipster.isAlive()
            ? Optional.of(jhipster)
            : ciCdGod.isAlive()
                ? Optional.of(ciCdGod)
                : legacyDuster.isAlive()
                    ? Optional.of(legacyDuster)
                    : Optional.empty();
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

    public static boolean needsHealing(Hero hero) {
        return
            hero.isAlive()
            && hero.getHealth() < hero.getMaxHealth()
            && (float) hero.getHealth() / hero.getMaxHealth() < .81f;
    }
}
