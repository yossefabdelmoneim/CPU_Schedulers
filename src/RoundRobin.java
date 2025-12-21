import java.util.*;

public class RoundRobin {

    public static class RRResult {
        public List<Process> processes;
        public List<String> executionOrder;

        public RRResult(List<Process> processes, List<String> executionOrder) {
            this.processes = processes;
            this.executionOrder = executionOrder;
        }
    }

    public static RRResult simulateRR(List<Process> processes,
                                  int timeQuantum,
                                  int contextSwitchTime) {

        // Create copies of processes to avoid modifying originals
        List<Process> processCopies = new ArrayList<>();
        for (Process p : processes) {
            processCopies.add(new Process(p));
        }

        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> finished = new ArrayList<>();
        List<String> executionOrder = new ArrayList<>();

        int currentTime = 0;
        int index = 0;

        // sort processes by arrival time
        processCopies.sort(Comparator.comparingInt(Process::getArrivalTime));

        while (finished.size() < processCopies.size()) {

            // add newly arrived processes before execution
            while (index < processCopies.size() && processCopies.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processCopies.get(index));
                index++;
            }

            // CPU idle
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = readyQueue.poll();

            // calculate waiting time
            int waitingThisRun = currentTime - Math.max(current.getArrivalTime(), current.getLastExecutedTime());
            current.incrementReadyQueueTime(waitingThisRun);

            // determine execution time
            int execTime = Math.min(timeQuantum, current.getRemainingBurstTime());

            // record execution order
            executionOrder.add(current.getName());
            // System.out.println("Time " + currentTime + " -> " + (currentTime + execTime) + " : " + current.getName());

            currentTime += execTime;
            current.setRemainingBurstTime(current.getRemainingBurstTime() - execTime);
            current.setLastExecutedTime(currentTime);

            // add newly arrived processes during this execution
            while (index < processCopies.size() && processCopies.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processCopies.get(index));
                index++;
            }

            // unfinished → back to queue
            if (current.getRemainingBurstTime() > 0) {
                readyQueue.add(current);
            }
            // finished → compute times
            else {
                current.setCompletionTime(currentTime);
                current.computeTurnaroundAndWaiting(); // computes TAT and WT internally
                finished.add(current);
            }

            // context switch if queue not empty
            if (!readyQueue.isEmpty()) {
                currentTime += contextSwitchTime;
            }
        }

        printResults(finished, executionOrder);
        return new RRResult(finished, executionOrder);
    }

    private static void printResults(List<Process> processes, List<String> executionOrder) {

        // System.out.println("Execution Order: " + executionOrder);
        // System.out.println("Process | Waiting Time | Turnaround Time");

        // List to track printed processes
        List<String> printed = new ArrayList<>();

        for (String name : executionOrder) {
            if (!printed.contains(name)) {  // check if already printed
                // Find the process with this name
                Process p = null;
                for (Process proc : processes) {
                    if (proc.getName().equals(name)) {
                        p = proc;
                        break;
                    }
                }

                if (p != null) {
                    // System.out.printf(
                    //         "%7s |      %2d      |       %2d\n",
                    //         p.getName(),
                    //         p.getWaitingTime(),
                    //         p.getTurnaroundTime()
                    // );
                    printed.add(name);  // mark as printed
                }
            }
        }
    }
}
