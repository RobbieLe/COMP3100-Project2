public class Refactor_Server {
    String serverType, serverID, state, curStartTime, coreCount, memory, disk;

    public Refactor_Server(String serverDetail) {
        String[] splitString = serverDetail.split(" ");

        serverType = splitString[0];
        serverID = splitString[1];
        state = splitString[2];
        curStartTime = splitString[3];
        coreCount = splitString[4];
        memory = splitString[5];
        disk = splitString[6];
    }

    public String getServerTypeID() {
        return serverType + " " + serverID;
    }
}
