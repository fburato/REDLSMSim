/**
 * 
 */
package threads.commonOperations;

import graph.nodes.StationNode;
import graph.support.operations.Operation;

/**
 * A generic Operation that acts on StationNode. Since the behavior of cost
 * management is the same in everyone of these Operation the only method to
 * override will be execute.
 * 
 * @author Francesco Burato and Mattia Merotto
 * @version 1
 * 
 */
public abstract class StationNodeOperation implements Operation {
  private double _cost;
  protected StationNode _station;

  public StationNodeOperation(StationNode s, double c) {
    _station = s;
    _cost = c;
  }

  /*
   * (non-Javadoc)
   * 
   * @see graph.interfaces.Operation#execute()
   */
  @Override
  public abstract void execute();

  /*
   * (non-Javadoc)
   * 
   * @see graph.interfaces.Operation#getCost()
   */
  @Override
  public double getCost() {
    return _cost;
  }

  /*
   * (non-Javadoc)
   * 
   * @see graph.interfaces.Operation#setCost(double)
   */
  @Override
  public void setCost(double d) {
    _cost = d;
  }

}
