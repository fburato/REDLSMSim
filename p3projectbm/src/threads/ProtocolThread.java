package threads;

import graph.nodes.StationNode;

/**
 * A Thread which runs on a StationNode and which simulate one protocol which
 * has the capability to communicate to the Hypervisor it is waiting for any
 * message to arrive and which is paused while the hypervisor is executing
 * 
 * @author Francesco Burato
 * @version 1
 */
public abstract class ProtocolThread extends Thread {
  protected StationNode _station;
  protected Hypervisor _controller;
  public static Object otherLock = null;

  /**
   * Build a thread on a StationNode with the given hypervisor supervising the
   * simulation. It is like the "software" part of the project of simulation
   * while the StationNode is the "hardware"
   * 
   * @param station
   *          The StationNode upon which executing operations
   * @param controller
   *          The Hypervisor for the current simulation
   */
  public ProtocolThread(StationNode station, Hypervisor controller) {
    super("Thread:" + station.getId());
    _station = station;
    _controller = controller;
  }

  public void run() {
    while (!_controller.isSimulationTerminated()) {
      // common part of the protocol to execute before the specific part
      _controller._lock.lock();
      try {
        while (_controller.isHypervisorExecuting() || _controller.isPaused()) {
          _controller.incWaitingHypervisorStations();
          _controller._hyperWake.signalAll();
          _controller._stationsWake.await();
        }
      } catch (InterruptedException e) {
      } finally {
        _controller._lock.unlock();
      }
      protocol(); // specific part of the protocol to be executed
    }
  }

  /**
   * Method to be redefined by subclass to make them act as the simulation asks
   */
  public abstract void protocol();

  public String toString() {
    return "\n" + _station.toString() + "\n{ Incoming: "
        + _station.getIncoming() + "\n Read: " + _station.getRead() + "}";
  }
}
