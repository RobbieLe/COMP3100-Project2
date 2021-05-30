public class Server {
    String serverType, serverID, state, curStartTime, coreCount, memory, disk;
    String waitingJobs, runningJobs;

    // Split up the server detail message from server into their fields based on the
    // set format
    public Server(String serverDetail) {
        String[] splitString = serverDetail.split(" ");

        serverType = splitString[0]; // Size of server/name of the design
        serverID = splitString[1]; // Identification number for the server
        state = splitString[2]; // Shows what the server is currently doing ("inactive, booting, idle, active")
        curStartTime = splitString[3]; // The last time in the simulation of when the server is now active
        coreCount = splitString[4]; // The amount of CPU cores present on the server
        memory = splitString[5]; // The amount of RAM present on the server
        disk = splitString[6]; // The amount of Disk Storage present on the server
        waitingJobs = splitString[7]; // The amount of queued jobs present on the server
        runningJobs = splitString[8]; // The amount of jobs being run present on the server
    }

    // return String to be used in the SCHD message, has the serverType and the ID
    // of it
    public String getServerTypeID() {
        return serverType + " " + serverID;
    }

    // returns the coreCount of the server in the form on an int to be used in
    // comparsion
    public int getCoreCount() {
        return Integer.parseInt(coreCount);
    }

    // returns the waiting jobs present on the server in the form of an int for
    // comparision
    public int getWaitingJobs() {
        return Integer.parseInt(waitingJobs);
    }
}