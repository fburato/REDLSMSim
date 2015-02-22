/**
 * 
 */
package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import exceptions.BadFileFormatException;

/**
 * This class implements the basic behavior of each methods of the Parser
 * interface and it's used to create different parsers simply inheriting from it
 * 
 * @author Mattia Merotto
 * 
 */
public class GenericParser implements Parser {

  protected TreeMap<String, String> _settings;
  protected BufferedReader _in;
  protected BufferedWriter _out;
  protected String _line;

  /**
   * Trims tabs and spaces from the beginning and the ending of the line
   * 
   * @throws IOException
   */
  public void lineTrimmer() throws IOException {
    if (_line != null)
      _line = _line.trim();
  }

  @Override
  public void parse() throws IOException, BadFileFormatException {
    // Fills the map with read values.
    _line = _in.readLine();
    lineTrimmer();
    while (_line != null) {
      if (_line.equals("")) {
      } // Jumps blank lines.
      else if (_line.charAt(0) != '%') { // Doesn't read comment lines.
        String[] temp = _line.split("[ \t]*=[ \t]*"); // Regular expression that
                                                      // considers all possible
                                                      // zeros and tabs before
                                                      // and after the equal.
        if (temp.length != 2 || _line.matches(".*=.*=.*")) {
          throw new BadFileFormatException(
              "The configuration file doesn't meet specifications.");
        }
        _settings.put(temp[0], temp[1]);
      }
      _line = _in.readLine();
      lineTrimmer();
    }
  }

  @Override
  public String[] getKeys() {
    if (!_settings.isEmpty()) {
      String[] tmp = new String[_settings.size()];
      int c = 0;
      for (Map.Entry<String, String> tmpEntry : _settings.entrySet()) {
        tmp[c] = tmpEntry.getKey();
        c++;
      }
      return tmp;
    }
    return null;
  }

  @Override
  public String getValues(String key) {
    String value = _settings.get(key);
    return value;
  }

}
