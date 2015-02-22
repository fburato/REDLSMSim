/**
 * 
 */
package graph.support.communications;

/**
 * A Rule define a generic comparison method between two Messages. It will used
 * when looking for Messages whose characteristics must be compared with an
 * other one.
 * 
 * @author Francesco Burato
 * @version 1
 */
public interface Rule {
  /**
   * It verify the similarity between two Messages
   * 
   * @param s
   *          It's the massage used as comparison term (so the original
   *          one)
   * @param d
   *          It's the message that has to be compared
   *          (so the destination one)
   * @return
   */
  public boolean compare(Message s, Message d);
}
