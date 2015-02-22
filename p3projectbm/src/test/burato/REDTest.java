package test.burato;

import graph.algorithms.EuclideanNodesAlgorithms;
import graph.interfaces.Node;
import graph.nodes.StationNode;
import graph.support.Arch;
import graph.support.metrics.EuclideanPoint;
import threads.*;
import threads.RED.*;
import java.util.ArrayList;
public class REDTest {
  // parte grafo
  private StationNode _grafo[];

  // parte thread
  private Hypervisor _controller;
  private RedProtocol[] _threads;
  // parte protocollo
  private int _g = 1;
  private double _recCost = 10;
  private double _sendCost = 20;
  private double _verifCost = 50;
  private double _p = 1;
  private double _initialBattery = 32000;

  public REDTest() {
    init(); // inizializza grafo
  }

  public void init() {
    // creazione di una topologia di nodi
    //double x[] = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9 };
    //double y[] = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.4, 0.3, 0.2, 0.1 };
    double x[] = { 0.1, 0.2, 0.3};
    double y[] = { 0.1, 0.2, 0.1};
    // alloco il grafo
    _grafo = new StationNode[x.length];
    for (int i = 0; i < x.length; ++i)
      _grafo[i] = new StationNode("" + i, new EuclideanPoint(x[i], y[i]),
          _initialBattery);
    // creo i thread necessari alla simulazione
    EuclideanNodesAlgorithms.buildLinks(_grafo, 0.15);
    _controller = new Hypervisor(_grafo);
    _threads = new RedProtocol[_grafo.length];
    Satellite sat = new Satellite();
    for (int i = 0; i < _grafo.length; ++i)
      /*
       * public RedProtocol(StationNode station, Hypervisor controller, int g,
       * double receiveEnergy, double sendEnergy, double verificationEnergy,
       * double probability, PseudoRandomTable table)
       */
      _threads[i] = new RedProtocol(_grafo[i], _controller, _g, _recCost,
          _sendCost, _verifCost, _p, sat);
  }

  private static void stampaAdiacenti(Node n) {
    System.out.print(n.getId() + "-> {");
    ArrayList<Arch> adiacenti = n.getAdjacents();
    for (int i = 0; i < adiacenti.size(); ++i)
      System.out.print(adiacenti.get(i)._node.getId() + " ");
    System.out.println("}");
  }

  private void stampaGrafo() {
    for (int i = 0; i < _grafo.length; ++i) {
      stampaAdiacenti(_grafo[i]);
    }
  }

  public void testCotruzione() {
    stampaGrafo();
  }
  
  public void testStartsimulation() {
    _controller.start();
    for(int i = 0; i < _threads.length; ++i)
      _threads[i].start();
  }

  public static void main(String argv[]) {
    REDTest p = new REDTest();
    p.testCotruzione();
    p.testStartsimulation();
  }
}
