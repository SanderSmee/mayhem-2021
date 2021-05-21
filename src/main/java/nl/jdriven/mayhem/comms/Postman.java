package nl.jdriven.mayhem.comms;

import ninja.robbert.mayhem.api.AcceptMessage;
import ninja.robbert.mayhem.api.OutputMessage;
import ninja.robbert.mayhem.api.StatusMessage;
import ninja.robbert.mayhem.api.WelcomeMessage;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.messages.MsgAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Postman extends Thread {
    private final Logger LOGGER = LoggerFactory.getLogger(Postman.class);

    private final Arena arena;
    private Client client;
    private final MsgAdapter adapter = new MsgAdapter();

    public Postman(Arena arena, Client client) {
        this.setDaemon(true);

        this.arena = arena;
        this.client = client;
    }

    @Override
    public void run() {
        String line;
        while ((line = client.readMessage()) != null) {
            var finalLine = line;
            Optional<OutputMessage> serverMsg = adapter.fromString(finalLine);

            serverMsg
                .map(msg -> {
                    if (msg instanceof WelcomeMessage) {
                        LOGGER.debug("W> {}", finalLine);
                        return arena.registerMessage();
                    } else if (msg instanceof StatusMessage m) {
                        LOGGER.info("S> {}, {}", m.getStatus(), finalLine);
                        arena.updateState(m);
                    } else if (msg instanceof AcceptMessage) {
                        LOGGER.info("A> {}", finalLine);
                    } else /* ErrorMessage, AcceptMessage */ {
                        LOGGER.error("E> {}", finalLine);
                    }
                    return null;
                })
                .ifPresent(msg -> {
                    var json = adapter.toString(msg);
                    LOGGER.info("<R {}", json);
                    this.client.sendMessageImmediate(json);
                });
        }
    }

    private void syncTime(WelcomeMessage message) {
        var systemClock = Clock.systemDefaultZone();

        var diff = message.getTimestamp().getTime() - systemClock.millis();

        LOGGER.warn("Adjusting clock {} with {} millis", systemClock, diff);

        var adjusted = Clock.offset(systemClock, Duration.of(diff, ChronoUnit.MILLIS));
    }
}
