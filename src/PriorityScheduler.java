import java.util.ArrayList;
import java.util.List;

public class PriorityScheduler {

    private List<Process> processes = new ArrayList<>();
    private int contextSwitching;
    private int agingInterval;
    private List<String> executionOrder;
    private int currentTime;

    public PriorityScheduler(List<Process> processes, int contextSwitching, int agingInterval) {

        for (Process p : processes) {
            this.processes.add(new Process(p));
        }

        this.contextSwitching = contextSwitching;
        this.agingInterval = agingInterval;
        this.executionOrder = new ArrayList<>();
        this.currentTime = 0;
    }

public void execute() {
    List<Process> readyQueue = new ArrayList<>();
    List<Process> completed = new ArrayList<>();
    Process currentProcess = null;

    while (completed.size() < processes.size()) {

        //add arriving processes at current time
        addArrivingProcesses(readyQueue, completed, currentTime);

        //select highest priority process
        if (!readyQueue.isEmpty()) {

            Process selected = null;
            int bestEffectivePriority = Integer.MAX_VALUE;

            for (Process p : readyQueue) {
                int effectivePriority;

                //Only apply aging if wait time is GREATER THAN aging interval
                if (p.getReadyQueueTime() > agingInterval) {
                    int agingFactor = p.getReadyQueueTime() / agingInterval;
                    effectivePriority = p.getPriority() - agingFactor;
                } else {
                    //No aging yet
                    effectivePriority = p.getPriority();
                }

                if (selected == null || effectivePriority < bestEffectivePriority || (effectivePriority == bestEffectivePriority && p.getArrivalTime() < selected.getArrivalTime())) {
                    selected = p;
                    bestEffectivePriority = effectivePriority;
                }
            }

            //regular aging
//            for (Process p : readyQueue) {
//                int agingFactor = p.getTotalReadyQueueTime() / agingInterval;
//                int effectivePriority = p.getPriority() - agingFactor;
//
//                if (selected == null || effectivePriority < bestEffectivePriority || (effectivePriority == bestEffectivePriority && p.getArrivalTime() < selected.getArrivalTime())) {
//                    selected = p;
//                    bestEffectivePriority = effectivePriority;
//                }
//            }

            //context switching
            if (currentProcess != null && currentProcess != selected) {
                contextSwitch(readyQueue, completed, currentProcess);
            }

            //execute for 1 time unit
            currentProcess = selected;
            executionOrder.add(selected.getName());

            for (Process p : readyQueue) {
                if (p != selected) {
                    p.incrementTotalReadyQueueTime(1);
                }
            }

            selected.setRemainingBurstTime(selected.getRemainingBurstTime() - 1);
            selected.resetTotalReadyQueueTime();
            currentTime++;

            //completion
            if (selected.getRemainingBurstTime() == 0) {
                selected.setCompletionTime(currentTime);
                selected.setTurnaroundTime(currentTime - selected.getArrivalTime());
                selected.setWaitingTime(selected.getTurnaroundTime() - selected.getBurstTime());

                contextSwitch(readyQueue, completed, currentProcess);

                completed.add(selected);
                readyQueue.remove(selected);
                currentProcess = null;
            }

        } else {
            currentTime++;
        }
    }
}


    //helper method to add arriving processes
    private void addArrivingProcesses(List<Process> readyQueue, List<Process> completed, int time) {
        for (Process p : processes) {
            if (p.getArrivalTime() <= time && !readyQueue.contains(p) && !completed.contains(p)) {
                readyQueue.add(p);
            }
        }
    }

    private void contextSwitch(List<Process> readyQueue, List<Process> completed, Process currentProcess) {
        for (Process p : readyQueue) {
            if (p != currentProcess) {
            p.incrementTotalReadyQueueTime(contextSwitching);
            }
        }
        currentTime += contextSwitching;

        //check for arrivals during/after context switch
        addArrivingProcesses(readyQueue, completed, currentTime);
    }

    public void printResults() {

        System.out.println("\n=== Preemptive Priority Scheduling ===");
        System.out.print("Execution Order: ");

        String last = "";
        for (String p : executionOrder) {
            if (!p.equals(last)) {
                System.out.print(p + " ");
                last = p;
            }
        }

        System.out.println("\n\nProcess   Waiting Time   Turnaround Time");

        double totalWT = 0, totalTAT = 0;
        for (Process p : processes) {
            System.out.printf("%-10s %-14d %-15d%n",
                    p.getName(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime());
            totalWT += p.getWaitingTime();
            totalTAT += p.getTurnaroundTime();
        }

        System.out.printf("%nAverage Waiting Time = %.2f%n",
                totalWT / processes.size());
        System.out.printf("Average Turnaround Time = %.2f%n",
                totalTAT / processes.size());
    }
}