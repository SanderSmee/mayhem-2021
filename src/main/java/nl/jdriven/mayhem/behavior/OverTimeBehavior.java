package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;

import java.util.Comparator;
import java.util.Optional;

public class OverTimeBehavior extends OnNextTickBehavior {

    public OverTimeBehavior(Arena arena) {
        super(arena);
    }

    @Override
    public boolean canTakeControl() {
        return
            StatusMessage.FightStatus.overtime == arena.currentStatus().getStatus()
            && arena.currentStatus().getYou().stream().anyMatch(Hero::isAlive);
    }

    @Override
    public void doAction() {
        arena.nextActions.clear();

        var enemies = Heroes.living(arena.currentStatus().getOpponent());
        var heroes = Heroes.living(arena.currentStatus().getYou());

        arena.currentStatus().getYou().stream()
            .filter(Hero::isAlive)
            .forEach(hero -> {
                final Hero.Skill skill;
                final Optional<Hero> target;

                switch (hero.getName()) {
                    case "CI/CD god" -> {
                        skill = Skills.get("yamlize", hero);
                        target = enemies.stream().filter(Hero::isAlive).max(Comparator.comparingInt(Hero::getHealth));
                    }
                    case "Legacy Duster" -> {
                        skill = Skills.get("PL/SQL Hell", hero);
                        target = enemies.stream().filter(Hero::isAlive).max(Comparator.comparingInt(Hero::getHealth));
                    }
                    case "JHipster" -> {
                        skill = Skills.get("yogaclass", hero);
                        target = heroes.stream().filter(Hero::isAlive).max(Comparator.comparingInt(Hero::getHealth));
                    }
                    default -> {
                        skill = null;
                        target = Optional.empty();
                    }
                }

                target.ifPresent(t -> {
                    if (Heroes.canExecute(t, skill))
                    Errors.suppress().getWithDefault(() -> arena.nextActions.offerFirst(new ActionMessage(hero.getId(), skill.getId(), t.getId(), true)), false);
                });
            });
    }
}
