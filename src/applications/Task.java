package applications;

// top-level nested classes
public class Task {
    // data members
    private int machine;
    private int singleTime;

    // constructor
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