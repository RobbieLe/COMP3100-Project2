import java.net.Socket;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(din));

        ArrayList<Job> jobList = new ArrayList<Job>();
        ArrayList<Server> serverList = new ArrayList<Server>();
        //ArrayList<Server> fullServerList = new ArrayList<Server>();

        Job job = null;
        Scheduler scheduler = new Scheduler(serverList, job);

        String inString = "", outString = "", state = "Initial";
        int serverCount = 0, jobCount = 0;
        String algorithm = "";
        boolean listUpdated = false;

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-a")) {
                algorithm = args[i + 1];
                break;
            }
        }

        while (!state.equals("QUIT")) {
            if (!state.equals("Initial") && !(outString.equals("REDY") && state.equals("JobScheduling"))) {
                inString = socketIn.readLine();
            }

            switch (state) {
                case "Initial":
                    outString = "HELO";
                    state = "Authorisation";
                    break;
                case "Authorisation":
                    outString = "AUTH " + System.getProperty("user.name");
                    state = "Ready";
                    break;
                case "Ready":
                    if (inString.equals("NONE")) {
                        outString = "QUIT";
                        state = "Quitting";
                    } else {
                        outString = "REDY";
                        state = "Decision";
                    }
                    break;

                case "Decision":
                    // if (fullServerList.isEmpty()) {
                    //     job = new Job(inString);
                    //     outString = "GETS All";
                    //     state = "serverListPrep";
                    // } else {
                        if ((serverCount == 0 || listUpdated == false) && inString.contains("JOBN")) {
                            job = new Job(inString);
                            outString = "GETS Capable " + job.getJobNeeds();
                            state = "serverListPrep";
                        }
                    // }

                    if (!serverList.isEmpty() && listUpdated == true) {
                        if (inString.contains("JOBN")) {
                            state = "JobScheduling";
                        } else {
                            state = "JobScheduling";
                        }
                    }
                    if (inString.contains("JCPL")) {
                        finishJob(inString, serverList);
                    }
                    if (inString.contains("NONE")) {
                        outString = "QUIT";
                        state = "Quitting";
                    }
                    break;

                case "serverListPrep":
                    serverCount = getDataCount(inString);
                    outString = "OK";
                    state = "serverListReading";
                    break;
                case "serverListReading":
                    // if (fullServerList.isEmpty()) {
                    //     readServerList(inString, fullServerList, socketIn, serverCount);
                    // } else {
                        //copyJobCount(true, serverList, fullServerList);
                        readServerList(inString, serverList, socketIn, serverCount);
                        //copyJobCount(false, serverList, fullServerList);

                        listUpdated = true;
                    // }
                    scheduler = new Scheduler(serverList, job);
                    outString = "OK";
                    state = "Ready";
                    break;

                case "JobScheduling":
                    if (inString.contains("JOBN")) {
                        job = new Job(inString);

                    }

                    if (inString.contains("NONE")) {
                        outString = "QUIT";
                        state = "Quitting";
                        break;
                    } else if (inString.contains("JCPL")) {
                        if (outString.equals("REDY")) {
                            break;
                        } else
                            state = "ERROR";
                    } else {
                        outString = "SCHD " + job.jobID + " " + scheduler.schedule(algorithm);
                        state = "Ready";
                    }
                    listUpdated = false;
                    break;

                case "Quitting":
                    outString = "QUIT";
                    state = "QUIT";
                    break;

                default:
                    System.out.println("Error has occurred");
                    quitCommunication(din, dout, s);
            }

            // System.out.println("InString: " + inString);
            // System.out.println("OutString: " + outString);
            // System.out.println("State: " + state + "\n");
            // Thread.sleep(500);

            if (!(outString.equals("REDY") && state.equals("JobScheduling"))) {
                dout.write((outString + "\n").getBytes());
            }
        }
        if (state.equals("QUIT")) {
            quitCommunication(din, dout, s);
        }
    }

    private static void readServerList(String inString, ArrayList<Server> serverList, BufferedReader socketIn,
            int serverCount) throws IOException {

        if (!serverList.isEmpty()) {
            serverList.removeAll(serverList);
        }
        Server temp;
        for (int i = 0; i < serverCount; i++) {
            if (i != 0) {
                inString = socketIn.readLine();
            }
            temp = new Server(inString);
            serverList.add(temp);
        }
    }

    private static void copyJobCount(boolean direction, ArrayList<Server> serverList,
            ArrayList<Server> fullServerList) {
        // direction, true is serverList -> fullServerList. false is fullServerList ->
        // serverList
        if (direction) {
            for (int i = 0; i < fullServerList.size(); i++) {
                for (int j = 0; j < serverList.size(); j++) {
                    if (serverList.get(j).getServerTypeID().equals(fullServerList.get(i).getServerTypeID())) {
                        fullServerList.get(i).jobCount = serverList.get(j).jobCount;
                    }
                }
            }
        } else {
            for (int i = 0; i < fullServerList.size(); i++) {
                for (int j = 0; j < serverList.size(); j++) {
                    if (serverList.get(j).getServerTypeID().equals(fullServerList.get(i).getServerTypeID())) {
                        serverList.get(j).jobCount = fullServerList.get(i).jobCount;
                    }
                }
            }
        }
    }

    private static int getDataCount(String inString) {
        String[] splitString = inString.split(" ");
        int count = Integer.parseInt(splitString[1]);

        return count;
    }

    private static void finishJob(String inString, ArrayList<Server> serverList) {
        String[] splitString = inString.split(" ");
        String serverType = splitString[3];
        String serverID = splitString[4];

        for (int i = 0; i < serverList.size(); i++) {
            if (serverList.get(i).serverType == serverType && serverList.get(i).serverID == serverID) {
                serverList.get(i).jobCount--;
                break;
            }
        }
    }

    private static void quitCommunication(DataInputStream din, DataOutputStream dout, Socket s) throws IOException {
        din.close(); // Close InputBufferStream
        dout.close(); // Close OutputBufferStream
        s.close(); // Close Socket with port 50000 as specified above
    }
}
