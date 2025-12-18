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
//        processes.add(new Process("p1", 0, 6, 3, 0));
//        processes.add(new Process("p2", 0, 3, 1, 0));
//        processes.add(new Process("p3", 0, 8, 2, 0));
//        processes.add(new Process("p4", 0, 4, 4, 0));
//        processes.add(new Process("p5", 0, 2, 5, 0));
//
//
//        int contextSwitch = 1;
//        int agingInterval = 5;

        // run sjf scheduler
//        System.out.println("\n--- sjf test ---");
//        SJFScheduler sjf = new SJFScheduler(processes, contextSwitch);
//        sjf.execute();
//        sjf.printResults();

         // run priority scheduler with aging
//         System.out.println("\n--- priority test ---");
//         PriorityScheduler priority =
//                 new PriorityScheduler(processes, contextSwitch, agingInterval);
//         priority.execute();
//         priority.printResults();

        // // rr and ag schedulers can be added later

//         name, arrival, burst, priority, quantum
//        processes.add(new Process("P1", 0, 20, 5, 8));
//        processes.add(new Process("P2", 3, 4, 3, 6));
//        processes.add(new Process("P3", 6, 3, 4, 5));
//        processes.add(new Process("P4", 10, 2, 2, 4));
//        processes.add(new Process("P5", 15, 5, 6, 7));
//        processes.add(new Process("P6", 20, 6, 1, 3));

//        {"name": "P1", "arrival": 0, "burst": 8, "priority": 3},
//        {"name": "P2", "arrival": 1, "burst": 4, "priority": 1},
//        {"name": "P3", "arrival": 2, "burst": 2, "priority": 4},
//        {"name": "P4", "arrival": 3, "burst": 1, "priority": 2},
//        {"name": "P5", "arrival": 4, "burst": 3, "priority": 5}

//        {"name": "P1", "waitingTime": 19, "turnaroundTime": 27},
//        {"name": "P2", "waitingTime": 14, "turnaroundTime": 18},
//        {"name": "P3", "waitingTime": 4, "turnaroundTime": 6},
//        {"name": "P4", "waitingTime": 9, "turnaroundTime": 10},
//        {"name": "P5", "waitingTime": 17, "turnaroundTime": 20}
//        processes.add(new Process("P1", 0, 8, 3,0));
//        processes.add(new Process("P2", 1, 4, 1,0));
//        processes.add(new Process("P3", 2, 2, 4,0));
//        processes.add(new Process("P4", 3, 1, 2,0 ));
//        processes.add(new Process("P5", 4, 3, 5,0 ));
//
//
//        int timeQuantum = 2;
//        int contextSwitchTime = 1;
//
//
//        RoundRobin rr = new RoundRobin();
//        rr.simulateRR(processes, timeQuantum, contextSwitchTime);

//        {"name": "P1", "arrival": 0, "burst": 17, "priority": 4, "quantum": 7},
//        {"name": "P2", "arrival": 2, "burst": 6, "priority": 7, "quantum": 9},
//        {"name": "P3", "arrival": 5, "burst": 11, "priority": 3, "quantum": 4},
//        {"name": "P4", "arrival": 15, "burst": 4, "priority": 6, "quantum": 6}

//        {"name": "P1", "arrival": 0, "burst": 20, "priority": 5, "quantum": 8},
//        {"name": "P2", "arrival": 3, "burst": 4, "priority": 3, "quantum": 6},
//        {"name": "P3", "arrival": 6, "burst": 3, "priority": 4, "quantum": 5},
//        {"name": "P4", "arrival": 10, "burst": 2, "priority": 2, "quantum": 4},
//        {"name": "P5", "arrival": 15, "burst": 5, "priority": 6, "quantum": 7},
//        {"name": "P6", "arrival": 20, "burst": 6, "priority": 1, "quantum": 3}

//        {"name": "P1", "arrival": 0, "burst": 25, "priority": 3, "quantum": 5},
//        {"name": "P2", "arrival": 1, "burst": 18, "priority": 2, "quantum": 4},
//        {"name": "P3", "arrival": 3, "burst": 22, "priority": 4, "quantum": 6},
//        {"name": "P4", "arrival": 5, "burst": 15, "priority": 1, "quantum": 3},
//        {"name": "P5", "arrival": 8, "burst": 20, "priority": 5, "quantum": 7},
//        {"name": "P6", "arrival": 12, "burst": 12, "priority": 6, "quantum": 4}

//        {"name": "P1","arrival":0,"burst":14,"priority":4,"quantum":6},
//        {"name": "P2","arrival":4,"burst":9,"priority":2,"quantum":8},
//        {"name": "P3","arrival":7,"burst":16,"priority":5,"quantum":5},
//        {"name": "P4","arrival":10,"burst":7,"priority":1,"quantum":10},
//        {"name": "P5","arrival":15,"burst":11,"priority":3,"quantum":4},
//        {"name": "P6","arrival":20,"burst":5,"priority":6,"quantum":7},
//        {"name": "P7","arrival":25,"burst":8,"priority":7,"quantum":9}
        processes.add(new Process("P1", 0, 14, 4,6));
        processes.add(new Process("P2", 4, 9, 2,8));
        processes.add(new Process("P3", 7, 16, 5,5));
        processes.add(new Process("P4", 10, 7, 1,10));
        processes.add(new Process("P5", 15, 11, 3,4));
        processes.add(new Process("P6", 20, 5, 6,7));
        processes.add(new Process("P7", 25, 8, 7,9));

        // run AG scheduler
        System.out.println("\n--- AG test ---");
        AG_Scheduling ag = new AG_Scheduling(processes);
        ag.simulate();
        ag.printResults();

// AG Test 1, 4 working for now
    }
}
