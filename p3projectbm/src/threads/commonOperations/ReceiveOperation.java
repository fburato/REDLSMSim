package threads.commonOperations;

import graph.nodes.StationNode;

/**
 * A ReceiveOperation is an operation which simulate the reception of a message
 * from one node. It has just pure cost.
 * 
 * @author Francesco Burato and Mattia Merotto
 * @version 1
 */
public class ReceiveOperation extends StationNodeOperation {

  public ReceiveOperation(StationNode s, double c) {
    super(s, c);
  }

  @Override
  public void execute() {
  }

}
