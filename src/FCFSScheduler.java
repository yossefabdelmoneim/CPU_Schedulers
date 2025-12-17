import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCFSScheduler {
    private List<Process> processes;
    private List<String> executionOrder;
    private int currentTime;

    public FCFSScheduler(List<Process> processes) {
        this.processes = new ArrayList<>();
        for (Process p : processes) this.processes.add(new Process(p));
        this.processes.sort(Comparator.comparingInt(Process::getArrivalTime)); // sorts by arrival time
        this.executionOrder = new ArrayList<>();
        this.currentTime = 0;
    }

    public void execute() {
        for (Process p : processes) {
            if (currentTime < p.getArrivalTime()) currentTime = p.getArrivalTime(); // simulates waiting time for next process in queue
            executionOrder.add(p.getName());
            int startTime = currentTime;
            currentTime += p.getBurstTime(); //gets burst time and adds it to current time
            p.setCompletionTime(currentTime); //saves completion time
            p.setTurnaroundTime(p.getCompletionTime() - p.getArrivalTime()); //gets turnaround time
            p.setWaitingTime(startTime - p.getArrivalTime()); //sets waiting time
        }
    }

    public double getAverageWaitingTime() {
        int cumulative = 0;
        int total = 0;
        for (int i = 0; i < processes.size() - 1; i++) {
            cumulative += processes.get(i).getBurstTime();
            total += cumulative;
        }
        return (double) total / processes.size();
    }

    public void printResults() {
        System.out.println("Execution Order: " + executionOrder);
        System.out.println("Total time: " + (double)currentTime);
        System.out.println("Average Waiting Time: " + getAverageWaitingTime());
        System.out.println("Process | Arrival Time | Burst Time | Completion Time | Turnaround Time | Waiting Time");
        for (Process p : processes) {
            System.out.println("   " + p.getName() + "           " + p.getArrivalTime() + "            " + p.getBurstTime() +"              " + p.getCompletionTime() + "                 " + p.getTurnaroundTime() + "             " + p.getWaitingTime());
        }
    }

    public List<String> getExecutionOrder() {
        return executionOrder;
    }

    public List<Process> getProcesses() {
        return processes;
    }
}
