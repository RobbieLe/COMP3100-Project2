import java.net.Socket;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Refactor_Main {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 50000);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(din));

        ArrayList<Refactor_Server> serverList = new ArrayList<Refactor_Server>();
        int serverCount = 0;
        
        Refactor_Job job;
        Refactor_Server server;
        Refactor_Scheduler scheduler = new Refactor_Scheduler(serverList);



        String inString = "", outString = "", state = "Initial";
        while (!state.equals("QUIT")) {
            if (!state.equals("Initial")) {
                inString = socketIn.readLine();
            }

            switch (state) {
                case "Initial":
                    outString = "HELO";
                    state = "Authorisation";
                    break;
                case "Authorisation":
                    outString = "AUTH " + System.getProperty("user.name");
                    state = "Authorised";
                    break;
                
                
                case "Ready":
                    if (inString.contains("JOBN")) {
                        job = new Refactor_Job(inString);
                        outString = "GETS Capable" + job.getJobNeeds();
                        state = "serverListPrep";
                    }
                    break;


                case "serverListPrep":
                    serverCount = getServerCount(inString);
                    outString = "OK";
                    state = "serverListReading";
                    break;
                case "serverListReading":
                    readServerList(inString, serverList, socketIn, serverCount);
                    outString = "OK";
                    state = "Ready";
                    break;


                case "JobScheduling":
                    scheduler.newAlgorithm();
            }

            dout.write((outString + "\n").getBytes());
        }
    }

    private static void readServerList(String inString, ArrayList<Refactor_Server> serverList, BufferedReader socketIn,
        int serverCount) throws IOException {
    
            Refactor_Server temp;
            for (int i = 0; i < serverCount; i++) {
                if (i != 0) { 
                    inString = socketIn.readLine();
                }
                temp = new Refactor_Server(inString);
                serverList.add(temp);
            }
    }

    private static int getServerCount(String inString) {
        String[] splitString = inString.split(" ");
        int serverCount = Integer.parseInt(splitString[1]);

        return serverCount;
    }
}
