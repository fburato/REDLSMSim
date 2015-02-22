/**
 * 
 */
package exceptions;

/**
 * Exception thrown by ProtocolFactory when it realizes that the protocol has
 * not been instantiated yet
 * 
 * @author Francesco Burato
 * @version 1
 */
public class UninitializedProtocolException extends Exception {

  private static final long serialVersionUID = -232695565434182789L;

  /**
   * Constructor which simply communicates that the factory has not yet been
   * initialized.
   */
  public UninitializedProtocolException() {
    super("Protocol has not yet been initialized");
  }
}
