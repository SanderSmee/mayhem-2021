package nl.jdriven.mayhem.behavior;

import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.messages.MsgAdapter;
import nl.jdriven.mayhem.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendActionsBehavior implements Behavior {
    private final Logger logger = LoggerFactory.getLogger(SendActionsBehavior.class);
    private final Arena arena;
    private final Client client;
    private final MsgAdapter adapter = new MsgAdapter();
    private boolean suppressed;

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
            logger.info("sending {} actions", arena.nextActions.size());
            while (!arena.nextActions.isEmpty()) {
                var msg = adapter.toString(arena.nextActions.pop());

                logger.debug("< {}", msg);
                client.bufferMessage(msg);
            }
            client.flushToServer();

            try {
                Thread.sleep(10);
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
