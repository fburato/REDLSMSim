/**
 * 
 */
package threads.RED;

import java.util.Random;

/**
 * This class simulate the Satellite object which is requested by the RED
 * protocol to differentiate the message forward position during the routing
 * algorithm.
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public class Satellite {
  private Random _generator;
  private int _value;

  /**
   * Build a Satellite as a random number generator which memorize its last
   * value and which is updated only at request.
   */
  public Satellite() {
    _generator = new Random((int) (Math.random() * Integer.MAX_VALUE));
    _value = (int) (_generator.nextDouble() * Integer.MAX_VALUE);
  }

  /**
   * Get the current memorized value of the satellite
   * 
   * @return The pseudorandom number actually memorized
   */
  public int getValue() {
    return _value;
  }
  /**
   * Advances with the next pseudorandom number.
   */
  public void next() {
    _value = (int) (_generator.nextDouble() * Integer.MAX_VALUE);
  }
}
