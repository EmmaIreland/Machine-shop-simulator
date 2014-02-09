/** machine shop simulation */

package applications;

import utilities.MyInputStream;
import exceptions.MyInputException;

public class MachineShopSimulator {
    
    public static final String NUMBER_OF_MACHINES_AND_JOBS_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";
    
    
    private static int timeNow;
    private static int numMachines;
    private static int numJobs;
    private static EventList eList; // pointer to event list
    private static Machine[] machine;
    private static int largeTime; // all machines finish before this

    /**
     * move currentJob to machine for its next task
     * @return false iff no next task
     */
    private static boolean moveToNextMachine(Job currentJob) {
        if (currentJob.taskIsEmpty()) {
            Print.isEmptyPrint(timeNow, currentJob);
            return false;
        }
        // get machine for next task
        int machineIndex = currentJob.jobFirstTask();
        // put on machineIndex's wait queue
        machine[machineIndex].addJob(currentJob);
        currentJob.setArrivalTime(timeNow);
        // if machineIndex is idle, schedule immediately
        if (eList.nextEventTime(machineIndex) == largeTime) {
            changeState(machineIndex);
        }
        return true;
    }




    /**
     * change the state of theMachine
     * @return last job run on this machine
     */
    private static Job changeState(int theMachine) {// Task on theMachine has finished, schedule next one.
        Job lastJob;
        Machine currentMachine = machine[theMachine];
        if (currentMachine.isJobNull()) {// in idle or change-over state
            lastJob = null;
            // the wait is over, ready for new job
            nextJob(theMachine);
        } else {// task has just finished on currentMachine, schedule change-over time
            lastJob = currentMachine.getActiveJob();
            currentMachine.setToNull();
            eList.machineEventTime(theMachine, currentMachine, timeNow);
        }

        return lastJob;
    }



    private static void nextJob(int theMachine) {
        Machine currentMachine = machine[theMachine];
        if (currentMachine.hasNoActiveJob())
            eList.eventFinishTime(theMachine, largeTime);
        else {// take job off the queue and work on it
            int time = currentMachine.advanceJob(timeNow);
            eList.eventFinishTime(theMachine, timeNow + time);
        }
    }



    /** input machine shop data */
    private static void inputData() {
        // define the input stream to be the standard input stream (the keyboard)
        MyInputStream keyboard = new MyInputStream();

        System.out.println("Enter number of machines and jobs");
        numMachines = keyboard.readInteger();
        numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1)
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_AT_LEAST_1);

        // create event and machine queues
        eList = new EventList(numMachines, largeTime);
        machine = new Machine[numMachines];
            

        // input the change-over times
        System.out.println("Enter change-over times for machines");
        for (int j = 0; j < numMachines; j++) {
            machine[j] = new Machine();
            int changeOverTime = keyboard.readInteger();
            if (changeOverTime < 0)
                throw new MyInputException(CHANGE_OVER_TIME_AT_LEAST_0);
            machine[j].setChangeTime(changeOverTime);
        }

        // input the jobs
        inputJobs(keyboard);
    }

    private static void inputJobs(MyInputStream keyboard) {
        Job theJob;
        for (int i = 0; i < numJobs; i++) {
            Print.numTasksForJob(i);
            int tasks = keyboard.readInteger(); // number of tasks
            int firstMachine = 0; // machine for first task
            if (tasks < 1)
                throw new MyInputException(EACH_JOB_AT_LEAST_1_TASK);

            // create the job
            theJob = new Job(i);
            System.out.println("Enter the tasks (machine, time)"
                    + " in process order");
            for (int j = 0; j < tasks; j++) {// get tasks for job i
                int theMachine = keyboard.readInteger() - 1;
                int theTaskTime = keyboard.readInteger();
                if (theMachine < 0 || theMachine >= numMachines
                        || theTaskTime < 1)
                    throw new MyInputException(BAD_MACHINE_NUMBER_OR_TASK_TIME);
                if (j == 0)
                    firstMachine = theMachine; // job's first machine
                theJob.addTask(theMachine, theTaskTime); // add to task queue
            }
            machine[firstMachine].addJob(theJob);
        }
    }






    /** load first jobs onto each machine */
    private static void startShop() {
        for (int p = 0; p < numMachines; p++)
            changeState(p);
    }

    /** process all jobs to completion */
    private static void simulate() {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change state of job on machine to nextToFinish
            Job theJob = changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !moveToNextMachine(theJob))
                numJobs--;
        }
    }

    /** output wait times at machines */
    private static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (int p = 0; p < numMachines; p++) {
            Print.machineResults(p, machine[p]);
        }
    }



    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        largeTime = Integer.MAX_VALUE;
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        timeNow = 0;
        inputData(); // get machine and job data
        startShop(); // initial machine loading
        simulate(); // run all jobs through shop
        outputStatistics(); // output machine wait times
    }
}
