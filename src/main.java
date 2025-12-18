import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
//        {"name": "P1", "arrival": 0, "burst": 6, "priority": 3},
//        {"name": "P2", "arrival": 0, "burst": 3, "priority": 1},
//        {"name": "P3", "arrival": 0, "burst": 8, "priority": 2},
//        {"name": "P4", "arrival": 0, "burst": 4, "priority": 4},
//        {"name": "P5", "arrival": 0, "burst": 2, "priority": 5}
        // name, arrival, burst, priority, quantum
        processes.add(new Process("p1", 0, 6, 3, 0));
        processes.add(new Process("p2", 0, 3, 1, 0));
        processes.add(new Process("p3", 0, 8, 2, 0));
        processes.add(new Process("p4", 0, 4, 4, 0));
        processes.add(new Process("p5", 0, 2, 5, 0));


        int contextSwitch = 1;
        int agingInterval = 5;

        // run sjf scheduler
        System.out.println("\n--- sjf test ---");
        SJFScheduler sjf = new SJFScheduler(processes, contextSwitch);
        sjf.execute();
        sjf.printResults();

         // run priority scheduler with aging
         System.out.println("\n--- priority test ---");
         PriorityScheduler priority =
                 new PriorityScheduler(processes, contextSwitch, agingInterval);
         priority.execute();
         priority.printResults();

        // // rr and ag schedulers can be added later

//         name, arrival, burst, priority, quantum
//        processes.add(new Process("P1", 0, 20, 5, 8));
//        processes.add(new Process("P2", 3, 4, 3, 6));
//        processes.add(new Process("P3", 6, 3, 4, 5));
//        processes.add(new Process("P4", 10, 2, 2, 4));
//        processes.add(new Process("P5", 15, 5, 6, 7));
//        processes.add(new Process("P6", 20, 6, 1, 3));


//        int timeQuantum = 2;
//        int contextSwitchTime = 1;


//        RoundRobin rr = new RoundRobin();
//        rr.simulateRR(processes, timeQuantum, contextSwitchTime);
        // run priority scheduler with aging


//        // run AG scheduler
//        System.out.println("\n--- AG test ---");
//        AG_Scheduling ag = new AG_Scheduling(processes);
//        ag.simulate();
//        ag.printResults();

//
    }
}
