package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.OnNextTickBehavior;
import nl.jdriven.mayhem.util.Randoms;

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

        arena.currentStatus().getYou().stream()
            .filter(Hero::isAlive)
            .forEach(hero -> {
                final Hero.Skill skill;
                final Hero target;

                switch (hero.getName()) {
                    case "CI/CD god" -> {
                        skill = Skills.get("yamlize", hero);
                        target = Randoms.randomFrom(enemies);
                    }
                    case "Legacy Duster" -> {
                        skill = Skills.get("PL/SQL Hell", hero);
                        target = Randoms.randomFrom(enemies);
                    }
                    case "JHipster" -> {
                        skill = Skills.get("yogaclass", hero);
                        target = Randoms.randomFrom(Heroes.living(arena.currentStatus().getYou()));
                    }
                    default -> {
                        skill = null;
                        target = null;
                    }
                }

                if (skill != null && target != null && Heroes.canExecute(hero, skill)) {
                    Errors.suppress().getWithDefault(() -> arena.nextActions.offerFirst(new ActionMessage(hero.getId(), skill.getId(), target.getId(), true)), false);
                }
            });
    }
}
