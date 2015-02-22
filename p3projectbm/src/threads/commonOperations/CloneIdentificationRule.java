package threads.commonOperations;

import threads.commonMessages.ForwardMessage;
import graph.support.communications.Message;
import graph.support.communications.Rule;

/**
 * This Rule is used to find out if there is a clone evidence between to
 * messages so if source and destination messages have same claimed id but
 * different claimed position.
 * 
 * @author Francesco Burato
 * @version 1
 */

public class CloneIdentificationRule implements Rule {
  @Override
  public boolean compare(Message s, Message d) {
    if (!(s instanceof ForwardMessage) || !(d instanceof ForwardMessage))
      return false;
    ForwardMessage source = (ForwardMessage) s, dest = (ForwardMessage) d;
    return source.getClaim().getId().equals(dest.getClaim().getId())
        && !source.getClaim().getPosition()
            .equals(dest.getClaim().getPosition());
  }

}
