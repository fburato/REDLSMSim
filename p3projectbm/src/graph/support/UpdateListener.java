package graph.support;

import graph.support.metrics.EuclideanPoint;


/**
 * Update Listener used to communicate to the out of the node that the
 * characteristics of the node has been changed
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public interface UpdateListener {
  /**
   * Method executed when a listened object ask for drawing a segment
   * 
   * @param s
   *          The first segment limit to be drawn
   * @param d
   *          The second segment limit to be drawn
   */
  public void updateLaunched(EuclideanPoint s, EuclideanPoint d,boolean doSleep);

  /**
   * Method executed when a node generate an update Event of simple update
   */
  public void updateLaunched();

  /**
   * Method executed when a listened object ask for drawing a destination point
   * of a clone
   * 
   * @param d
   *          The destination point of the clone
   */
  public void updateLaunched(EuclideanPoint d);

  /**
   * Cleans out every kind of information that should't be present after
   * iterations
   */
  public void clearState();

  /**
   * Communicate if the listener is executing anything
   * 
   * @return
   */
  public boolean isExecuting();

  /**
   * Sets the time interval between one step and the other
   * 
   * @param ms
   *          Time in millisenconds to wait
   */
  public void setSleepTime(long ms);
}
