import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        // name, arrival, burst, priority, quantum
//        processes.add(new Process("p1", 0, 6, 3, 0));


        //----------------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------
        //SEIF CODE TESTING
        /* test case 1: basic mixed arrivals */
//        List<Process> tc1 = new ArrayList<>();
//
//        tc1.add(new Process("p1", 0, 8, 3, 0));
//        tc1.add(new Process("p2", 1, 4, 1, 0));
//        tc1.add(new Process("p3", 2, 2, 4, 0));
//        tc1.add(new Process("p4", 3, 1, 2, 0));
//        tc1.add(new Process("p5", 4, 3, 5, 0));
//
//        int contextSwitch1 = 1;
//        int agingInterval1 = 5;
//
//        System.out.println("\n--- priority test case 1 ---");
//        PriorityScheduler priority1 =
//                new PriorityScheduler(tc1, contextSwitch1, agingInterval1);
//        priority1.execute();
//        priority1.printResults();
////
////        /* test case 2: all processes arrive at time 0 */
//        List<Process> tc2 = new ArrayList<>();
//
//        tc2.add(new Process("p1", 0, 6, 3, 0));
//        tc2.add(new Process("p2", 0, 3, 1, 0));
//        tc2.add(new Process("p3", 0, 8, 2, 0));
//        tc2.add(new Process("p4", 0, 4, 4, 0));
//        tc2.add(new Process("p5", 0, 2, 5, 0));
//
//        int contextSwitch2 = 1;
//        int agingInterval2 = 5;
//
//        System.out.println("\n--- priority test case 2 ---");
//        PriorityScheduler priority2 =
//                new PriorityScheduler(tc2, contextSwitch2, agingInterval2);
//        priority2.execute();
//        priority2.printResults();


//        /* test case 3: varied burst times with starvation risk */
        List<Process> tc3 = new ArrayList<>();

        tc3.add(new Process("p1", 0, 10, 5, 0));
        tc3.add(new Process("p2", 2, 5, 1, 0));
        tc3.add(new Process("p3", 5, 3, 2, 0));
        tc3.add(new Process("p4", 8, 7, 1, 0));
        tc3.add(new Process("p5", 10, 2, 3, 0));

        int contextSwitch3 = 1;
        int agingInterval3 = 4;

        System.out.println("\n--- priority test case 3 ---");
        PriorityScheduler priority3 =
                new PriorityScheduler(tc3, contextSwitch3, agingInterval3);
        priority3.execute();
        priority3.printResults();


        /* test case 4: large bursts with gaps in arrivals */
        List<Process> tc4 = new ArrayList<>();

        tc4.add(new Process("p1", 0, 12, 2, 0));
        tc4.add(new Process("p2", 4, 9, 3, 0));
        tc4.add(new Process("p3", 8, 15, 1, 0));
        tc4.add(new Process("p4", 12, 6, 4, 0));
        tc4.add(new Process("p5", 16, 11, 2, 0));
        tc4.add(new Process("p6", 20, 5, 5, 0));

        int contextSwitch4 = 2;
        int agingInterval4 = 6;

        System.out.println("\n--- priority test case 4 ---");
        PriorityScheduler priority4 =
                new PriorityScheduler(tc4, contextSwitch4, agingInterval4);
        priority4.execute();
        priority4.printResults();


        /* Test Case 5: Short bursts with high frequency */
        List<Process> tc5 = new ArrayList<>();

        tc5.add(new Process("p1", 0, 3, 3, 0));
        tc5.add(new Process("p2", 1, 2, 1, 0));
        tc5.add(new Process("p3", 2, 4, 2, 0));
        tc5.add(new Process("p4", 3, 1, 4, 0));
        tc5.add(new Process("p5", 4, 3, 5, 0));

        int contextSwitch5 = 1;
        int agingInterval5 = 3;

        System.out.println("\n--- priority test case 5 ---");
        PriorityScheduler priority5 =
                new PriorityScheduler(tc5, contextSwitch5, agingInterval5);
        priority5.execute();
        priority5.printResults();

        /* Test Case 6: Mixed scenario - comprehensive test*/
        List<Process> tc6 = new ArrayList<>();

        tc6.add(new Process("p1", 0, 14, 4, 0));
        tc6.add(new Process("p2", 3, 7, 2, 0));
        tc6.add(new Process("p3", 6, 10, 5, 0));
        tc6.add(new Process("p4", 9, 5, 1, 0));
        tc6.add(new Process("p5", 12, 8, 3, 0));
        tc6.add(new Process("p6", 15, 4, 6, 0));

        int contextSwitch6 = 1;
        int agingInterval6 = 5;

        System.out.println("\n--- priority test case 6 ---");
        PriorityScheduler priority6 =
                new PriorityScheduler(tc6, contextSwitch6, agingInterval6);
        priority6.execute();
        priority6.printResults();

        //END OF SEIF TESTING
        //----------------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------

        // run sjf scheduler
//        System.out.println("\n--- sjf test ---");
//        SJFScheduler sjf = new SJFScheduler(processes, contextSwitch);
//        sjf.execute();
//        sjf.printResults();



        // // rr and ag schedulers can be added later
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
//test 1
//        processes.add(new Process("P1", 0, 17, 4,7));
//        processes.add(new Process("P2", 2, 6, 7,9));
//        processes.add(new Process("P3", 5, 11, 3,4));
//        processes.add(new Process("P4", 15, 4, 6,6));
//
//        {"name": "P1", "arrival": 0, "burst": 10, "priority": 3, "quantum": 4},
//        {"name": "P2", "arrival": 0, "burst": 8, "priority": 1, "quantum": 5},
//        {"name": "P3", "arrival": 0, "burst": 12, "priority": 2, "quantum": 6},
//        {"name": "P4", "arrival": 0, "burst": 6, "priority": 4, "quantum": 3},
//        {"name": "P5", "arrival": 0, "burst": 9, "priority": 5, "quantum": 4}
// test 2
//        processes.add(new Process("P1", 0, 10, 3,4));
//        processes.add(new Process("P2", 0, 8, 1,5));
//        processes.add(new Process("P3", 0, 12, 2,6));
//        processes.add(new Process("P4", 0, 6, 4,3));
//        processes.add(new Process("P5", 0, 9, 5,4));

//        {"name": "P1", "arrival": 0, "burst": 20, "priority": 5, "quantum": 8},
//        {"name": "P2", "arrival": 3, "burst": 4, "priority": 3, "quantum": 6},
//        {"name": "P3", "arrival": 6, "burst": 3, "priority": 4, "quantum": 5},
//        {"name": "P4", "arrival": 10, "burst": 2, "priority": 2, "quantum": 4},
//        {"name": "P5", "arrival": 15, "burst": 5, "priority": 6, "quantum": 7},
//        {"name": "P6", "arrival": 20, "burst": 6, "priority": 1, "quantum": 3}
//test 3
//        processes.add(new Process("P1", 0, 20, 5,8));
//        processes.add(new Process("P2", 3, 4, 3,6));
//        processes.add(new Process("P3", 6, 3, 4,5));
//        processes.add(new Process("P4", 10, 2, 2,4));
//        processes.add(new Process("P5", 15, 5, 6,7));
//        processes.add(new Process("P6", 20, 6, 1,3));

//        {"name": "P1", "arrival": 0, "burst": 3, "priority": 2, "quantum": 10},
//        {"name": "P2", "arrival": 2, "burst": 4, "priority": 3, "quantum": 12},
//        {"name": "P3", "arrival": 5, "burst": 2, "priority": 1, "quantum": 8},
//        {"name": "P4", "arrival": 8, "burst": 5, "priority": 4, "quantum": 15},
//        {"name": "P5", "arrival": 12, "burst": 3, "priority": 5, "quantum": 9}
// test 4
//        processes.add(new Process("P1", 0, 3, 2,10));
//        processes.add(new Process("P2", 2, 4, 3,12));
//        processes.add(new Process("P3", 5, 2, 1,8));
//        processes.add(new Process("P4", 8, 5, 4,15));
//        processes.add(new Process("P5", 12, 3, 5,9));

//        {"name": "P1", "arrival": 0, "burst": 25, "priority": 3, "quantum": 5},
//        {"name": "P2", "arrival": 1, "burst": 18, "priority": 2, "quantum": 4},
//        {"name": "P3", "arrival": 3, "burst": 22, "priority": 4, "quantum": 6},
//        {"name": "P4", "arrival": 5, "burst": 15, "priority": 1, "quantum": 3},
//        {"name": "P5", "arrival": 8, "burst": 20, "priority": 5, "quantum": 7},
//        {"name": "P6", "arrival": 12, "burst": 12, "priority": 6, "quantum": 4}
//test 5
//        processes.add(new Process("P1", 0, 25, 3,5));
//        processes.add(new Process("P2", 1, 18, 2,4));
//        processes.add(new Process("P3", 3, 22, 4,6));
//        processes.add(new Process("P4", 5, 15, 1,3));
//        processes.add(new Process("P5", 8, 20, 5,7));
//        processes.add(new Process("P6", 12, 12, 6,4));

//Test 6
//        {"name": "P1","arrival":0,"burst":14,"priority":4,"quantum":6},
//        {"name": "P2","arrival":4,"burst":9,"priority":2,"quantum":8},
//        {"name": "P3","arrival":7,"burst":16,"priority":5,"quantum":5},
//        {"name": "P4","arrival":10,"burst":7,"priority":1,"quantum":10},
//        {"name": "P5","arrival":15,"burst":11,"priority":3,"quantum":4},
//        {"name": "P6","arrival":20,"burst":5,"priority":6,"quantum":7},
//        {"name": "P7","arrival":25,"burst":8,"priority":7,"quantum":9}

//        processes.add(new Process("P1", 0, 14, 4,6));
//        processes.add(new Process("P2", 4, 9, 2,8));
//        processes.add(new Process("P3", 7, 16, 5,5));
//        processes.add(new Process("P4", 10, 7, 1,10));
//        processes.add(new Process("P5", 15, 11, 3,4));
//        processes.add(new Process("P6", 20, 5, 6,7));
//        processes.add(new Process("P7", 25, 8, 7,9));
////
////        // run AG scheduler
//        System.out.println("\n--- AG test ---");
//        AG_Scheduling ag = new AG_Scheduling(processes);
//        ag.simulate();
//        ag.printResults();

// AG Test 1, 2 , 4, working for now
    }
}