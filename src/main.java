import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args){

        List<Process> processes = new ArrayList<>();
//        {"name": "P1", "arrival": 0, "burst": 17, "priority": 4, "quantum": 7},
//        {"name": "P2", "arrival": 2, "burst": 6, "priority": 7, "quantum": 9},
//        {"name": "P3", "arrival": 5, "burst": 11, "priority": 3, "quantum": 4},
//        {"name": "P4", "arrival": 15, "burst": 4, "priority": 6, "quantum": 6}
        // List<Process> processes = new ArrayList<>();

        // // name, arrival, burst, priority, quantum
        // processes.add(new Process("p1", 0, 8, 3, 0));
        // processes.add(new Process("p2", 1, 4, 1, 0));
        // processes.add(new Process("p3", 2, 2, 4, 0));
        // processes.add(new Process("p4", 3, 1, 2, 0));
        // processes.add(new Process("p5", 4, 3, 5, 0));

        // int contextSwitch = 1;
        // int agingInterval = 5;

        // // run sjf scheduler
        // System.out.println("\n--- sjf test ---");
        // SJFScheduler sjf = new SJFScheduler(processes, contextSwitch);
        // sjf.execute();
        // sjf.printResults();

        // // run priority scheduler with aging
        // System.out.println("\n--- priority test ---");
        // PriorityScheduler priority =
        //         new PriorityScheduler(processes, contextSwitch, agingInterval);
        // priority.execute();
        // priority.printResults();

        // // rr and ag schedulers can be added later

//         name, arrival, burst, priority, quantum
        processes.add(new Process("P1", 0, 17, 4, 7));
        processes.add(new Process("P2", 2, 6, 7, 9));
        processes.add(new Process("P3", 5, 11, 3, 4));
        processes.add(new Process("P4", 15, 4, 6, 6));
//        processes.add(new Process("P5", 4, 3, 5, 0));
        processes.add(new Process("P1", 0, 5, 1, 2));
        processes.add(new Process("P2", 1, 4, 1, 2));
        processes.add(new Process("P3", 2, 2, 1, 2));

        int timeQuantum = 2;
        int contextSwitchTime = 1;


        RoundRobin rr = new RoundRobin();
        rr.simulateRR(processes, timeQuantum, contextSwitchTime);
//        // run priority scheduler with aging
//        System.out.println("\n--- priority test ---");
//        PriorityScheduler priority =
//                new PriorityScheduler(processes, contextSwitch, agingInterval);
//        priority.execute();
//        priority.printResults();

        // run AG scheduler
        System.out.println("\n--- AG test ---");
        AG_Scheduling ag = new AG_Scheduling(processes);
        ag.simulate();
        ag.printResults();

//        AG_Scheduling scheduler = new AG_Scheduling(processes);
//        scheduler.simulate();
//
//        // =====================
//        // Fetch results
//        // =====================
//        ScheduleResult result = scheduler.getResult();
//
//        // =====================
//        // Print results
//        // =====================
//        System.out.println("Execution Order:");
//        for (int i = 0; i < result.executionOrder.size(); i++) {
//            System.out.print(result.executionOrder.get(i));
//            if (i != result.executionOrder.size() - 1) {
//                System.out.print(" -> ");
//            }
//        }
//
//        System.out.println("\n\nProcess Details:");
//        for (int i = 0; i < processes.size(); i++) {
//            Process p = processes.get(i);
//            System.out.println("Process " + p.getName());
//            System.out.println("Waiting Time: " +
//                    result.waitingTimes.get(p.getName()));
//            System.out.println("Turnaround Time: " +
//                    result.turnaroundTimes.get(p.getName()));
//            System.out.println("----------------------");
//        }
//
//        System.out.println("Average Waiting Time = " +
//                result.averageWaitingTime);
//        System.out.println("Average Turnaround Time = " +
//                result.averageTurnaroundTime);
    }
}
