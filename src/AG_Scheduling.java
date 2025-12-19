import java.util.*;

class AG_Scheduling{

    private List<Process> processes;
    private List<Process> readyQueue;
    private List<String> executionOrder; // To track execution order of processes
    private int currentTime;
    private Process currentlyExecuting; // Currently executing process
    private Process current;
    int completed = 0;


    // Constructor
    public AG_Scheduling(List<Process> processes) {
        this.processes = new ArrayList<Process>();
        for (int i = 0; i < processes.size(); i++) {
            this.processes.add(new Process(processes.get(i)));
        }

        readyQueue = new LinkedList<Process>();
        executionOrder = new ArrayList<String>();
        currentTime = 0;
        currentlyExecuting = null;
    }

    // Main simulation method
    public void simulate() {
        // Total number of completed processes

        int n = processes.size();

        // Add initially arrived processes
        checkArrivals();
        //
        current = readyQueue.getFirst();

        for (Process p : readyQueue) {
            if (p.getArrivalTime() < current.getArrivalTime()) {
                current = p;
            }
        }


        // Main loop
        while (completed < n) {
            // If queue is empty, advance time and check for arrivals
            if (readyQueue.isEmpty()) {
                currentTime++;
                checkArrivals();
                continue;
            }




            int currentQuantum = current.getQuantum();
            // execute for up to currentQuantum
            int executed = 0;

            // Calculate 25% of current quantum
            int q25 = (int) Math.ceil(currentQuantum * 0.25);

            // Phase 1: FCFS : first come,first served - Execute 25% of quantum
            int time = Math.min(q25, current.getRemainingBurstTime());
            execute(current, time);
            executed += (time);
            checkArrivals(); // Check for new arrivals after execution

            if (current.getRemainingBurstTime() == 0) {
                finish(current);
                completed++;
                currentlyExecuting = null;
                continue;
            }

            // check whether the current process has higher priority than any newly arrived process
            Process selected = current;
            for (Process p : readyQueue) {
                if(p.getPriority() < selected.getPriority()) {
                    selected = p;
                }
            }


            if (selected != current) {
                int remainingQ = currentQuantum - executed;
                int quantumIncrease = (int) Math.ceil(remainingQ / 2.0);
                current.setQuantum(current.getQuantum() + quantumIncrease);
                readyQueue.remove(current);
                readyQueue.add(current); // move current to end of queue
                current = selected;
                // preempt current process
                currentlyExecuting = null;
                continue;
            }

            // Phase 2: Priority - Execute another 25% of quantum
            time = Math.min(q25, current.getRemainingBurstTime());
            execute(current, time);
            executed += (time);
            checkArrivals(); // Check for new arrivals after execution


            if (current.getRemainingBurstTime() == 0) {
                finish(current);
                completed++;
                currentlyExecuting = null;
                continue;
            }


            for(Process p : readyQueue) {
                if(p.getRemainingBurstTime() < current.getRemainingBurstTime()) {
                    selected = p;
                }
                else if (p.getRemainingBurstTime() == selected.getRemainingBurstTime() && p.getPriority() < selected.getPriority()) {
                    selected = p;
                }
            }

            // check whether a shorter job has arrived
            if (current != selected) {
                int remainingQ = currentQuantum - executed;
                current.setQuantum(current.getQuantum() + remainingQ);
                readyQueue.remove(current);
                readyQueue.add(current); // move current to end of queue
                current = selected;
                currentlyExecuting = null;
                continue;
            }


            // Phase 3: SJF - Execute remaining quantum
            int remainingQ = currentQuantum - (executed);
            boolean preempted = false;

            while (remainingQ > 0 && current.getRemainingBurstTime() > 0) {
                execute(current, 1);
                remainingQ--;
                checkArrivals(); // Check for new arrivals after each time unit

                // Check if a shorter job has arrived
                for(Process p : readyQueue) {
                    if(p.getRemainingBurstTime() < current.getRemainingBurstTime()) {
                        selected = p;
                    }
//                    else if (p.getRemainingBurstTime() == selected.getRemainingBurstTime() && p.getPriority() < selected.getPriority()) {
//                        selected = p;
//                    }
                }

                // check whether a shorter job has arrived
                if (current != selected) {
                    current.setQuantum(current.getQuantum() + remainingQ);
                    readyQueue.remove(current);
                    readyQueue.add(current); // move current to end of queue
                    preempted = true;
                    current = selected;
                    break;
                }
            }



            // Handle process completion or quantum exhaustion
            if(!preempted) {
                if (current.getRemainingBurstTime() < 0) {
                    finish(current);
                    completed++;
                    continue;
                }
                else if (remainingQ <= 0) {
                    // Used full quantum, still has work to do
                    current.setQuantum(current.getQuantum() + 2);
                    readyQueue.remove(current);
                    readyQueue.add(current); // move current to end of queue
                    // Select next process to execute
                    current = readyQueue.get(0);
                    for (Process p : readyQueue) {
                      if (p.getArrivalTime() < current.getArrivalTime()) {
                            current = p;
                        }
                    }
                    continue;

//                    for (Process p : readyQueue) {
//                        if (p.getArrivalTime() < current.getArrivalTime()) {
//                            current = p;
//                        }
//                        // In case of tie, select process with higher priority
//                        else if (p.getArrivalTime() == current.getArrivalTime() && p.getPriority() < current.getPriority()) {
//                            current = p;
//                        }
//
//                        // In case of tie, select process with shorter remaining burst time
//                        else if (p.getArrivalTime() == current.getArrivalTime() && p.getPriority() == current.getPriority() && p.getRemainingBurstTime() < current.getRemainingBurstTime()) {
//                            current = p;
//                        }
//
//                    }
                }
                currentlyExecuting = null;
            }
        }
    }

    private void checkArrivals() {
        for (Process p : processes) {
            // Add process to queue if:
            // 1. It has arrived
            // 2. It still has work to do
            // 3. It's not already in the queue
            // 4. It's not currently executing
            if (p.getArrivalTime() <= currentTime &&
                    p.getRemainingBurstTime() > 0 &&
                    !readyQueue.contains(p) &&
                    p != currentlyExecuting) {
                readyQueue.add(p);
            }
        }
    }

    private void execute(Process p, int time) {
        // Add to execution order once per continuous execution
        if (executionOrder.isEmpty() ||
                !executionOrder.getLast().equals(p.getName())) {
            executionOrder.add(p.getName());
        }

        // Execute for the given time units
        for (int i = 0; i < time; i++) {
            p.setRemainingBurstTime(p.getRemainingBurstTime() - 1);
            currentTime++;
        }
    }

    private void finish(Process p) {
        p.setCompletionTime(currentTime);
        p.setTurnaroundTime(currentTime - p.getArrivalTime());
        p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
        p.setQuantum(0); // Set quantum to 0 when process completes
        readyQueue.remove(p);

        if(processes.size() > completed) {
            for (Process proc : readyQueue) {
                if (proc.getArrivalTime() < current.getArrivalTime()) {
                    current = proc;
                }
                if (proc.getRemainingBurstTime() > 0 && current.getRemainingBurstTime()<= 0) {
                    current = proc;
                }
            }
        }
        else{
            current = null;
            currentlyExecuting = null;
        }
    }

    public boolean checkArrival(List<Process> P){
        for (Process proc : P) {
            if (proc.getArrivalTime() < current.getArrivalTime() || proc.getArrivalTime() > current.getArrivalTime()) {
                return false;
            }
        }
        return true;
    }

    public void printResults() {
        System.out.println("\n=== AG Scheduling Results ===");
        System.out.print("Execution Order: ");

        for (String p : executionOrder) {
            System.out.print(p + " ");
        }

        System.out.println("\n\nProcess   Waiting Time   Turnaround Time   Quantum History");
        System.out.println("-------   ------------   ---------------   ---------------");

        double totalWT = 0, totalTAT = 0;
        for (Process p : processes) {
            System.out.printf("%-10s %-14d %-18d %s%n",
                    p.getName(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime(),
                    p.getQuantumHistory().toString());
            totalWT += p.getWaitingTime();
            totalTAT += p.getTurnaroundTime();
        }

        System.out.printf("%nAverage Waiting Time = %.2f%n",
                totalWT / processes.size());
        System.out.printf("Average Turnaround Time = %.2f%n",
                totalTAT / processes.size());
    }

    // Getter for execution order (useful for testing)
    public List<String> getExecutionOrder() {
        return executionOrder;
    }

    // Getter for processes (useful for testing)
    public List<Process> getProcesses() {
        return processes;
    }
}