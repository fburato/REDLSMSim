/**
 * 
 */
package graph.interfaces;

import graph.support.communications.Message;

/**
 * A ComunicationNode is a Node capable of receiving messages. It will have a
 * MailBox to memorize incoming messages.
 * 
 * @author Francesco Burato
 * @version 1.0.1
 * 
 */
public interface CommunicationNode extends Node {
  /**
   * Adds to the current Node Mailbox a message
   * 
   * @param m
   *          The message to be added to the MailBox
   */
  public void putMessage(Message m);
}
