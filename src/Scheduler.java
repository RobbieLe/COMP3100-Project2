import java.util.ArrayList;

public class Scheduler {
    ArrayList<Server> serverList;

    public Scheduler(ArrayList<Server> serverList) {
        this.serverList = serverList;
    }

    // Determine which scheduling method to be used based on the command line
    // argument
    public String schedule(String algorithm) {
        switch (algorithm) {
            case "ATL":
                return allToLargest();
            case "NEW":
                return newAlgorithm();
            default: // When argument provided to client was not ATL or NEW, exit program and print
                     // statement to let user know
                System.out.println("Please input a valid algorithm");
                System.exit(1); // Exit program
                return null; // To satisfy the function needs of a return statement
        }
    }

    // Simple algorithm to schedule all the jobs to the first largest server
    public String allToLargest() {
        Server largest = getLargestServer(serverList); // Finds the largest server within the current serverList and
                                                       // saves it
        String outString = largest.serverType + " " + largest.serverID; // have the string prep for the SCHD message
                                                                        // with the server details (type and ID)

        return outString;
    }

    // Custom algorithm to schedule jobs to the server with the least amount of
    // waiting jobs
    public String newAlgorithm() {
        Server best = shortestQueue(serverList); // Finds the server with the least amount of jobs

        return best.getServerTypeID(); // have the string prep for the SCHD message with the server details (type and
                                       // ID)
    }

    // From within a ArrayList of servers, find the server with the most amount of
    // cores
    private Server getLargestServer(ArrayList<Server> serverList) {
        Server largest = serverList.get(0); // Assign the largest server as the first item in the ArrayList
        Server test;

        // Loop to check through every item in the ArrayList for comparison
        for (int i = 0; i < serverList.size() - 1; i++) {
            test = serverList.get(i + 1); // Set the server to check as one ahead of the loop
            if (largest.getCoreCount() < test.getCoreCount()) { // See if the server being checked has more CPU cores
                                                                // available
                largest = test; // If so, set the largest server to the one just checked
            }
        }
        return largest; // Return the server with the most CPU cores available
    }

    // Same as getLargestServer but insteads finds the server with the least amount
    // of CPU cores available
    private Server getSmallestServer(ArrayList<Server> serverList) {
        Server smallest = serverList.get(0); // Assign the smallest server as the first item in the ArrayList
        Server test;

        // Loop to check through every item in the ArrayList for comparison
        for (int i = 0; i < serverList.size() - 1; i++) {
            test = serverList.get(i + 1); // Set the server to check as one ahead of the loop
            if (smallest.getCoreCount() > test.getCoreCount()) { // See if the server being checked has less CPU cores
                                                                 // available
                smallest = test; // If so, set the smallest server to the one just checked
            }
        }
        return smallest; // Return the server with the least amount of CPU cores available
    }

    // Finds the server with the least amount of waiting jobs present
    private Server shortestQueue(ArrayList<Server> serverList) {
        Server best = getSmallestServer(serverList); // Assign the best server as the first item in the ArrayList
        Server test;

        // Loop to check through every item in the ArrayList for comparison
        for (int i = 0; i < serverList.size() - 1; i++) {
            test = serverList.get(i + 1); // Set the server to check as one ahead of the loop
            if (best.getWaitingJobs() > test.getWaitingJobs()) { // See if the server being checked has less waiting
                                                                 // jobs present
                best = test; // If so, set the best server to the one just checked
            }
        }
        return best; // Return the server with the least amount of waiting jobs
    }

}