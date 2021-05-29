public class Job {
    String submitTime, jobID, estRuntime, coreCount, memory, storage;

    // Splits the JOBN String into different values based on the set format
    public Job(String jobString) {
        String[] splitString = jobString.split(" ");

        submitTime = splitString[1]; // When the job was submitted to the client
        jobID = splitString[2]; // The job identification number, typically a integer
        estRuntime = splitString[3]; // How long the job is expected to run
        coreCount = splitString[4]; // How many CPU cores the job will need
        memory = splitString[5]; // How much RAM the job needs
        storage = splitString[6]; // How much Disk Storage the job needs
    }

    // Returns a string to be used in the GETS Capable message to show what the jobs
    // needs (CPU, RAM, HDD)
    public String getJobNeeds() {
        return coreCount + " " + memory + " " + storage;
    }
}