package nl.jdriven.mayhem.behavior;

import ninja.robbert.mayhem.api.StatusMessage.FightStatus;
import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.messages.MsgAdapter;
import nl.jdriven.mayhem.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public final class SendActionsBehavior implements Behavior {
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
            EnumSet.of(FightStatus.fighting, FightStatus.overtime).contains(arena.currentStatus().getStatus())
            && !this.arena.nextActions.isEmpty();
    }

    @Override
    public void action() {
        this.suppressed = false;

        logger.debug("sending {} actions", arena.nextActions.size());
        while (!arena.nextActions.isEmpty() && !suppressed) {

            try {
                var msg = arena.nextActions.pollFirst();

                if (!arena.currentStatus().getYou().get(msg.getHero()).isAlive()) {
                    continue;
                }

                var json = adapter.toString(msg);

                logger.info("<A {}", json);
                client.sendMessageImmediate(json);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    @Override
    public void suppress() {
        this.suppressed = true;
    }
}
