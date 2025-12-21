import java.util.ArrayList;
import java.util.List;

public class PriorityScheduler {

    private List<Process> processes = new ArrayList<>();
    private int contextSwitching;
    private int agingInterval;
    private List<String> executionOrder;
    private int currentTime;
    Process nextSelected = null;


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

            Process selected = getHighestPriority(readyQueue);
            //this variable will be set if the before process finished
            if(nextSelected != null) {
                //if the predetermined next process to run is no longer highest prio after switch
                while (nextSelected != selected) {
                    executionOrder.add(nextSelected.getName());

                    //context switch again, and pick the next process
                    contextSwitch(readyQueue, completed, currentProcess);
                    //nextselected = selected for the next iteration, keep going until the proc
                    //before context switch is still the best proc after context switch
                    nextSelected = selected;
                    selected = getHighestPriority(readyQueue);

                }
            }
            nextSelected = null;


            //context switching
            if (currentProcess != null && currentProcess != selected) {

                //if this process just arrived, special context switch
                if (selected.getArrivalTime() == currentTime){
                    for (Process p : readyQueue) {
                        //do not count the context switch time for the just arrived process
                        if(p!=selected) {
                            p.incrementTotalReadyQueueTime(contextSwitching);
                        }
                    }
                    currentTime += contextSwitching;
                    //check for arrivals during/after context switch
                    addArrivingProcesses(readyQueue, completed, currentTime);
                }
                else {
                    //regular context switch
                    contextSwitch(readyQueue, completed, currentProcess);
                }
                //RECHECK again after context switching and incrementing current time by context switch time
                Process oldSelected = selected;
                selected = getHighestPriority(readyQueue);
                if (selected != oldSelected) {
                    executionOrder.add(oldSelected.getName());

                    for (Process p : readyQueue) {
                        if(p!=oldSelected) {
                        p.incrementTotalReadyQueueTime(contextSwitching);
                        }
                    }
                    currentTime += contextSwitching;
                    //check for arrivals during/after context switch
                    addArrivingProcesses(readyQueue, completed, currentTime);
                }

            }


            //execute for 1 time unit
            currentProcess = selected;
            executionOrder.add(selected.getName());

            //increment ready queue waiting time for all except executing process
            for (Process p : readyQueue) {
                if (p != selected) {
                    p.incrementTotalReadyQueueTime(1);
                }
            }

            selected.setRemainingBurstTime(selected.getRemainingBurstTime() - 1);
            currentTime++;

            //completion
            if (selected.getRemainingBurstTime() == 0) {
                selected.setCompletionTime(currentTime);
                selected.setTurnaroundTime(currentTime - selected.getArrivalTime());
                selected.setWaitingTime(selected.getTurnaroundTime() - selected.getBurstTime());

                completed.add(selected);
                readyQueue.remove(selected);
                addArrivingProcesses(readyQueue, completed, currentTime);
                //before context switching check the next process to run
                nextSelected = getHighestPriority(readyQueue);
                contextSwitch(readyQueue, completed, currentProcess);

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

            p.incrementTotalReadyQueueTime(contextSwitching);

        }
        currentTime += contextSwitching;
        //check for arrivals during/after context switch
        addArrivingProcesses(readyQueue, completed, currentTime);
    }


    public Process getHighestPriority(List<Process> readyQueue) {
        Process selected = null;
        int bestEffectivePriority = Integer.MAX_VALUE;
        for (Process p : readyQueue) {

            int agingFactor = p.getReadyQueueTime() / agingInterval;
            int effectivePriority = p.getPriority() - agingFactor;
            if (effectivePriority < 1){
                effectivePriority = 1;
            }


            if (selected == null) {
                selected = p;
                bestEffectivePriority = effectivePriority;
            } else if (effectivePriority < bestEffectivePriority) {
                selected = p;
                bestEffectivePriority = effectivePriority;
            } else if (effectivePriority == bestEffectivePriority) {
                // Tie in effective priority
                if (p.getArrivalTime() < selected.getArrivalTime()) {
                    selected = p;
                } else if (p.getArrivalTime() == selected.getArrivalTime()) {
                    //tie in arrival time, compare names
                    if (p.getName().compareTo(selected.getName()) < 0) {
                        selected = p;
                    }
                }
            }
        }
        return selected;
    }

    public List<String> getExecutionOrder() {
        return executionOrder;
    }
    
    public List<Process> getProcesses() {
        return processes;
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
