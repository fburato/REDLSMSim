/**
 * 
 */
package gui;

import javax.swing.JPanel;
import graph.nodes.StationNode;
import graph.support.Arch;
import graph.support.UpdateListener;
import graph.support.metrics.EuclideanPoint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import client.interfaces.NewGraphListener;

import java.util.Dictionary;
import java.util.Hashtable;
import java.awt.BasicStroke;

/**
 * GraphPanel is the drawing desktop in which is drawn a visual representation
 * of a graph so of an array of StationNode. It has a internal daemon used to
 * update the state of the panel which executes only when necessary
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public class GraphPanel extends JPanel implements NewGraphListener,
    UpdateListener {
  /**
   * Internal class used to manage the request of different colored segment in
   * the drawing of the graph state
   * 
   * @author Francesco Burato
   * @version 1
   * 
   */
  private class Segment {
    public EuclideanPoint _first;
    public EuclideanPoint _second;

    public Segment(EuclideanPoint f, EuclideanPoint s) {
      _first = new EuclideanPoint(f);
      _second = new EuclideanPoint(s);
    }

    public boolean equals(Object o) {
      if (!(o instanceof Segment))
        return false;
      Segment other = (Segment) o;
      return _first.equals(other._first) && _second.equals(other._second);
    }
  }

  /**
   * Internal daemon which executes the update of the graph when at least one
   * modification on node's flag has been performed or one message of forward
   * from a clone has been forwarded
   * 
   * @author Francesco Burato
   * @version 1
   * 
   */
  /*
   * private class UpdateDaemon extends Thread { public void run() { while
   * (true) { synchronized (GraphPanel.this) { if (_update) repaint(); _update =
   * false; } try { sleep(40); } catch (InterruptedException e) {
   * 
   * } } } }
   */

  private StationNode[] _graph; // the graph to be used for drawings
  private int _nodesSize; // size of the circles representing nodes
  private int _crossSize;
  /*
   * flag used to determine if every arch connecting the nodes must be drawn or
   * not
   */
  private boolean _printArch;
  private boolean _isExecuting;
  /*
   * flag used by daemon to determine if an update must be performed or not
   */
  // private boolean _update;
  // private UpdateDaemon _daemon; // update daemon
  private ArrayList<Segment> _segments; // list of GREEN segment to be drawn
  private ArrayList<EuclideanPoint> _dests;
  private long _ms;
  /**
   * 
   */
  private static final long serialVersionUID = -4512219623781217332L;

  /**
   * Build a new GraphPanel with the given initial graph and node points size.
   * It also instantiate every data structure needed by repaint to operate
   * 
   * @param g
   *          Initial graph
   * @param nodesSize
   *          Size of circles to represent the nodes
   */
  public GraphPanel(StationNode[] g, int nodesSize,int crossSize) {
    super();
    _nodesSize = nodesSize;
    _crossSize = crossSize;
    _graph = g;
    _printArch = true;
    // _update = false;
    _segments = new ArrayList<Segment>();
    _dests = new ArrayList<EuclideanPoint>();
    _ms = 0;
    // _daemon = new UpdateDaemon();
    // _daemon.setDaemon(true);
    // _daemon.start();
  }

  /**
   * Update the circle size for node and repaints the graph
   * 
   * @param nodesSize
   *          New dimensions of the circles. At converts any negative number
   *          passed to the equivalent (in the case that the user used ad
   *          dimension calculated as ray from the origin of a Euclidean space
   *          in the second quadrant)
   */
  public void setNodesSize(int nodesSize) {
    _nodesSize = Math.abs(nodesSize);
    repaint();
  }
  
  public void setCrossSize(int crossSize) {
    _crossSize = Math.abs(crossSize);
    repaint();
  }

  /**
   * Update the flag which indicates if the graph must be shown connected or not
   * by archs
   * 
   * @param value
   *          New value of the flag
   */
  public void setPrintArch(boolean value) {
    _printArch = value;
    repaint();
  }

  /**
   * Set the graph used by repaint and repaint the drawing
   * 
   * @param g
   *          The new graph to be rendered
   */
  public synchronized void setGraph(StationNode[] g) {
    _graph = g;
    repaint();
  }

  public void graphChanged(StationNode[] g) {
    setGraph(g);
  }

  public synchronized void updateLaunched() {
    repaint();
  }

  @SuppressWarnings("static-access")
  public synchronized void updateLaunched(EuclideanPoint s, EuclideanPoint d,boolean doSleep) {
    Segment seg = new Segment(s, d);
    synchronized (_segments) {
      if (!_segments.contains(seg))
        _segments.add(seg);
    }
    repaint();
    try{
      if(doSleep && _ms>0)
        Thread.currentThread().sleep(_ms);
    }catch(InterruptedException e) {}
  }

  public synchronized void updateLaunched(EuclideanPoint d) {
    synchronized (_dests) {
      if (!_dests.contains(d))
        _dests.add(d);
    }
    repaint();
  }

  @SuppressWarnings("static-access")
  public synchronized void clearState() {
    synchronized (_segments) {
      _segments.clear();
    }
    synchronized (_dests) {
      _dests.clear();
    }
    repaint();
    try{
      if(_ms>0)
        Thread.currentThread().sleep(_ms);
    }catch(InterruptedException e) {}
  }

  @SuppressWarnings("static-access")
  public void paint(Graphics g) {
    super.paint(g);
    if (_graph != null) {
      setExecuting(true);
      Graphics2D g2d = (Graphics2D) g;
      // calculating the size of the panel to make a graph proportional to it
      final int xSize = getSize().width;
      final int ySize = getSize().height;
      // arch drawing
      // boolean[] found = new boolean[_graph.length];
      // determining which color should assume a given arch
      if (_printArch) {
        Dictionary<StationNode, Integer> d = new Hashtable<StationNode, Integer>(
            _graph.length);
        for (int i = 0; i < _graph.length; ++i) {
          ArrayList<Arch> adjacents = _graph[i].getAdjacents();
          for (Arch arch : adjacents) {
            StationNode destNode = (StationNode) arch._node;
            EuclideanPoint source = _graph[i].getPosition(), destination = destNode
                .getPosition();
            if (d.get(destNode) == null) {
              Line2D line = new Line2D.Double(source.getX()
                  * (xSize - _nodesSize) + _nodesSize / 2, source.getY()
                  * (ySize - _nodesSize) + _nodesSize / 2, destination.getX()
                  * (xSize - _nodesSize) + _nodesSize / 2, destination.getY()
                  * (ySize - _nodesSize) + _nodesSize / 2);
              g2d.setColor(Color.BLACK);
              g2d.draw(line);
            }
          }
          d.put(_graph[i], new Integer(1));
        }
      }
      // overwriting the colored arch
      synchronized (_segments) {
        for (Segment seg : _segments) {
          Line2D line = new Line2D.Double(seg._first.getX()
              * (xSize - _nodesSize) + _nodesSize / 2, seg._first.getY()
              * (ySize - _nodesSize) + _nodesSize / 2, seg._second.getX()
              * (xSize - _nodesSize) + _nodesSize / 2, seg._second.getY()
              * (ySize - _nodesSize) + _nodesSize / 2);
          g2d.setColor(Color.GREEN);
          g2d.setStroke(new BasicStroke(2F));
          g2d.draw(line);
        }
      }
      // creating an ellipse of the correct size for each of the node
      for (int i = 0; i < _graph.length; ++i) {
        // creating the ellipse
        Shape s = new Ellipse2D.Double(_graph[i].getPosition().getX()
            * (xSize - _nodesSize), _graph[i].getPosition().getY()
            * (ySize - _nodesSize), _nodesSize, _nodesSize);
        // determining which color should have the ellipses
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke());
        g2d.draw(s);
        if (_graph[i].getDetectClone())
          g2d.setColor(Color.RED);
        else if (_graph[i].getId().equals("detectMe"))
          g2d.setColor(Color.BLUE);
        else if (_graph[i].getForwardingCloneMsg())
          g2d.setColor(Color.GREEN);
        g2d.fill(s);
      }
      synchronized (_dests) {
        for (EuclideanPoint dest : _dests) {
          Line2D first = new Line2D.Double(dest.getX() * (xSize - _crossSize)
              - _crossSize / 2, dest.getY() * (ySize - _crossSize) - _crossSize
              / 2, dest.getX() * (xSize - _crossSize) + _crossSize / 2,
              dest.getY() * (ySize - _crossSize) + _crossSize / 2),
          second = new Line2D.Double(dest.getX() * (xSize - _crossSize)
              + _crossSize / 2, dest.getY() * (ySize - _crossSize) - _crossSize
              / 2, dest.getX() * (xSize - _crossSize) - _crossSize / 2,
              dest.getY() * (ySize - _crossSize) + _crossSize / 2);
          g2d.setColor(Color.RED);
          g2d.setStroke(new BasicStroke(2F));
          g2d.draw(first);
          g2d.draw(second);
        }
      }
      try{
        if(_ms>0)
          Thread.currentThread().sleep(_ms);
      }catch(InterruptedException e) {}
      setExecuting(false);
    }
  }
  
  public synchronized void setExecuting(boolean value) {
    _isExecuting = value;
    if(!_isExecuting)
      notifyAll();
  }
  
  public synchronized boolean isExecuting() {
    return _isExecuting;
  }
  
  public synchronized void setSleepTime(long ms) {
    if(ms>=0)
      _ms = ms;
  }
}
