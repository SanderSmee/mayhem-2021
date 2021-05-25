package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Action;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;

public class TeamCounterEjbejbejbBehavior extends OnNextTickBehavior {
    public TeamCounterEjbejbejbBehavior(Arena arena) {
        super(arena);
    }

    @Override
    public boolean canTakeControl() {
        var anyHasEjb = this.arena.currentStatus().getYou().stream()
            .filter(hero -> !hero.equals(Heroes.getCiCdGod(arena.currentStatus().getYou())))
            .filter(Hero::isAlive)
            .anyMatch(hero -> hero.getBuffs().containsKey("ejbejbejb"));

        return
            StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
            && Heroes.getLegacyDuster(arena.currentStatus().getYou()).isAlive()
            && anyHasEjb;
    }

    @Override
    public void doAction() {
        var legacyDuster = Heroes.getLegacyDuster(arena.currentStatus().getYou());
        var ejbAffected = arena.currentStatus().getYou().stream()
            .filter(hero -> hero.getId() != legacyDuster.getId())
            .filter(hero -> hero.getBuffs().containsKey("ejbejbejb"))
            .filter(Hero::isAlive)
            .findAny();

        var dustMainframe = Skills.get("dust mainframe", legacyDuster);

        ejbAffected
            .filter(affected -> Heroes.canExecute(legacyDuster, dustMainframe))
            .ifPresent(Errors.suppress().wrap( affected -> {
                arena.nextActions.offerFirst(new Action(legacyDuster, dustMainframe, affected, false));
            }));
    }
}
