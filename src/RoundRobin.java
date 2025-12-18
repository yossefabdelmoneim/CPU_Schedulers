//import java.util.*;
//
//public class RoundRobin {
//
//    public static void simulateRR(List<Process> processes,
//                                  int timeQuantum,
//                                  int contextSwitchTime) {
//
//        Queue<Process> readyQueue = new LinkedList<>();
//        List<Process> finished = new ArrayList<>();
//
//        int currentTime = 0;
//        int index = 0;
//        Process previous = null;
//
//        // sort by arrival time
//        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
//
//        while (finished.size() < processes.size()) {
//
//            // add arrived processes
//            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
//                readyQueue.add(processes.get(index));
//                index++;
//            }
//
//            // CPU idle
//            if (readyQueue.isEmpty()) {
//                currentTime++;
//                continue;
//            }
//
//            Process current = readyQueue.poll();
//
//            // context switch
//            if (previous != null && previous != current) {
//                currentTime += contextSwitchTime;
//            }
//
//            // waiting in ready queue
//            current.incrementReadyQueueTime( currentTime - current.getLastExecutedTime());
//
//            int execTime = Math.min(timeQuantum, current.getRemainingBurstTime());
//
//            System.out.println("Time " + currentTime + " -> " + (currentTime + execTime) + " : " + current.getName());
//
//            currentTime += execTime;
//            current.setRemainingBurstTime(current.getRemainingBurstTime() - execTime);
//            current.setLastExecutedTime(currentTime);
//
//            // add newly arrived during execution
//            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
//                readyQueue.add(processes.get(index));
//                index++;
//            }
//
//            // unfinished → back to queue
//            if (current.getRemainingBurstTime() > 0) {
//                readyQueue.add(current);
//            }
//            // finished
//            else {
//                current.setCompletionTime(currentTime);
//                current.setTurnaroundTime(current.getCompletionTime() - current.getArrivalTime());
//                current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());
//                finished.add(current);
//            }
//
//            previous = current;
//        }
//
//        printResults(finished);
//    }
//
//    private static void printResults(List<Process> processes) {
//
//        double totalWT = 0;
//        double totalTAT = 0;
//
//        System.out.println("Process | Arrival | Burst | Completion | Turn | Waiting");
//
//        for (Process p : processes) {
//            totalWT += p.getWaitingTime();
//            totalTAT += p.getTurnaroundTime();
//
//            System.out.printf(
//                    "%7s |   %2d    |  %2d   |     %2d     | %3d  |   %2d\n",
//                    p.getName(),
//                    p.getArrivalTime(),
//                    p.getBurstTime(),
//                    p.getCompletionTime(),
//                    p.getTurnaroundTime(),
//                    p.getWaitingTime()
//            );
//        }
//
//        System.out.println("\nAverage Waiting Time = "
//                + (totalWT / processes.size()));
//        System.out.println("Average Turnaround Time = "
//                + (totalTAT / processes.size()));
//    }
//}
