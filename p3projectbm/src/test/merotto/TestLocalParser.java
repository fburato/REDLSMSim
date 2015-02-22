package test.merotto;

import parser.*;

public class TestLocalParser {
  public static void main(String args[]) throws Exception {
    LocalParser lp = new LocalParser("/home/mitiu/Desktop/config.txt");
    lp.parse();
    for (int i = 0; i < lp.getKeys().length; ++i) {
      System.out.println(lp.getKeys()[i] + ":" + lp.getValues(lp.getKeys()[i]));
    }
  }
}