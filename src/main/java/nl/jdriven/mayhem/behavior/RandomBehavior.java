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
import java.util.stream.Collectors;

public class RandomBehavior implements Behavior {
    private final Logger logger = LoggerFactory.getLogger(RandomBehavior.class);

    private final Arena arena;
    private boolean suppressed;
    private long lastTick = 0L;

    public RandomBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        return
            StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
                && arena.currentStatus().getTimestamp().getTime() > lastTick
                && arena.currentStatus().getYou().stream().anyMatch(Hero::isAlive)
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
                var coolingDown = hero.getCooldowns().keySet();
                var applicableSkills = hero.getSkills().stream()
                    .filter(skill -> !coolingDown.contains(skill.getId()))
                    .filter(skill -> -skill.getPower() < hero.getPower())
                    .collect(Collectors.toList());
                var skill = Randoms.randomFrom(applicableSkills);

                if (skill == null) {
                    return null;
                }

                Hero target = Skills.isNegative(skill)
                    ? Heroes.getSpecificAliveHero(enemies, skill)
                    : Skills.pickTarget(skill, hero, heroes);

                if (target == null) {
                    return null;
                }

                return new ActionMessage(hero.getId(), skill.getId(), target.getId(), false);
            })
            .forEach(Errors.suppress().wrap(arena.nextActions::offer));
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
