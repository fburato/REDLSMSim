package client;

import graph.nodes.StationNode;
import client.interfaces.StationFactory;
import client.GraphInstantiator;
import exceptions.IncompleteParameterException;
import exceptions.WrongDataTypeException;

import java.util.Map;

import parser.Parser;

/**
 * A StationFactory which uses a Parser to retrieve its information and which is
 * able to produce Standard Wireless nodes.
 * 
 * @author Francesco Burato
 * @version 1
 */
public class SWFactory implements StationFactory {
  private boolean _validateExec;
  private int _n;
  private double _ray;
  private double _initBattery;
  private Parser _parser;
  private GraphInstantiator _instantiator;

  /**
   * Build the factory from a Parser
   * 
   * @param p
   *          The parser used for retrieving data
   */
  public SWFactory(Parser p) {
    _parser = p;
    _validateExec = false;
    _instantiator = null;
  }

  @Override
  public void validate() throws WrongDataTypeException,
      IncompleteParameterException {
    _n = TypesValidator.getPositiveInteger("n", _parser);
    _ray = TypesValidator.getPositiveDouble("r", _parser);
    _initBattery = TypesValidator.getPositiveDouble("E", _parser);
    // at this point every parameter has been instantiated
    _validateExec = true;
    // generate _n+1 StationNode because one of them will be the clone
    _instantiator = new GraphInstantiator(_n + 1, _ray, _initBattery);

  }

  @Override
  public StationNode[] makeGraph() {
    if (_validateExec)
      return _instantiator.buildGraph();
    return null;
  }

  @Override
  public boolean writeStatisticalData(Map<String, String> data) {
    if (_validateExec) {
      data.put("E", "" + _initBattery);
      data.put("n", "" + _n);
      data.put("r", "" + _ray);
      return true;
    }
    return false;
  }
}
