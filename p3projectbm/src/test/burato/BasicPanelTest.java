package test.burato;

import graph.nodes.*;
import javax.swing.*;
import client.GraphInstantiator;
import gui.*;

public class BasicPanelTest {
  private StationNode[] _graph;

  public BasicPanelTest() {
    init();
  }

  public void init() {
    GraphInstantiator inst = null;
    try {
      inst = new GraphInstantiator(100, 0.1, 10);
    } catch (Exception e) {
    }
    _graph = inst.buildGraph();
    _graph[0].setId("detectMe");
    _graph[1].setId("detectMe");
    ((StationNode)(_graph[0].getAdjacents().get(0)._node)).setForwardingCloneMsg();
    // inizializzo la finestra
    JFrame frame = new JFrame("prova");
    GraphPanel panel = new GraphPanel(_graph, 10,10);
    panel.setPrintArch(true);
    frame.add(panel);
    frame.setVisible(true);
  }

  public static void main(String argv[]) {
    BasicPanelTest b = new BasicPanelTest();
    String s = b.toString();
    s.length();
  }
}
