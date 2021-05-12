package nl.jdriven.mayhem.comms;

import nl.jdriven.mayhem.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 *
 */
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final PrintWriter out;
    private final BufferedReader in;
    private Socket clientSocket;

    public Client() throws IOException {
        LOGGER.info("Connecting to {}:{}", Config.HOST, Config.PORT);

        clientSocket = new Socket(Config.HOST, Config.PORT);
        clientSocket.setKeepAlive(true);

        LOGGER.info("Connection status: {}", clientSocket.isConnected());

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void sendMessage(String msg) {
        LOGGER.info("Sending msg: {}", msg);
        out.println(msg);
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
