package test.burato;

import graph.algorithms.*;
import graph.interfaces.*;
import graph.nodes.*;
import graph.support.Arch;
import graph.support.metrics.EuclideanPoint;

import java.util.*;
public class ProvaAlgoConnessione {
  private StationNode _grafo[];
  public ProvaAlgoConnessione(){
  }
  
  public void init() {
 // creazione di una topologia di nodi
    double x[] = {1,2,1.5,2.5,4,5,2,1};
    double y[] = {1,1,1.5,2.5,1,1,2,2};
    // alloco il grafo
    _grafo = new StationNode[x.length];
    for(int i = 0; i < x.length; ++i) 
      _grafo[i] = new StationNode(""+i, new EuclideanPoint(x[i],y[i]),0);
  }
  
  private static void stampaAdiacenti(Node n) {
    System.out.print(n.getId() + "-> {");
    ArrayList<Arch> adiacenti = n.getAdjacents();
    for(int i = 0; i < adiacenti.size(); ++i)
      System.out.print(adiacenti.get(i)._node.getId()+ " ");
    System.out.println("}");
  }
  
  private void stampaGrafo(){
    for(int i = 0; i < _grafo.length; ++i) {
      stampaAdiacenti(_grafo[i]);
    }
  }
  
  public void testCotruzione(){
    init();
    stampaGrafo();
  }
  
  public void testAlgoritmoEuclide(){
    init();
    EuclideanNodesAlgorithms.buildLinks(_grafo,2);
    stampaGrafo();
  }
  public static void main(String argv[]) {
    ProvaAlgoConnessione p = new ProvaAlgoConnessione();
    p.testCotruzione();
    p.testAlgoritmoEuclide();
  }
}
