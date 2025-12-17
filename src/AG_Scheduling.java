import java.util.*;

class AG_Scheduling{

    List<Process> processes = new ArrayList<>();
    Queue<Process> readyQueue = new LinkedList<>();
    private int currentTime = 0;


    public AG_Scheduling(List<Process> processes) {

        this.processes = new ArrayList<>();
        for (Process p : processes) {
            this.processes.add(new Process(p)); 
        }

        this.readyQueue = new LinkedList<>();
        this.currentTime = 0;
    }

    public void simulate() {
        int completed = 0;
        int n = processes.size();

        while (completed < n) {
            
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingBurstTime() > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = readyQueue.poll(); //poll() : Retrieves and removes the head of this queue, or returns null if this queue is empty.
            int originalQuantum = current.getQuantum();

            int q25 = (int) Math.ceil(originalQuantum * 0.25);
            int executed = 0;

            // 1. FCFS (first 25%)
            int time = Math.min(q25, current.getRemainingBurstTime());
            execute(current, time);//m
            executed += time;

            if (current.getRemainingBurstTime() == 0) {
                current.setQuantum(0); // Case iv
                completed++;
                continue;
            }

            // 2. Non-preemptive Priority (second 25%)
            q25 = (int) Math.ceil(originalQuantum * 0.25);
            time = Math.min(q25, current.getRemainingBurstTime());
            execute(current, time);
            executed += time;

            Process higherPriority = checkHigherPriority(current.getPriority());
            if (higherPriority != null) {
                int remainingQ = originalQuantum - executed;
                int newQuantum = current.getQuantum() + (int) Math.ceil(remainingQ / 2.0);
                current.setQuantum(newQuantum);
                readyQueue.add(current);
                continue;
            }

            if (current.getRemainingBurstTime() == 0) {
                current.setQuantum(0);; // Case iv
                completed++;
                continue;
            }

            // 3. Preemptive SJF (remaining quantum)
            int remainingQ = originalQuantum - executed;
            while (remainingQ > 0 && current.getRemainingBurstTime() > 0) {
                Process shorterJob = checkShorterJob(current.getRemainingBurstTime());
                if (shorterJob != null) {
                    int newQuantum = current.getQuantum() + remainingQ;
                    current.setQuantum(newQuantum);
                    readyQueue.add(current);
                    break;
                }
                
                execute(current, 1);
                remainingQ--;
            }

            if (current.getRemainingBurstTime() == 0) {
                current.setQuantum(0);; // Case iv
                completed++;
            } 
            else if (remainingQ == 0) {
                int newQuantum = current.getQuantum() + 2;
                current.setQuantum(newQuantum);
                readyQueue.add(current);
            }
        }
    }

    public void execute(Process p, int time) {
        int newRemainingTime = p.getRemainingBurstTime() - time;
        p.setRemainingBurstTime(newRemainingTime);
        currentTime += time;
        System.out.println("Time " + currentTime + ": P" + p.getName() + " executed for " + time + " units");
    }

    public Process checkHigherPriority(int priority) {
        for (Process p : readyQueue) {
            if (p.getPriority() < priority) return p;
        }
        return null;
    }

    public Process checkShorterJob(int remaining) {
        for (Process p : readyQueue) {
            if (p.getRemainingBurstTime() < remaining) return p;
        }
        return null;
    }
}