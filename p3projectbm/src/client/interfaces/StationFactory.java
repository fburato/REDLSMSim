/**
 * 
 */
package client.interfaces;



import exceptions.IncompleteParameterException;
import exceptions.WrongDataTypeException;
import graph.nodes.StationNode;
import java.util.Map;

/**
 * StationFactory is used to make easier to extend the generation of the graph
 * 
 * @author Francesco Burato
 * @version 1
 */
public interface StationFactory {
  /**
   * After construction the parameters given must be consistent with the
   * property requested from the protocol
   * 
   * @throws WrongDataTypeException
   *           If one of the parameters has wrong type
   * @throws IncompleteParameterException
   *           If one parameter is missing
   */
  public void validate() throws WrongDataTypeException,
      IncompleteParameterException;

  /**
   * Return a graph with the parameters retrieved if the validation was
   * successful.
   * 
   * @return The graph with the given parameters if the validation was
   *         successful, null in the other case
   */
  public StationNode[] makeGraph();
  
  /**
   * Writes on a list the parameters used for generating protocol threads
   * 
   * @param l
   *          The list which is written to append at the end of it the
   *          parameters
   *          @return If writing on the Map was successful
   */
  public boolean writeStatisticalData(Map<String,String> l);
}
