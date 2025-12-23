import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class AGTest {

    @Test
    public void ag_test1() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/AG/AG_test1.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.AGTestResult res = runner.runAGTest(f);
        if (!res.agPassed) {
            String msg = TestUtils.onlyFailures(res.details);
            fail(f.getName() + " : " + msg);
        }
    }

    @Test
    public void ag_test2() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/AG/AG_test2.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.AGTestResult res = runner.runAGTest(f);
        assertTrue(res.details, res.agPassed);
    }

    @Test
    public void ag_test3() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/AG/AG_test3.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.AGTestResult res = runner.runAGTest(f);
        assertTrue(res.details, res.agPassed);
    }

    @Test
    public void ag_test4() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/AG/AG_test4.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.AGTestResult res = runner.runAGTest(f);
        assertTrue(res.details, res.agPassed);
    }

    @Test
    public void ag_test5() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/AG/AG_test5.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.AGTestResult res = runner.runAGTest(f);
        assertTrue(res.details, res.agPassed);
    }

    @Test
    public void ag_test6() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/AG/AG_test6.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.AGTestResult res = runner.runAGTest(f);
        assertTrue(res.details, res.agPassed);
    }
}
