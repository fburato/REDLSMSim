/**
 * 
 */
package graph.support.communications;


/**
 * Defines property which a message must respect to satisfy the filter 
 * @author Francesco Burato
 * @version 1.0.1
 *
 */
public interface Filter {
  /**
   * Apply the current filter to message m
   * @param m Message on which apply the filter
   * @return true Iff message m satisfy current filter.
   */
  public boolean apply(Message m);
}