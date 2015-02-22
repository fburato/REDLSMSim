package threads;

import graph.nodes.StationNode;
import java.util.concurrent.locks.*;

import threads.commonMessages.TerminationMessage;

/**
 * The Hypervisor is responsible to assure that the simulation executes
 * correctly so it must verify that every other protocol is executing or has
 * stopped because of particular event. It is also a common point of
 * synchronization between Threads used to stop the simulation when a clone
 * detection occurred.
 * 
 * @author Francesco Burato
 * @version 1
 */
public class Hypervisor extends Thread {
  private boolean _terminateSimulation;
  private boolean _hypervisorExecuting;
  private boolean _cloneDetected;
  private boolean _pauseSimulation;
  private boolean _terminationRequest;
  private int _numStations;
  private StationNode[] _stations;
  private int _waitingHypervisorStations;
  private int _emptyMailBoxStations;
  public Lock _lock;
  public Condition _hyperWake;
  public Condition _stationsWake;
  public Lock _pauseLock;
  public Condition _step1;
  public Condition _step2;

  /**
   * Create a new hypervisor with the given graph of stations.
   * 
   * @param stations
   *          The stations to be used to control execution of simulations
   */
  public Hypervisor(StationNode[] stations) {
    super("Hypervisor");
    _terminateSimulation = false;
    _hypervisorExecuting = false;
    _cloneDetected = false;
    _pauseSimulation = false;
    _terminationRequest = false;
    _numStations = stations.length;
    _stations = stations;
    _waitingHypervisorStations = _emptyMailBoxStations = 0;
    _lock = new ReentrantLock();
    _hyperWake = _lock.newCondition();
    _stationsWake = _lock.newCondition();
    _pauseLock = new ReentrantLock();
    _step1 = _pauseLock.newCondition();
    _step2 = _pauseLock.newCondition();
  }

  /**
   * Get the hypervisor state for the execution stop
   * 
   * @return true iff the execution state of the simulation is stop
   */
  public synchronized boolean isSimulationTerminated() {
    return _terminateSimulation;
  }

  /**
   * Set the value of the hypervisor state for the execution stop
   * 
   * @param val
   *          The value which _terminateSimulation will assume at the end of the
   *          method
   */
  public synchronized void setSimulationTermination(boolean val) {
    _terminateSimulation = val;
  }

  /**
   * Impose the hypervisor to stop the simulation
   */
  public synchronized void terminateSimulation() {
    setSimulationTermination(true);
  }

  /**
   * Get the hypervisor state of clone Detected
   * 
   * @return true iff a clone has been detected
   */
  public synchronized boolean getCloneDetected() {
    return _cloneDetected;
  }

  /**
   * Set the value of the hypervisor state regarding the clone detection
   * 
   * @param val
   *          The value which _cloneDetected will assume at the end of the
   *          method
   */
  public synchronized void setCloneDetection(boolean val) {
    _cloneDetected = val;
  }

  /**
   * Tells the hypervisor that a clone has been detected
   */
  public synchronized void cloneDetected() {
    setCloneDetection(true);
  }

  /**
   * Get the value of execution of the hypervisor.
   * 
   * @return true if the hypervisor is executing
   */
  public synchronized boolean isHypervisorExecuting() {
    return _hypervisorExecuting;
  }

  /**
   * Set the value of execution of the hypervisor
   * 
   * @param newState
   *          The new state of execution of the hypervisor
   */
  public synchronized void setHypervisorState(boolean newState) {
    _hypervisorExecuting = newState;
  }

  /**
   * Get the value of pause state of the hypervisor and so of the simulation
   * 
   * @return true iff the hypervisor is paused
   */
  public synchronized boolean isPaused() {
    return _pauseSimulation;
  }

  /**
   * Set the value of pause state of the hypervisor
   * 
   * @param state
   *          The new state of pause of the hypervisor
   */
  public synchronized void setPause(boolean state) {
    _pauseSimulation = state;
  }

  /**
   * This method is used to warn the hypervisor that a protocol running onto a
   * Station has paused because it realized that the hypervisor asked for run.
   * It simply increment the number of waiting threads for this particular
   * condition.
   */
  public synchronized void incWaitingHypervisorStations() {
    _waitingHypervisorStations++;
  }

  /**
   * This method is used to warn the hypervisor that a protocol running onto a
   * Station has paused because it has no more messages to process. It simply
   * increment the number of waiting threads for this particular condition.
   */
  public synchronized void incEmptyMailBoxStations() {
    _emptyMailBoxStations++;
  }

  /**
   * This method is used to warn the hypervisor that a protocol running onto a
   * Station has restarted because someone sent it a messages to process. It
   * simply decrement the number of waiting threads for this particular
   * condition.
   */
  public synchronized void decEmptyMailBoxStations() {
    _emptyMailBoxStations--;
  }

  /**
   * Puts on each incoming mailbox of each station a message which tells them
   * that they have to stop the execution of every activity because some event
   * caused the simulation to stop.
   */
  private void sendTerminationMessage() {
    for (int i = 0; i < _stations.length; ++i) {
      _stations[i].getIncoming().addFirst(new TerminationMessage());
    }
  }

  /**
   * Warns the hypervisor that someone asked for terminating the simulation.
   */
  public synchronized void requestTermination() {
    _terminationRequest = true;
  }

  /**
   * Get if anyone has asked the hypervisor to terminating the simulation.
   * 
   * @return true iff anyone asked the hypervisor to terminating the simulation.
   */
  private synchronized boolean terminationRequired() {
    return _terminationRequest;
  }

  @Override
  public void run() {
    while (!isSimulationTerminated()) {
      // managing pause request
      _pauseLock.lock();
      try {
        while (this.isPaused()) {
          _step1.signalAll();
          _step2.await();
        }
      } catch (InterruptedException e) {
        // Never happens
      } finally {
        _pauseLock.unlock();
      }
      setHypervisorState(true);
      _lock.lock();
      try {
        // the hypervisor wait until every protocol running on station nodes has
        // stopped
        while (_waitingHypervisorStations + _emptyMailBoxStations < _numStations)
          _hyperWake.await();
      } catch (InterruptedException e) {
      } finally {
        _lock.unlock();
      }
      // every protocol Thread has suspended its execution and is waiting for
      // hypervisor to finish
      if (getCloneDetected() || _emptyMailBoxStations == _numStations
          || terminationRequired()) {
        terminateSimulation();
        sendTerminationMessage();
      }
      setHypervisorState(false);
      _lock.lock();
      _waitingHypervisorStations = 0;
      try {
        /*
         * waking up every protocol which stopped because the hypervisor asked
         * to run. Note that the protocol which were suspended because they
         * hadn't any message to process will stay suspended until a message
         * arrives to them. If the message was a termination message they will
         * be all awaken because of the previous instruction of this method.
         */
        _stationsWake.signalAll();
      } finally {
        _lock.unlock();
      }
      try {
        sleep(20);
      } catch (InterruptedException e) {
      }
    }
  }
}
