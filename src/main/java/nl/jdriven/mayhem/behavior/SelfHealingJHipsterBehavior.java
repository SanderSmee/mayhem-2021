package nl.jdriven.mayhem.behavior;

import com.diffplug.common.base.Errors;
import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.domain.Action;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.domain.Heroes;
import nl.jdriven.mayhem.domain.Skills;
import nl.jdriven.mayhem.subsumption.Behavior;

public class SelfHealingJHipsterBehavior implements Behavior {
    private final Arena arena;
    private long lastTick = 0L;
    private boolean suppressed;

    public SelfHealingJHipsterBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        var myHipster = Heroes.getJHipster(arena.currentStatus().getYou());

        return StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
            && arena.currentStatus().getTimestamp().getTime() > lastTick
            && myHipster.isAlive()
            && myHipster.getHealth() < 50;

    }

    @Override
    public void action() {
        this.lastTick = arena.currentStatus().getTimestamp().getTime();

        var jhipster = Heroes.getJHipster(arena.currentStatus().getYou());
        var meditation = Skills.get("meditation", jhipster.getSkills());

        if (jhipster.isAlive() && Heroes.canExecute(jhipster, meditation)) {
            Errors.suppress().getWithDefault(() -> arena.nextActions.offerFirst(new Action(jhipster, meditation, jhipster, true)), false);
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
