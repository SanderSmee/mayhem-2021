package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.OnNextTickBehavior;

public class TeamCounterMulticloudBehavior extends OnNextTickBehavior {
    public TeamCounterMulticloudBehavior(Arena arena) {
        super(arena);
    }

    @Override
    public boolean canTakeControl() {
        var anyHasMulticloud = this.arena.currentStatus().getYou().stream()
            .filter(Hero::isAlive)
            .anyMatch(hero -> hero.getBuffs().containsKey("multicloud"));

        return
            StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
            && Heroes.getCiCdGod(arena.currentStatus().getYou()).isAlive()
            && anyHasMulticloud;
    }

    @Override
    public void doAction() {
        var multicloudAffected = arena.currentStatus().getYou().stream()
            .filter(Hero::isAlive)
            .filter(hero -> hero.getBuffs().containsKey("multicloud"))
            .findFirst();

        var ciCdGod = Heroes.getCiCdGod(arena.currentStatus().getYou());
        var infraascode = Skills.get("infraascode", ciCdGod);

        multicloudAffected
            .filter(affected -> Heroes.canExecute(ciCdGod, infraascode))
            .ifPresent(Errors.suppress().wrap( affected -> {
                arena.nextActions.offerFirst(new ActionMessage(ciCdGod.getId(), infraascode.getId(), affected.getId(), false));
            }));
    }
}
