package nl.jdriven.mayhem.domain;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.Hero;
import ninja.robbert.mayhem.api.StatusMessage;

import java.util.ArrayList;
import java.util.List;

public class Fight {

    private List<Hero> me = new ArrayList<>();
    private List<Hero> opponent = new ArrayList<>();

    public ActionMessage handle(StatusMessage input) {
        return switch (input.getStatus()) {
            case idle, finished -> new ActionMessage(0, 0, 0, false);
            case ready, fighting, overtime -> new ActionMessage(1, 1, 1, true);
        };
    }

    void reset() {
    }
}
