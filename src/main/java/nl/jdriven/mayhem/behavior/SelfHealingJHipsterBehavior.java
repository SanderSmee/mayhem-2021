package nl.jdriven.mayhem.behavior;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.StatusMessage;
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

        if (jhipster.isAlive() && jhipster.getCooldowns().containsKey(meditation.getId())) {
            arena.nextActions.offerFirst(new ActionMessage(jhipster.getId(), meditation.getId(), jhipster.getId(), true));
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
