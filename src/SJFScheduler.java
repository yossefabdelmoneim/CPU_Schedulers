import java.util.ArrayList;
import java.util.List;

public class SJFScheduler {
    private List<Process> processes = new ArrayList();
    private int contextSwitching;
    private List<String> executionOrder;
    private int currentTime;

    public SJFScheduler(List<Process> processes, int contextSwitching) {
        for(Process p : processes) {
            this.processes.add(new Process(p));
        }

        this.contextSwitching = contextSwitching;
        this.executionOrder = new ArrayList();
        this.currentTime = 0;
    }

    public void execute() {
        List<Process> readyQueue = new ArrayList();
        List<Process> completed = new ArrayList();
        Process currentProcess = null;
        String lastProcessName = null;

        while(completed.size() < this.processes.size()) {

            addArrivingProcesses(readyQueue, completed, currentTime);

            if (!readyQueue.isEmpty()) {

                Process shortestJob = readyQueue.get(0);

                for(Process p : readyQueue) {
                    if (p.getRemainingBurstTime() < shortestJob.getRemainingBurstTime()) {
                        shortestJob = p;
                    } else if (p.getRemainingBurstTime() == shortestJob.getRemainingBurstTime()
                            && p.getPriority() < shortestJob.getPriority()) {
                        shortestJob = p;
                    }
                }



                boolean shouldSwitch = false;

                if (currentProcess == null) {

                    shouldSwitch = true;
                } else if (currentProcess != shortestJob) {

                    if (shortestJob.getRemainingBurstTime() < currentProcess.getRemainingBurstTime()) {
                        shouldSwitch = true;
                    }
                }


                if (shouldSwitch && lastProcessName != null &&
                        (currentProcess == null || currentProcess != shortestJob)) {


                    Process targetProcess = shortestJob;

                    currentTime += contextSwitching;
                    addArrivingProcesses(readyQueue, completed, currentTime);


                    shortestJob = targetProcess;
                }


                if (currentProcess == null || shouldSwitch) {
                    currentProcess = shortestJob;
                }


                this.executionOrder.add(currentProcess.getName());
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
                ++this.currentTime;
                lastProcessName = currentProcess.getName();


                if (currentProcess.getRemainingBurstTime() == 0) {
                    currentProcess.setCompletionTime(this.currentTime);
                    currentProcess.setTurnaroundTime(this.currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    completed.add(currentProcess);
                    readyQueue.remove(currentProcess);
                    currentProcess = null;
                }
            } else {

                ++this.currentTime;
            }
        }
    }


    private void addArrivingProcesses(List<Process> readyQueue, List<Process> completed, int time) {
        for (Process p : processes) {
            if (p.getArrivalTime() <= time && !readyQueue.contains(p) && !completed.contains(p)) {
                readyQueue.add(p);
            }
        }
    }

    public void printResults() {
        System.out.println("\n=== SJF Scheduling ===");
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

    public List<Process> getProcesses() {
        return this.processes;
    }

    public List<String> getExecutionOrder() {
        return this.executionOrder;
    }
}