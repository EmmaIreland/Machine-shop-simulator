/** machine shop simulation */

package applications;

import utilities.MyInputStream;
import exceptions.MyInputException;

public class MachineShopSimulator {
    
    public static final String NUMBER_OF_MACHINES_AT_LEAST_1 = "number of machines must be >= 1";
    public static final String NUMBER_OF_MACHINES_AND_JOBS_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";
    
    // data members of MachineShopSimulator
    private static int timeNow; // current time
    private static int numMachines; // number of machines
    private static int numJobs; // number of jobs
    private static EventList eList; // pointer to event list
    private static Machine[] machine; // array of machines
    private static int largeTime; // all machines finish before this

    // methods
    /**
     * move theJob to machine for its next task
     * 
     * @return false iff no next task
     */
    private static boolean moveToNextMachine(Job currentJob) {
        if (currentJob.getTaskQ().isEmpty()) {// no next task
            System.out.println("Job " + currentJob.getJobId() + " has completed at "
                    + timeNow + " Total wait was " + (timeNow - currentJob.getTaskTimes()));
            return false;
        } else {// theJob has a next task
                // get machine for next task
            int p = ((Task) currentJob.getTaskQ().getFrontElement()).getMachine();
            // put on machine p's wait queue
            machine[p].addJob(currentJob);
            currentJob.setArrivalTime(timeNow);
            // if p idle, schedule immediately
            if (eList.nextEventTime(p) == largeTime) {// machine is idle
                changeState(p);
            }
            return true;
        }
    }



    /**
     * change the state of theMachine
     * 
     * @return last job run on this machine
     */
    private static Job changeState(int theMachine) {// Task on theMachine has finished,
                                            // schedule next one.
        Job lastJob;
        Machine currentMachine = machine[theMachine];
        if (currentMachine.getActiveJob() == null) {// in idle or change-over
                                                    // state
            lastJob = null;
            // wait over, ready for new job
            nextJob(theMachine);
        } else {// task has just finished on machine[theMachine]
                // schedule change-over time
            lastJob = currentMachine.getActiveJob();
            currentMachine.setActiveJob(null);
            eList.setFinishTime(theMachine, timeNow
                    + currentMachine.getChangeTime());
        }

        return lastJob;
    }

    private static void nextJob(int theMachine) {
        Machine currentMachine = machine[theMachine];
        if (currentMachine.hasNoActiveJob()) // no waiting job
            eList.setFinishTime(theMachine, largeTime);
        else {// take job off the queue and work on it
            currentMachine.setActiveJob(currentMachine.removeJob());
            currentMachine.setTotalWait(currentMachine.getTotalWait() + timeNow - currentMachine.getActiveJob().getArrivalTime());
            currentMachine.setNumTasks(currentMachine.getNumTasks() +1);
            int t = currentMachine.getActiveJob().removeNextTask();
            eList.setFinishTime(theMachine, timeNow + t);
        }
    }





    /** input machine shop data */
    private static void inputData() {
        // define the input stream to be the standard input stream
        MyInputStream keyboard = new MyInputStream();

        System.out.println("Enter number of machines and jobs");
        numMachines = keyboard.readInteger();
        numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1)
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_AT_LEAST_1);

        // create event and machine queues
        eList = new EventList(numMachines, largeTime);
        machine = new Machine[numMachines+1];
        for (int i = 1; i <= numMachines; i++)
            machine[i] = new Machine();

        // input the change-over times
        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= numMachines; j++) {
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
        for (int i = 1; i <= numJobs; i++) {
            System.out.println("Enter number of tasks for job " + i);
            int tasks = keyboard.readInteger(); // number of tasks
            int firstMachine = 0; // machine for first task
            if (tasks < 1)
                throw new MyInputException(EACH_JOB_AT_LEAST_1_TASK);

            // create the job
            theJob = new Job(i);
            System.out.println("Enter the tasks (machine, time)"
                    + " in process order");
            for (int j = 1; j <= tasks; j++) {// get tasks for job i
                int theMachine = keyboard.readInteger();
                int theTaskTime = keyboard.readInteger();
                if (theMachine < 1 || theMachine > numMachines
                        || theTaskTime < 1)
                    throw new MyInputException(BAD_MACHINE_NUMBER_OR_TASK_TIME);
                if (j == 1)
                    firstMachine = theMachine; // job's first machine
                theJob.addTask(theMachine, theTaskTime); // add to
            } // task queue
            machine[firstMachine].addJob(theJob);
        }
    }

    /** load first jobs onto each machine */
    private static void startShop() {
        for (int p = 1; p <= numMachines; p++)
            changeState(p);
    }

    /** process all jobs to completion */
    private static void simulate() {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
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
        for (int p = 1; p <= numMachines; p++) {
            System.out.println("Machine " + p + " completed "
                    + machine[p].getNumTasks() + " tasks");
            System.out.println("The total wait time was "
                    + machine[p].getTotalWait());
            System.out.println();
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
