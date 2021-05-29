import java.util.ArrayList;
public class Scheduler {
    ArrayList<Server> serverList;
    Job job;

    public Scheduler(ArrayList<Server> serverList, Job job) {
        this.serverList = serverList;
        this.job = job;
    }

    public String schedule(String algorithm) {
        switch (algorithm) {
            case "ATL":
                return allToLargest();
            case "NEW":
                return newAlgorithm();
            default:
                System.out.println("Please input a valid algorithm");
                System.exit(1);
                return null;
        }
    }

    public String allToLargest() {
        Server largest = getLargestServer(serverList);
        String outString = largest.serverType + " " + largest.serverID;
        
        return outString;
    }

    public String newAlgorithm() {
        Server best = null;
        String outString = "";
        
        if (best == null ) {
            best = shortestQueue(serverList);
        }
        outString = best.serverType + " " + best.serverID;
        return outString;
    }

    private Server getLargestServer(ArrayList<Server> serverList) {
        Server largest = serverList.get(0);
        Server test;

        for (int i = 0; i < serverList.size() -1; i++) {
            test = serverList.get(i + 1);
            if (largest.getCoreCount() < test.getCoreCount()) {
                largest = test;
            }
        }
        return largest;
    }

    private Server getSmallestServer(ArrayList<Server> serverList) {
        Server largest = serverList.get(0);
        Server test;

        for (int i = 0; i < serverList.size() -1; i++) {
            test = serverList.get(i + 1);
            if (largest.getCoreCount() > test.getCoreCount()) {
                largest = test;
            }
        }
        return largest;
    }

    private Server shortestQueue(ArrayList<Server> serverList) {
        Server best = getSmallestServer(serverList);
        Server test;

        for (int i = 0; i < serverList.size() -1; i++) {
            test = serverList.get(i + 1);
            if (best.getWaitingJobs() > test.getWaitingJobs()) {
                best = test;
            }
        }
        return best;
    }

}