package graph.nodes;

import java.util.*;

import graph.interfaces.Node;
import graph.support.Arch;
import graph.support.BaseArch;

/**
 * A BaseNode is an implementation of Node that provide the following properties
 * on the graph that uses it:<br>
 * <ul>
 * <il>the graph isn't oriented</il> <il>the graph isn't weighed</il> <il>the
 * graph does not allow loops</il>
 * </ul>
 * 
 * @author Francesco Burato
 * @version 1
 */
public class BaseNode implements Node {
  String _id;
  ArrayList<BaseArch> _adjacents;

  /**
   * Create a node with the given id. <br>
   * POST: The created node has the symbolic id specified, has allocated the
   * collection for its adjacent nodes and it hasn't entered any node in it.
   * 
   * @param id
   *          The symbolic id of the BaseNode under construction
   */
  public BaseNode(String id) {
    _id = id;
    _adjacents = new ArrayList<BaseArch>();
  }

  @Override
  public String getId() {
    return _id;
  }

  @Override
  public void setId(String s) {
    _id = s;

  }

  @Override
  public ArrayList<Arch> getAdjacents() {
    return new ArrayList<Arch>(_adjacents);
  }

  /**
   * PRE: The Node is allocated and it is consistent with BaseNode properties, a
   * is a valid Arch and all adjacent nodes are BaseNode that meet properties<br>
   * POST: The Node is still compatible with BaseNode properties. If a isn't an
   * Arch that points to the current Node and it isn't already inside the
   * adjacency collection, it is added to it.
   * 
   * @return False if the specified Arch already exists in the adjacency
   *         collection or if it points to the current Node.<br>
   *         True if the Node wasn't existing and it has been successfully
   *         added.
   * @see graph.interfaces.Node#addAdiacente()
   */
  @Override
  public boolean addAdjacent(Arch a) {
    if (a._node == this)
      return false;
    // it isn't a loop, a is searched in the adjacency collection
    boolean found = _adjacents.contains(a);
    if (found)
      return false;
    // it isn't an existing Arch; so it is added to the collection and returns
    // True
    _adjacents.add(new BaseArch(a._node));
    /*
     * Now, to keep the non-orientation property, the current node is added to
     * node pointed by a
     */
    BaseNode b = (BaseNode) a._node;
    b._adjacents.add(new BaseArch(this));
    return true;
  }

  /**
   * 
   * PRE: The current BaseNode and all neightbours respect graph properties.<br>
   * POST: If n is a Node that was between caller's arches, the caller doesn't
   * anymore have the arch and the node passed as argument is removed from
   * current node neigthbours.
   * 
   * @return True if and only if the removal was successful i.e. if exist
   *         an arch in _adjacents such that a._node == n
   */
  @Override
  public boolean removeAdjacent(Node n) {
    int i = _adjacents.indexOf(new BaseArch(n));// searching for the possible
                                                // presence of n
    if (i != -1) {
      // exists an arch with n as pointer
      BaseNode nb = (BaseNode) _adjacents.get(i)._node;
      _adjacents.remove(i);
      BaseArch ab = new BaseArch(this);
      nb._adjacents.remove(ab);
    }
    return i != -1;
  }

}
