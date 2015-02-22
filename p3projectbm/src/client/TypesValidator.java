/**
 * 
 */
package client;

import parser.Parser;
import exceptions.IncompleteParameterException;
import exceptions.WrongDataTypeException;

/**
 * A set of commonly used method for retrieve from the parser a particular kind
 * of data
 * 
 * @author Francesco Burato
 * @version 1
 */
public class TypesValidator {
  /**
   * Getter of a generic key from parser which must be a positive non-zero
   * integer
   * 
   * @param keyName
   *          Name of the key which must be retrieved from parser
   * @return Integer retrieved from parser
   * @throws WrongDataTypeException
   *           value associated to keyName is not a positive non-zero integer
   * @throws IncompleteParameterException
   *           keyName is not present in the parser memory
   */
  public static Integer getPositiveInteger(String keyName, Parser parser)
      throws WrongDataTypeException, IncompleteParameterException {
    String value = parser.getValues(keyName);
    if (value == null)
      throw new IncompleteParameterException(keyName);
    // must be a positive integer
    Integer tempInt = null;
    try {
      tempInt = Integer.valueOf(value);
    } catch (NumberFormatException e) {
      // value is not Integer parseble
      tempInt = null;
    }
    if (tempInt == null || tempInt <= 0)
      // value is not Integer or it's a negative integer
      throw new WrongDataTypeException(keyName, "Positive non-zero Integer");
    return tempInt;
  }

  /**
   * Getter of a generic key from parser which must be a positive double in
   * [0,1[
   * 
   * @param keyName
   *          Name of the key which must be retrieved from parser
   * @return Double retrieved from parser
   * @throws WrongDataTypeException
   *           value associated to keyName is not a positive double in [0,1[
   * @throws IncompleteParameterException
   *           keyName is not present in the parser memory
   */
  public static Double getDouble01(String keyName, Parser parser)
      throws WrongDataTypeException, IncompleteParameterException {
    String value = parser.getValues(keyName);
    if (value == null)
      throw new IncompleteParameterException(keyName);
    Double tempDouble = null;
    try {
      tempDouble = Double.valueOf(value);
    } catch (NumberFormatException e) {
      // value is not Integer parseble
      tempDouble = null;
    }
    if (tempDouble == null || tempDouble < 0 || tempDouble > 1)
      throw new WrongDataTypeException(keyName, "Double in [0,1]");
    return tempDouble;
  }

  /**
   * Getter of a generic key from parser which must be a positive double
   * 
   * @param keyName
   *          Name of the key which must be retrieved from parser
   * @return Double retrieved from parser
   * @throws WrongDataTypeException
   *           value associated to keyName is not a positive double
   * @throws IncompleteParameterException
   *           keyName is not present in the parser memory
   */
  public static Double getPositiveDouble(String keyName, Parser parser)
      throws WrongDataTypeException, IncompleteParameterException {
    String value = parser.getValues(keyName);
    if (value == null)
      throw new IncompleteParameterException(keyName);
    Double tempDouble = null;
    try {
      tempDouble = Double.valueOf(value);
    } catch (NumberFormatException e) {
      // value is not Integer parseble
      tempDouble = null;
    }
    if (tempDouble == null || tempDouble <= 0)
      throw new WrongDataTypeException(keyName, "Positive Double");
    return tempDouble;
  }
}
