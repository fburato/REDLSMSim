/**
 * 
 */
package graph.support;

import graph.support.metrics.EuclideanPoint;


/**
 * A class which is able to communicate out of its knowledge domain that some
 * update has happened
 * 
 * @author Francesco Burato
 * 
 */
public interface UpdateLauncher {
  /**
   * Add a listener to the sequence of listener of this listened object. It must
   * refuse null references and repeated Listeners
   * 
   * @param u
   *          The new listener to be added
   */
  public void addUpdateListener(UpdateListener u);

  /**
   * For each listeners in its sequence calls updateLaunched()
   */
  public void generateUpdate();

  /**
   * Calls updateLaunched(EuclideanPoint,EuclideanPoint) for each of the
   * registered Listeners. It's used to communicate that a path has been
   * followed when forwarding a particular message
   * 
   * @param s
   *          The first limit of the segment to be drawn
   * @param d
   *          The second limit of the segment to be drawn
   */
  public void generateUpdate(EuclideanPoint s, EuclideanPoint d, boolean doSleep);

  /**
   * Calls updateLaunched(EuclideanPoint) for each of the registered Listeners.
   * It's used to communicate what is the destination of a Forward involving a
   * clone
   * 
   * @param d
   */
  public void generateUpdate(EuclideanPoint d);

  /**
   * Impose the Event launcher to wait until the listener has terminated its
   * execution
   */
  public void waitListeners();

}
