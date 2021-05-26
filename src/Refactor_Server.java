public class Refactor_Server {
    String serverType, serverID, state, curStartTime, coreCount, memory, disk, waitingJobs, runningJobs;

    public Refactor_Server(String serverDetail) {
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

    public void setAvailability(String serverDetail) {
        String[] splitString = serverDetail.split(" ");

        state = splitString[2];
        coreCount = splitString[4];
        memory = splitString[5];
        disk = splitString[6];
        waitingJobs = splitString[7];
        runningJobs = splitString[8];
    }
}
