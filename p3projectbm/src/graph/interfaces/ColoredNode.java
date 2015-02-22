/**
 * 
 */
package graph.interfaces;

/**
 * A ColoredNode is a Node associated to a color which is an integer flag
 * 
 * @author Mattia Merotto
 * @version 1.0.1
 * 
 */

public interface ColoredNode extends Node {

  /**
   * Sets the color of the current node
   * 
   * @param c
   *          The color that has to be set
   */
  public void setColor(int c);

  /**
   * Gets the color of the current node
   * 
   * @return The color of the current node
   */
  public int getColor();

  /**
   * Returns whether the current node is colored or not
   * 
   * @return TRUE if the node is colored, FALSE otherwise
   */
  public boolean isColored();

  /**
   * Gets the integer used as null color
   * 
   * @return the integer used as null color
   */
  public int getNullColor();
}
