/**
 * 
 */
package client.interfaces;

import graph.nodes.StationNode;

/**
 * NewGraphLauncher implementers are able to warn other objects that a graph has
 * changed
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public interface NewGraphLauncher {
  /**
   * Adds to a local list the Listeners who are registered to listen the
   * launcher.
   * 
   * @param l
   *          The Listener to be added. If l is null or it's already present on
   *          the list it is not added
   */
  public void addNewGraphListener(NewGraphListener l);

  /**
   * Method called to warn the listener that they have to change their copy of
   * graph
   * 
   * @param g
   *          The new graph to be processed by listeners
   */
  public void generateEvent(StationNode[] g);
}
