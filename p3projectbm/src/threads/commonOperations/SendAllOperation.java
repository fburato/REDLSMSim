/**
 * 
 */
package threads.commonOperations;

import graph.nodes.StationNode;
import graph.support.Arch;
import graph.support.communications.Message;
import graph.interfaces.CommunicationNode;
import java.util.ArrayList;

/**
 * A SendAllOperation is a broadcast of a particular Message to each neighbor of
 * a StationNode. We assume that sending a broadcast costs as sending a message
 * to a particular Station will need the same amount of energy.
 * 
 * @author Francesco Burato and Mattia Merotto
 * @version 1
 */
public class SendAllOperation extends StationNodeOperation {

  private Message _msg;

  public SendAllOperation(StationNode s, double c, Message msg) {
    super(s, c);
    _msg = msg;
  }

  /*
   * (non-Javadoc)
   * 
   * @see threads.RED.StationNodeOperation#execute()
   */
  @Override
  public void execute() {
    // sending to every adjacent Node the message of instance
    ArrayList<Arch> adjacents = _station.getAdjacents();
    for (Arch a : adjacents) {
      // PRE: every pointed Node is a Communication Node
      CommunicationNode node = (CommunicationNode) a._node;
      node.putMessage(_msg);
    }
  }

}
