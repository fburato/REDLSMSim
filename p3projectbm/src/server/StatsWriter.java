/**
 * 
 */
package server;

import java.rmi.*;
import java.util.Map;
import java.io.IOException;

/**
 * Implementers are able to print data received
 * 
 * @author Mattia Merotto
 * 
 */
public interface StatsWriter extends Remote {

  /**
   * Prints statistical data received
   * 
   * @param w
   *          The map with data that have to be print
   * @throws RemoteException
   * @throws IOException
   */
  public void writeStat(Map<String, String> w) throws RemoteException,
      IOException;

}
