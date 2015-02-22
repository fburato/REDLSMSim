/**
 * 
 */
package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import client.interfaces.SimulationEventListener;
import exceptions.WrongDataTypeException;

/**
 * This is the panel of the gui that contains all buttons used to configure the
 * simulation
 * 
 * @author Mattia Merotto
 * 
 */
public class ButtonPanel extends JPanel implements SimulationEventListener {
  private static final long serialVersionUID = 9157000095017504893L;
  private JSpinner _sleepTime, _nodeDim, _crossDim;
  private JButton _confirmSleep, _confirmNode, _confirmCross, _start, _stop,
      _pauseTest, _pauseSim;
  private JCheckBox _showArches;
  private List<SimulationDataListener> _listeners;

  public ButtonPanel() {
    // This spinner is used to set the sleep time
    SpinnerModel sleep = new SpinnerNumberModel(100, 0, 1000, 100);
    _sleepTime = new JSpinner(sleep);
    // This spinner is used to set the node dimension
    SpinnerModel node = new SpinnerNumberModel(5, 1, 50, 1);
    _nodeDim = new JSpinner(node);
    // This spinner is used to set the destination cross dimension
    SpinnerModel cross = new SpinnerNumberModel(10, 5, 50, 1);
    _crossDim = new JSpinner(cross);
    _listeners = new LinkedList<SimulationDataListener>();
    // These confirm buttons are used to confirm inserted data during a pause
    _confirmSleep = new JButton("Confirm");
    _confirmSleep.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sleepTimeEvent();
      }
    });
    _confirmNode = new JButton("Confirm");
    _confirmNode.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nodeDimEvent();
      }
    });
    _confirmCross = new JButton("Confirm");
    _confirmCross.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        crossDimEvent();
      }
    });
    // This button starts the simulation
    _start = new JButton("Start");
    _start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startEvent();
      }
    });
    // This button stops the simulation
    _stop = new JButton("Stop");
    _stop.setEnabled(false);
    _stop.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopEvent();
      }
    });
    // This button suspend a running test
    _pauseTest = new JButton("Pause Test");
    _pauseTest.setEnabled(false);
    _pauseTest.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pauseTestEvent();
      }
    });
    // This button suspend a running simulation
    _pauseSim = new JButton("Pause Sim");
    _pauseSim.setEnabled(false);
    _pauseSim.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pauseSimEvent();
      }
    });
    // This checkbox is used to toggle arches during a simulation
    _showArches = new JCheckBox("View Arches");
    _showArches.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showArchesEvent();
      }
    });

    GridBagLayout gridBag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    setLayout(gridBag);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(5, 0, 0, 0);

    JPanel p = new JPanel(new GridLayout(1, 2));
    p.add(_sleepTime);
    p.add(_confirmSleep);
    TitledBorder tb = BorderFactory.createTitledBorder("Sleep Time");
    p.setBorder(tb);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(p, c);
    add(p);

    p = new JPanel(new GridLayout(1, 2));
    p.add(_nodeDim);
    p.add(_confirmNode);
    tb = BorderFactory.createTitledBorder("Node Size");
    p.setBorder(tb);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(p, c);
    add(p);

    p = new JPanel(new GridLayout(1, 2));
    p.add(_crossDim);
    p.add(_confirmCross);
    tb = BorderFactory.createTitledBorder("Cross Size");
    p.setBorder(tb);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(p, c);
    add(p);

    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(_showArches, c);
    add(_showArches);
    c.gridwidth = 1;
    gridBag.setConstraints(_start, c);
    add(_start);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(_stop, c);
    add(_stop);
    c.gridwidth = 1;
    gridBag.setConstraints(_pauseTest, c);
    add(_pauseTest);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridBag.setConstraints(_pauseSim, c);
    add(_pauseSim);

    setVisible(true);
  }

  /**
   * Disables all commands
   */
  private void disableCommands() {
    _sleepTime.setEnabled(false);
    _nodeDim.setEnabled(false);
    _crossDim.setEnabled(false);
    _confirmSleep.setEnabled(false);
    _confirmNode.setEnabled(false);
    _confirmCross.setEnabled(false);
    _start.setEnabled(false);
    _stop.setEnabled(false);
    _pauseTest.setEnabled(false);
    _pauseSim.setEnabled(false);
    _showArches.setEnabled(false);
  }

  /**
   * Event that occurs when the sleep confirm button is pressed
   */
  public void sleepTimeEvent() {
    for (SimulationDataListener s : _listeners)
      s.sleepTimeEventPerformed(this);
  }

  /**
   * Event that occurs when the node confirm button is pressed
   */
  public void nodeDimEvent() {
    for (SimulationDataListener s : _listeners)
      s.nodeDimEventPerformed(this);
  }

  /**
   * Event that occurs when the cross confirm button is pressed
   */
  public void crossDimEvent() {
    for (SimulationDataListener s : _listeners)
      s.crossDimEventPerformed(this);
  }

  /**
   * Event that occurs when the start button is pressed
   */
  public void startEvent() {
    for (SimulationDataListener s : _listeners)
      s.startEventPerformed(this);
  }

  /**
   * Event that occurs when the stop button is pressed
   */
  public void stopEvent() {
    disableCommands();
    for (SimulationDataListener s : _listeners)
      s.stopEventPerformed(this);
  }

  /**
   * Event that occurs when the pause simulation button is pressed
   */
  public void pauseSimEvent() {
    disableCommands();
    for (SimulationDataListener s : _listeners)
      s.pauseSimEventPerformed(this);
  }

  /**
   * Event that occurs when the pause test button is pressed
   */
  public void pauseTestEvent() {
    disableCommands();
    for (SimulationDataListener s : _listeners)
      s.pauseTestEventPerformed(this);
  }

  /**
   * Event that occurs when the show arches checkbox changes
   */
  public void showArchesEvent() {
    for (SimulationDataListener s : _listeners)
      s.visibleArchEventPerformed(this);
  }

  /**
   * Gets the sleep time set
   * 
   * @return The sleep time set
   * @throws WrongDataTypeException
   */
  public int getSleepTime() throws WrongDataTypeException {
    try {
      int st = Integer.valueOf(_sleepTime.getValue().toString());
      if (st < 0) {
        throw new WrongDataTypeException("Sleep Time", "Positive integer");
      }
      return st;
    } catch (ClassCastException e) {
      throw new WrongDataTypeException("Sleep Time", "Positive integer");
    } catch (NumberFormatException e) {
      throw new WrongDataTypeException("Sleep Time", "Positive integer");
    }
  }

  /**
   * Gets the node dimension set
   * 
   * @return The node dimension set
   * @throws WrongDataTypeException
   */
  public int getNodeDim() throws WrongDataTypeException {
    try {
      int st = Integer.valueOf(_nodeDim.getValue().toString());
      if (st < 1) {
        throw new WrongDataTypeException("Node dimension",
            "Positive non-zero integer");
      }
      return st;
    } catch (ClassCastException e) {
      throw new WrongDataTypeException("Node dimension",
          "Positive non-zero integer");
    }
  }

  /**
   * Gets the destination cross dimension set
   * 
   * @return The destination cross dimension set
   * @throws WrongDataTypeException
   */
  public int getCrossDim() throws WrongDataTypeException {
    try {
      int st = Integer.valueOf(_crossDim.getValue().toString());
      if (st < 5) {
        throw new WrongDataTypeException("Cross dimension",
            "Greater than 4 positive integer");
      }
      return st;
    } catch (ClassCastException e) {
      throw new WrongDataTypeException("Cross dimension",
          "Greater than 4 positive integer");
    }
  }

  /**
   * Gets the state of the showArches checkbox
   * 
   * @return TRUE iff the showArches checkbox is selected
   */
  public boolean getShowArchs() {
    return _showArches.isSelected();
  }

  public void addSimulationDataListener(SimulationDataListener s) {
    if (s != null && !_listeners.contains(s))
      _listeners.add(s);
  }

  @Override
  public void changeStatePerformed(String eventName) {
    if (eventName.equals("Running") || eventName.equals("Simulation Restarted")
        || eventName.equals("Test Restarted")) {
      _sleepTime.setEnabled(false);
      _nodeDim.setEnabled(false);
      _crossDim.setEnabled(false);
      _confirmSleep.setEnabled(false);
      _confirmNode.setEnabled(false);
      _confirmCross.setEnabled(false);
      _start.setEnabled(false);
      _stop.setEnabled(true);
      _pauseTest.setEnabled(true);
      _pauseSim.setEnabled(true);
      _showArches.setEnabled(false);
    }
    if (eventName.equals("Stopped")) {
      _sleepTime.setEnabled(true);
      _nodeDim.setEnabled(true);
      _crossDim.setEnabled(true);
      _confirmSleep.setEnabled(true);
      _confirmNode.setEnabled(true);
      _confirmCross.setEnabled(true);
      _start.setEnabled(true);
      _stop.setEnabled(false);
      _pauseTest.setEnabled(false);
      _pauseSim.setEnabled(false);
      _showArches.setEnabled(true);
    }
    if (eventName.equals("Test Paused")) {
      _sleepTime.setEnabled(true);
      _nodeDim.setEnabled(true);
      _crossDim.setEnabled(true);
      _confirmSleep.setEnabled(true);
      _confirmNode.setEnabled(true);
      _confirmCross.setEnabled(true);
      _start.setEnabled(false);
      _stop.setEnabled(true);
      _pauseTest.setEnabled(true);
      _pauseSim.setEnabled(false);
      _showArches.setEnabled(true);
    }
    if (eventName.equals("Simulation Paused")) {
      _sleepTime.setEnabled(true);
      _nodeDim.setEnabled(true);
      _crossDim.setEnabled(true);
      _confirmSleep.setEnabled(true);
      _confirmNode.setEnabled(true);
      _confirmCross.setEnabled(true);
      _start.setEnabled(false);
      _stop.setEnabled(true);
      _pauseTest.setEnabled(false);
      _pauseSim.setEnabled(true);
      _showArches.setEnabled(true);
    }
  }

  @Override
  public void newSimulation(int i, int tot) {
  }

  @Override
  public void serverError(Exception e) {
  }

  @Override
  public void changeProtocol(String protoName) {
  }
}
