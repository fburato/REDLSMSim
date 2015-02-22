/**
 * 
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.io.*;

/**
 * This class is an implementation of StatsWriter and is used to print data in a
 * file
 * 
 * @author Mattia Merotto
 * 
 */
public class StatsWriterImpl extends UnicastRemoteObject implements StatsWriter {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  // This array is used to print statistical data with the requested order
  private String[] _labels = { "PROTO", "NSIM", "n", "r", "p", "g", "E",
      "E_send", "E_receive", "E_signature", "minSent", "maxSent", "avgSent",
      "stdDevSent", "minReceived", "maxReceived", "avgReceived",
      "stdDevReceived", "minVerifications", "maxVerifications",
      "avgVerifications", "stdDevVerifications", "minConsumedEnergy",
      "maxConsumedEnergy", "avgConsumedEnergy", "stdDevConsumedEnergy",
      "minStoredMessage", "maxStoredMessage", "avgStoredMessage",
      "stdDevStoredMessage", "cloneDetected" };
  private BufferedWriter _out;

  /**
   * Initiate the BufferedWriter with the given file path
   * 
   * @param path
   *          The path of the file where to write data
   * @throws RemoteException
   * @throws IOException
   */
  public StatsWriterImpl(String path) throws RemoteException, IOException {
    _out = new BufferedWriter(new FileWriter(path));
  }

  @Override
  public synchronized void writeStat(Map<String, String> w)
      throws RemoteException, IOException {
    for (int i = 0; i < _labels.length; ++i) {
      _out.write(w.get(_labels[i]) + " ");
      _out.flush();
    }
    _out.write("\n");
    _out.flush();
  }
}
