public class Client_Job {
    String jobID;
    String submitTime;
    String estRuntime;
    String coreCount;
    String memory;
    String storage;

    public Client_Job (String jobString) {
        String[] unparsedString = jobString.split(" "); // Getting the words in the inString whilst ignore spaces to add
                                                        // into the array

        submitTime = unparsedString[1];
        jobID = unparsedString[2];
        estRuntime = unparsedString[3];
        coreCount = unparsedString[4];
        memory = unparsedString[5];
        storage = unparsedString[6];
    }

    String JobNeeds() {
        return coreCount + " " + memory + " " + storage;
    }
}
