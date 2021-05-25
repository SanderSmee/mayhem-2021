package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.Behavior;
import nl.jdriven.mayhem.util.Randoms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class RandomBehavior implements Behavior {
    private final Logger logger = LoggerFactory.getLogger(RandomBehavior.class);

    private final Arena arena;
    private long lastTick = 0L;
    private boolean suppressed;

    public RandomBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        return
            StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
                && arena.currentStatus().getTimestamp().getTime() > lastTick
                && arena.currentStatus().getYou().stream().anyMatch(Hero::isAlive)
                && arena.nextActions.isEmpty()
            ;
    }

    @Override
    public void action() {
        this.lastTick = arena.currentStatus().getTimestamp().getTime();

        var heroes = arena.currentStatus().getYou();
        var enemies = arena.currentStatus().getOpponent();

        heroes.stream()
            .filter(Hero::isAlive)
            .filter(Predicate.not(Hero::isBusy))
            .map(hero -> {
                var applicableSkills = hero.getSkills().stream()
                    .filter(skill -> Heroes.canExecute(hero, skill))
                    .toList();
                var skill = Randoms.randomFrom(applicableSkills);

                if (skill == null) {
                    return null;
                }

                var target = Skills.isNegative(skill)
                    ? Skills.applicableEnemy(skill, enemies)
                    : Skills.applicableHero(skill, hero, heroes);

                return target
                    .map(t -> new ActionMessage(hero.getId(), skill.getId(), t.getId(), false))
                    .orElse(null);
            })
            .forEach(Errors.suppress().wrap(arena.nextActions::offerLast));
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
