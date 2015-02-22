package parser;

import exceptions.BadFileFormatException;
import java.io.IOException;

public interface Parser {
  /**
   * Returns the sequence of keys parsed from the file specified.
   * 
   * @return Array of String which contains all the keys parsed
   */
  public String[] getKeys();

  /**
   * Return the value of the requested key.
   * 
   * @param key
   *          The key whose value must be returned.
   * @return The value associated to key
   */
  public String getValues(String key);

  /**
   * Parses the content
   * 
   * @throws IOException
   * @throws BadFileFormatException
   */
  public void parse() throws IOException, BadFileFormatException;
}
