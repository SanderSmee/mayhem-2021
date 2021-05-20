package nl.jdriven.mayhem.behavior;

import ninja.robbert.mayhem.api.StatusMessage;
import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.messages.MsgAdapter;
import nl.jdriven.mayhem.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SendActionsBehavior implements Behavior {
    private final long TIMEOUT = 10L;
    private final Logger logger = LoggerFactory.getLogger(SendActionsBehavior.class);
    private boolean suppressed;

    private final Arena arena;
    private final Client client;
    private final MsgAdapter adapter = new MsgAdapter();

    public SendActionsBehavior(Arena arena, Client client) {
        this.arena = arena;
        this.client = client;
    }

    @Override
    public boolean takeControl() {
        return
            StatusMessage.FightStatus.fighting == arena.currentStatus().getStatus()
            && !this.arena.nextActions.isEmpty();
    }

    @Override
    public void action() {
        this.suppressed = false;

        logger.debug("sending {} actions", arena.nextActions.size());
        while (!arena.nextActions.isEmpty() && !suppressed) {
            var msg = arena.nextActions.poll();

            if (!arena.currentStatus().getYou().get(msg.getHero()).isAlive()) {
                continue;
            }

            var json = adapter.toString(msg);

            logger.info("<A {}", json);
            client.sendMessageImmediate(json);
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
