package applications;

public class Print {

    public static void machineResults(int p, Machine machine) {
        System.out.println("Machine " + (p + 1) + " completed " + machine.getNumTasks() + " tasks");
        System.out.println("The total wait time was " + machine.getTotalWait());
        System.out.println();
    }
    
    public static void numTasksForJob(int i) {
        System.out.println("Enter number of tasks for job " + (i+1));
    }
    
    public static void isEmptyPrint(int timeNow, Job job) {
        System.out.println("Job " + (job.getJobId() + 1) + " has completed at "
                + timeNow + " Total wait was " + job.totalWait(timeNow));
    }

}
