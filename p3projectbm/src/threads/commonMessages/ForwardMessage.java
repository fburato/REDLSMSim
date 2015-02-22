/**
 * 
 */
package threads.commonMessages;

import graph.support.communications.Message;
import graph.support.metrics.EuclideanPoint;

/**
 * A forward message is a Claim Message encapsulated in another message which
 * has also a position to which the message must be forwarded
 * 
 * @author Francesco Burato and Mattia Merotto
 * @version 1
 */
public class ForwardMessage implements Message {
  private ClaimMessage _claim;
  private EuclideanPoint _destination;

  public ForwardMessage(ClaimMessage msg, EuclideanPoint des) {
    _claim = msg;
    _destination = des;
  }

  /**
   * Get the ClaimMessage inside the ForwardMessage
   * 
   * @return The ClaimMessage inside the current Message
   */
  public ClaimMessage getClaim() {
    return _claim;
  }

  /**
   * Get the destination of the current Message
   * 
   * @return The destionation of the Message
   */
  public EuclideanPoint getDestination() {
    return _destination;
  }

  public String toString() {
    return "Forward(" + _claim + "," + _destination + ")";
  }
}
