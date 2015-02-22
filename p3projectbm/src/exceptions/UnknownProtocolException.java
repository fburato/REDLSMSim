package exceptions;

/**
 * Exception thrown by ProtocolFactory when the requested protocol is unknown
 * 
 * @author Francesco Burato
 * @version 1
 */
public class UnknownProtocolException extends Exception {

  private static final long serialVersionUID = 7506728540754429051L;

  /**
   * Constructor of the exception which the factory is not able to process
   * 
   * @param p
   *          Name of the protocol requested
   */
  public UnknownProtocolException(String p) {
    super("Not able to handle a protocol named '" + p + "'");
  }
}
