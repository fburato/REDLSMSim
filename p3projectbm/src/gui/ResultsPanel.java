/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import server.StatsWriter;

/**
 * This panel shows into a text area configurations and statistical data of the
 * ended tests
 * 
 * @author Mattia Merotto
 * 
 */
public class ResultsPanel extends JPanel implements StatsWriter {
  private static final long serialVersionUID = 6851708349818441350L;
  // This array is used to print all data from the map with the requested order
  private String[] _labels = { "PROTO", "NSIM", "n", "r", "p", "g", "E",
      "E_send", "E_receive", "E_signature", "minSent", "maxSent", "avgSent",
      "stdDevSent", "minReceived", "maxReceived", "avgReceived",
      "stdDevReceived", "minVerifications", "maxVerifications",
      "avgVerifications", "stdDevVerifications", "minConsumedEnergy",
      "maxConsumedEnergy", "avgConsumedEnergy", "stdDevConsumedEnergy",
      "minStoredMessage", "maxStoredMessage", "avgStoredMessage",
      "stdDevStoredMessage", "cloneDetected" };
  private JTextArea _resultsArea;

  public ResultsPanel() {
    setLayout(new BorderLayout());
    _resultsArea = new JTextArea(2, 80);
    _resultsArea.setEditable(false);
    _resultsArea.setMaximumSize(new Dimension(600, 200));
    JScrollPane scrollingArea = new JScrollPane(_resultsArea);
    scrollingArea.setMaximumSize(new Dimension(600, 200));
    scrollingArea.setMinimumSize(new Dimension(600, 200));
    add(scrollingArea, BorderLayout.CENTER);
    setVisible(true);
  }

  /**
   * Adds an asterisks line between a simulation results and the others
   * 
   * @param num
   */
  public void addAsterisks(int num) {
    String s = "";
    for (int i = 0; i < num; ++i) {
      s = s + "*";
    }
    _resultsArea.setText(_resultsArea.getText() + s + "\n");
  }

  @Override
  public void writeStat(Map<String, String> w) throws RemoteException,
      IOException {
    if (w != null) {
      String s = _resultsArea.getText();
      for (int i = 0; i < _labels.length; i++) {
        s = s + w.get(_labels[i]) + ";";
      }
      s = s + "\n";
      _resultsArea.setText(s);
    }
  }
}
