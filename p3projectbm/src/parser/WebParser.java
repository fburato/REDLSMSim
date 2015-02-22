/**
 * 
 */
package parser;

import java.util.*;
import java.io.*;
import java.net.*;

import exceptions.BadFileFormatException;

/**
 * WebParser is a subclass of GenericParser that requests a file from the web,
 * parses it and fills a map container with data read from that file.
 * 
 * @author Mattia Merotto
 * 
 */
public class WebParser extends GenericParser {

  private Socket _socket;

  /**
   * Initiate the map, the socket and the buffered reader and writer.
   * 
   * @param host
   *          The host to connect to get the file.
   * @throws IOException
   * @throws UnknownHostException
   */
  public WebParser(String host) throws IOException, UnknownHostException {
    _settings = new TreeMap<String, String>();
    _socket = new Socket(host, 80);
    _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
    _out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
  }

  /**
   * Sends the GET request.
   * 
   * @param path
   *          The path of the file to down.
   * @throws IOException
   */
  public void sendGetRequest(String path) throws IOException {
    String request = "GET " + path + " HTTP/1.0\r\n\r\n";
    _out.write(request);
    _out.flush();
  }

  /**
   * Jumps the HTTP header
   * 
   * @throws IOException
   */
  public void headerJumper() throws IOException {
    _line = _in.readLine();
    lineTrimmer();
    while (_line != null && !_line.equals("")) {
      _line = _in.readLine();
    }
  }

  /**
   * Parses the file got from the GET response and fills the map with its data.
   */
  @Override
  public void parse() throws IOException, BadFileFormatException {
    headerJumper();
    super.parse();
    _in.close();
    _out.close();
  }
}
