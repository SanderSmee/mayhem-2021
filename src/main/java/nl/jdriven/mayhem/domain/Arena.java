package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Arena {
    private final Logger logger = LoggerFactory.getLogger(Arena.class);

    private StatusMessage currentStatus = startStatus();
    public final Deque<ActionMessage> nextActions = new ArrayDeque<>();

    public Arena() {
    }

    public void updateState(StatusMessage input) {
        this.currentStatus = input;
    }

    StatusMessage startStatus() {
        return new StatusMessage(List.of(), List.of(), StatusMessage.FightStatus.idle, null, StatusMessage.CompetitionStatus.idle);
    }

    public StatusMessage currentStatus() {
        return this.currentStatus;
    }
}
