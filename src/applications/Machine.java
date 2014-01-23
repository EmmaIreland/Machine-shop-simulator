package applications;

import dataStructures.LinkedQueue;

class Machine {
    // data members
    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine

    // constructor
    Machine() {
        jobQ = new LinkedQueue();
    }
    
    @Deprecated
    public LinkedQueue getJobQ(){
        return jobQ;
    }
    
    public boolean hasNoActiveJob() {
        return jobQ.isEmpty();
    }
    
    public Job removeJob() {
        return (Job) this.jobQ.remove();
    }
    
    public int getChangeTime(){
        return changeTime;
    }
    
    public void setChangeTime(int time){
        changeTime = time;
    }
    
    public int getTotalWait(){
        return totalWait;
    }
    
    public void setTotalWait(int wait){
        totalWait = wait;
    }
    
    public int getNumTasks(){
        return numTasks;
    }
    
    public void setNumTasks(int tasks){
        numTasks = tasks;
    }
    
    public Job getActiveJob(){
        return activeJob;
    }
    
    public void setActiveJob(Job job){
        activeJob = job;
    }
}