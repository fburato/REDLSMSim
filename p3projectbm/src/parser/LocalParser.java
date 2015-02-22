/**
 * 
 */
package parser;

import java.io.*;
import java.util.TreeMap;

import exceptions.BadFileFormatException;

/**
 * LocalParser is a subclass of GenericParser that takes a local file, parses it
 * and fills a map container with data read form that file.
 * 
 * @author Mattia Merotto
 * 
 */
public class LocalParser extends GenericParser {
  /**
   * Initiate the map and the input buffer.
   * 
   * @param path
   *          The local path to the file to read.
   * @throws FileNotFoundException
   */
  public LocalParser(String path) throws FileNotFoundException {
    _settings = new TreeMap<String, String>();
    _in = new BufferedReader(new FileReader(path));
  };

  /**
   * Parses the local file and fills the map with its data.
   */
  @Override
  public void parse() throws IOException, BadFileFormatException {
    super.parse();
    _in.close();
  }
}
