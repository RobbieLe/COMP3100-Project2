import java.util.ArrayList;

public class Scheduler {
    ArrayList<Server> serverList;
    Job job;

    public Scheduler(ArrayList<Server> serverList, Job job) {
        this.serverList = serverList;
        this.job = job;
    }

    public String schedule(String algorithm) {
        // To be completed
        switch (algorithm) {
            case "ATL":
                return allToLargest();
            case "NEW":
                return newAlgorithm();
            default:
                return "UNABLE TO LOCATE ALGORITHM";
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
        
        if (!filterList("inactive").isEmpty()) {
            if (!filterList("idle").isEmpty()) {
                best = getSmallestServer(filterList("idle"));
            }
            best = getSmallestServer(filterList("inactive"));
        }

        if (best == null) {
            best = shortestQueue();
        }
        best.jobCount++;
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

    private ArrayList<Server> filterList(String state) {
        ArrayList<Server> temp = new ArrayList<Server>();

        for (int i = 0; i < serverList.size(); i++) {
            if (serverList.get(i).state.equals(state) ){//&& serverList.get(i).jobCount < 3) {
                temp.add(serverList.get(i));
            }
        }
        return temp;
    }

    private Server shortestQueue() {
        Server best = getLargestServer(serverList);
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