/**
 * 
 */
package test.merotto;

import java.rmi.*;
import server.StatsWriter;
import java.util.*;

/**
 * @author Mattia Merotto
 * 
 */
public class TestClient {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    try {
      String[] labels = { "PROTO", "NSIM", "n", "r", "p", "g", "E", "E_send",
          "E_receive", "E_signature", "minSent", "maxSent", "avgSent",
          "stdDevSent", "minReceived", "maxReceived", "avgReceived",
          "stdDevReceived", "minVerifications", "maxVerifications",
          "avgVerifications", "stdDevVerifications", "minConsumedEnergy",
          "maxConsumedEnergy", "avgConsumedEnergy", "stdDevConsumedEnergy",
          "minStoredMessages", "maxStoredMessages", "avgStoredMessages",
          "stdDevStoredMessages", "cloneDetected" };
      System.out.println("creating map");
      Map<String, String> m = new TreeMap<String, String>();
      for (int i = 0; i < labels.length; ++i) {
        m.put(labels[i], "" + i);
      }
      for (int i = 0; i < labels.length; ++i) {
        System.out.println(m.get(labels[i]));
      }
      System.out.println("remote call");
      StatsWriter ref = (StatsWriter) Naming.lookup("rmi://localhost/stats");
      ref.writeStat(m);
      System.out.println("done");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
