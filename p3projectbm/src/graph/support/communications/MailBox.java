/**
 * 
 */
package graph.support.communications;



import java.util.ArrayList;
import java.util.Iterator;

/**
 * An undefined MailBox which offers services of adding messages, find messages
 * and returning messages in an order.
 * 
 * @author Francesco Burato
 * @version 1.0.1
 * 
 */
public class MailBox extends java.util.LinkedList<Message>{
  /**
   * 
   */
  private static final long serialVersionUID = -6829891870258941162L;

  /**
   * Returns all messages which satisfy a given Rule.
   * 
   * @param m
   *          The source message to be used as comparison term by the Rule
   * @param r
   *          The Rule object use to compare every message in the MailBox with m
   * @return A Message list m1 in M s.t. r.compare(m1,m)==true
   */
  public synchronized ArrayList<Message> find(Message m, Rule r) {
    ArrayList<Message> res = new ArrayList<Message>();
    for (Iterator<Message> i = iterator(); i.hasNext();) {
      Message temp = i.next();
      if (r.compare(m, temp))
        res.add(temp);
    }
    return res;
  }
  /**
   * Returns all messages which satisfy a given Filter
   * 
   * @param f
   *          The filter to be applied
   * @return A Message list m1 in M s.t. f.apply(m1)==true
   */
  public synchronized ArrayList<Message> find(Filter f) {
    ArrayList<Message> res = new ArrayList<Message>();
    for (Iterator<Message> i = iterator(); i.hasNext();) {
      Message temp = i.next();
      if (f.apply(temp))
        res.add(temp);
    }
    return res;
  }
  
  @Override
  public synchronized void addFirst(Message m){
    super.addFirst(m);
    notifyAll();
  }
  
  @Override
  public synchronized void addLast(Message m) {
    super.addLast(m);
    notifyAll();
  }
  
  @Override
  public synchronized void clear(){
    super.clear();
  }
  
  @Override
  public synchronized Message getFirst() {
    if(isEmpty())
      return null;
    return super.getFirst();
  }
  
  @Override
  public synchronized Message getLast() {
    if(isEmpty())
      return null;
    return super.getLast();
  }
  
  @Override
  public synchronized Message removeFirst() {
    if(isEmpty())
      return null;
    return super.removeFirst();
  }
  
  @Override
  public synchronized Message removeLast() {
    if(isEmpty())
      return null;
    return super.removeLast();
  }
  
  @Override
  public synchronized int size() {
    return super.size();
  }
  
  @Override
  public synchronized boolean isEmpty(){
    return super.size() == 0;
  }
  
  @Override
  public synchronized String toString() {
    return super.toString();
  }
}
