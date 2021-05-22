import java.util.ArrayList;

public class Client_Schedule{
    int algorithm = 1;
    String inString = "", outString = "", jobString = "", state = "";
    Client_Job test = null;
    ArrayList<String[]> serverList = null;
    
    public Client_Schedule(int algorithm, ArrayList<String[]> serverList) {
        algorithm = this.algorithm;
        serverList = this.serverList;
    }

    public void firstFit() {
        if (inString.contains("JOBN")) {        // Check if the message the server sent was indeed the JOBN
            jobString = inString;       //  Keep the job assigned to client for later use
            test = new Client_Job(jobString);
        }
        
        if (inString.contains("NONE")) {    //If the message was NONE, start stopping the program
            outString = "QUIT";     //  Change the message we send to QUIT to tell the server we are done
            state = "Quitting";     //  Begin the quitting process 
        } else if (inString.contains("JCPL")) {     //  If the server message had JCPL (meaning that a job was completed)
            if (outString.equals("REDY")) {     
                /*  
                /   If the message the client was going to send was indeed REDY ensure we do not send another message. 
                /   This was neccessary because of a bug
                /
                /   The bug being if the last message JCPL it tended to send 2 REDY messages, due to the nature of the switch allowing only one case to operate
                /   And the nature of this case, was that it wouldn't change the outString but just the state so it would send out 2 REDY, one for JCPL and one repeated
                /   As it would in the process of just swapping the state during multiple loops
                */ 
            } else
            state = "NewJob";       //  Send to NewJob to ask for the new JOBN
        } else {
            outString = "SCHD " + test.jobID + " " + getLargestServer(serverList);     //SCHD Message construction
            state = "Ready";        //  Change state, to then ask for a new job
        }
    }

    private static String getLargestServer(ArrayList<String[]> serverList) {
        String largestServer[] = serverList.get(0); // Creating a string array to store the information of the current
                                                    // largest server
        String test[]; // Create a second string array to hold what we are going to compared
                       // largestServer[] to

        for (int i = 0; i < serverList.size() - 1; i++) { // Loop with serverList
            test = serverList.get(i + 1); // Have test have a copy of the server details one interation ahead
            if (Integer.parseInt(largestServer[4]) < Integer.parseInt(test[4])) { // Compare the Core-Count in the
                                                                                  // String which will be parsed to an
                                                                                  // int
                largestServer = test; // If the test had the server with more counts, swap largestServer to that
            }
        }

        return largestServer[0] + " " + largestServer[1]; // We want to return the Server-type and ID
    }
}