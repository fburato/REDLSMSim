/**
 * 
 */
package threads.RED;

import threads.REDLSMCommon;
import graph.nodes.StationNode;
import graph.support.metrics.EuclideanPoint;
import threads.Hypervisor;
import threads.commonMessages.*;
import threads.commonOperations.*;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * REDProtocol implementation in the simulation environment proposed in this
 * project
 * 
 * @author Francesco Burato
 * @version 1
 */
public class RedProtocol extends REDLSMCommon {

  /*
   * (non-Javadoc)
   * 
   * @see threads.ProtocolThread#protocol()
   */
  private Satellite _satellite;

  /**
   * Builds a RED protocol Thread passing to the superclass REDLSMCommon every
   * parameters it requires and instantiating the reference to the Satellite.
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
   * @param table
   *          The PseudoRandomFunction which is common to every RedProtocol
   */
  public RedProtocol(StationNode station, Hypervisor controller, int g,
      double receiveEnergy, double sendEnergy, double verificationEnergy,
      double probability, Satellite s) {
    super(station, controller, g, receiveEnergy, sendEnergy,
        verificationEnergy, probability);
    _satellite = s;
  }

  /**
   * Execute the part of RED dedicated to the reception of a ClaimMessage
   * 
   * @param c
   *          The ClaimMessage to process
   * @return true iff every operation involved with this part of the protocol
   *         has successfully been completed
   */
  protected boolean processClaim(ClaimMessage c) {
    // I have to treat the ClaimMessage
    if (Math.random() > _p) {
      // the node will ignore the message, so the treatment is positive
      return true;
    }
    // the node won't ignore the message. It has to select _g different
    // destination decided by the PseudoRandomTable
    boolean success = true;
    VerificationOperation verif = new VerificationOperation(_station,
        _verifCost);
    success = _station.performs(verif);
    // creating pseudo-random generator
    Random rand = new Random(hashFunction(c.getId()) + _satellite.getValue());
    for (int i = 0; success && i < _g; ++i) {
      EuclideanPoint newPosition = new EuclideanPoint(rand.nextDouble(),
          rand.nextDouble());
      if (c.getId().equals("detectMe")) {
        _station.generateUpdate(newPosition);
      }
      ForwardMessage forward = new ForwardMessage(c, newPosition);
      success = processForward(forward);
    }
    return success;
  }

  /**
   * Execute the part of RED dedicated to the reception of a ForwardMessage
   * 
   * @param msg
   *          ForwardMessage to be routed in the graph
   * @return true iff every operation involved with this part of the protocol
   *         has successfully been completed
   */
  protected boolean processForward(ForwardMessage msg) {
    // determine whether or not the message must by sent or a verification must
    // be done
    boolean success = true;
    // get the nearest station to the position specified in msg
    StationNode nearest = calculateNearest(msg.getDestination());
    if (nearest != _station) {
      // the actual station is not the nearest station. msg must be forwarded
      SendOperation send = new SendOperation(nearest, _sendCost, msg);
      success = _station.performs(send);
      if (success) {
        ReceiveOperation rec = new ReceiveOperation(nearest, _recCost);
        boolean othersuccess = nearest.performs(rec);
        if (othersuccess)
          nearest.incrementReceived();
        _station.incrementSent();
        if (msg.getClaim().getId().equals("detectMe")) {
          _station.setForwardingCloneMsg();
          _station.generateUpdate(_station.getPosition(),
              nearest.getPosition(), true);
        }
      }
    } else {
      // the actual station is the nearest. I must verify if there is a clone
      // and then store the message after having verified the signature
      VerificationOperation verif = new VerificationOperation(_station,
          _verifCost);
      success = _station.performs(verif);
      if (success) {
        _station.incrementVerification();
        boolean cloneDetected = !_station.getRead()
            .find(msg, new CloneIdentificationRule()).isEmpty();
        _station.getRead().addFirst(msg);
        if (cloneDetected) {
          // if a clone has been detected I must warn the hypervisor
          _controller.cloneDetected();
          _station.setCloneDetected();
          _station.generateUpdate();
        }
      }
    }
    return success;
  }

  /**
   * Hash function which accept a string and returns a long between 0 and 2^48-1
   * because this is the highest value accepted from the pseudorandom number
   * generator java.util.Random. In this way the uniform distribution of values
   * is ensured as much as possible. This method uses md5 cryptographic hash to
   * get potentially very different values even with short strings and very
   * similar ones.
   * 
   * @param s
   * @return
   */
  private static long hashFunction(String s) {
    MessageDigest dig = null;
    try {
      dig = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      // never happens
    }
    // calculating hash of string
    byte[] arr = dig.digest(s.getBytes());
    long res = 0;
    long max = (1L << 48);
    for (int i = 0; i < arr.length; ++i) {
      res = (31 * res + (arr[i] + 256)) % max;
    }
    return res;
  }
}
