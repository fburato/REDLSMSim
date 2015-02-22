/**
 * 
 */
package graph.support.operations;

//Da tradurre: Mattia
/**
 * An Operation is an action that has a cost and it is done by an agent. An
 * Operation is executed through the call of the method execute.
 * 
 * @author Francesco Burato
 * @version 1
 */
public interface Operation {
  /**
   * Executes the current operation
   */
  public void execute();

  /**
   * Returns the current operation cost
   * 
   * @return The current operation cost
   */
  public double getCost();

  /**
   * Modifies the operation cost with the specified value
   * 
   * @param d
   *          The new operation cost
   */
  public void setCost(double d);
}
