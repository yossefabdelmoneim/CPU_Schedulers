//process class
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
    private int lastExecutedTime;

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
        this.lastExecutedTime = arrivalTime;
    }

    public Process(Process other) {
        this.name = other.name;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.remainingBurstTime = other.remainingBurstTime;
        this.priority = other.priority;
        this.quantum = other.quantum;
        this.waitingTime = other.waitingTime;
        this.turnaroundTime = other.turnaroundTime;
        this.completionTime = other.completionTime;
        this.lastExecutedTime = other.lastExecutedTime;
    }

    public String getName() {
        return this.name;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public int getBurstTime() {
        return this.burstTime;
    }

    public int getRemainingBurstTime() {
        return this.remainingBurstTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getQuantum() {
        return this.quantum;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }

    public int getTurnaroundTime() {
        return this.turnaroundTime;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public int getLastExecutedTime() {
        return this.lastExecutedTime;
    }

    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public void setLastExecutedTime(int lastExecutedTime) {
        this.lastExecutedTime = lastExecutedTime;
    }
}