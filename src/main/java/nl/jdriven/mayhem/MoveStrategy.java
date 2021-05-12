package nl.jdriven.mayhem;

import ninja.robbert.mayhem.api.InputMessage;
import ninja.robbert.mayhem.api.OutputMessage;

/**
 *
 */
public interface MoveStrategy {
    InputMessage handleTick(OutputMessage tick);


}
