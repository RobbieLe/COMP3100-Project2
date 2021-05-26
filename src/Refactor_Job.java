public class Refactor_Job {
    String submitTime, jobID, estRuntime, coreCount, memory, storage;

    public Refactor_Job(String jobString) {
        String[] splitString = jobString.split(" ");

        submitTime = splitString[1];
        jobID = splitString[2];
        estRuntime = splitString[3];
        coreCount = splitString[4];
        memory = splitString[5];
        storage = splitString[6];
    }

    //  Maybe setup getters for all attributes
    
    // public String getSubmitTime() {
    //     return submitTime;
    // }
    
    public String JobNeeds() {
        return coreCount + " " + memory + " " + storage;
    }
}
