/**
 * 
 */
package client;

import java.util.Map;

import parser.Parser;

import graph.nodes.StationNode;
import threads.ProtocolThread;
import exceptions.IncompleteParameterException;
import exceptions.UninitializedProtocolException;
import exceptions.UnknownProtocolException;
import exceptions.WrongDataTypeException;
import threads.LSM.LSMProtocol;
import threads.RED.Satellite;

/**
 * ProtocolFactory that, extending REDFactory, is able to produce ProtocolThread
 * instantiated for both LSM and RED simulation from a given parser
 * 
 * @author Mattia Merotto
 * 
 */
public class REDLSMFactory extends REDFactory {

  /**
   * Builds the factory from a parser
   * 
   * @param p
   *          The parser that will be used to retrieve informations
   */
  public REDLSMFactory(Parser p, Satellite s) {
    super(p, s);
  }

  @Override
  public void instantiateProtocol(String name) throws UnknownProtocolException,
      WrongDataTypeException, IncompleteParameterException {
    try {
      super.instantiateProtocol(name);
    } catch (UnknownProtocolException e) {
      // Protocol name isn't RED
      if (name.equals("LSM")) {
        _parameters.clear();
        _protocolName = name;
        redLsmInitializer();
        _protocolInitialized = true;
      } else
        throw new UnknownProtocolException(name);
    }
  }

  @Override
  public ProtocolThread[] generateProtocol(StationNode[] stations)
      throws UninitializedProtocolException {
    ProtocolThread[] res = super.generateProtocol(stations);
    if (res != null)
      // The protocol has already been initialized and is RED
      return res;
    if (_protocolInitialized == false)
      throw new UninitializedProtocolException();
    // The protocol has been initialized and is LSM
    if (_protocolName.equals("LSM")) {
      res = new ProtocolThread[stations.length];
      for (int i = 0; i < stations.length; ++i) {
        res[i] = new LSMProtocol(stations[i], _controller,
            (Integer) _parameters.get("g"),
            (Double) _parameters.get("E_receive"),
            (Double) _parameters.get("E_send"),
            (Double) _parameters.get("E_signature"),
            (Double) _parameters.get("p"));
      }
      return res;
    }
    return null;
  }

  @Override
  public boolean writeStatisticalData(Map<String, String> data) {
    boolean success = super.writeStatisticalData(data);
    if (success)
      return true;
    if (_protocolInitialized && _protocolName.equals("LSM")) {
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
