package graph.interfaces;

import graph.support.Arch;

import java.util.ArrayList;

/**
 * A generic Node in a graph
 * 
 * @author Francesco Burato
 * @version 1.0.1
 */
public interface Node {
  /**
   * Returns the symbolic id of the Node
   * 
   * @return The symbolic id of the Node
   */
  String getId();

  /**
   * Modify the symbolic id of the current Node<br>
   * PRE: The Node has been created<br>
   * POST: The Node is the the same state as before the method but has its id
   * set to s
   * 
   * @param s
   *          The new id
   */
  public void setId(String s);

  /**
   * Return every node adjacent to the current with its eventual weight<br>
   * PRE: The Node has been created<br>
   * POST: The Node is in the same state as before the method invocation
   * 
   * @return Contains the copy of all the Arches to the adjacent Nodes.
   */
  public ArrayList<Arch> getAdjacents();

  /**
   * Add an adjacent Node with a given weight to the current
   * 
   * @param a
   *          New arch to add
   * @return True Iff the add has success
   */
  public boolean addAdjacent(Arch a);

  /**
   * Removes a Node from the adjacent set of the current Node
   * 
   * @param n
   *          Reference to the Node to be removed
   * @return True Iff the removal has success
   */
  public boolean removeAdjacent(Node n);
}
