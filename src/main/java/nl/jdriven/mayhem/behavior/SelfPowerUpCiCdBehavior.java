package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.Behavior;

public class SelfPowerUpCiCdBehavior implements Behavior {
    private final Arena arena;
    private long lastTick = 0L;
    private boolean suppressed;

    public SelfPowerUpCiCdBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        var ciCdGod = Heroes.getCiCdGod(arena.currentStatus().getYou());

        return StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
            && arena.currentStatus().getTimestamp().getTime() > lastTick
            && ciCdGod.isAlive()
            && ciCdGod.getPower() < 50;

    }

    @Override
    public void action() {
        this.lastTick = arena.currentStatus().getTimestamp().getTime();

        var ciCdGod = Heroes.getCiCdGod(arena.currentStatus().getYou());
        var greenfield = Skills.get("greenfield", ciCdGod.getSkills());


        if (ciCdGod.isAlive() && Heroes.canExecute(ciCdGod, greenfield)) {
            Errors.suppress().getWithDefault(() -> arena.nextActions.offerFirst(new ActionMessage(ciCdGod.getId(), greenfield.getId(), ciCdGod.getId(), true)), false);
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
