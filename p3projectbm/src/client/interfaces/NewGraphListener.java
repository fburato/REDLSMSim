/**
 * 
 */
package client.interfaces;

import graph.nodes.StationNode;

/**
 * NewGraphListener is an interface which is able to accept Events of change in
 * graphs;
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public interface NewGraphListener {
  /**
   * Method called by NewGraphLauncher when a change event happens
   * 
   * @param n
   *          The new graph to be processed by Listeners
   */
  public void graphChanged(StationNode[] n);
}
