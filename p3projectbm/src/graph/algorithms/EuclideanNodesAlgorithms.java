/**
 * 
 */
package graph.algorithms;

import graph.interfaces.*;
import graph.support.Arch;
import graph.support.BaseArch;

import java.util.*;

public class EuclideanNodesAlgorithms {
  /**
   * Build the adjacent set of every Nodes in an array<br>
   * PRE: every element in arr has been instantiated and has its position in the
   * euclidean plan<br>
   * POST: for every j in vett is defined A = j.getAdjacents() s.t. for each i
   * in A j.distance(i)<=r
   * 
   * @author Francesco Burato
   * @version 1.0.1
   * @param arr
   *          Is the array on Node used to construct adjacent sets.
   * @param ray
   *          Is the maximum distance which a Node must have to be in the
   *          adjacent set of another Node
   */
  public static void buildLinks(EuclideanNode[] arr, double ray) {
    /*
     * It build a map of nodes ordered by its x-axis coordinate. There could
     * occur nodes with the same coordinate so it's necessary to associate every
     * coordinate with many objects. Given that in java.util doesn't exists any
     * multiple value map collection, it's necessary to simulate this behavior
     * via a list of Nodes as value.
     */
    TreeMap<Double, List<EuclideanNode>> ordX = new TreeMap<Double, List<EuclideanNode>>();
    // populate a map using arr as source
    // O(n*log(n))
    for (int i = 0; i < arr.length; ++i) {
      List<EuclideanNode> l = ordX.get(arr[i].getPosition().getX());
      if (l == null)
        ordX.put(arr[i].getPosition().getX(),
            l = new LinkedList<EuclideanNode>());
      l.add(arr[i]);
    }
    /*
     * every node of arr has been inserted in the map. The link must be built.
     * O(n*log(n))
     */
    for (int i = 0; i < arr.length; ++i) {
      /*
       * only nodes whose x-axis coordinate is between a given range has the
       * possibility to be put in the adjacent set of a certain node. So we
       * exclude every node which has no possibility to be in the adjacent set
       * via searching in the map
       * 
       * Complexity: O(log(m) + alfa + beta) where m is the number of different
       * x-axis coordinates in the nodes of arr alfa is the number of x-axis
       * coordinates within the range beta is the number of elements in the list
       * In the worst case m -> n,alfa -> n, beta->1 so O(log(m) + alfa + beta)
       * = O(log(n) + n + 1) = O(log(n))
       */
      NavigableMap<Double, List<EuclideanNode>> viciniX = ordX.subMap(arr[i]
          .getPosition().getX() - ray, true, arr[i].getPosition().getX()
          + ray, true);

      // Find out the actual adjacent set to the arr[i] Node via calculating the
      // distance
      for (Map.Entry<Double, List<EuclideanNode>> nodo : viciniX.entrySet()) {
        for (Iterator<EuclideanNode> j = nodo.getValue().iterator(); j
            .hasNext();) {
          EuclideanNode currentNode = j.next();
          if (arr[i] != currentNode
              && arr[i].distance(currentNode.getPosition()) <= ray)
            arr[i].addAdjacent(new BaseArch(currentNode));
        }

      }
    }
  }

  /**
   * 
   * 
   * @author Mattia Merotto
   * @version 1
   * 
   * @param nc
   *          The current ColoredNode
   * @param color
   *          The current color
   */
  public static void paintComponent(ColoredNode nc, int color) {
    if (nc.isColored())
      return;
    nc.setColor(color);
    ArrayList<Arch> reachables = nc.getAdjacents();
    for (int i = 0; i < reachables.size(); ++i) {
      paintComponent((ColoredNode) reachables.get(i)._node, color);
    }
  }

  /**
   * 
   * 
   * @author Mattia Merotto
   * @version 1
   * 
   * @param vect
   *          The set of nodes that have to be colored
   * @return The number of connected components. (1=connected graph; >1=not
   *         connected graph)
   * 
   */
  public static int paintGraph(ColoredNode[] vect) {
    int color = 1;
    for (int i = 0; i < vect.length; ++i) {
      if (!vect[i].isColored()) {
        paintComponent(vect[i], color);
        color++;
      }
    }
    return color - 1;
  }

}
