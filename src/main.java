import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args){
        // create process list
        List<Process> processes = new ArrayList<>();

        // name, arrival, burst, priority, quantum
        processes.add(new Process("p1", 0, 8, 3, 0));
        processes.add(new Process("p2", 1, 4, 1, 0));
        processes.add(new Process("p3", 2, 2, 4, 0));
        processes.add(new Process("p4", 3, 1, 2, 0));
        processes.add(new Process("p5", 4, 3, 5, 0));

        int contextSwitch = 1;
        int agingInterval = 5;

        // run priority scheduler
        System.out.println("\n--- priority test ---");
        PriorityScheduler priority =
                new PriorityScheduler(processes, contextSwitch, agingInterval);
        priority.execute();
        priority.printResults();

    }
}
