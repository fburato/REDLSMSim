/**
 * 
 */
package graph.support.metrics;

//Da tradurre: Mattia
/**
 * 
 * A point in the Euclidean plane. It obviously has two coordinates (x,y)
 * 
 * @author Francesco Burato
 * @version 1
 */
public class EuclideanPoint implements Position {
  private double _x; // abscissas
  private double _y; // ordinates
  private final static double TOLERANCE = 1e-9;

  /**
   * 
   * It builds a new point in a Euclidean point.
   * 
   * @param x
   *          abscissas coordinate
   * @param y
   *          ordinates coordinate
   */
  public EuclideanPoint(double x, double y) {
    _x = x;
    _y = y;
  }

  public EuclideanPoint(EuclideanPoint other) {
    _x = other._x;
    _y = other._y;
  }

  /**
   * 
   * It builds a new point on axes origin.
   * 
   */
  public EuclideanPoint() {
    this(0, 0);
  }

  /**
   * Getter of the x coordinate value
   * 
   * @return x coordinate value
   */
  public double getX() {
    return _x;
  }

  /**
   * Getter of the y coordinate value
   * 
   * @return y coordinate value
   */
  public double getY() {
    return _y;
  }

  /**
   * Comparison of two different Point with a given tolerance
   */
  public boolean equals(Object other) {
    if (other instanceof EuclideanPoint) {
      EuclideanPoint pother = (EuclideanPoint) other;
      return Math.abs(_x - pother._x) < TOLERANCE
          && Math.abs(_y - pother._y) < TOLERANCE;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + _x + "," + _y + ")";
  }
}
