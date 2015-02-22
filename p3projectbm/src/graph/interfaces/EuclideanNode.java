/**
 * 
 */
package graph.interfaces;

import graph.support.metrics.EuclideanPoint;

/**
 * 
 * EuclideanNode is a Node belonging to a metric space. So it uses as distance
 * the Pythagorean distance between two points on the Euclidean plane.
 * 
 * @author Francesco Burato
 * @version 1
 */
public interface EuclideanNode extends MetricNode {
  /**
   * Get the euclidean position of the current Node
   * 
   * @return The EuclideanPoint corresponding to the position of the Node
   */
  public EuclideanPoint getPosition();
}
