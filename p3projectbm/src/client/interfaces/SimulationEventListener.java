/**
 * 
 */
package client.interfaces;

/**
 * A listener of different events generated from the client when particular
 * situation happens. We will use the events: the simulation is stopped, the
 * simulation is started, a test is paused, a simulation is paused, the protocol
 * has changed.
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public interface SimulationEventListener {
  /**
   * Communicate that an event must be considered to change the state of the
   * listener. The event type is the eventName
   * 
   * @param eventName
   *          Name of the kind of event generated from launchers
   */
  public void changeStatePerformed(String eventName);

  /**
   * Communicate that the simulation has passed from i-1 to i over NSIM test
   * that must be performed
   * 
   * @param i
   *          Current test executing
   * @param tot
   *          Total of simulation to execute
   */
  public void newSimulation(int i, int tot);

  /**
   * One of the server which are used to print to the user the statistical data
   * failed because of any reason. This reason (Exception) is communicated to
   * the listener
   * 
   * @param e
   *          Exception which caused the server to fail.
   */
  public void serverError(Exception e);

  /**
   * Changing of the protocol simulated by the client simulator.
   * 
   * @param protoName
   */
  public void changeProtocol(String protoName);
}
