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
        this(Config.HOST, Config.PORT);
    }

    public Client(String host, int port) throws IOException {
        LOGGER.info("Connecting to {}:{}", host, port);

        clientSocket = new Socket(host, port);
        clientSocket.setKeepAlive(true);

        LOGGER.info("Connection status: {}", clientSocket.isConnected());

        out = new PrintWriter(clientSocket.getOutputStream(), false);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String readMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void sendMessageImmediate(String msg) {
        LOGGER.trace("Sending msg: {}", msg);
        out.println(msg);
        out.flush();
    }

    public void bufferMessage(String msg) {
        LOGGER.trace("Buffering msg: {}", msg);
        out.println(msg);
    }

    public void flushToServer() {
        LOGGER.trace("Sending buffered messages");
        out.flush();
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
