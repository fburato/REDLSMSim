package threads.commonOperations;

import graph.nodes.StationNode;
import graph.support.communications.Message;

/**
 * A SendOperation is a broadcast of a particular Message to another
 * StationNode. We assume that sending a broadcast costs as sending a message to
 * a particular Station will need the same amount of energy.
 * 
 * @author Francesco Burato and Mattia Merotto
 * @version 1
 */
public class SendOperation extends StationNodeOperation {
  private Message _msg;

  public SendOperation(StationNode s, double c, Message msg) {
    super(s, c);
    _msg = msg;
  }

  @Override
  public void execute() {
    _station.putMessage(_msg);
  }

}
