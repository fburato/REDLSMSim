package test.burato;
import client.*;
import threads.RED.*;
import java.io.*;
import parser.*;
import exceptions.BadFileFormatException;
import exceptions.IncompleteParameterException;
import exceptions.UnknownProtocolException;
import exceptions.WrongDataTypeException;

public class ClientREDTest {
  private Parser _parser;
  private SimulationClient _client;
  public ClientREDTest() throws FileNotFoundException,WrongDataTypeException,
  IncompleteParameterException, UnknownProtocolException, BadFileFormatException,IOException{
    _parser = new LocalParser("test.txt");
    _parser.parse();
    Satellite s = new Satellite();
    String[] labels = { "PROTO", "NSIM", "n", "r", "p", "g", "E",
        "E_send", "E_receive", "E_signature", "minSent", "maxSent", "avgSent",
        "stdDevSent", "minReceived", "maxReceived", "avgReceived",
        "stdDevReceived", "minVerifications", "maxVerifications",
        "avgVerifications", "stdDevVerifications", "minConsumedEnergy",
        "maxConsumedEnergy", "avgConsumedEnergy", "stdDevConsumedEnergy",
        "minStoredMessage", "maxStoredMessage", "avgStoredMessage",
        "stdDevStoredMessage", "cloneDetected" };
    for(String str : labels)
      System.out.print(str + ";");
    System.out.println("ignore");
    _client = new SimulationClient(_parser,null,null,s);
    _client.startSimulation();
  }
  
  public static void main(String argv[]) throws Exception {
    @SuppressWarnings("unused")
    ClientREDTest client = new ClientREDTest();
  }
}
