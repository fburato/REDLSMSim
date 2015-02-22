package exceptions;

/**
 * Exception thrown by client when it realizes that the type associated to a
 * parameter is incompatible with the one expected
 * 
 * @author Francesco Burato
 * @version 1
 */
public class WrongDataTypeException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -3566024941884155995L;

  /**
   * Constructor of the Exception
   * 
   * @param data
   *          Is the name of the parameter
   * @param expectedType
   *          Is the expected type of parameter 'data'
   * 
   */
  public WrongDataTypeException(String data, String expectedType) {
    super("'" + data + "' parameter is wrong. '" + expectedType
        + "' type expected");
  }
}
