package nl.jdriven.mayhem;

import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.comms.Postman;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.subsumption.Arbitrator;
import nl.jdriven.mayhem.subsumption.Behavior;

import java.util.List;

public class MayhemBot {
    private final Arbitrator arbitrator;
    private final Postman postman;

    public MayhemBot(Client client, Arena arena, List<Behavior> behaviorList) {
        this.postman = new Postman(arena, client);
        this.arbitrator = new Arbitrator(behaviorList.toArray(new Behavior[0]));
    }

    public void start() {
        postman.start();
        arbitrator.go();
    }

    public void stop() {
        arbitrator.stop();
        postman.stop();
    }
}
