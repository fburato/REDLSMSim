package test.merotto;

import parser.*;

public class TestWebParser {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    WebParser wp = new WebParser("www.math.unipd.it");
    System.out.println("sending request");
    wp.sendGetRequest("/~conti/teaching/PCD1112/project_config.txt");
    System.out.println("receiveing response");
    wp.parse();
    for (int i = 0; i < wp.getKeys().length; ++i) {
      System.out.println(wp.getKeys()[i] + ":" + wp.getValues(wp.getKeys()[i])
          + ";");

    }
  }

}
