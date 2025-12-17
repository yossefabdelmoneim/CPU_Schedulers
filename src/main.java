import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args){
        // create process list
        List<Process> processes = new ArrayList<>();

        // name, arrival, burst, priority, quantum
        processes.add(new Process("p1", 0, 10, 3, 0));
        processes.add(new Process("p2", 1, 29, 1, 0));
        processes.add(new Process("p3", 2, 3, 4, 0));
        processes.add(new Process("p4", 3, 7, 2, 0));
        processes.add(new Process("p5", 4, 12, 5, 0));

        int contextSwitch = 1;
        int agingInterval = 5;

        // run priority scheduler
//         System.out.println("\n--- priority test ---");
//         PriorityScheduler priority =
//         new PriorityScheduler(processes, contextSwitch, agingInterval);
//         priority.execute();
//         priority.printResults();


//         System.out.println("\n--- AG test ---");
//         AG_Scheduling scheduling = new AG_Scheduling(processes);
//         // Sample processes
//         processes.add(new Process("p1", 0, 17, 2, 8));
//         processes.add(new Process("p2", 2, 6, 1, 6));
//         processes.add(new Process("p3", 4, 10, 3, 7));
//
//
//         scheduling.simulate();

        System.out.println("\n--- FCFS test ---");
        FCFSScheduler fcfs = new FCFSScheduler(processes);
        fcfs.execute();
        fcfs.printResults();
    }
}
