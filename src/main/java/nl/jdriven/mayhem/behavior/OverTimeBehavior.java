package nl.jdriven.mayhem.behavior;

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
                if (Heroes.is(hero, "CI/CD god")) {
                    var skill = Skills.get("yamlize", hero);
                    var canDo = Heroes.canExecute(hero, skill);

                    if (canDo) { arena.nextActions.offerFirst(new ActionMessage(hero.getId(), skill.getId(), Randoms.randomFrom(enemies).getId(), true)); }
                }
                if (Heroes.is(hero, "Legacy Duster")) {
                    var skill = Skills.get("PL/SQL Hell", hero);
                    var canDo = Heroes.canExecute(hero, skill);

                    if (canDo) { arena.nextActions.offerFirst(new ActionMessage(hero.getId(), skill.getId(), Randoms.randomFrom(enemies).getId(), true)); }
                }
                if (Heroes.is(hero, "JHipster")) {
                    var skill = Skills.get("yogaclass", hero);
                    var canDo = Heroes.canExecute(hero, skill);

                    if (canDo) { arena.nextActions.offerFirst(new ActionMessage(hero.getId(), skill.getId(), Randoms.randomFrom(Heroes.living(arena.currentStatus().getYou())).getId(), true)); }
                }
            });
    }
}
