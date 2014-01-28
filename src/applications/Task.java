package applications;

public class Task {
    private int machine;
    private int singleTime;

    Task(int theMachine, int theTime) {
        machine = theMachine;
        singleTime = theTime;
    }
    
    public int getMachine() {
        return machine;
    }
    
   
    public int getTime(){
        return singleTime;
    }
}