import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class PriorityTest {

    @Test
    public void test_1() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/Other_Schedulers/test_1.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.TestResult res = runner.runTest(f);
        if (!res.priorityPassed) {
            String msg = TestUtils.onlyFailures(res.details);
            fail(f.getName() + " : " + msg);
        }
    }

    @Test
    public void test_2() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/Other_Schedulers/test_2.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.TestResult res = runner.runTest(f);
        assertTrue(res.details, res.priorityPassed);
    }

    @Test
    public void test_3() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/Other_Schedulers/test_3.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.TestResult res = runner.runTest(f);
        assertTrue(res.details, res.priorityPassed);
    }

    @Test
    public void test_4() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/Other_Schedulers/test_4.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.TestResult res = runner.runTest(f);
        assertTrue(res.details, res.priorityPassed);
    }

    @Test
    public void test_5() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/Other_Schedulers/test_5.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.TestResult res = runner.runTest(f);
        assertTrue(res.details, res.priorityPassed);
    }

    @Test
    public void test_6() throws Exception {
        File f = new File("test_cases_v3/test_cases_v3/Other_Schedulers/test_6.json");
        assertTrue("Test file missing: " + f.getPath(), f.exists());
        TestRunner runner = new TestRunner();
        TestRunner.TestResult res = runner.runTest(f);
        assertTrue(res.details, res.priorityPassed);
    }
}
