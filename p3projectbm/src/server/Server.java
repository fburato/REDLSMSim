/**
 * 
 */
package server;

import java.rmi.*;

/**
 * This is the RMI server that receive the map with statistical data from the
 * client and write them into a file
 * 
 * @author Mattia Merotto
 * 
 */
public class Server {
  public static void main(String args[]) {
    if (args.length < 1) {
      // Server must be started with the path of the file where data have to be
      // written. So, if this doesn't happens a usage massage is print on screen
      System.out
          .println("Usage: java server.Server <path for the output file> ");
    } else {
      // The file of the file was correctly given
      String path = args[0];
      StatsWriterImpl sw = null;
      try {
        sw = new StatsWriterImpl(path);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        System.exit(-1);
      }
      // finding out if there is already a running server
      Object o = null;
      try {
        o = Naming.lookup("rmi://localhost/stats");
      } catch (Exception e) {
      }
      if (o != null) {
        System.out
            .println("RMI Object conflict:\n" +
                "The remote object has been alredy published or a server is running.\n"
                + "Shutdown rmiregistry and any existing servers before creating a new server");
        System.exit(-1);
      }
      try {
        String stats = "rmi://localhost/stats";
        Naming.rebind(stats, sw);
        System.out.println("Server is running...");
      } catch (Exception e) {
        System.out.println("RMI Error: cannot connect to rmiregistry.\n" +
        		"Probably rmiregistry is down");
        System.exit(-1);
      }
    }
  }
}
