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

        //  Check argument for scheduling algorithm
        //      Pass argument to schedule
        //      Schedule Methods:
        //          Get Job etc.
        Refactor_Job job;
        Refactor_Scheduler algorithm;
        Refactor_Server server;

        ArrayList<Refactor_Server> serverList = new ArrayList<Refactor_Server>();

        String inString = "", outString = "", state = "Initial";
        while (!state.equals("QUIT")) {
            if (!state.equals("Initial")) {
                inString = socketIn.readLine();
            }

            switch (state) {
                case "Initial":
                    outString = "HELO";
                    break;
            }

            dout.write((outString + "\n").getBytes());
        }
    }
}
