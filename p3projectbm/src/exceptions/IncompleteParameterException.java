/**
 * 
 */
package exceptions;

/**
 * Exception thrown by client when it realizes that some parameter is absent
 * 
 * @author Francesco Burato
 * @version
 */
public class IncompleteParameterException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -7609052902792024212L;

  /**
   * Construct an exception which report that a parameter is missing from the
   * data passed to the client
   * 
   * @param name
   *          Name of the missing parameter
   */
  public IncompleteParameterException(String name) {
    super("'" + name + "' parameter missing in parsed file");
  }
}
