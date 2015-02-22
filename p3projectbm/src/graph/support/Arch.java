package graph.support;

import graph.interfaces.Node;

/**
 * An arch, possibly weighed, which connect two nodes
 * 
 * @author Francesco Burato
 * @version 1.0.1
 */
public class Arch {
  public double _cost;
  public Node _node;

  public Arch(double cost, Node adjacent) {
    _cost = cost;
    _node = adjacent;
  }

  public boolean equals(Object o) {
    if (!(o instanceof Arch))
      return false;
    return _node == ((Arch)o)._node;
  }
}
