package edu.neu.csye6200.ca;

import java.util.Set;
import java.util.Observable;

public class CADataCalculationRunnable extends Observable implements Runnable
{
    private int steps;
    private Thread myThread;
    private Runnable myRunnable;
    private CACrystal crystal;
    private CARule rule;
    private Set<CACell> outerLayer;
    private int maxSteps;
    //Essentially, volatile is used to indicate that a variable's value 
    //will be modified by different threads.
    private volatile boolean running;
    //ready means the status when simulation rule is selected and
    //the start button has not be pressed by the user yet
    private volatile boolean ready;
    public volatile boolean done;
    public volatile boolean paused;
    
    public int getSteps() {
        return this.steps;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    //user has selected rule and max steps, ready to start the simulation
    public boolean isReady(final boolean ruleSelected, final int maxSteps) {
        return this.ready = (ruleSelected && maxSteps <= 50 && maxSteps >= 1);
    }
    //during running, the user pressed paused/resume, get into the status of paused
    public boolean isPaused() {
        return this.paused;
    }
    //when the simulation process is done or the user pressed stop button
    public boolean isDone() {
        return !this.ready;
    }
    
    public CADataCalculationRunnable() {
        steps = 0;
        myThread = null;
        myRunnable = null;
        running = false;
        ready = true;
        done = false;
        paused = false;
        myRunnable = this;
    }
    
    public boolean isReady() {
        return this.ready;
    }
    
    public void setReady(final boolean ready) {
        this.ready = ready;
    }
    
    public void setRunning(final boolean running) {
        this.running = running;
    }
    
    public void setDone(final boolean done) {
        this.done = done;
    }
    
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
    
    public void setCrystal(final CACrystal crystal) {
        this.crystal = crystal;
        //initialized the crystal with the seed in the center
        //and get the initialized outer layer CACell set
        outerLayer = crystal.getInitializedOuterLayer();
       // System.out.println("This is initialized outerlayer: ");
        for (CACell cell : outerLayer) {
    //        System.out.println("row: " + cell.x + " col: " + cell.y);
        }
    }
    
    public void setRule(final CARule rule) {
        this.rule = rule;
    }
    
    public void setMaxSteps(final int maxSteps) {
        this.maxSteps = maxSteps;
    }
    
    public void startCalculation() {
        if (!ready) {
            System.out.println("Please provide all simulation parameters:");
            return;
        }
        done = false;
        paused = false;
        if (myThread == null) {
     //       System.out.println("CADataCalRun: This is a null thread: have to create one");
            setCrystal(crystal);
            myThread = new Thread(myRunnable);
        }
//        System.out.println("-----------CADataCal start sim-------------");
//        System.out.println("running = " + running);
//        System.out.println("done = " + done);
//        System.out.println("ready = " + ready);
//        System.out.println("paused = " + paused);
        if (!myThread.isAlive()) {
            myThread.start();
        }
        running = true;
    }
    
    public void pauseOrResumeCalculation() {
        paused = !paused;
    }
    
    public void stopCalculation() {
        done = true;
        running = false;
        myThread = null;
        steps = 0;
        rule.reset();
    }
    
    private void delayThread(final long delay) {
        try {
            Thread.sleep(delay);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    //core codes of handling thread
    @Override
    public void run() {
        while (!done & steps < maxSteps) {
            if (!paused) {
                ++steps;
          //      System.out.println("At this time steps = " + steps + " and done is: " + done);
                cellularAutomate();
                delayThread(200L);
                //to set buttons to corresponding status
                checkCurrentStatus(running, paused, done);
            }
        }
        done = true;
        myThread = null;
        running = false;
//        System.out.println("We've finished the whole data calculation process!");
//        System.out.println("done= " + done);
//        System.out.println("ready= " + ready);
//        System.out.println("running= " + running);
//        System.out.println("paused = " + paused);
    }
    
    private void checkCurrentStatus(final boolean running, final boolean paused, final boolean done) {
        setChanged();
        final boolean[] status = { running, paused, done };
        notifyObservers(status);
     //   System.out.println("notify running, paused, done to CAAppUI at this time.");
    }
    //spread one layer to outside in 6 directions
    private void cellularAutomate() {
        outerLayer = rule.automateNGetOuterLayerSet(crystal, outerLayer);
    }
}