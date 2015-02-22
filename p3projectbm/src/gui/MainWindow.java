package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import parser.*;
import server.StatsWriter;
import threads.RED.Satellite;
import client.SimulationClient;
import client.interfaces.SimulationEventListener;
import exceptions.BadFileFormatException;
import exceptions.IncompleteParameterException;
import exceptions.UnknownProtocolException;
import exceptions.WrongDataTypeException;

import java.rmi.*;

/**
 * This is the base frame of the program.
 * 
 * @author Mattia Merotto
 * 
 */
public class MainWindow extends JFrame implements SimulationDataListener,
    SimulationEventListener {
  private static final long serialVersionUID = 5066011729249701732L;
  private TextPanel _tp;
  private ResultsPanel _rp;
  private ButtonPanel _bp;
  private GraphPanel _gp;
  private Satellite _sat;
  private Parser _parser;
  private SimulationState _state;
  private SimulationClient _client;

  public MainWindow() {
    super("Clone detector");
    _tp = new TextPanel();
    _rp = new ResultsPanel();
    _rp.setMaximumSize(new Dimension(600, 200));
    _bp = new ButtonPanel();
    _gp = new GraphPanel(null, 5, 5);
    _state = new SimulationState();
    _sat = new Satellite();
    _bp.addSimulationDataListener(this);
    _client = null;

    Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

    JPanel p = new JPanel(new BorderLayout());
    p.add(_bp, BorderLayout.NORTH);
    p.add(_state, BorderLayout.SOUTH);
    p.setBorder(border);
    add(p, BorderLayout.EAST);
    _gp.setBorder(border);
    add(_gp, BorderLayout.CENTER);
    p = new JPanel(new GridLayout(2, 1));
    p.add(_rp);
    p.add(_tp);
    p.setMaximumSize(new Dimension(600, 400));
    p.setBorder(border);
    add(p, BorderLayout.SOUTH);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setSize(1000, 600);
  }

  /**
   * @param args
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {

    try {
      // Sets system look&feel so graphics will change on different operative
      // systems
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (UnsupportedLookAndFeelException e) {
      // handle exception
    } catch (ClassNotFoundException e) {
      // handle exception
    } catch (InstantiationException e) {
      // handle exception
    } catch (IllegalAccessException e) {
      // handle exception
    }

    MainWindow tg = new MainWindow();
  }

  /**
   * Sets the new sleep time
   */
  public void sleepTimeEventPerformed(ButtonPanel b) {
    int sleepTime = 0;
    try {
      sleepTime = b.getSleepTime();
    } catch (WrongDataTypeException e) {
      // exception thrown... shows a dialog.
      JOptionPane.showMessageDialog(this, e.getMessage(), "Type Error",
          JOptionPane.ERROR_MESSAGE);
    }
    if (_client != null)
      _client.setSleepTime(sleepTime);
  }

  /**
   * Sets the new node dimension
   */
  public void nodeDimEventPerformed(ButtonPanel b) {
    int nodeDim = 1;
    try {
      nodeDim = b.getNodeDim();
    } catch (WrongDataTypeException e) {
      // exception thrown... shows a dialog.
      JOptionPane.showMessageDialog(this, e.getMessage(), "Type Error",
          JOptionPane.ERROR_MESSAGE);
    }
    if (_client != null)
      _gp.setNodesSize(nodeDim);
  }

  /**
   * Sets the new destination cross dimension
   */
  public void crossDimEventPerformed(ButtonPanel b) {
    int crossDim = 5;
    try {
      crossDim = b.getCrossDim();
    } catch (WrongDataTypeException e) {
      // exception thrown... shows a dialog.
      JOptionPane.showMessageDialog(this, e.getMessage(), "Type Error",
          JOptionPane.ERROR_MESSAGE);
    }
    if (_client != null)
      _gp.setCrossSize(crossDim);
  }

  /**
   * Prepares and starts the simulation
   */
  public void startEventPerformed(ButtonPanel b) {
    if (_client != null && !_client.getStopSimulation())
      return;
    if (_client == null || _client.getStopSimulation()) {
      try {
        if (_tp.getSource().equals("local")) {
          // The configuration file to parse is stored locally
          _parser = new LocalParser(_tp.getParsePath());
        } else if (_tp.getSource().equals("web")) {
          // The configuration file to parse is stored into the web
          // The path given is divided in host and path of the file into this
          // host
          String s[] = _tp.getParsePath().split("/");
          if (s.length <= 1)
            throw new WrongDataTypeException("Server path", "URL identifier");
          _parser = new WebParser(s[0]);
          String path = "";
          for (int i = 1; i < s.length; ++i) {
            path = path + "/" + s[i];
          }
          WebParser web = (WebParser) _parser;
          web.sendGetRequest(path);
        }
        StatsWriter rmiServer = null;
        if (!_tp.getIgnore()) {
          // Ignore isn't checked so an RMI server is initiated
          rmiServer = (StatsWriter) Naming.lookup("rmi://"
              + _tp.getServerAddress() + "/stats");
        }
        _parser.parse();
        System.gc();
        _client = new SimulationClient(_parser, _gp, _gp, _sat);
        _client.addListener(_state);
        _client.addListener(_bp);
        _client.addListener(this);
        _client.registerServer(_rp);
        _client.registerServer(rmiServer);
        _client.setSleepTime(b.getSleepTime());
        _gp.setPrintArch(b.getShowArchs());
        _gp.setNodesSize(b.getNodeDim());
        _gp.setCrossSize(b.getCrossDim());
        _client.startSimulation();
      } catch (FileNotFoundException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage(), "File Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (UnknownHostException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage() + "Unknown host",
            "Host Error", JOptionPane.ERROR_MESSAGE);
      } catch (NotBoundException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage(), "RMI Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (IOException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage(), "IO Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (BadFileFormatException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage(), "File Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (WrongDataTypeException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage(), "Type Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (IncompleteParameterException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage(), "Parameter Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (UnknownProtocolException e) {
        // exception thrown... shows a dialog.
        JOptionPane.showMessageDialog(this, e.getMessage(), "Protocol Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Stops the simulation
   */
  public void stopEventPerformed(ButtonPanel b) {
    if (_client == null)
      return;
    synchronized (_client) {
      if (_client.getStopSimulation())
        return;
      _client.stopSimulation();
      _client = null;
    }
  }

  /**
   * Pauses the simulation
   */
  public void pauseSimEventPerformed(ButtonPanel b) {
    if (_client == null)
      return;
    synchronized (_client) {
      _client.pauseSimulation(!_client.getPauseSim());
    }
  }

  /**
   * Pauses a test
   */
  public void pauseTestEventPerformed(ButtonPanel b) {
    if (_client == null)
      return;
    synchronized (_client) {
      _client.pauseTest(!_client.getPauseTest());
    }
  }

  /**
   * Toggles the view of the arches
   */
  public void visibleArchEventPerformed(ButtonPanel b) {
    _gp.setPrintArch(_bp.getShowArchs());
  }

  @Override
  public void changeStatePerformed(String eventName) {
    if (eventName.equals("Stopped"))
      // Adds an asterisks line into the results panel once the simulation is
      // ended
      _rp.addAsterisks(40);
  }

  @Override
  public void newSimulation(int i, int tot) {
  }

  @Override
  public void serverError(Exception e) {
    // exception thrown... shows a dialog.
    JOptionPane.showMessageDialog(this, e.getMessage(), "Stats Server Error",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void changeProtocol(String protoName) {
  }

}
