package nl.jdriven.mayhem.behavior;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.subsumption.Behavior;
import nl.jdriven.mayhem.util.Randoms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class RandomBehavior implements Behavior {
    private final Logger logger = LoggerFactory.getLogger(RandomBehavior.class);

    private final Arena arena;
    private boolean suppressed;

    private int[] opponentHeroIndexes = new int[]{3, 4, 5};

    public RandomBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        return StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus();
    }

    @Override
    public void action() {
        logger.info("Selecting random action(s)");
        var heroes = arena.currentStatus().getYou();

        heroes.stream()
            .filter(Hero::isAlive)
            .filter(Predicate.not(Hero::isBusy))
            .map(hero -> {
                var skill = Randoms.randomFrom(hero.getSkills());
                var target = Hero.Skill.AllowedTarget.self == skill.getAllowedTarget() ? hero.getId() : Randoms.randomFrom(opponentHeroIndexes);
                return new ActionMessage(hero.getId(), skill.getId(), target, false);
            })
            .forEach(arena.nextActions::add);
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
