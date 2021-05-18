package nl.jdriven.mayhem.behavior;

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
        return !this.arena.nextActions.isEmpty();
    }

    @Override
    public void action() {
        this.suppressed = false;

        if (!suppressed) {
            logger.debug("sending {} actions", arena.nextActions.size());
            while (!arena.nextActions.isEmpty()) {
                var msg = adapter.toString(arena.nextActions.pop());

                logger.info("<A {}", msg);
                client.bufferMessage(msg);
            }
            client.flushToServer();

            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
