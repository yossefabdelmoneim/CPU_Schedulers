import java.util.*;

public class RoundRobin {

    public static void simulateRR(List<Process> processes,
                                  int timeQuantum,
                                  int contextSwitchTime) {

        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> finished = new ArrayList<>();
        List<String> executionOrder = new ArrayList<>();

        int currentTime = 0;
        int index = 0;
        Process previous = null;

        // sort processes by arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        while (finished.size() < processes.size()) {

            // add newly arrived processes before execution
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index));
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
            System.out.println("Time " + currentTime + " -> " + (currentTime + execTime) + " : " + current.getName());

            currentTime += execTime;
            current.setRemainingBurstTime(current.getRemainingBurstTime() - execTime);
            current.setLastExecutedTime(currentTime);

            // add newly arrived processes during this execution
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(processes.get(index));
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

            previous = current;
        }

        printResults(finished, executionOrder);
    }

    private static void printResults(List<Process> processes, List<String> executionOrder) {

        System.out.println("Execution Order: " + executionOrder);
        System.out.println("Process | Waiting Time | Turnaround Time");

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
                    System.out.printf(
                            "%7s |      %2d      |       %2d\n",
                            p.getName(),
                            p.getWaitingTime(),
                            p.getTurnaroundTime()
                    );
                    printed.add(name);  // mark as printed
                }
            }
        }
    }
}
