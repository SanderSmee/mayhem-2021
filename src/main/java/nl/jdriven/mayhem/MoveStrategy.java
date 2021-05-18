package nl.jdriven.mayhem;

import ninja.robbert.mayhem.api.InputMessage;
import ninja.robbert.mayhem.api.OutputMessage;

import java.util.stream.Stream;

/**
 *
 */
public interface MoveStrategy {
    Stream<InputMessage> handleTick(OutputMessage tick);


}
