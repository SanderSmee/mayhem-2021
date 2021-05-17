package nl.jdriven.mayhem;

import ninja.robbert.mayhem.api.OutputMessage;
import ninja.robbert.mayhem.api.StatusMessage;
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
        Client client = new Client();

        String line;
        while ((line = client.readMessage()) != null) {
            Optional<OutputMessage> serverMsg = adapter.fromString(line);

            serverMsg.map(msg -> {
                if (msg instanceof WelcomeMessage) {
                    return MsgAdapter.registerMessage();
                } else if (msg instanceof StatusMessage m) {
                    LOGGER.info("status: {}", m.getStatus());
                    return null;
                } else /* ErrorMessage, AcceptMessage */ {
                    return null;
                }
            }).ifPresent(response -> client.sendMessageImmediate(adapter.toString(response)));
        }

        client.closeConnection();
    }
}
