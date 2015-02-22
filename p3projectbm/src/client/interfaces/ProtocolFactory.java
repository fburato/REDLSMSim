/**
 * 
 */
package client.interfaces;

import threads.ProtocolThread;
import exceptions.IncompleteParameterException;
import exceptions.UninitializedProtocolException;
import exceptions.UnknownProtocolException;
import exceptions.WrongDataTypeException;
import graph.nodes.StationNode;
import threads.Hypervisor;
import java.util.Map;

/**
 * ProtocolFactory interface will make easier to implement different kinds of
 * protocol
 * 
 * @author Francesco Burato
 * @version 1
 */
public interface ProtocolFactory {
  /**
   * The method simply consists on a check on the name of the protocol that has
   * been asked to instantiate. Protocol instantiations consists in the
   * initialization of the parameters needed to the protocol to be used.
   * 
   * @param name
   *          Name of the protocol to be instantiated
   * @throws UnknownProtocolException
   *           Is thrown if the implemented factory is not able to handle the
   *           requested protocol
   * @throws WrongDataTypeException
   *           Is thrown if one of the parameter parsed are of the wrong type
   * @throws IncompleteParameterException
   *           Is thrown if one parameter is missing
   */
  public void instantiateProtocol(String name) throws UnknownProtocolException,
      WrongDataTypeException, IncompleteParameterException;

  /**
   * After having instantiate correctly the protocol this methods produces the
   * correct ProtocolThread associated to that
   * 
   * @param stations
   *          Are the stations upon which the ProtocolThread must be created
   * @return The ProtocolThread correctly instantiated
   * @throws UninitializedProtocolException
   *           Is thrown if the user is trying to generate ProtocolThread upon
   *           an uninstantiated factory
   */
  public ProtocolThread[] generateProtocol(StationNode[] stations)
      throws UninitializedProtocolException;

  /**
   * Sets the (optional) hypervisor which will be used by protocol constructors
   * 
   * @param h
   *          The new Hypervisor
   */
  public void setHypervisor(Hypervisor h);

  /**
   * Writes on a list the parameters used for generating protocol threads
   * 
   * @param l
   *          The list which is written to append at the end of it the
   *          parameters
   * @return If writing on the Map was successful
   */
  public boolean writeStatisticalData(Map<String, String> l);
}
