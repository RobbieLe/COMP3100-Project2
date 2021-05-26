import java.util.ArrayList;

public class Refactor_Scheduler{
    Refactor_Job job;

    public Refactor_Scheduler(Refactor_Job job) {
        job = this.job;
    }

    public void schdAlgorithm(String algorithm) {
        switch (algorithm) {
            case "AF":
                allToFirst();
                break;
            case "YES":
                //Some new algorithm, MAYBE MIDDLE FIT???
                break;
            default:
                System.out.println("No Algorithm Found");
                System.exit(1);
        }
    }

    private void allToFirst() {
        
    }
}