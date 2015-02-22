/**
 * 
 */
package threads.LSM;

import threads.REDLSMCommon;
import threads.commonMessages.ClaimMessage;
import threads.commonMessages.ForwardMessage;
import threads.commonOperations.*;
import graph.nodes.StationNode;
import graph.support.metrics.EuclideanPoint;
import threads.Hypervisor;

/**
 * LSMProtocol implementation in the simulation environment proposed in this
 * project
 * 
 * @author Mattia Merotto
 * 
 */
public class LSMProtocol extends REDLSMCommon {

  /**
   * Builds a RED protocol Thread passing to the superclass REDLSMCommon every
   * parameters it requires
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
  public LSMProtocol(StationNode station, Hypervisor controller, int g,
      double receiveEnergy, double sendEnergy, double verificationEnergy,
      double probability) {
    super(station, controller, g, receiveEnergy, sendEnergy,
        verificationEnergy, probability);
  }

  @Override
  protected boolean processForward(ForwardMessage msg) {
    boolean success = true;
    boolean cloneDetected = false;
    VerificationOperation verif = new VerificationOperation(_station,
        _verifCost);
    success = _station.performs(verif);
    if (success) {// the station has enough energy to execute the clone
                  // detection
      _station.incrementVerification();
      //
      cloneDetected = !_station.getRead()
          .find(msg, new CloneIdentificationRule()).isEmpty();
      _station.getRead().addFirst(msg);
    }
    if (success && !cloneDetected) {
      // Clone was not found so station resends the message
      StationNode nearest = calculateNearest(msg.getDestination());
      if (nearest != _station) {
        // The actual station is not the nearest station
        SendOperation send = new SendOperation(nearest, _sendCost, msg);
        success = _station.performs(send);
        if (success) {
          // The actual station has enough energy to send the massege
          ReceiveOperation rec = new ReceiveOperation(nearest, _recCost);
          boolean othersuccess = nearest.performs(rec);
          if (othersuccess)
            // The nearest station has enough energy to receive the message
            nearest.incrementReceived();
          _station.incrementSent();
          if (msg.getClaim().getId().equals("detectMe")) {
            _station.setForwardingCloneMsg();
            _station.generateUpdate(_station.getPosition(),
                nearest.getPosition(), true);
          }
        }
      }
    } else if (success && cloneDetected) {
      // A clone has been detected so the hypervisor must be warn
      _station.setCloneDetected();
      _station.generateUpdate();
      _controller.cloneDetected();
      _station.getRead().addLast(msg);
    }
    return success;
  }

  @Override
  protected boolean processClaim(ClaimMessage c) {
    if (Math.random() > _p) {
      // The station ignores the message
      return true;
    }
    // The station doesn't ignore the message
    boolean success = true;
    VerificationOperation verif = new VerificationOperation(_station,
        _verifCost);
    success = _station.performs(verif);
    for (int i = 0; success && i < _g; ++i) {
      // The actual station has enough energy so it sends to all its neighbors a
      // ForwardMessage with the ClaimMessage and a random position, different
      // for all neightbors
      EuclideanPoint newPosition = new EuclideanPoint(Math.random(),
          Math.random());
      if (c.getId().equals("detectMe")) {
        _station.generateUpdate(newPosition);
      }
      ForwardMessage forward = new ForwardMessage(c, newPosition);
      success = processForward(forward);
    }
    return success;
  }

}
