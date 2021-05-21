package nl.jdriven.mayhem.subsumption;

import nl.jdriven.mayhem.domain.Arena;

public abstract class OnNextTickBehavior implements Behavior {
    protected final Arena arena;
    protected long lastTick = 0L;
    protected boolean suppressed;

    public OnNextTickBehavior(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean takeControl() {
        return
            arena.currentStatus().getTimestamp().getTime() > lastTick
            && canTakeControl();
    }

    public abstract boolean canTakeControl();

    @Override
    public void action() {
        this.lastTick = arena.currentStatus().getTimestamp().getTime();

        this.doAction();
    }

    public abstract void doAction();

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
