package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Action;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.Behavior;

public class SelfRebootLegacyDusterBehavior implements Behavior {
    private final Arena arena;
    private long lastTick = 0L;
    private boolean suppressed;

    public SelfRebootLegacyDusterBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        var legacyDuster = Heroes.getLegacyDuster(arena.currentStatus().getYou());
        return StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
            && arena.currentStatus().getTimestamp().getTime() > lastTick
            && legacyDuster.isAlive()
            && legacyDuster.getHealth() < 300;

    }

    @Override
    public void action() {
        this.lastTick = arena.currentStatus().getTimestamp().getTime();

        var legacyDuster = Heroes.getLegacyDuster(arena.currentStatus().getYou());
        var reboot = Skills.get("reboot", legacyDuster.getSkills());

        if (legacyDuster.isAlive() && Heroes.canExecute(legacyDuster, reboot)) {
            Errors.suppress().getWithDefault(() -> arena.nextActions.offerFirst(new Action(legacyDuster, reboot, legacyDuster, true)), false);
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
