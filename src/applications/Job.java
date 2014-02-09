package applications;

import dataStructures.LinkedQueue;

class Job {
    private LinkedQueue taskQ; // this job's tasks
    private int taskTimes; // sum of scheduled task times
    private int arrivalTime;
    private int jobId;

    Job(int theId) {
        jobId = theId;
        taskQ = new LinkedQueue();
    }

    void addTask(int theMachine, int theTime) {
        taskQ.put(new Task(theMachine, theTime));
    }

    /**
     * remove next task of job and return its time and also update length
     */
    int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).getTime();
        taskTimes += theTime;
        return theTime;
    }
    
    
    public int totalWait(int timeNow) {
        return timeNow - taskTimes;
    }
    
    public int getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(int time){
        arrivalTime = time;
    }
    
    
    public int jobFirstTask() {
        return ((Task) taskQ.getFrontElement()).getMachine();
    }
    
    public boolean taskIsEmpty() {
        return taskQ.isEmpty();
    }
    
    public int getJobId(){
        return jobId;
    }
    
    
}