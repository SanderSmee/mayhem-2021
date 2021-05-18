package nl.jdriven.mayhem;

import nl.jdriven.mayhem.behavior.RandomBehavior;
import nl.jdriven.mayhem.behavior.SendActionsBehavior;
import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.comms.Postman;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.subsumption.Arbitrator;
import nl.jdriven.mayhem.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        Arena arena = new Arena();

        Postman postman = new Postman(arena, client);

        Behavior sendActions = new SendActionsBehavior(arena, client);
        Behavior randomActions = new RandomBehavior(arena);

        Behavior[] behaviors = {randomActions, sendActions};
        Arbitrator arby = new Arbitrator(behaviors);

        postman.start();

        arby.go();
    }
}
