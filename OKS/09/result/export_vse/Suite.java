import junit.framework.Test;
import junit.framework.TestSuite;

public class Suite {

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(TS01.class);
    suite.addTestSuite(TS0401.class);
    suite.addTestSuite(TC04030401.class);
    suite.addTestSuite(TC040401.class);
    suite.addTestSuite(TC04030306.class);
    suite.addTestSuite(TC04030305.class);
    suite.addTestSuite(TC04030304.class);
    suite.addTestSuite(TC04030303.class);
    suite.addTestSuite(TC04030101.class);
    suite.addTestSuite(TC04020602.class);
    suite.addTestSuite(TC04020502.class);
    suite.addTestSuite(TC04020203.class);
    suite.addTestSuite(TC04020107.class);
    suite.addTestSuite(TC04020103.class);
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
