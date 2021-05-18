package nl.jdriven.mayhem;

import nl.jdriven.mayhem.behavior.RandomBehavior;
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

        Behavior sendActions = new SendActionsBehavior(arena, client);
        Behavior randomActions = new RandomBehavior(arena);

        List<Behavior> behaviors = List.of(
            randomActions,
            sendActions
        );

        MayhemBot bot = new MayhemBot(client, arena, behaviors);
        Runtime.getRuntime().addShutdownHook(new Thread(bot::stop));

        bot.start();
    }
}
