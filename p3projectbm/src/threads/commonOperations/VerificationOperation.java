package threads.commonOperations;

import graph.nodes.StationNode;

/**
 * VerificationOperation is the simulation of a signature check. Since in this
 * project there is no actual signature check it is a dummy operation of pure
 * cost.
 * 
 * @author Francesco Burato and Mattia Merotto
 * 
 */
public class VerificationOperation extends StationNodeOperation {

  public VerificationOperation(StationNode s, double c) {
    super(s, c);
  }

  @Override
  public void execute() {
    // do nothing
  }

}
