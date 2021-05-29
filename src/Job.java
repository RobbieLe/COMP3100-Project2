public class Job {
    String submitTime, jobID, estRuntime, coreCount, memory, storage;

    public Job(String jobString) {
        String[] splitString = jobString.split(" ");

        submitTime = splitString[1];
        jobID = splitString[2];
        estRuntime = splitString[3];
        coreCount = splitString[4];
        memory = splitString[5];
        storage = splitString[6];
    }
    
    public String getJobNeeds() {
        return coreCount + " " + memory + " " + storage;
    }
}
