package graph.support;

import graph.interfaces.Node;

/**
 * A BaseArch is an unweighed Arch i.e. its cost field is never used and it's
 * fixed to 0
 * 
 * @author Francesco Burato
 * @version 1.0.1
 */
public class BaseArch extends Arch {
  public BaseArch(Node b) {
    super(0, b);
  }
}
