package gui;

import client.interfaces.SimulationEventListener;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.FlowLayout;

/**
 * This panel prints the current state of the simulation, the protocol running
 * and the current number of test
 * 
 * @author Francesco Burato
 * 
 */
public class SimulationState extends JPanel implements SimulationEventListener {
  private static final long serialVersionUID = 7485009958663906084L;
  private JLabel _simulationState;
  private JLabel _protocolSimulated;
  private JLabel _simulationNumber;
  private final String _simCaption = "State: ";
  private final String _protoCaption = "Protocol: ";
  private final String _numberCaption = "Test #:";

  public SimulationState() {
    super(new GridLayout(3, 1));
    _simulationState = new JLabel(_simCaption + "Nothing active");
    _protocolSimulated = new JLabel(_protoCaption + "None");
    _simulationNumber = new JLabel(_numberCaption + "*/*");

    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(_simulationState);
    add(p);

    p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(_protocolSimulated);
    add(p);

    p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(_simulationNumber);
    add(p);

  }

  @Override
  public void changeStatePerformed(String eventName) {
    _simulationState.setText(_simCaption + eventName);
  }

  @Override
  public void newSimulation(int i, int tot) {
    _simulationNumber.setText(_numberCaption + i + "/" + tot);
  }

  @Override
  public void serverError(Exception e) {
  }

  @Override
  public void changeProtocol(String protoName) {
    _protocolSimulated.setText(_protoCaption + protoName);
  }
}
