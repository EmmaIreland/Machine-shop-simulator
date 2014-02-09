package applications;

import dataStructures.LinkedQueue;

class Machine {
    private LinkedQueue jobQ;
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks;
    private Job activeJob;

    Machine() {
        jobQ = new LinkedQueue();
    }
    
    
    public boolean hasNoActiveJob() {
        return jobQ.isEmpty();
    }
    
    public Job removeJob() {
        return (Job) this.jobQ.remove();
    }
    
    public void addJob(Job currentJob) {
        this.jobQ.put(currentJob);
    }
    
    
    public int machineTime(int timeNow) {
        return timeNow + changeTime;
    }
    
    public void setChangeTime(int time){
        changeTime = time;
    }
    
    
    public Job getActiveJob(){
        return activeJob;
    }
    
    public boolean isJobNull() {
        return activeJob == null;
    }
    
    public void setToNull() {
        activeJob = null;
    }

    public int advanceJob(int timeNow) {
        activeJob = (Job) this.jobQ.remove(); // Remove activeJob from jobQ and start working on it.
        totalWait = totalWait + timeNow - activeJob.getArrivalTime(); // Calculate current activeJob's total wait time.
        numTasks = numTasks +1;
        int time = activeJob.removeNextTask(); // Current activeJob's task time.
        return time;
    }
    
    
    public int getNumTasks(){
        return numTasks;
    }
    
    public int getTotalWait(){
        return totalWait;
    }
}