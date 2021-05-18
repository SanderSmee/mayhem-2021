package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.InputMessage;
import ninja.robbert.mayhem.api.RegisterMessage;
import ninja.robbert.mayhem.api.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Arena {
    private final Logger logger = LoggerFactory.getLogger(Arena.class);
    private final long TIMEOUT = 5L;
    private final String botName;

    private StatusMessage currentStatus = emptyStatus();
    public final Deque<ActionMessage> nextActions = new ConcurrentLinkedDeque<>();

    public Arena() {
        this("F0obAr");
    }

    public Arena(String bot) {
        this.botName = bot;
    }

    public void updateState(StatusMessage input) {
        this.currentStatus = input;

        logger.info("updated current state");
    }

    StatusMessage emptyStatus() {
        return new StatusMessage(List.of(), List.of(), StatusMessage.FightStatus.idle, null, StatusMessage.CompetitionStatus.idle);
    }

    public InputMessage registerMessage() {
        return new RegisterMessage(botName, "sander@mayhem.com", "yadda-barf-%s".formatted(LocalDate.now()));
    }

    public StatusMessage currentStatus() {
        return this.currentStatus;
    }
}
