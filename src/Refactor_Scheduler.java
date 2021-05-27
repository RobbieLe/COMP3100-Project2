import java.util.ArrayList;

public class Refactor_Scheduler {
    ArrayList<Refactor_Server> serverList;
    Refactor_Job job;

    public Refactor_Scheduler(ArrayList<Refactor_Server> serverList, Refactor_Job job) {
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
        }
        return "UNABLE TO LOCATE VARIABLE";
    }

    public String allToLargest() {

        Refactor_Server largest = getLargestServer(serverList);
        String outString = largest.serverType + " " + largest.serverID;
        
        return outString;
    }

    public String newAlgorithm() {
        
        System.out.println("New Shit Selected \n");

        return null;    // To be completed
    }

    private Refactor_Server getLargestServer(ArrayList<Refactor_Server> serverList) {
        Refactor_Server largest = serverList.get(0);
        Refactor_Server test;

        for (int i = 0; i < serverList.size() -1; i++) {
            test = serverList.get(i + 1);
            if (largest.intCoreCount() < test.intCoreCount()) {
                largest = test;
            }
        }

        return largest;
    }

    private Refactor_Server bestFit() {

        return null;    //To be completed
    }
}