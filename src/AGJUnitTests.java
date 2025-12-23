import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import static org.junit.jupiter.api.Assertions.*;

public class AGJUnitTests {
    static Stream<Arguments> agFiles() {
        File dir = new File("test_cases_v3/test_cases_v3/AG");
        File[] testFiles = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (testFiles == null) return Stream.empty();
        Arrays.sort(testFiles);
        return Arrays.stream(testFiles).map(f -> Arguments.of(f.getName(), f));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("agFiles")
    void testAGScheduler(String testName, File testFile) throws Exception {
        TestRunner runner = new TestRunner();
        TestRunner.AGTestResult result = runner.runAGTest(testFile);
        assertTrue(result.agPassed, "AG scheduling failed for " + testName + "\n" + result.details);
    }
}

