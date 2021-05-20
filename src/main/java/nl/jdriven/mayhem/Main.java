package nl.jdriven.mayhem;

import nl.jdriven.mayhem.behavior.PrepareAtReadyBehavior;
import nl.jdriven.mayhem.behavior.RandomBehavior;
import nl.jdriven.mayhem.behavior.SelfHealingJHipsterBehavior;
import nl.jdriven.mayhem.behavior.SelfPowerUpCiCdBehavior;
import nl.jdriven.mayhem.behavior.SelfRebootLegacyDusterBehavior;
import nl.jdriven.mayhem.behavior.SendActionsBehavior;
import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.domain.Arena;
import nl.jdriven.mayhem.subsumption.Behavior;

import java.util.List;

/**
 *
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        Arena arena = new Arena();

        var sendActions = new SendActionsBehavior(arena, client);
        var randomActions = new RandomBehavior(arena);
        var prepareAtReadyBehavior = new PrepareAtReadyBehavior(arena);

        List<Behavior> behaviors = List.of(
            prepareAtReadyBehavior,
            randomActions,
            new SelfPowerUpCiCdBehavior(arena),
            new SelfRebootLegacyDusterBehavior(arena),
            new SelfHealingJHipsterBehavior(arena),
            sendActions
        );

        MayhemBot bot = new MayhemBot(client, arena, behaviors);
        Runtime.getRuntime().addShutdownHook(new Thread(bot::stop));

        bot.start();
    }
}
