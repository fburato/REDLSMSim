package test.burato;

import graph.nodes.*;
import graph.support.Arch;
import graph.support.communications.MailBox;
import graph.support.communications.Message;
import graph.support.communications.Rule;
import graph.support.metrics.EuclideanPoint;
import graph.support.operations.Operation;
import threads.*;
import java.util.*;

class ClaimMessage implements Message {
  public String _id;
  public EuclideanPoint _pos;

  public ClaimMessage(String id, EuclideanPoint pos) {
    _id = id;
    _pos = new EuclideanPoint(pos);
  }
}

class SendallOperation implements Operation {
  private StationNode _station;
  private Message _m;

  public SendallOperation(StationNode s, Message m) {
    _station = s;
    _m = m;
  }

  public void setCost(double d) {
  }

  public double getCost() {
    return 0;
  }

  public void execute() {
    MailBox read = _station.getRead();
    ArrayList<Message> arr = read.find(_m, new Rule() {
      public boolean compare(Message m, Message d) {
        if (!(m instanceof ClaimMessage) || !(d instanceof ClaimMessage))
          return false;
        ClaimMessage newM = (ClaimMessage) m, newD = (ClaimMessage) d;
        return newM._id.equals(newD._id)
            && newM._pos.getX() == newD._pos.getX()
            && newM._pos.getY() == newD._pos.getY();
      }
    });
    // se arr non e' vuota vuol dire che il messaggio l'ho gia' inviato. Quindi
    // lo salto. Se e' vuoto vuol dire che e' la prima volta che lo trovo quindi
    // lo invio a tutti i vicini
    if (arr.isEmpty()) {
      ArrayList<Arch> adjacent = _station.getAdjacents();
      for (Arch arc : adjacent) {
        StationNode nextStation = (StationNode) arc._node;
        nextStation.putMessage(_m);
      }
    }
  }
}

class ReceiveOperation implements Operation {
  private StationNode _station;
  private Message _m;

  public ReceiveOperation(StationNode s) {
    _station = s;
  }

  public void setCost(double d) {
  }

  public double getCost() {
    return 0;
  }

  public void execute() {
    _m = _station.getIncoming().getFirst();
  }

  public Message receiveMessage() {
    return _m;
  }
}

class FindreplicaOperation implements Operation {
  private StationNode _station;
  private boolean _replicaFinded;
  private ClaimMessage _m;

  public FindreplicaOperation(StationNode s, ClaimMessage m) {
    _station = s;
    _replicaFinded = false;
    _m = m;
  }

  public void setCost(double d) {
  }

  public double getCost() {
    return 0;
  }

  public void execute() {
    MailBox read = _station.getRead();
    ArrayList<Message> arr = read.find(_m, new Rule() {
      public boolean compare(Message m, Message d) {
        if (!(d instanceof ClaimMessage))
          return false;
        ClaimMessage newM = (ClaimMessage) m, newD = (ClaimMessage) d;
        return newM._id.equals(newD._id)
            && (newM._pos.getX() != newD._pos.getX() || newM._pos.getY() != newD._pos
                .getY());
      }
    });
    _replicaFinded = !arr.isEmpty();
  }

  public boolean getResult() {
    return _replicaFinded;
  }
}

public class FloodingProtocol extends ProtocolThread {
  private boolean _first;

  public FloodingProtocol(StationNode station, Hypervisor controller) {
    super(station, controller);
    _first = true;
  }

  public void protocol() {
    if (_first) {
      // se e' la prima iterazione invio a tutti i vicini un pacchetto di Claim
      // contenente i dati della stazione
      ClaimMessage originalClaim = new ClaimMessage(
          _station.getId(), _station.getPosition());
      SendallOperation s = new SendallOperation(_station, originalClaim);
      _station.performs(s);
      _station.getRead().push(originalClaim);
      _first = false;
    } else {
      // se non e' la prima iterazione devo inoltrare i messaggi e basta
      synchronized (_station.getIncoming()) {
        try {
          while (_station.getIncoming().isEmpty()) {// || _controller.getCloneDetected())
            _controller.incEmptyMailBoxStations();
            _station.getIncoming().wait();
            _controller.decEmptyMailBoxStations();
          }
        } catch (InterruptedException e) {
        }
      }
      // c'e' almeno un messaggio di Claim. Lo ricevo
      ReceiveOperation r = new ReceiveOperation(_station);
      _station.performs(r);
      ClaimMessage m = (ClaimMessage)r.receiveMessage();
      // effettuo un controllo sull'eventualita' che ci sia un clone
      FindreplicaOperation f = new FindreplicaOperation(_station,m);
      _station.performs(f);
      if(f.getResult())
        //se l'operazione di individuazione del clone ha successo devo segnalarlo all'hypervisor
        _controller.cloneDetected();
      // a prescindere dal risultato dell'individuzione faccio il forward del Claim corrente
      SendallOperation s = new SendallOperation(_station,m);
      _station.performs(s);
      // aggiungo il messaggio corrente alla posta letta e lo rimuovo dalla posta in arrivo
      _station.getRead().push(_station.getIncoming().pop());
    }
  }
}