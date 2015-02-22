package threads.commonMessages;

import graph.support.communications.Message;
import graph.support.metrics.EuclideanPoint;

/**
 * Simple ClaimMessage which contains an Id and a Position
 * 
 * @author Francesco Burato and Mattia Merotto
 * @version 1
 */
public class ClaimMessage implements Message {
  private String _id;
  private EuclideanPoint _position;

  public ClaimMessage(String id, EuclideanPoint pos) {
    _id = id;
    _position = new EuclideanPoint(pos);
  }

  /**
   * Get the Id of the Node which built the Claim
   * 
   * @return Id of the Node which built the Claim
   */
  public String getId() {
    return _id;
  }

  /**
   * Get the position of the Node which built the Claim
   * 
   * @return Position of the Node which built the Claim
   */
  public EuclideanPoint getPosition() {
    return _position;
  }

  public String toString() {
    return "Claim(" + _id + "," + _position + ")";
  }
}
