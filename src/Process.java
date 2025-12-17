import java.util.ArrayList;
import java.util.List;

public class Process {

    private String name;
    private int arrivalTime;
    private int burstTime;
    private int remainingBurstTime;
    private int priority;
    private int quantum;

    private int waitingTime;
    private int turnaroundTime;
    private int completionTime;

    private List<Integer> quantumHistory;

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;

        quantumHistory = new ArrayList<Integer>();
        quantumHistory.add(quantum);
    }

    // Copy constructor
    public Process(Process other) {
        this.name = other.name;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.remainingBurstTime = other.remainingBurstTime;
        this.priority = other.priority;
        this.quantum = other.quantum;

        quantumHistory = new ArrayList<Integer>();
        for (int i = 0; i < other.quantumHistory.size(); i++) {
            quantumHistory.add(other.quantumHistory.get(i));
        }
    }

    // Getters
    public String getName() { return name; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingBurstTime() { return remainingBurstTime; }
    public int getPriority() { return priority; }
    public int getQuantum() { return quantum; }
    public int getWaitingTime() { return waitingTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public int getCompletionTime() { return completionTime; }
    public int getTotalReadyQueueTime() {
        int total = 0;
        for (int q : quantumHistory) {
            total += q;
        }
        return total;
    }
    public List<Integer> getQuantumHistory() { return quantumHistory; }

    // Setters
    public void setRemainingBurstTime(int t) {
        remainingBurstTime = t;
    }

    public void setTurnaroundTime(int t) {
        turnaroundTime = completionTime - arrivalTime;
    }

    public void setWaitingTime(int t) {
        waitingTime = turnaroundTime - burstTime;
    }

    public void setQuantum(int q) {
        quantum = q;
        quantumHistory.add(q);
    }

    public void setCompletionTime(int t) {
        completionTime = t;
    }

    public void setQuantumHistory(List<Integer> history) {
        quantumHistory = history;
    }

    //calculation
    public void incrementReadyQueueTime(int t) {
        if (quantumHistory.isEmpty()) return;
        int lastIndex = quantumHistory.size() - 1;
        quantumHistory.set(lastIndex, quantumHistory.get(lastIndex) + t);
    }
}
