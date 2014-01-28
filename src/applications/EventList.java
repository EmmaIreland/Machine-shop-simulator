package applications;

public class EventList {
    // data members
    private int[] finishTime; // finish time array

    // constructor
    EventList(int machines, int largestTime) {// initialize
                                                             // finish
                                                             // times for
                                                             // m
                                                             // machines
        if (machines < 1)
            throw new IllegalArgumentException(MachineShopSimulator.NUMBER_OF_MACHINES_AT_LEAST_1);
        finishTime = new int[machines+1];

        // all machines are idle, initialize with
        // large finish time
        for (int i = 1; i <= machines; i++)
            finishTime[i] = largestTime;
    }

    /** @return machine for next event */
    int nextEventMachine() {
        // find first machine to finish, this is the
        // machine with smallest finish time
        int smallTimePos = 1;
        int smallestTime = finishTime[1];
        for (int i = 2; i < finishTime.length; i++)
            if (finishTime[i] < smallestTime) {// i finishes earlier
                smallTimePos = i;
                smallestTime = finishTime[i];
            }
        return smallTimePos;
    }

    int nextEventTime(int theMachine) {
        return finishTime[theMachine];
    }

   
    public void eventFinishTime(int theMachine, int time) {
        finishTime[theMachine] = time; 
    }
    
    public void machineEventTime(int theMachine, Machine currentMachine, int timeNow) {
        finishTime[theMachine] = currentMachine.machineTime(timeNow);
    }


}