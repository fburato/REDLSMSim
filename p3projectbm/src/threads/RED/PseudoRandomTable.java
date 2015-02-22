package threads.RED;

import graph.support.metrics.EuclideanPoint;

import java.util.Hashtable;

/**
 * PseudoRandomTable has the capability of offer an hash function that accept
 * Strings and returns couple of coordinates (EuclideanPoints) for RED protocol.
 * 
 * @author Francesco Burato
 * @version 1
 */
public class PseudoRandomTable extends Hashtable<String, EuclideanPoint> {

  /**
   * 
   */
  private static final long serialVersionUID = 8266256576100027632L;

  /**
   * Instantiate a new PseudoRandomTable with an initial capacity. Each of the
   * NSIM simulation of RED requires at maximum N*g points to be produced by an
   * hash function because each Node (N) send a ClaimMessage and for each
   * ClaimMessage a every neighbor has to calculate in which position send the
   * ForwardMessage containing the claim. Since that every node must select (g)
   * different nodes the total amount of positions is N*g. The HashTable is
   * initialized to the size of this capacity so that the complexity to
   * calculate the positions is O(1). The HashTable is then cleaned for each of
   * the NSIM simulations.
   * 
   * @param capacity
   *          The initial capacity of the table to obtain a O(1) complexity
   */
  public PseudoRandomTable(int capacity) {
    super(capacity);
  }

  /**
   * 
   * @param s
   *          The String passed to the table to get the position. We assume that
   *          this String will be computed this way:<br>
   *          "<ID of claiming station>:<iteration on g>"<br>
   *          so that the capacity of the table will be sufficient.<br>
   *          Note that since we use a random generated point which is created
   *          the first time a String is asked and we return an already
   *          generated point from the second time over the satellite number is
   *          futile.
   * @return The point associated with the String. If this is the first time s
   *         is asked then a new point will be generated.
   */
  public synchronized EuclideanPoint get(Object s) {
    EuclideanPoint res = super.get(s.toString());
    if (res == null) {
      // first time s asked. Generate new point.
      res = new EuclideanPoint(Math.random(), Math.random());
      super.put(s.toString(), res);
    }
    return new EuclideanPoint(res);
  }
}
