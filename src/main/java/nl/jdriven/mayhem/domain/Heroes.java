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
}
