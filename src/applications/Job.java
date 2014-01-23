package applications;

import dataStructures.LinkedQueue;

class Job {
    // data members
    private LinkedQueue taskQ; // this job's tasks
    private int taskTimes; // sum of scheduled task times
    private int arrivalTime; // arrival time at current queue
    private int jobId; // job identifier

    // constructor
    Job(int theId) {
        jobId = theId;
        taskQ = new LinkedQueue();
        // length and arrivalTime have default value 0
    }

    // other methods
    void addTask(int theMachine, int theTime) {
        taskQ.put(new Task(theMachine, theTime));
    }

    /**
     * remove next task of job and return its time also update length
     */
    int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).getTime();
        taskTimes += theTime;
        return theTime;
    }
    
    public int getTaskTimes(){
        return taskTimes;
    }
    
    public int getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(int time){
        arrivalTime = time;
    }
    
    public int getJobId(){
        return jobId;
    }
    
    public LinkedQueue getTaskQ(){
        return taskQ;
    }
}