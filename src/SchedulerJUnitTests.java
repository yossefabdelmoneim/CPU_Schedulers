import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import static org.junit.jupiter.api.Assertions.*;

public class SchedulerJUnitTests {
    static Stream<Arguments> otherSchedulerFiles() {
        File dir = new File("test_cases_v3/test_cases_v3/Other_Schedulers");
        File[] testFiles = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (testFiles == null) return Stream.empty();
        Arrays.sort(testFiles);
        return Arrays.stream(testFiles).map(f -> Arguments.of(f.getName(), f));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("otherSchedulerFiles")
    void testOtherScheduler(String testName, File testFile) throws Exception {
        TestRunner runner = new TestRunner();
        TestRunner.TestResult result = runner.runTest(testFile);

        assertAll(
            () -> assertTrue(result.sjfPassed, "SJF failed for " + testName + "\n" + result.details),
            () -> assertTrue(result.rrPassed, "Round Robin failed for " + testName + "\n" + result.details),
            () -> assertTrue(result.priorityPassed, "Priority failed for " + testName + "\n" + result.details)
        );
    }
}
