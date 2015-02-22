/**
 * 
 */
package graph.nodes;

import graph.interfaces.EuclideanNode;
import graph.interfaces.ColoredNode;
import graph.interfaces.CommunicationNode;
import graph.interfaces.OperationalNode;
import graph.support.UpdateLauncher;
import graph.support.UpdateListener;
import graph.support.communications.MailBox;
import graph.support.communications.Message;
import graph.support.metrics.EuclideanPoint;
import graph.support.metrics.Position;
import graph.support.operations.Operation;

import java.util.List;
import java.util.LinkedList;

/**
 * @author Merotto feat. Burato
 * 
 */
public class StationNode extends BaseNode implements ColoredNode,
    CommunicationNode, EuclideanNode, OperationalNode, UpdateLauncher {
  private final int NULL_COLOR = 0;
  private int _color; // implementation ColoredNode
  private MailBox _incoming; // implementation CommunicationNode
  private MailBox _read; // implementation CommunicationNode
  private EuclideanPoint _position; // implementation EuclideanNode
  private double _battery; // implementation OperationalNode
  private double _initialBattery;
  private int _sentMessages; // number of sent messages
  private int _signaturesCheck; // number of signature verified
  private int _recMessages; // number of messages received
  private boolean _forwardingCloneMsg;
  private boolean _detectClone;
  private List<UpdateListener> _listeners;

  /**
   * Builds the StationNode with the given id, position and initial battery
   * 
   * @param id
   *          The id of the node
   * @param pos
   *          The position in the Euclidean plane
   * @param bat
   *          The value of initial battery
   */
  public StationNode(String id, EuclideanPoint pos, double bat) {
    super(id);
    _color = NULL_COLOR; // absent color
    _incoming = new MailBox();
    _read = new MailBox();
    _position = new EuclideanPoint(pos);
    _battery = _initialBattery = bat;
    _sentMessages = 0;
    _signaturesCheck = 0;
    _forwardingCloneMsg = false;
    _detectClone = false;
    _listeners = new LinkedList<UpdateListener>();
  }

  /**
   * We assume that each Node is an EuclideanNode<br>
   * PRE: Position given is an EuclideanPoint
   * 
   * @see graph.interfaces.MetricNode#distance(graph.support.metrics.Position)
   */
  @Override
  public double distance(Position p) {
    EuclideanPoint newp = (EuclideanPoint) p;// cast has success for
                                             // precondition
    return Math.sqrt((newp.getX() - _position.getX())
        * (newp.getX() - _position.getX()) + (newp.getY() - _position.getY())
        * (newp.getY() - _position.getY())); // compute the distance as
                                             // pitagorean distance
  }

  @Override
  public double getBattery() {
    return _battery;
  }

  /**
   * Returns the energy value with which the Station has been initialized
   * 
   * @return Original battery level
   */
  public double getInitBattery() {
    return _initialBattery;
  }

  @Override
  public void setBattery(double d) {
    _battery = d;
  }

  @Override
  public void resetBattery() {
    _battery = _initialBattery;
  }

  @Override
  public synchronized boolean performs(Operation o) {
    if (o.getCost() <= _battery) {
      // c'e' abbastanza batteria -> eseguo l'operazione e scalo la batteria
      _battery -= o.getCost();
      o.execute();
      return true;
    }
    return false;
  }

  @Override
  public EuclideanPoint getPosition() {
    return new EuclideanPoint(_position);
  }

  @Override
  public void putMessage(Message m) {
    _incoming.addLast(m);
  }

  @Override
  public void setColor(int c) {
    _color = c;
  }

  @Override
  public int getColor() {
    return _color;
  }

  @Override
  public boolean isColored() {
    return _color != NULL_COLOR;
  }

  @Override
  public int getNullColor() {
    return NULL_COLOR;
  }

  @Override
  public String toString() {
    return _id + "(" + _position.getX() + "," + _position.getY() + ")";
  }

  /**
   * Get the incoming MailBox of the current StationNode
   * 
   * @return Reference to the incoming MailBox of the StationNode
   */
  public MailBox getIncoming() {
    return _incoming;
  }

  /**
   * Get the read MailBox of the current StationNode
   * 
   * @return Reference to the read MailBox of the StationNode
   */
  public MailBox getRead() {
    return _read;
  }

  /**
   * Number of sent messages of the current StationNode
   * 
   * @return Number of sent messages
   */
  public int getSentMessages() {
    return _sentMessages;
  }

  /**
   * Increments the number of sent messages of the current StationNode
   */
  public void incrementSent() {
    _sentMessages++;
  }

  /**
   * Reset the state of the current StationNode to a fresh one
   */
  public void reset() {
    _incoming.clear();
    _read.clear();
    _sentMessages = 0;
    _signaturesCheck = 0;
    resetBattery();
  }

  /**
   * Return the amount of signature verification performed successfully by the
   * node
   * 
   * @return Number of signature verification operation executed
   */
  public int getVerification() {
    return _signaturesCheck;
  }

  /**
   * Increment the number of verified messages of the current station
   */
  public void incrementVerification() {
    _signaturesCheck++;
  }

  /**
   * Return the amount of signature verification performed successfully by the
   * node
   * 
   * @return Number of signature verification operation executed
   */
  public int getReceived() {
    return _recMessages;
  }

  /**
   * Increment the number of verified messages of the current station
   */
  public synchronized void incrementReceived() {
    _recMessages++;
  }

  @Override
  public void addUpdateListener(UpdateListener u) {
    if (u != null && !_listeners.contains(u)) {
      _listeners.add(u);
    }
  }

  @Override
  public void generateUpdate(EuclideanPoint s, EuclideanPoint d, boolean doSleep) {
    for (UpdateListener u : _listeners)
      synchronized (u) {
        u.updateLaunched(s, d, doSleep);
      }

  }

  @Override
  public void generateUpdate(EuclideanPoint d) {
    for (UpdateListener u : _listeners)
      synchronized (u) {
        u.updateLaunched(d);
      }
  }

  @Override
  public void generateUpdate() {
    for (UpdateListener u : _listeners)
      synchronized (u) {
        u.updateLaunched();
      }
  }

  /**
   * Sets the flag which indicates that this node has been asked to forward a
   * message coming from a clone
   */
  public synchronized void setForwardingCloneMsg() {
    if (!_forwardingCloneMsg) {
      _forwardingCloneMsg = true;
      generateUpdate();
    }
  }

  /**
   * Sets the flag which indicates that the protocol running on this node found
   * an evidence about the presence of a clone in the network
   */
  public synchronized void setCloneDetected() {
    if (!_detectClone) {
      _detectClone = true;
      generateUpdate();
    }
  }
  /**
   * Get the flag which indicates that this node has been asked to forward a
   * message coming from a clone
   * @return Value of the flag
   */
  public synchronized boolean getForwardingCloneMsg() {
    return _forwardingCloneMsg;
  }
  /**
   * Sets the flag which indicates that the protocol running on this node found
   * an evidence about the presence of a clone in the network
   * @return Value of the flasg
   */
  public synchronized boolean getDetectClone() {
    return _detectClone;
  }
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof StationNode))
      return false;
    return this == o;
  }

  @Override
  public void waitListeners() {
    for (UpdateListener u : _listeners)
      synchronized (u) {
        try {
          while (u.isExecuting())
            u.wait();
        } catch (InterruptedException e) {
        }
      }
  }
}
