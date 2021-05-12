package nl.jdriven.mayhem;

import ninja.robbert.mayhem.api.InputMessage;
import ninja.robbert.mayhem.api.OutputMessage;
import ninja.robbert.mayhem.api.RegisterMessage;
import ninja.robbert.mayhem.api.WelcomeMessage;
import nl.jdriven.mayhem.comms.Client;
import nl.jdriven.mayhem.messages.MsgAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 *
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        MsgAdapter adapter = new MsgAdapter();
        MoveStrategy strategy = new MoveStrategy() {
            @Override
            public InputMessage handleTick(OutputMessage tick) {
                return null;
            }
        };
        Client client = new Client();

        String line;
        while ((line = client.readMessage()) != null) {
            Optional<OutputMessage> msg = adapter.fromString(line);

            msg.ifPresent(out -> {
                if (out instanceof WelcomeMessage) {
                    client.sendMessage(adapter.toString(new RegisterMessage("FOobAr", "sander.smeman@jdriven.com", "yadda-yadda")));
                } else {

                }
            });
        }

        client.closeConnection();
    }
}
