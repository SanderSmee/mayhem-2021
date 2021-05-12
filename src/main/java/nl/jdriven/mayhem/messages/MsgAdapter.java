package nl.jdriven.mayhem.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ninja.robbert.mayhem.api.InputMessage;
import ninja.robbert.mayhem.api.OutputMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 *
 */
public class MsgAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgAdapter.class);
    private ObjectMapper mapper;

    public MsgAdapter() {
        mapper = new ObjectMapper();
    }

    public String toString(InputMessage msg) {
        try {
            return mapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public Optional<OutputMessage> fromString(String line) {
        try {
            return Optional.of(mapper.readValue(line, OutputMessage.class));
        } catch (IOException e) {
            LOGGER.error("Cant map LINE: {}", line, e);
            return Optional.empty();
        }
    }
}
