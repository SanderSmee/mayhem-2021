package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.InputMessage;
import ninja.robbert.mayhem.api.RegisterMessage;
import ninja.robbert.mayhem.api.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;

public class Arena {
    private final Logger logger = LoggerFactory.getLogger(Arena.class);
    private final String botName;

    public final AtomicReference<StatusMessage> atomicState = new AtomicReference<>(emptyStatus());
    public final Deque<Action> nextActions = new LinkedBlockingDeque<>(100);

    public Arena() {
        this("T-1000");
    }

    public Arena(String bot) {
        this.botName = bot;
    }

    public void updateState(StatusMessage input) {
        this.atomicState.set(input);

        logger.trace("updated current state");
    }

    StatusMessage emptyStatus() {
        var brothers = List.of(new CiCdGod(), new JHipster(), new LegacyDuster());
        return new StatusMessage(List.copyOf(brothers), List.copyOf(brothers), StatusMessage.FightStatus.idle, null, StatusMessage.CompetitionStatus.idle);
    }

    public InputMessage registerMessage() {
        return new RegisterMessage(botName, "sander.smeman+brutal@jdriven.com", "yadda-barf-%s".formatted(LocalDate.now()));
    }

    public StatusMessage currentStatus() {
        return atomicState.get();
    }
}
