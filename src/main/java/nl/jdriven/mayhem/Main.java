package nl.jdriven.mayhem;

import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.domain.Arena;

/**
 *
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        Arena arena = new Arena();

        MayhemBot bot = new MayhemBot(client, arena);
        Runtime.getRuntime().addShutdownHook(new Thread(bot::stop));

        bot.start();
    }
}
