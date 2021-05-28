public class Server {
    String serverType, serverID, state, curStartTime, coreCount, memory, disk;
    String waitingJobs, runningJobs;
    int jobCount;

    public Server(String serverDetail) {
        String[] splitString = serverDetail.split(" ");

        serverType = splitString[0];
        serverID = splitString[1];
        state = splitString[2];
        curStartTime = splitString[3];
        coreCount = splitString[4];
        memory = splitString[5];
        disk = splitString[6];
        waitingJobs = splitString[7];
        runningJobs = splitString[8];
    }

    public String getServerTypeID() {
        return serverType + " " + serverID;
    }

    public int getCoreCount() {
        return Integer.parseInt(coreCount);
    }

    public int getWaitingJobs() {
        return Integer.parseInt(waitingJobs);
    }

    public int getRunningJobs() {
        return Integer.parseInt(runningJobs);
    }
}
