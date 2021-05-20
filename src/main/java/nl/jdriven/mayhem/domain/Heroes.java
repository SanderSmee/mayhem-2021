package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.Hero;
import nl.jdriven.mayhem.util.Streams;

import java.util.List;

public final class Heroes {

    public static Hero getHero(String name, List<Hero> heroes) {
        return heroes.stream()
            .filter(hero -> name.equals(hero.getName()))
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

        return jhipster.isAlive() && !jhipster.getBuffs().containsKey(skill.getName())
            ? jhipster
            : (ciCdGod.isAlive() && !ciCdGod.getBuffs().containsKey(skill.getName())
                ? ciCdGod
                : legacyDuster.isAlive() && !legacyDuster.getBuffs().containsKey(skill.getName())
                    ? legacyDuster
                    : null);
    }
}
