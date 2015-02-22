package exceptions;

/**
 * Exception which communicate to the file which has been passed to the parser
 * doesn't respect the file specification given in requisites.
 * 
 * @author Francesco Burato
 * @version 1
 * 
 */
public class BadFileFormatException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 5900807200987100278L;

  public BadFileFormatException(String s) {
    super(s);
  }
}
