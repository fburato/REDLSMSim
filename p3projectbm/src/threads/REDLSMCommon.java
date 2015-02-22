package threads;

import threads.ProtocolThread;
import graph.nodes.StationNode;
import graph.support.Arch;
import graph.support.communications.Message;
import graph.support.metrics.EuclideanPoint;
import threads.Hypervisor;
import threads.commonMessages.*;
import threads.commonOperations.*;

import java.util.ArrayList;

/**
 * This class is a direct subclass of ProtocolThread because it defines the
 * common operations that RED and LSM protocol must perform. It's clear that the
 * two of them are different just in the treatment of the different messages
 * that are generated be the protocol itself. So the common operations are in
 * this class while the specific operations should be overrided by two different
 * classes.
 * 
 * @author Francesco Burato
 * @version 1
 */
public abstract class REDLSMCommon extends ProtocolThread {
  /*
   * (non-Javadoc)
   * 
   * @see threads.ProtocolThread#protocol()
   */
  protected int _g;
  protected double _recCost;
  protected double _sendCost;
  protected double _verifCost;
  protected boolean _firstTime;
  protected double _p;

  /**
   * Common builder of LSM and RED protocol which accept all the common
   * parameters.
   * 
   * @param station
   *          The station on which the protocol must run
   * @param controller
   *          The Hypervisor of the simulation
   * @param g
   *          The number of different position to which forward messages
   * @param receiveEnergy
   *          Cost for station to receive a message
   * @param sendEnergy
   *          Cost for station to send a message or many messages
   * @param verificationEnergy
   *          Cost for station to verify a signature
   * @param probability
   *          The probability of the thread to participate to the protocol when
   *          a ClaimMessage arrives
   */
  public REDLSMCommon(StationNode station, Hypervisor controller, int g,
      double receiveEnergy, double sendEnergy, double verificationEnergy,
      double probability) {
    super(station, controller);
    _g = g;
    _recCost = receiveEnergy;
    _sendCost = sendEnergy;
    _verifCost = verificationEnergy;
    _firstTime = true;
    _p = probability;
  }

  /**
   * Execute the part of RED or LSM dedicated to the reception of a
   * ForwardMessage
   * 
   * @param msg
   *          ForwardMessage to be routed in the graph
   * @return true iff every operation involved with this part of the protocol
   *         has successfully been completed
   */
  protected abstract boolean processForward(ForwardMessage msg);

  /**
   * Execute the part of RED or LSM dedicated to the reception of a ClaimMessage
   * 
   * @param c
   *          The ClaimMessage to process
   * @return true iff every operation involved with this part of the protocol
   *         has successfully been completed
   */
  protected abstract boolean processClaim(ClaimMessage c);

  /**
   * Returns the nearest station to point p among {_station} U _stattion
   * adjacent set
   * 
   * @param p
   *          The point which is used to calculate the distance
   * @return _station StationNode if it is the nearest Station to p, the nearest
   *         neighbor else
   */
  protected StationNode calculateNearest(EuclideanPoint p) {
    // I must calculate the nearest neighbor that is still alive
    ArrayList<Arch> adjacent = _station.getAdjacents();
    StationNode nearest = _station;
    double distance = nearest.distance(p);
    for (Arch arch : adjacent) {
      // DownCast to StationNode of Node in arch
      StationNode actual = (StationNode) arch._node;
      if (actual.getBattery() > 0 && actual.distance(p) < distance) {
        // actual is alive and is nearer than the nearest found so far
        distance = actual.distance(p);
        nearest = actual;
      }
    }
    return nearest;
  }

  @Override
  public void protocol() {
    boolean success = true;
    if (_firstTime) {
      // this is the first time the protocol has entered in this state. It must
      // Claim to its neighbors its identity and position
      ClaimMessage claim = new ClaimMessage(_station.getId(),
          _station.getPosition());
      SendAllOperation sendAll = new SendAllOperation(_station, _sendCost
          + _verifCost, claim);
      success = _station.performs(sendAll);
      if (success) {
        ArrayList<Arch> adjacents = _station.getAdjacents();
        for (Arch a : adjacents) {
          StationNode s = (StationNode) a._node;
          ReceiveOperation rec = new ReceiveOperation(s, _recCost);
          boolean othersuccess = s.performs(rec);
          if (othersuccess)
            s.incrementReceived();
        }
      }
      if (success && _station.getId().equals("detectMe")) {
        ArrayList<Arch> adj = _station.getAdjacents();
        for (Arch arc : adj) {
          StationNode s = (StationNode) arc._node;
          _station.generateUpdate(_station.getPosition(), s.getPosition(),
              false);
        }
      }
      if (success)
        _station.incrementSent();
      _firstTime = false;
    } else {
      // from the second time on the flow of operation must be the same.
      // First of all I must find out if there is any message to process
      synchronized (_station.getIncoming()) {
        try {
          while (_station.getIncoming().isEmpty()) {
            _controller.incEmptyMailBoxStations();
            _station.getIncoming().wait();
            _controller.decEmptyMailBoxStations();
          }
        } catch (InterruptedException e) {
        }
      }
      // POST: The MailBox is not empty anymore or a clone has been detected
      // Now I must distinguish if there is a TerminationMessage as first
      // message in the incoming MailBox. If this is true it means that
      // Hypervisor has recognized that the simulation has to be terminated
      boolean terminationSignaled = false;
      if (_station.getIncoming().getFirst() instanceof TerminationMessage) {
        terminationSignaled = true;
        _station.getIncoming().clear();
      }
      _station.waitListeners();
      if (!terminationSignaled) {
        // The first message is not a TerminationSignal. It means that there is
        // a request by another RedProtocol to execute some instruction. I have
        // to find out what kind of request it is.

        // Instantiating a ReceiveOperation to get the Message
        Message msg = _station.getIncoming().getFirst();
        if (success) {
          // If the receive was a success it means I have to do different thing
          // upon the Message Received
          if (msg instanceof ClaimMessage) {
            success = processClaim((ClaimMessage) msg);
          }
          if (msg instanceof ForwardMessage) {
            success = processForward((ForwardMessage) msg);
          }
          _station.getIncoming().removeFirst();
        }
      }
    }
    if (!success) {
      // one of the operation has gone bad or a clone has been detected... no
      // other operation should be executed
      _station.setBattery(0);
      _station.getIncoming().clear();
    }
    if (_controller.getCloneDetected()) {
      _station.getIncoming().clear();
    }
  }

}
