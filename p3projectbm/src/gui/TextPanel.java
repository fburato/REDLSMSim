/**
 * 
 */
package gui;

import javax.swing.*;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This panel is used to insert the path of the file to parse (that can be a
 * local or remote file) and the server address (which can also be ignored)
 * 
 * @author Mattia Merotto
 * 
 */
public class TextPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private JTextField _parserPathText, _serverAddressText;
  private JLabel _parserPathLabel, _serverAddressLabel;
  private JRadioButton _localRadio, _webRadio;
  private ButtonGroup _sourceChoose;
  private JCheckBox _ignoreCheck;

  public TextPanel() {

    _parserPathText = new JTextField(
        "www.math.unipd.it/~conti/teaching/PCD1112/project_config.txt");
    _serverAddressText = new JTextField("localhost");
    _parserPathLabel = new JLabel("Parser Path:");
    _serverAddressLabel = new JLabel("Server Address:");
    _localRadio = new JRadioButton("Local");
    _webRadio = new JRadioButton("Web", true);
    _sourceChoose = new ButtonGroup();
    _ignoreCheck = new JCheckBox("Ignore");
    _ignoreCheck.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (getIgnore())
          _serverAddressText.setEnabled(false);
        else
          _serverAddressText.setEnabled(true);
      }
    });

    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();

    setLayout(gridBag);
    c.fill = GridBagConstraints.HORIZONTAL;

    JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    p.add(_parserPathLabel);
    gridBag.setConstraints(p, c);
    add(p);
    c.weightx = 1.0;
    c.gridwidth = GridBagConstraints.RELATIVE;
    gridBag.setConstraints(_parserPathText, c);
    add(_parserPathText);
    p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(_localRadio);
    _sourceChoose.add(_localRadio);
    p.add(_webRadio);
    _sourceChoose.add(_webRadio);
    c.weightx = 0.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(p, c);
    add(p);
    c.gridwidth = 1;
    p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    p.add(_serverAddressLabel);
    gridBag.setConstraints(p, c);
    add(p);
    c.weightx = 1.0;
    c.gridwidth = GridBagConstraints.RELATIVE;
    gridBag.setConstraints(_serverAddressText, c);
    add(_serverAddressText);
    c.weightx = 0.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(_ignoreCheck);
    gridBag.setConstraints(p, c);
    add(p);

    setVisible(true);
  }

  /**
   * Gets the inserted path of the file to parse
   * 
   * @return The inserted path of the file to parse
   */
  public String getParsePath() {
    return _parserPathText.getText();
  }

  /**
   * Gets the inserted server address
   * 
   * @return The inserted server address
   */
  public String getServerAddress() {
    return _serverAddressText.getText();
  }

  /**
   * Gets if the inserted file path should be intended as local or web
   * 
   * @return "local" if the file is in a local storage, "web" if it is tooken
   *         from the web
   */
  public String getSource() {
    String s = null;
    if (_localRadio.isSelected()) {
      s = "local";
    } else if (_webRadio.isSelected()) {
      s = "web";
    }
    return s;
  }

  /**
   * Gets the state of the ignore checkbox
   * 
   * @return TRUE iff ignore checkbox is selected
   */
  public Boolean getIgnore() {
    return _ignoreCheck.isSelected();
  }

}
