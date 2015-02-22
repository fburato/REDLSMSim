/**
 * 
 */
package graph.interfaces;

import graph.support.metrics.Position;

/**
 * A MetricNode is a Node member of a metric space. It must be able to calculate
 * a distance between it and a given Position compatible with the metric space.
 * 
 * @author Francesco Burato
 * @version 1.0.1
 */
public interface MetricNode extends Node {
  /**
   * Calculate the distance of the MetricNode from the given position
   * 
   * @param p
   *          The position used in distance calculation
   * @return The actual distance from the Node from p
   */
  public double distance(Position p);
}
