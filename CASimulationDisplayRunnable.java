package edu.neu.csye6200.ca;

public class CASimulationDisplayRunnable implements Runnable
{
    private int steps;
    private int maxSteps;
    private Thread myThread;
    private Runnable myRunnable;
    private boolean running;
    private CAPanel canvas;
    private CACrystal crystal;
    private volatile boolean done;
    private volatile boolean paused;
    
    public CACrystal getCrystal() {
        return this.crystal;
    }
    
    public void setCrystal(final CACrystal crystal) {
        this.crystal = crystal;
    }
    
    public void setCanvas(final CAPanel canvas) {
        this.canvas = canvas;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public CASimulationDisplayRunnable() {
        steps = 0;
        myThread = null;
        myRunnable = null;
        running = false;
        crystal = null;
        done = false;
        paused = false;
        myRunnable = this;
    }
    
    public void setSteps(final int steps) {
        this.steps = steps;
    }
    
    public void setMyThread(final Thread myThread) {
        this.myThread = myThread;
    }
    
    public void setMyRunnable(final Runnable myRunnable) {
        this.myRunnable = myRunnable;
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
    
    public void setMaxSteps(final int maxSteps) {
        this.maxSteps = maxSteps;
    }
    
    public void startDisplay() {
        done = false;
        paused = false;
        //change the done to false in order to start running
        if (myThread == null) {
            myThread = new Thread(myRunnable);
        }
        if (!myThread.isAlive()) {
            myThread.start();
        }
    }
    
    public void pauseOrResumeDisplay() {
        paused = !paused;
    }
    
    public void stopDisplay() {
        myThread = null;
        done = true;
        running = false;
        steps = 0;
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
        while (!done && steps < maxSteps) {
            if (!paused) {
                ++steps;
                delayThread(200L);
                canvas.repaint();
            }
        }
        done = true;
        myThread = null;
        running = false;
    }
}
