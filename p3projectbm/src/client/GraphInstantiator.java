package client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import exceptions.WrongDataTypeException;
import graph.nodes.StationNode;
import graph.support.BaseArch;
import graph.support.metrics.EuclideanPoint;
import graph.interfaces.EuclideanNode;

/**
 * A GraphInstantiator produce an instance of graph topology useful for simulate
 * a protocol which works on these characteristics:<br>
 * <ul>
 * <li>1 unit side square area</li>
 * <li>Fixed size limit of transmission range</li>
 * <li>Unmodifiable topology</li>
 * </ul>
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public class GraphInstantiator {
  private int _N;
  private double _ray;
  private double _initialBattery;

  /**
   * Fixes the parameters for the graphs produced by the instance built
   * 
   * @param N
   *          Number of node in the graph
   * @param ray
   *          Maximum distance of transmission range
   * @param initBat
   *          Initial battery of every node
   * @throws NegativeException
   *           If one of the parameters is negative
   */
  public GraphInstantiator(int N, double ray, double initBat)
      throws WrongDataTypeException {
    if (N < 0)
      throw new WrongDataTypeException("N","Positive non-zero integer");
    if (ray < 0)
      throw new WrongDataTypeException("ray","Positive non-zero double");
    _N = N;
    _ray = ray;
    _initialBattery = initBat;
  }

  /**
   * Return a fixed size String representation of i. E.g. if i=3 && fix =3
   * returns 003
   * 
   * @param i
   *          The number to be tabbed
   * @param fix
   *          The size to be reached from the decimal representation of i
   * @return The decimal representation of i with size of at least fix
   */
  private static String name(int i, int fix) {
    String res = "";
    int size = 0, tempI = i;
    do {
      tempI /= 10;
      size++;
    } while (tempI > 0);
    if (size < fix) {
      tempI = 0;
      while (tempI < size){
        res = res + "0";
        tempI++;
      }
    }
    res = res + i;
    return res;
  }

  /**
   * Given the instance variables used in construction return a graph with the
   * requested number of nodes i which will have has neighbor every other nodes
   * j whose position distance from i will be less than the requested maximum
   * ray.
   * 
   * @return The graph with the requested properties
   */
  public StationNode[] buildGraph() {
    // creating a new StationNode[]
    StationNode[] arr = new StationNode[_N];
    int size = 0, tempN = _N;
    // figure out how many figures have _N
    do {
      tempN /= 10;
      size++;
    } while (tempN > 0);
    // creating the stations
    for (int i = 0; i < arr.length; ++i)
      arr[i] = new StationNode(name(i, size), new EuclideanPoint(Math.random(),
          Math.random()), _initialBattery);
    TreeMap<Double, List<EuclideanNode>> ordX = new TreeMap<Double, List<EuclideanNode>>();
    // creating the connection between the stations
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
       * 
       * In the worst case m -> n,alfa -> n, beta->1 so O(log(m) + alfa + beta)
       * = O(log(n) + n + 1) = O(n)
       */
      NavigableMap<Double, List<EuclideanNode>> viciniX = ordX.subMap(arr[i]
          .getPosition().getX() - _ray, true, arr[i].getPosition().getX()
          + _ray, true);

      // Find out the actual adjacent set to the arr[i] Node via calculating the
      // distance
      for (Map.Entry<Double, List<EuclideanNode>> nodo : viciniX.entrySet()) {
        for (Iterator<EuclideanNode> j = nodo.getValue().iterator(); j
            .hasNext();) {
          EuclideanNode currentNode = j.next();
          if (arr[i] != currentNode
              && arr[i].distance(currentNode.getPosition()) <= _ray)
            arr[i].addAdjacent(new BaseArch(currentNode));
        }

      }
    }
    return arr;
  }
}
