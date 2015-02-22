package client;

import client.interfaces.*;
import graph.nodes.*;
import graph.support.UpdateListener;
import threads.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import parser.Parser;

import exceptions.IncompleteParameterException;
import exceptions.UninitializedProtocolException;
import exceptions.UnknownProtocolException;
import exceptions.WrongDataTypeException;
import threads.RED.Satellite;
import server.StatsWriter;

/**
 * The client logic controller
 * 
 * @author Francesco Burato
 * 
 */
public class SimulationClient {
  /**
   * The thread is used to manage the different part of the simulation. It
   * provides management of pause between different simulation and it
   * instantiate correctly every part of the needed object to complete
   * simulations
   * 
   * @author Francesco Burato
   * @version 1
   */
  private class SimulationController extends Thread implements NewGraphLauncher {
    /**
     * Collect every requested statistical data from available Stations
     * 
     * @return The map containing all the data
     */
    private List<NewGraphListener> _listeners;

    /**
     * Create the simulation controller instantiating the Listener Array
     */
    public SimulationController() {
      _listeners = new ArrayList<NewGraphListener>();
    }

    /**
     * Adds a NewGraphListner to the listeners of the controller if the new
     * listener passed is not null and is not repeated
     * 
     * @param l
     *          The NewGraphListener to be added
     */
    public void addNewGraphListener(NewGraphListener l) {
      if (l != null && !_listeners.contains(l))
        _listeners.add(l);
    }

    /**
     * Warns every listeners that the graph is changed
     * 
     * @param g
     *          The graph on which currently the SimulationController is working
     *          on
     */
    public void generateEvent(StationNode[] g) {
      for (NewGraphListener l : _listeners)
        l.graphChanged(g);
    }

    /**
     * Returns a Map of couple key, value containing all the statistical data
     * which is collectible with the current state of the Controller
     * 
     * @return The Map of string with all the statistical data
     */
    private Map<String, String> collectData() {
      Map<String, String> data = new TreeMap<String, String>();
      // adding data about the simulation
      data.put("PROTO", _proto);
      data.put("NSIM", "" + _NSIM);
      // collecting parameters from factories
      _statFactory.writeStatisticalData(data);
      _protoFactory.writeStatisticalData(data);
      // calculating all other values
      /*
       * Min, max, average, and standard deviation of each of the following
       * variables referred to the network nodes: number of sent messages,
       * number of received messages, number of signature checks, amount of
       * consumed energy, number of messages stored in the node
       */
      String meaning[] = { "Sent", "Received", "Verifications",
          "ConsumedEnergy", "StoredMessage" };
      int matrix[][] = { // sent,received,sign,consumed,stored
          { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
              Integer.MAX_VALUE, Integer.MAX_VALUE }, // min row
          { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
              Integer.MIN_VALUE, Integer.MIN_VALUE }, // max row
          { 0, 0, 0, 0, 0 },// total row
      };
      for (int i = 0; i < _graph.length; ++i) {
        // getting actual values
        int actual[] = { _graph[i].getSentMessages(), _graph[i].getReceived(),
            _graph[i].getVerification(),
            (int) (_graph[i].getInitBattery() - _graph[i].getBattery()),
            _graph[i].getRead().size() };
        for (int j = 0; j < actual.length; ++j) {
          matrix[0][j] = actual[j] < matrix[0][j] ? actual[j] : matrix[0][j];
          matrix[1][j] = actual[j] > matrix[1][j] ? actual[j] : matrix[1][j];
          matrix[2][j] += actual[j];
        }
      }
      // calculating avg of values
      double avg[] = new double[5];
      for (int i = 0; i < matrix[0].length; ++i)
        avg[i] = ((double) matrix[2][i]) / _graph.length;

      // calculating standard deviations of values
      double stdDev[] = { 0, 0, 0, 0, 0 };
      // first variance
      for (int i = 0; i < _graph.length; ++i) {
        int actual[] = { _graph[i].getSentMessages(), _graph[i].getReceived(),
            _graph[i].getVerification(),
            (int) (_graph[i].getInitBattery() - _graph[i].getBattery()),
            _graph[i].getRead().size() };
        for (int j = 0; j < stdDev.length; ++j)
          // for definition the variance of a given set of data A is
          // sum(i in A)(avg-i)^2
          stdDev[j] += (avg[j] - actual[j]) * (avg[j] - actual[j]);
      }
      // calculating square root of stdDev to obtain the real standard deviation
      for (int i = 0; i < stdDev.length; ++i)
        stdDev[i] = Math.sqrt(stdDev[i] / stdDev.length);
      // putting data on the Map
      for (int i = 0; i < meaning.length; ++i) {
        data.put("min" + meaning[i], matrix[0][i] + "");
        data.put("max" + meaning[i], matrix[1][i] + "");
        data.put("avg" + meaning[i], avg[i] + "");
        data.put("stdDev" + meaning[i], stdDev[i] + "");
      }
      data.put("cloneDetected", "" + _hyper.getCloneDetected());
      return data;
    }

    public void run() {
      int i = 0;
      while (!getStopSimulation() && i < _NSIM) {
        for (SimulationEventListener l : SimulationClient.this._listeners)
          l.newSimulation(i + 1, _NSIM);
        synchronized (SimulationClient.this) {
          try {
            // if a pause between simulation the simulation manager must wait
            // until pause has been removed
            while (_pauseSimRequest) {
              for (SimulationEventListener l : SimulationClient.this._listeners)
                l.changeStatePerformed("Simulation Paused");
              SimulationClient.this.wait();
              for (SimulationEventListener l : SimulationClient.this._listeners)
                l.changeStatePerformed("Simulation Restarted");
            }
          } catch (InterruptedException e) {
            // never happens
          }
        }
        // generating the new random number
        _sat.next();
        // creating the test graph and the hypervisor managing it
        _graph = _statFactory.makeGraph();
        // updating listeners for graph changed
        generateEvent(_graph);
        // adding to every new node the listener for changes
        for (StationNode s : _graph)
          s.addUpdateListener(_updateListener);
        if (_updateListener != null)
          _updateListener.clearState();
        _hyper = new Hypervisor(_graph);
        synchronized (SimulationClient.this) {
          SimulationClient.this.notifyAll();
        }
        if (_updateListener != null)
          _updateListener.setSleepTime(_sleepTime);
        _protoFactory.setHypervisor(_hyper);
        _graph[0].setId("detectMe");
        _graph[1].setId("detectMe");
        try {
          _protocolThreads = _protoFactory.generateProtocol(_graph);
        } catch (UninitializedProtocolException e) {
          // never happens
        }
        // starting the threads
        _hyper.start();
        for (int j = 0; j < _protocolThreads.length; ++j)
          _protocolThreads[j].start();
        try {
          // waiting for the test to finish
          _hyper.join();
          for (int j = 0; j < _protocolThreads.length; ++j)
            _protocolThreads[j].join();
        } catch (InterruptedException e) {
          // never happens
        }
        // warns every listener of the simulation client that simulation has
        // been terminated
        if (_updateListener != null)
          _updateListener.updateLaunched();
        // now all threads are certainly terminated
        if (!getStopSimulation()) {
          Map<String, String> statisticalData = collectData();
          try {
            // writing on every registered server the data collected
            for (StatsWriter w : _servers)
              w.writeStat(statisticalData);
          } catch (Exception e) {
            for (SimulationEventListener l : SimulationClient.this._listeners)
              l.serverError(e);
          }
        }
        // cleaning out every data
        _graph = null;
        synchronized (SimulationClient.this) {
          _hyper = null;
        }
        _protocolThreads = null;
        System.gc();
        i++;
      }
      _stopSimulation = true;
      // warning every listener that the simulation has been stopped
      for (SimulationEventListener l : SimulationClient.this._listeners)
        l.changeStatePerformed("Stopped");
      // controllerTerminato = true
      synchronized (SimulationClient.this) {
        // waking up the client if it is for this thread to complete
        SimulationClient.this.notifyAll();
      }
    }
  }

  private ProtocolFactory _protoFactory;
  private StationFactory _statFactory;
  private Parser _parser; // resource has been already parsed
  // useful information for the simulation
  private String _proto;
  private int _NSIM;

  // part of the data structure of StationNodes
  private StationNode[] _graph;
  // part of threading
  private Hypervisor _hyper;
  private ProtocolThread[] _protocolThreads;
  private SimulationController _controller;
  private boolean _pauseTestRequest;
  private boolean _pauseSimRequest;
  private boolean _stopSimulation;
  private NewGraphListener _graphListener;
  private UpdateListener _updateListener;
  private int _sleepTime;
  private Satellite _sat; // Common satellite for every RED protocol simulation
  private List<StatsWriter> _servers; // list of server to send statistical data
                                      // to
  private List<SimulationEventListener> _listeners; // list of Listener waiting
                                                    // for particular event on
                                                    // simulations

  /**
   * Instantiating the simulation client with the given parser, Listeners and
   * Satellite
   * 
   * @param p
   *          Parser to be used for getting parameters
   * @param gl
   *          Graph listener for change of graphs between simulations
   * @param ul
   *          Update Listener for changing of graph state during every
   *          simulations
   * @param s
   *          The common Satellite
   * @throws WrongDataTypeException
   *           Launched if any parameter obtained from the parser has the wrong
   *           type
   * @throws IncompleteParameterException
   *           Launched if a parameter in the parser is missing
   * @throws UnknownProtocolException
   *           Launched if the requested protocol (in the parser) is intractable
   *           from this SimulationClient
   */
  public SimulationClient(Parser p, NewGraphListener gl, UpdateListener ul,
      Satellite s) throws WrongDataTypeException, IncompleteParameterException,
      UnknownProtocolException {
    _sat = s;
    initSimulation(p);
    _graphListener = gl;
    _updateListener = ul;
    _servers = new LinkedList<StatsWriter>();
    _listeners = new LinkedList<SimulationEventListener>();
  }

  /**
   * Build a new ProtocolFactory and communicate whether or not there has been
   * problems during parsing
   * 
   * @throws WrongDataTypeException
   *           One or more of the parameters required for protocol this client
   *           is able to handle don't have the correct type
   * @throws IncompleteParameterException
   *           One or more of the parameters required for protocol this client
   *           is able to handle are missing
   */
  public void initSimulation(Parser p) throws WrongDataTypeException,
      IncompleteParameterException, UnknownProtocolException {
    _parser = p;
    _protoFactory = new REDLSMFactory(_parser, _sat);
    _statFactory = new SWFactory(_parser);
    _proto = _parser.getValues("PROTO");
    if (_proto == null)
      // if 'PROTO' values doesn't exists the file is not well formed
      throw new IncompleteParameterException("PROTO");
    // getting NSIM value from parser
    _NSIM = TypesValidator.getPositiveInteger("NSIM", _parser);
    // the 'PROTO' value is defined so I try to instantiate the factory values
    _protoFactory.instantiateProtocol(_proto);
    _statFactory.validate();
    // here the factories are correctly instantiated
  }

  /**
   * Start a new simulation by instantiating a new simulation controller and
   * starting it
   */
  public void startSimulation() {
    _controller = new SimulationController();
    _controller.addNewGraphListener(_graphListener);
    _pauseSimRequest = false;
    _pauseTestRequest = false;
    _stopSimulation = false;
    for (SimulationEventListener l : SimulationClient.this._listeners)
      l.changeStatePerformed("Running");
    for (SimulationEventListener l : SimulationClient.this._listeners)
      l.changeProtocol(_proto);
    _controller.start();
  }

  /**
   * Interrupts safely execution of a simulation between two different test in
   * NSIM
   * 
   * @param value
   *          Value of the pause imposed to simulation
   */
  public synchronized void pauseSimulation(boolean value) {
    if (_controller != null) {
      _pauseSimRequest = value;
      notifyAll();
    }
  };

  /**
   * Gets the state of pause of the simulation
   * 
   * @return TRUE if the simulation is paused, FALSE otherwise
   */
  public synchronized boolean getPauseSim() {
    return _pauseSimRequest;
  }

  /**
   * Interrupt safely execution of one of the test which is active.
   * 
   * @param value
   *          Value of the pause to be set
   */
  public synchronized void pauseTest(boolean value) {
    if (_controller != null && _hyper != null) {
      if (!_pauseTestRequest && value) {
        // the simulation is not paused and user wants to pause it
        _hyper._pauseLock.lock();
        try {
          _hyper.setPause(true);
          _hyper._step1.await();
        } catch (InterruptedException e) {
          // Never happens
        } finally {
          _hyper._pauseLock.unlock();
        }
        for (SimulationEventListener l : SimulationClient.this._listeners)
          l.changeStatePerformed("Test Paused");
      } else if (_pauseTestRequest && !value) {
        _hyper._pauseLock.lock();
        try {
          _hyper.setPause(false);
          _hyper._step2.signalAll();
        } finally {
          _hyper._pauseLock.unlock();
        }
        for (SimulationEventListener l : SimulationClient.this._listeners)
          l.changeStatePerformed("Test Restarted");
      }
      _pauseTestRequest = value;
    }
  }

  /**
   * Getter of the state of pause during a simulation over NSIM
   * 
   * @return true iff a pause has been requested
   */
  public synchronized boolean getPauseTest() {
    return _pauseTestRequest;
  }

  /**
   * Getter of the graph the current SimulationClient is working on
   * 
   * @return The graph used
   */
  public StationNode[] getGraph() {
    return _graph;
  }

  /**
   * Sets the sleepTime which must occur between each step of the simulation. It
   * is used to show the user the evolution of the test
   * 
   * @param sleep
   *          The new sleeptime to be setted
   */
  public void setSleepTime(int sleep) {
    _sleepTime = sleep;
    if (_updateListener != null)
      _updateListener.setSleepTime(sleep);
  }

  /**
   * Impose any current simulation to stop in any case
   */
  public synchronized void stopSimulation() {
    // waking up the controller if it is frozen because of a pause
    pauseTest(false);
    pauseSimulation(false);
    try {
      // wait until the _hyper is null
      while (_hyper == null)
        wait();
    } catch (InterruptedException e) {

    }
    // now hyper is certainly not null so I impose the hypervisor to stop
    _hyper.requestTermination();
    // blocking the simulation controller if it has still any test to complete
    _stopSimulation = true;
  }

  /**
   * Getter of the stopSimulationState
   * 
   * @return true iff the simulation has been stopped
   */
  public synchronized boolean getStopSimulation() {
    return _stopSimulation;
  }

  /**
   * Add a server to the list of server to be used for sending statistical data
   * if it is not null and not yet in the list of registered servers.
   * 
   * @param s
   *          The new server to be added
   */
  public void registerServer(StatsWriter s) {
    if (s != null && !_servers.contains(s))
      _servers.add(s);
  }

  /**
   * Add a listener for particular events happening during the simulation if it
   * is not null and not in inserted
   * 
   * @param l
   *          The new listener to be added.
   */
  public void addListener(SimulationEventListener l) {
    if (l != null && !_listeners.contains(l))
      _listeners.add(l);
  }
}
