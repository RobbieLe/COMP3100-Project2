import java.net.Socket;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 50000); // Local machine with the port 50000, used to communicate with ds-sim
        DataInputStream din = new DataInputStream(s.getInputStream()); // Allows receiving of messages from server
        DataOutputStream dout = new DataOutputStream(s.getOutputStream()); // Allows sending of messages to server
        
        // Keeps the messages from the server in the memory buffer to be read in the client later
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(din)); 

        // Holds the servers available for job scheduling in an ArrayList
        ArrayList<Server> serverList = new ArrayList<Server>();

        Job job = null; // Create the Job object, to allow for storage and easy reference to it
        Scheduler scheduler = new Scheduler(serverList); // Calls the schedule class,
                                                         // to allow for scheduling of the jobs

        // inString = message from server, outString = message to be sent
        // state = determine which switch to be in, based on what was sent and received
        String inString = "", outString = "", state = "Initial";
        int serverCount = 0; // Holds the number of servers that to be read
        String algorithm = "ATL"; // Defaults to ATL largest algorithm
        boolean listUpdated = false; // Allows the Client to know if the ArrayList serverList was updated with the
                                     // new GETS Capable message

        for (int i = 0; i < args.length - 1; i++) { // Check for command line argument for algorithm to use
            if (args[i].equals("-a")) { // "-a" was used it means there was an specific algorithm selected
                algorithm = args[i + 1]; // if command line had "-a", take argument straight after and save it
                break;
            }
        }

        while (!state.equals("QUIT")) { // Loop used to maintain communication with the server
            // Make sure that we don't try to read a message from the server 
            // when we are beginning OR when the message to be SENT is "REDY" and we were in the "Ready" state
            if (!state.equals("Initial") && !(outString.equals("REDY") && state.equals("JobScheduling"))) {
                inString = socketIn.readLine(); // Read the message sent from the server and save into inString
            }

            // Switch used to determine what messages get sent to the server
            switch (state) {
                case "Initial": // Send HELO to the server then move on to the AUTH
                    outString = "HELO";
                    state = "Authorisation";
                    break;
                case "Authorisation": // Send the AUTH message with the system's username
                    outString = "AUTH " + System.getProperty("user.name");
                    state = "Ready";
                    break;

                case "Ready": // Sends the REDY message to the server or proceed to quit
                    if (inString.equals("NONE")) { // If the message sent by the server was NONE,
                                                   // begin to quit communication
                        outString = "QUIT";
                        state = "Quitting";
                    } else { // If anything else, send REDY and change to Decision to determine what to do
                             // next based on response
                        outString = "REDY";
                        state = "Decision";
                    }
                    break;

                case "Decision": // Determine what to do based on current situtation
                    // If on new job schedule, prepare to update the server list by send GETS
                    // message with the job's needs
                    if ((serverCount == 0 || listUpdated == false) && inString.contains("JOBN")) {
                        job = new Job(inString);
                        outString = "GETS Capable " + job.getJobNeeds();
                        state = "serverListPrep";
                    }

                    // If serverlist is already updated, proceed to schedule the job to a server
                    if (!serverList.isEmpty() && listUpdated == true) {
                        state = "JobScheduling";
                    }

                    // When no more jobs to schedule, start to quit communication
                    if (inString.contains("NONE")) {
                        outString = "QUIT";
                        state = "Quitting";
                    }
                    break;

                case "serverListPrep": // Read the DATA message from the server
                    serverCount = getDataCount(inString); // read the 2nd word of the DATA message and save into
                                                          // serverCount
                    outString = "OK";
                    state = "serverListReading";
                    break;
                case "serverListReading": // Read the serverList generated from GETS capable message
                    readServerList(inString, serverList, socketIn, serverCount); //
                    listUpdated = true; // Change to true, to allow the Client to know that the serverList has been
                                        // updated with servers to be able to handle the job
                    scheduler = new Scheduler(serverList); // Update scheduler to have the current serverList and
                                                           // the current job
                    outString = "OK";
                    state = "Ready";
                    break;

                case "JobScheduling":
                    // When server sends a job message, save the job into a JOB object
                    if (inString.contains("JOBN")) {
                        job = new Job(inString);
                    }

                    // Once there are no more jobs, begin to quit
                    if (inString.contains("NONE")) {
                        outString = "QUIT";
                        state = "Quitting";
                        break;
                    } else if (inString.contains("JCPL")) { // If the message was notification of jobCompletion
                        if (outString.equals("REDY")) { // Check if we did send a REDY before
                            break; // In order to not send a duplicate message
                        }
                    } else {
                        outString = "SCHD " + job.jobID + " " + scheduler.schedule(algorithm); // Schedule job with
                                                                                               // algorithm
                        state = "Ready"; // Once scheduled, swap back to Ready to send another REDY message to server
                    }
                    listUpdated = false; // Once schedule, swap to false to make the Client to update the list for new
                                         // servers
                    break;

                case "Quitting": // Proceed to start quitting, send a QUIT message to server and end loop
                    outString = "QUIT";
                    state = "QUIT";
                    break;

                default:
                    System.out.println("Error has occurred"); // If some coding error made it land here, print this
                                                              // statement
                    quitCommunication(din, dout, s); // Close sockets and end communication
            }

            // Send the message to the server, unless we're about to send REDY and we were
            // in JobScheduling
            if (!(outString.equals("REDY") && state.equals("JobScheduling"))) {
                dout.write((outString + "\n").getBytes()); // Send the message, with a newline character in byte form.
                                                           // Ensures the server can communicate with the client
            }
        } // End of loop

        if (state.equals("QUIT")) {
            quitCommunication(din, dout, s); // Once we sent the QUIT message, begin to close the connection
        }
    }

    // Read the messages sent by the server when the client requests for the list of servers
    // with the GETS message
    private static void readServerList(String inString, ArrayList<Server> serverList, BufferedReader socketIn,
            int serverCount) throws IOException {

        if (!serverList.isEmpty()) { // If the current serverList is not empty, delete all entries
            serverList.removeAll(serverList);
        }
        Server temp; // Will be used to create a Server object and then be added to the ArrayList serverList
        for (int i = 0; i < serverCount; i++) { // Loop through messages that list the servers from GETS Capable
            if (i != 0) { // Ensure we don't skip the first entry of the server list by reading past it
                inString = socketIn.readLine();
            }
            temp = new Server(inString); // Add sever entry with values from the message from the server
            serverList.add(temp); // Add the server we just made into the ArrayList serverList
        }
    }

    // Reads the DATA message to grab how many lines are to be sent to the Client
    // and save it to be used in another function
    private static int getDataCount(String inString) {
        String[] splitString = inString.split(" "); // Cut up the DATA message by adding it to an String Array based on
                                                    // whitespaces
        int count = Integer.parseInt(splitString[1]); // Save the 2nd value in the DATA message (DATA N) and converts
                                                      // String to int

        return count;
    }

    // Closes off the connection with the server
    private static void quitCommunication(DataInputStream din, DataOutputStream dout, Socket s) throws IOException {
        din.close(); // Close InputBufferStream
        dout.close(); // Close OutputBufferStream
        s.close(); // Close Socket with port 50000 as specified above
    }
}