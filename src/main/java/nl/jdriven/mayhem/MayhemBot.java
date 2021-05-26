package nl.jdriven.mayhem;

import nl.jdriven.mayhem.behavior.OverTimeBehavior;
import nl.jdriven.mayhem.behavior.PrepareAtReadyBehavior;
import nl.jdriven.mayhem.behavior.RandomBehavior;
import nl.jdriven.mayhem.behavior.SelfHealingJHipsterBehavior;
import nl.jdriven.mayhem.behavior.SelfPowerUpCiCdBehavior;
import nl.jdriven.mayhem.behavior.SelfRebootLegacyDusterBehavior;
import nl.jdriven.mayhem.behavior.SendActionsBehavior;
import nl.jdriven.mayhem.behavior.TeamCounterEjbejbejbBehavior;
import nl.jdriven.mayhem.behavior.TeamCounterMulticloudBehavior;
import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.comms.Postman;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.subsumption.Arbitrator;
import nl.jdriven.mayhem.subsumption.Behavior;

import java.util.List;

public class MayhemBot {
    private final Arbitrator arbitrator;
    private final Postman postman;

    public MayhemBot(Client client, Arena arena) {
        this(client, arena, List.of(
            new PrepareAtReadyBehavior(arena),
//            new AttackMulticloudBehavior(arena),
//            new AttackPlsqlBehavior(arena),
            new RandomBehavior(arena),
            new TeamCounterEjbejbejbBehavior(arena),
            new TeamCounterMulticloudBehavior(arena),
            new SelfPowerUpCiCdBehavior(arena),
            new SelfRebootLegacyDusterBehavior(arena),
            new SelfHealingJHipsterBehavior(arena),
            new OverTimeBehavior(arena),
            new SendActionsBehavior(arena, client)
        )
        );
    }

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
