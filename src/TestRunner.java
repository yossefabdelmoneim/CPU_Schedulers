import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class TestRunner {
    
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        
        // Run AG tests
        String agTestDir = "test_cases_v3/test_cases_v3/AG";
        runner.runAGTests(agTestDir);
        
        // Run Other Scheduler tests
        String testDir = "test_cases_v3/test_cases_v3/Other_Schedulers";
        runner.runAllTests(testDir);
    }
    
    public void runAllTests(String testDirectory) {
        File dir = new File(testDirectory);
        File[] testFiles = dir.listFiles((d, name) -> name.endsWith(".json"));
        
        if (testFiles == null || testFiles.length == 0) {
            System.out.println("No test files found in " + testDirectory);
            return;
        }
        
        Arrays.sort(testFiles);
        
        int totalTests = 0;
        int passedTests = 0;
        
        System.out.println("=".repeat(20));
        System.out.println("Running CPU Scheduler Tests");
        System.out.println("=".repeat(20));
        
        for (File testFile : testFiles) {
            System.out.println("\n" + "=".repeat(20));
            System.out.println("Testing: " + testFile.getName());
            System.out.println("=".repeat(20));
            
            TestResult result = runTest(testFile);
            totalTests++;
            
            if (result.allPassed) {
                passedTests++;
                System.out.println("\n[PASS] TEST PASSED");
            } else {
                System.out.println("\n[FAIL] TEST FAILED");
            }
            
            System.out.println(result.details);
        }
        
        System.out.println("\n" + "=".repeat(20));
        System.out.println("SUMMARY: " + passedTests + "/" + totalTests + " tests passed");
        System.out.println("=".repeat(20));
    }
    
    public TestResult runTest(File testFile) {
        TestResult result = new TestResult();
        StringBuilder details = new StringBuilder();
        
        try {
            String jsonContent = new String(Files.readAllBytes(testFile.toPath()));
            TestCase testCase = parseTestCase(jsonContent);
            
            // Create processes from input
            List<Process> processes = new ArrayList<>();
            for (ProcessInput p : testCase.input.processes) {
                processes.add(new Process(p.name, p.arrival, p.burst, p.priority, 0));
            }
            
            // Test SJF
            details.append("\n--- SJF Scheduler ---\n");
            boolean sjfPassed = testSJF(processes, testCase.input.contextSwitch, 
                                       testCase.expectedOutput.SJF, details);
            result.sjfPassed = sjfPassed;
            
            // Test Round Robin
            details.append("\n--- Round Robin Scheduler ---\n");
            boolean rrPassed = testRoundRobin(processes, testCase.input.rrQuantum, 
                                            testCase.input.contextSwitch, 
                                            testCase.expectedOutput.RR, details);
            result.rrPassed = rrPassed;
            
            // Test Priority
            details.append("\n--- Priority Scheduler ---\n");
            boolean priorityPassed = testPriority(processes, testCase.input.contextSwitch, 
                                                  testCase.input.agingInterval, 
                                                  testCase.expectedOutput.Priority, details);
            result.priorityPassed = priorityPassed;
            
            result.allPassed = sjfPassed && rrPassed && priorityPassed;
            result.details = details.toString();
            
        } catch (Exception e) {
            details.append("ERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
            result.allPassed = false;
            result.details = details.toString();
        }
        
        return result;
    }
    
    private boolean testSJF(List<Process> processes, int contextSwitch, 
                           ExpectedSchedulerOutput expected, StringBuilder details) {
        SJFScheduler sjf = new SJFScheduler(processes, contextSwitch);
        sjf.execute();
        
        List<String> actualOrder = sjf.getExecutionOrder();
        List<Process> actualProcesses = sjf.getProcesses();
        
        // Compare execution order
        boolean orderMatch = compareExecutionOrder(actualOrder, expected.executionOrder, details, "SJF");
        
        // Compare process results
        boolean processMatch = compareProcessResults(actualProcesses, expected.processResults, details, "SJF");
        
        // Compare averages
        double avgWT = calculateAverageWaitingTime(actualProcesses);
        double avgTAT = calculateAverageTurnaroundTime(actualProcesses);
        boolean avgMatch = compareDoubles(avgWT, expected.averageWaitingTime, details, "SJF Average Waiting Time") &&
                          compareDoubles(avgTAT, expected.averageTurnaroundTime, details, "SJF Average Turnaround Time");
        
        return orderMatch && processMatch && avgMatch;
    }
    
    private boolean testRoundRobin(List<Process> processes, int quantum, int contextSwitch, 
                                  ExpectedSchedulerOutput expected, StringBuilder details) {
        RoundRobin.RRResult result = RoundRobin.simulateRR(processes, quantum, contextSwitch);
        
        List<String> actualOrder = result.executionOrder;
        List<Process> actualProcesses = result.processes;
        
        // Compare execution order
        boolean orderMatch = compareExecutionOrder(actualOrder, expected.executionOrder, details, "RR");
        
        // Compare process results
        boolean processMatch = compareProcessResults(actualProcesses, expected.processResults, details, "RR");
        
        // Compare averages
        double avgWT = calculateAverageWaitingTime(actualProcesses);
        double avgTAT = calculateAverageTurnaroundTime(actualProcesses);
        boolean avgMatch = compareDoubles(avgWT, expected.averageWaitingTime, details, "RR Average Waiting Time") &&
                          compareDoubles(avgTAT, expected.averageTurnaroundTime, details, "RR Average Turnaround Time");
        
        return orderMatch && processMatch && avgMatch;
    }
    
    private boolean testPriority(List<Process> processes, int contextSwitch, int agingInterval, 
                                ExpectedSchedulerOutput expected, StringBuilder details) {
        PriorityScheduler priority = new PriorityScheduler(processes, contextSwitch, agingInterval);
        priority.execute();
        
        List<String> actualOrder = priority.getExecutionOrder();
        List<Process> actualProcesses = priority.getProcesses();
        
        // Compare execution order
        boolean orderMatch = compareExecutionOrder(actualOrder, expected.executionOrder, details, "Priority");
        
        // Compare process results
        boolean processMatch = compareProcessResults(actualProcesses, expected.processResults, details, "Priority");
        
        // Compare averages
        double avgWT = calculateAverageWaitingTime(actualProcesses);
        double avgTAT = calculateAverageTurnaroundTime(actualProcesses);
        boolean avgMatch = compareDoubles(avgWT, expected.averageWaitingTime, details, "Priority Average Waiting Time") &&
                          compareDoubles(avgTAT, expected.averageTurnaroundTime, details, "Priority Average Turnaround Time");
        
        return orderMatch && processMatch && avgMatch;
    }
    
    private boolean compareExecutionOrder(List<String> actual, List<String> expected, 
                                         StringBuilder details, String scheduler) {
        List<String> filteredActual = removeConsecutiveDuplicates(actual);
        
        details.append(String.format("  %s Execution Order:\n", scheduler));
        details.append(String.format("    Expected: %s\n", expected));
        details.append(String.format("    Actual:   %s\n", filteredActual));
        
        if (filteredActual.size() != expected.size()) {
            details.append(String.format("  [FAIL] Length mismatch (Expected: %d, Actual: %d)\n", 
                        expected.size(), filteredActual.size()));
            return false;
        }
        
        for (int i = 0; i < filteredActual.size(); i++) {
            if (!filteredActual.get(i).equals(expected.get(i))) {
                details.append(String.format("  [FAIL] Mismatch at index %d\n", i));
                return false;
            }
        }
        
        details.append(String.format("  [PASS] PASSED\n"));
        return true;
    }
    
    private List<String> removeConsecutiveDuplicates(List<String> list) {
        List<String> result = new ArrayList<>();
        if (list.isEmpty()) {
            return result;
        }
        
        String last = null;
        for (String item : list) {
            if (!item.equals(last)) {
                result.add(item);
                last = item;
            }
        }
        return result;
    }
    
    private boolean compareProcessResults(List<Process> actual, List<ProcessResult> expected, 
                                         StringBuilder details, String scheduler) {
        Map<String, Process> actualMap = new HashMap<>();
        for (Process p : actual) {
            actualMap.put(p.getName(), p);
        }
        
        // Always print expected and actual values
        details.append(String.format("  %s Process Results:\n", scheduler));
        details.append("    Expected:\n");
        for (ProcessResult exp : expected) {
            details.append(String.format("      %s - Waiting: %d, Turnaround: %d\n", 
                        exp.name, exp.waitingTime, exp.turnaroundTime));
        }
        details.append("    Actual:\n");
        for (ProcessResult exp : expected) {
            Process act = actualMap.get(exp.name);
            if (act != null) {
                details.append(String.format("      %s - Waiting: %d, Turnaround: %d\n", 
                            act.getName(), act.getWaitingTime(), act.getTurnaroundTime()));
            }
        }
        
        boolean allMatch = true;
        for (ProcessResult exp : expected) {
            Process act = actualMap.get(exp.name);
            if (act == null) {
                details.append(String.format("  [FAIL] Process %s: Not found\n", exp.name));
                allMatch = false;
                continue;
            }
            
            boolean wtMatch = act.getWaitingTime() == exp.waitingTime;
            boolean tatMatch = act.getTurnaroundTime() == exp.turnaroundTime;
            
            if (!wtMatch || !tatMatch) {
                details.append(String.format("  [FAIL] Process %s mismatch:\n", exp.name));
                if (!wtMatch) {
                    details.append(String.format("    Waiting Time: Expected %d, Actual %d\n", 
                                exp.waitingTime, act.getWaitingTime()));
                }
                if (!tatMatch) {
                    details.append(String.format("    Turnaround Time: Expected %d, Actual %d\n", 
                                exp.turnaroundTime, act.getTurnaroundTime()));
                }
                allMatch = false;
            }
        }
        
        if (allMatch) {
            details.append(String.format("  [PASS] PASSED\n"));
        }
        
        return allMatch;
    }
    
    private boolean compareDoubles(double actual, double expected, StringBuilder details, String label) {
        if (Math.abs(actual - expected) < 0.01) {
            details.append(String.format("  [PASS] %s: PASSED (Expected: %.2f, Actual: %.2f)\n", 
                        label, expected, actual));
            return true;
        } else {
            details.append(String.format("  [FAIL] %s: FAILED (Expected: %.2f, Actual: %.2f)\n", 
                        label, expected, actual));
            return false;
        }
    }
    
    private double calculateAverageWaitingTime(List<Process> processes) {
        double sum = 0;
        for (Process p : processes) {
            sum += p.getWaitingTime();
        }
        return sum / processes.size();
    }
    
    private double calculateAverageTurnaroundTime(List<Process> processes) {
        double sum = 0;
        for (Process p : processes) {
            sum += p.getTurnaroundTime();
        }
        return sum / processes.size();
    }
    
    // JSON Parsing
    private TestCase parseTestCase(String json) {
        TestCase testCase = new TestCase();
        
        // Extract name
        Pattern namePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");
        Matcher nameMatcher = namePattern.matcher(json);
        if (nameMatcher.find()) {
            testCase.name = nameMatcher.group(1);
        }
        
        // Extract input
        testCase.input = parseInput(json);
        
        // Extract expected output
        testCase.expectedOutput = parseExpectedOutput(json);
        
        return testCase;
    }
    
    private Input parseInput(String json) {
        Input input = new Input();
        
        // Extract contextSwitch
        Pattern csPattern = Pattern.compile("\"contextSwitch\"\\s*:\\s*(\\d+)");
        Matcher csMatcher = csPattern.matcher(json);
        if (csMatcher.find()) {
            input.contextSwitch = Integer.parseInt(csMatcher.group(1));
        }
        
        // Extract rrQuantum
        Pattern qPattern = Pattern.compile("\"rrQuantum\"\\s*:\\s*(\\d+)");
        Matcher qMatcher = qPattern.matcher(json);
        if (qMatcher.find()) {
            input.rrQuantum = Integer.parseInt(qMatcher.group(1));
        }
        
        // Extract agingInterval
        Pattern aiPattern = Pattern.compile("\"agingInterval\"\\s*:\\s*(\\d+)");
        Matcher aiMatcher = aiPattern.matcher(json);
        if (aiMatcher.find()) {
            input.agingInterval = Integer.parseInt(aiMatcher.group(1));
        }
        
        // Extract processes
        Pattern processPattern = Pattern.compile("\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"arrival\"\\s*:\\s*(\\d+)\\s*,\\s*\"burst\"\\s*:\\s*(\\d+)\\s*,\\s*\"priority\"\\s*:\\s*(\\d+)\\s*\\}");
        Matcher processMatcher = processPattern.matcher(json);
        input.processes = new ArrayList<>();
        while (processMatcher.find()) {
            ProcessInput p = new ProcessInput();
            p.name = processMatcher.group(1);
            p.arrival = Integer.parseInt(processMatcher.group(2));
            p.burst = Integer.parseInt(processMatcher.group(3));
            p.priority = Integer.parseInt(processMatcher.group(4));
            input.processes.add(p);
        }
        
        return input;
    }
    
    private ExpectedOutput parseExpectedOutput(String json) {
        ExpectedOutput output = new ExpectedOutput();
        
        // Extract SJF output
        output.SJF = parseSchedulerOutput(json, "SJF");
        
        // Extract RR output
        output.RR = parseSchedulerOutput(json, "RR");
        
        // Extract Priority output
        output.Priority = parseSchedulerOutput(json, "Priority");
        
        return output;
    }
    
    private ExpectedSchedulerOutput parseSchedulerOutput(String json, String schedulerName) {
        ExpectedSchedulerOutput output = new ExpectedSchedulerOutput();
        
        // Find the scheduler block
        String schedulerBlock = extractSchedulerBlock(json, schedulerName);
        if (schedulerBlock == null) return output;
        
        // Extract execution order
        Pattern orderPattern = Pattern.compile("\"executionOrder\"\\s*:\\s*\\[([^\\]]+)\\]");
        Matcher orderMatcher = orderPattern.matcher(schedulerBlock);
        if (orderMatcher.find()) {
            String orderStr = orderMatcher.group(1);
            output.executionOrder = new ArrayList<>();
            Pattern namePattern = Pattern.compile("\"([^\"]+)\"");
            Matcher nameMatcher = namePattern.matcher(orderStr);
            while (nameMatcher.find()) {
                output.executionOrder.add(nameMatcher.group(1));
            }
        }
        
        // Extract process results
        output.processResults = new ArrayList<>();
        Pattern processResultPattern = Pattern.compile("\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"waitingTime\"\\s*:\\s*(\\d+)\\s*,\\s*\"turnaroundTime\"\\s*:\\s*(\\d+)\\s*\\}");
        Matcher processResultMatcher = processResultPattern.matcher(schedulerBlock);
        while (processResultMatcher.find()) {
            ProcessResult pr = new ProcessResult();
            pr.name = processResultMatcher.group(1);
            pr.waitingTime = Integer.parseInt(processResultMatcher.group(2));
            pr.turnaroundTime = Integer.parseInt(processResultMatcher.group(3));
            output.processResults.add(pr);
        }
        
        // Extract average waiting time
        Pattern avgWTPattern = Pattern.compile("\"averageWaitingTime\"\\s*:\\s*([\\d.]+)");
        Matcher avgWTMatcher = avgWTPattern.matcher(schedulerBlock);
        if (avgWTMatcher.find()) {
            output.averageWaitingTime = Double.parseDouble(avgWTMatcher.group(1));
        }
        
        // Extract average turnaround time
        Pattern avgTATPattern = Pattern.compile("\"averageTurnaroundTime\"\\s*:\\s*([\\d.]+)");
        Matcher avgTATMatcher = avgTATPattern.matcher(schedulerBlock);
        if (avgTATMatcher.find()) {
            output.averageTurnaroundTime = Double.parseDouble(avgTATMatcher.group(1));
        }
        
        return output;
    }
    
    private String extractSchedulerBlock(String json, String schedulerName) {
        // Find the scheduler block
        Pattern blockPattern = Pattern.compile("\"" + schedulerName + "\"\\s*:\\s*\\{");
        Matcher blockMatcher = blockPattern.matcher(json);
        if (!blockMatcher.find()) {
            return null;
        }
        
        int start = blockMatcher.end() - 1; // Start at the opening brace
        int braceCount = 0;
        int i = start;
        
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '{') braceCount++;
            if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    return json.substring(start, i + 1);
                }
            }
            i++;
        }
        
        return null;
    }
    
    // AG Test Methods
    public void runAGTests(String testDirectory) {
        File dir = new File(testDirectory);
        File[] testFiles = dir.listFiles((d, name) -> name.endsWith(".json"));
        
        if (testFiles == null || testFiles.length == 0) {
            System.out.println("No AG test files found in " + testDirectory);
            return;
        }
        
        Arrays.sort(testFiles);
        
        int totalTests = 0;
        int passedTests = 0;
        
        System.out.println("=".repeat(20));
        System.out.println("Running AG Scheduler Tests");
        System.out.println("=".repeat(20));
        
        for (File testFile : testFiles) {
            System.out.println("\n" + "=".repeat(20));
            System.out.println("Testing: " + testFile.getName());
            System.out.println("=".repeat(20));
            
            AGTestResult result = runAGTest(testFile);
            totalTests++;
            
            if (result.agPassed) {
                passedTests++;
                System.out.println("\n[PASS] TEST PASSED");
            } else {
                System.out.println("\n[FAIL] TEST FAILED");
            }
            
            System.out.println(result.details);
        }
        
        System.out.println("\n" + "=".repeat(20));
        System.out.println("AG SUMMARY: " + passedTests + "/" + totalTests + " tests passed");
        System.out.println("=".repeat(20));
    }
    
    public AGTestResult runAGTest(File testFile) {
        AGTestResult result = new AGTestResult();
        StringBuilder details = new StringBuilder();
        
        try {
            String jsonContent = new String(Files.readAllBytes(testFile.toPath()));
            AGTestCase testCase = parseAGTestCase(jsonContent);
            
            // Create processes from input with quantum
            List<Process> processes = new ArrayList<>();
            for (AGProcessInput p : testCase.input.processes) {
                Process proc = new Process(p.name, p.arrival, p.burst, p.priority, p.quantum);
                processes.add(proc);
            }
            
            // Test AG Scheduling
            details.append("\n--- AG Scheduler ---\n");
            boolean agPassed = testAG(processes, testCase.expectedOutput, details);
            result.agPassed = agPassed;
            result.details = details.toString();
            
        } catch (Exception e) {
            details.append("ERROR: " + e.getMessage() + "\n");
            e.printStackTrace();
            result.agPassed = false;
            result.details = details.toString();
        }
        
        return result;
    }
    
    private boolean testAG(List<Process> processes, AGExpectedOutput expected, StringBuilder details) {
        AG_Scheduling ag = new AG_Scheduling(processes);
        ag.simulate();
        
        ag.printResults();
        
        List<String> actualOrder = ag.getExecutionOrder();
        List<Process> actualProcesses = ag.getProcesses();
        
        // Compare execution order
        boolean orderMatch = compareExecutionOrder(actualOrder, expected.executionOrder, details, "AG");
        
        // Compare process results
        boolean processMatch = compareAGProcessResults(actualProcesses, expected.processResults, details, "AG");
        
        // Compare averages
        double avgWT = calculateAverageWaitingTime(actualProcesses);
        double avgTAT = calculateAverageTurnaroundTime(actualProcesses);
        boolean avgMatch = compareDoubles(avgWT, expected.averageWaitingTime, details, "AG Average Waiting Time") &&
                          compareDoubles(avgTAT, expected.averageTurnaroundTime, details, "AG Average Turnaround Time");
        
        return orderMatch && processMatch && avgMatch;
    }
    
    private boolean compareAGProcessResults(List<Process> actual, List<AGProcessResult> expected, 
                                           StringBuilder details, String scheduler) {
        Map<String, Process> actualMap = new HashMap<>();
        for (Process p : actual) {
            actualMap.put(p.getName(), p);
        }
        
        // Always print expected and actual values
        details.append(String.format("  %s Process Results:\n", scheduler));
        details.append("    Expected:\n");
        for (AGProcessResult exp : expected) {
            details.append(String.format("      %s - Waiting: %d, Turnaround: %d, QuantumHistory: %s\n", 
                        exp.name, exp.waitingTime, exp.turnaroundTime, exp.quantumHistory));
        }
        details.append("    Actual:\n");
        for (AGProcessResult exp : expected) {
            Process act = actualMap.get(exp.name);
            if (act != null) {
                details.append(String.format("      %s - Waiting: %d, Turnaround: %d, QuantumHistory: %s\n", 
                            act.getName(), act.getWaitingTime(), act.getTurnaroundTime(), 
                            act.getQuantumHistory()));
            }
        }
        
        boolean allMatch = true;
        for (AGProcessResult exp : expected) {
            Process act = actualMap.get(exp.name);
            if (act == null) {
                details.append(String.format("  [FAIL] Process %s: Not found\n", exp.name));
                allMatch = false;
                continue;
            }
            
            boolean wtMatch = act.getWaitingTime() == exp.waitingTime;
            boolean tatMatch = act.getTurnaroundTime() == exp.turnaroundTime;
            boolean qhMatch = compareQuantumHistory(act.getQuantumHistory(), exp.quantumHistory);
            
            if (!wtMatch || !tatMatch || !qhMatch) {
                details.append(String.format("  [FAIL] Process %s mismatch:\n", exp.name));
                if (!wtMatch) {
                    details.append(String.format("    Waiting Time: Expected %d, Actual %d\n", 
                                exp.waitingTime, act.getWaitingTime()));
                }
                if (!tatMatch) {
                    details.append(String.format("    Turnaround Time: Expected %d, Actual %d\n", 
                                exp.turnaroundTime, act.getTurnaroundTime()));
                }
                if (!qhMatch) {
                    details.append(String.format("    Quantum History: Expected %s, Actual %s\n", 
                                exp.quantumHistory, act.getQuantumHistory()));
                }
                allMatch = false;
            }
        }
        
        if (allMatch) {
            details.append(String.format("  [PASS] PASSED\n"));
        }
        
        return allMatch;
    }
    
    private boolean compareQuantumHistory(List<Integer> actual, List<Integer> expected) {
        if (actual.size() != expected.size()) {
            return false;
        }
        for (int i = 0; i < actual.size(); i++) {
            if (!actual.get(i).equals(expected.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private AGTestCase parseAGTestCase(String json) {
        AGTestCase testCase = new AGTestCase();
        testCase.input = parseAGInput(json);
        testCase.expectedOutput = parseAGExpectedOutput(json);
        return testCase;
    }
    
    private AGInput parseAGInput(String json) {
        AGInput input = new AGInput();
        
        // Extract processes with quantum field
        Pattern processPattern = Pattern.compile("\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"arrival\"\\s*:\\s*(\\d+)\\s*,\\s*\"burst\"\\s*:\\s*(\\d+)\\s*,\\s*\"priority\"\\s*:\\s*(\\d+)\\s*,\\s*\"quantum\"\\s*:\\s*(\\d+)\\s*\\}");
        Matcher processMatcher = processPattern.matcher(json);
        input.processes = new ArrayList<>();
        while (processMatcher.find()) {
            AGProcessInput p = new AGProcessInput();
            p.name = processMatcher.group(1);
            p.arrival = Integer.parseInt(processMatcher.group(2));
            p.burst = Integer.parseInt(processMatcher.group(3));
            p.priority = Integer.parseInt(processMatcher.group(4));
            p.quantum = Integer.parseInt(processMatcher.group(5));
            input.processes.add(p);
        }
        
        return input;
    }
    
    private AGExpectedOutput parseAGExpectedOutput(String json) {
        AGExpectedOutput output = new AGExpectedOutput();
        
        // Extract execution order from expectedOutput block
        Pattern orderPattern = Pattern.compile("\"executionOrder\"\\s*:\\s*\\[([^\\]]+)\\]");
        Matcher orderMatcher = orderPattern.matcher(json);
        if (orderMatcher.find()) {
            String orderStr = orderMatcher.group(1);
            output.executionOrder = new ArrayList<>();
            Pattern namePattern = Pattern.compile("\"([^\"]+)\"");
            Matcher nameMatcher = namePattern.matcher(orderStr);
            while (nameMatcher.find()) {
                output.executionOrder.add(nameMatcher.group(1));
            }
        }
        
        // Extract process results with quantumHistory
        output.processResults = new ArrayList<>();
        // Pattern to match process results with quantumHistory array
        Pattern processResultPattern = Pattern.compile(
            "\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"waitingTime\"\\s*:\\s*(\\d+)\\s*,\\s*\"turnaroundTime\"\\s*:\\s*(\\d+)\\s*,\\s*\"quantumHistory\"\\s*:\\s*\\[([^\\]]+)\\]\\s*\\}"
        );
        Matcher processResultMatcher = processResultPattern.matcher(json);
        while (processResultMatcher.find()) {
            AGProcessResult pr = new AGProcessResult();
            pr.name = processResultMatcher.group(1);
            pr.waitingTime = Integer.parseInt(processResultMatcher.group(2));
            pr.turnaroundTime = Integer.parseInt(processResultMatcher.group(3));
            
            // Parse quantumHistory array
            String qhStr = processResultMatcher.group(4);
            pr.quantumHistory = new ArrayList<>();
            Pattern numPattern = Pattern.compile("(\\d+)");
            Matcher numMatcher = numPattern.matcher(qhStr);
            while (numMatcher.find()) {
                pr.quantumHistory.add(Integer.parseInt(numMatcher.group(1)));
            }
            output.processResults.add(pr);
        }
        
        // Extract averages
        Pattern avgWTPattern = Pattern.compile("\"averageWaitingTime\"\\s*:\\s*([\\d.]+)");
        Matcher avgWTMatcher = avgWTPattern.matcher(json);
        if (avgWTMatcher.find()) {
            output.averageWaitingTime = Double.parseDouble(avgWTMatcher.group(1));
        }
        
        Pattern avgTATPattern = Pattern.compile("\"averageTurnaroundTime\"\\s*:\\s*([\\d.]+)");
        Matcher avgTATMatcher = avgTATPattern.matcher(json);
        if (avgTATMatcher.find()) {
            output.averageTurnaroundTime = Double.parseDouble(avgTATMatcher.group(1));
        }
        
        return output;
    }
    
    // Data classes
    static class TestResult {
        boolean sjfPassed = false;
        boolean rrPassed = false;
        boolean priorityPassed = false;
        boolean allPassed = false;
        String details = "";
    }
    
    static class TestCase {
        String name;
        Input input;
        ExpectedOutput expectedOutput;
    }
    
    static class Input {
        int contextSwitch;
        int rrQuantum;
        int agingInterval;
        List<ProcessInput> processes;
    }
    
    static class ProcessInput {
        String name;
        int arrival;
        int burst;
        int priority;
    }
    
    static class ExpectedOutput {
        ExpectedSchedulerOutput SJF;
        ExpectedSchedulerOutput RR;
        ExpectedSchedulerOutput Priority;
    }
    
    static class ExpectedSchedulerOutput {
        List<String> executionOrder;
        List<ProcessResult> processResults;
        double averageWaitingTime;
        double averageTurnaroundTime;
    }
    
    static class ProcessResult {
        String name;
        int waitingTime;
        int turnaroundTime;
    }
    
    // AG Test Data Classes
    static class AGTestResult {
        boolean agPassed = false;
        String details = "";
    }
    
    static class AGTestCase {
        AGInput input;
        AGExpectedOutput expectedOutput;
    }
    
    static class AGInput {
        List<AGProcessInput> processes;
    }
    
    static class AGProcessInput {
        String name;
        int arrival;
        int burst;
        int priority;
        int quantum;
    }
    
    static class AGExpectedOutput {
        List<String> executionOrder;
        List<AGProcessResult> processResults;
        double averageWaitingTime;
        double averageTurnaroundTime;
    }
    
    static class AGProcessResult {
        String name;
        int waitingTime;
        int turnaroundTime;
        List<Integer> quantumHistory;
    }
}

