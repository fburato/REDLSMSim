/**
 * 
 */
package graph.interfaces;

import graph.support.operations.Operation;

/**
 * OperationalNode is a Node capable of operations. In more general assumption
 * it can't perform all possible operations but an energy cost will be
 * associated with every operation. So an OperationalNode will have a well
 * defined life or rather a battery.
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public interface OperationalNode extends Node {
  /**
   * 
   * Returns current life of the operational node or rather its battery
   * 
   * @return Remaining battery value
   */
  public double getBattery();

  /**
   * 
   * Executes the operation passed as argument
   * 
   * @param o
   *          the operation to be executed
   * @return true if and only if the OperationalNode has enough battery to
   *         execute o
   */
  public boolean performs(Operation o);
  
  /**
   * Set Node battery to specified value
   * @param d The new value of the battery
   */
  public void setBattery(double d);
  /**
   * Set the Node battery to a default value
   */
  public void resetBattery(); 
}
