/**
 * 
 */
package client;

import exceptions.IncompleteParameterException;
import exceptions.UninitializedProtocolException;
import exceptions.UnknownProtocolException;
import exceptions.WrongDataTypeException;
import graph.nodes.StationNode;
import threads.Hypervisor;
import threads.ProtocolThread;
import client.interfaces.ProtocolFactory;
import java.util.Map;
import java.util.TreeMap;

import parser.Parser;
import threads.RED.RedProtocol;
import threads.RED.Satellite;

/**
 * ProtocolFactory able to produce ProtocolThread instantiated for RED
 * simulation from a given Parser
 * 
 * @author Francesco Burato
 * @version 1
 */
public class REDFactory implements ProtocolFactory {
  protected Map<String, Object> _parameters;
  protected boolean _protocolInitialized;
  protected String _protocolName;
  protected Parser _parser;
  protected Hypervisor _controller;
  protected Satellite _satellite;

  /**
   * This is a method used to instantiate the basic parameter used for LSM and
   * RED
   * 
   * @throws WrongDataTypeException
   *           If one of the parameter if of the wrong type
   * @throws IncompleteParameterException
   *           If one of the parameter is missing
   */
  protected void redLsmInitializer() throws WrongDataTypeException,
      IncompleteParameterException {
    try {
      _parameters.put("g", TypesValidator.getPositiveInteger("g", _parser));
      _parameters.put("p", TypesValidator.getDouble01("p", _parser));
      _parameters.put("E_send",
          TypesValidator.getPositiveDouble("E_send", _parser));
      _parameters.put("E_receive",
          TypesValidator.getPositiveDouble("E_receive", _parser));
      _parameters.put("E_signature",
          TypesValidator.getPositiveDouble("E_signature", _parser));
    } catch (WrongDataTypeException e) {
      _parameters.clear();
      throw e;
    } catch (IncompleteParameterException e) {
      _parameters.clear();
      throw e;
    }
  }

  /**
   * Builds the factory from a Parser
   * 
   * @param p
   *          The parser that will be used to retrieve informatios
   */
  public REDFactory(Parser p, Satellite s) {
    _parameters = new TreeMap<String, Object>();
    _protocolInitialized = false;
    _parser = p;
    _satellite = s;
  }

  @Override
  public void setHypervisor(Hypervisor h) {
    _controller = h;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * client.interfaces.ProtocolFactory#instantiateProtocol(java.lang.String,
   * graph.interfaces.Parser)
   */
  @Override
  public void instantiateProtocol(String name) throws UnknownProtocolException,
      WrongDataTypeException, IncompleteParameterException {
    if (name.equals("RED")) {
      _parameters.clear();
      _protocolName = name;
      redLsmInitializer();
      _protocolInitialized = true;
    } else
      throw new UnknownProtocolException(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * client.interfaces.ProtocolFactory#generateProtocol(graph.nodes.StationNode
   * [])
   */
  @Override
  public ProtocolThread[] generateProtocol(StationNode[] stations)
      throws UninitializedProtocolException {
    if (_protocolInitialized == false)
      throw new UninitializedProtocolException();
    // the protocol has been initialized and is RED
    /**
     * RedProtocol(StationNode station, Hypervisor controller, int g, double
     * receiveEnergy, double sendEnergy, double verificationEnergy, double
     * probability, PseudoRandomTable table)
     */
    if (_protocolName.equals("RED")) {
      ProtocolThread[] res = new ProtocolThread[stations.length];
      for (int i = 0; i < stations.length; ++i) {
        res[i] = new RedProtocol(stations[i], _controller,
            (Integer) _parameters.get("g"),
            (Double) _parameters.get("E_receive"),
            (Double) _parameters.get("E_send"),
            (Double) _parameters.get("E_signature"),
            (Double) _parameters.get("p"), _satellite);
      }
      return res;
    }
    return null;
  }

  @Override
  public boolean writeStatisticalData(Map<String, String> data) {
    if (_protocolInitialized && _protocolName.equals("RED")) {
      data.put("g", "" + _parameters.get("g"));
      data.put("E_receive", "" + _parameters.get("E_receive"));
      data.put("E_send", "" + _parameters.get("E_send"));
      data.put("E_signature", "" + _parameters.get("E_signature"));
      data.put("p", "" + _parameters.get("p"));
      return true;
    }
    return false;
  }
}
