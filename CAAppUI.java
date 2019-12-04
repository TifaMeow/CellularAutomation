package edu.neu.csye6200.ca;

import java.util.Observable;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.util.Observer;
//the AppUI observes CADataCalRunnable, 
//and implements ChangeListener for the JSlider
public class CAAppUI implements Observer, ChangeListener
{
    private static CAPanel canvas;
    private static JFrame frame;
    private static JButton startBtn;
    private static JButton stopBtn;
    private static JButton pauseOrResumeBtn;
    private static JComboBox<String> comboBox;
    private static JLabel comboItemSelectedLabel;
    private static JPanel mainPanel;
    //if no max Steps is set or during the JSlider is being adjusted, use the default max steps.
    private static int maxStepsDefault;
    private static CARule caRule;
    //check if the user has already selected a rule
    private boolean ruleSelected;
    private CACrystal crystal;
    private static int maxStepsInput;
    //a slider for the user to input the max steps the user want to simulate
    private static JSlider maxStepsSlider;
    private static final int MaxSteps_MIN = 2;
    private static final int MAXSteps_MAX = 20;
    private static final int MAXSteps_INIT = 10;
    
    public CADataCalculationRunnable dataCalRun;
    private CASimulationDisplayRunnable simDisplayRun;
    
    private boolean running;
    private boolean paused;
    private boolean done;
    private boolean ready;
    
    static {
        canvas = null;
        frame = null;
        startBtn = null;
        stopBtn = null;
        pauseOrResumeBtn = null;
        comboBox = null;
        comboItemSelectedLabel = null;
        mainPanel = null;
        maxStepsDefault = 20;
        maxStepsInput = 10;
    }
    
    public CAAppUI() {
        crystal = null;
        running = false;
        paused = false;
        done = false;
        ready = true;
        initGUI();
        dataCalRun = new CADataCalculationRunnable();
        dataCalRun.addObserver(this);
        simDisplayRun = new CASimulationDisplayRunnable();
    }
    
    private void initGUI() {
        ruleSelected = false;
        frame = new JFrame();
        frame.setSize(1200, 648);
        frame.setTitle("CAAppUI");
        frame.setResizable(true);
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new BorderLayout());
        frame.add(this.getMainPanel(), "North");
        canvas = new CAPanel();
        frame.add(CAAppUI.canvas, "Center");
        frame.setVisible(true);
    }
    
    private JPanel getMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        startBtn = new JButton("Start");
        pauseOrResumeBtn = new JButton("Pause/Resume");
        stopBtn = new JButton("Stop");
        comboItemSelectedLabel = new JLabel("Please select a rule for simulation.");
        mainPanel.add(new JLabel("Rule: "));
        maxStepsSlider = new JSlider(JSlider.HORIZONTAL,MaxSteps_MIN,MAXSteps_MAX, MAXSteps_INIT);
        maxStepsSlider.setMajorTickSpacing(4);
        maxStepsSlider.setMinorTickSpacing(1);
        maxStepsSlider.setPaintTicks(true);
        maxStepsSlider.setPaintLabels(true);
        maxStepsSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        maxStepsSlider.addChangeListener(this);
        comboBox = new JComboBox<String>();
        comboBox.addItem("Simple");
        comboBox.addItem("Medium");
        comboBox.addItem("Complex");
        mainPanel.add(CAAppUI.comboBox);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (e.getSource() == CAAppUI.comboBox) {
                    comboItemSelectedLabel.setText(CAAppUI.comboBox.getSelectedItem() + " Rule Selected!");
                    if (comboBox.getSelectedItem() == "Simple") {
                        caRule = new SimpleRule();
                        ruleSelected = true;
                    }
                    else if (CAAppUI.comboBox.getSelectedItem() == "Medium") {
                    	caRule = new MediumRule();
                    	ruleSelected = true;
                    }
                    else if (CAAppUI.comboBox.getSelectedItem() == "Complex") {
                        caRule = new ComplexRule();
                        ruleSelected = true;
                    }
                    else {
                        JOptionPane.showMessageDialog(CAAppUI.mainPanel, "Please select a simulation rule!");
                    }
  //                  System.out.println("combo box selected item: " + comboBox.getSelectedItem());
                }
            }
        });
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!dataCalRun.isReady(ruleSelected, maxStepsInput)) {
         //           System.out.println("Please select rule and max steps");
                    return;
                }
                
                //change done to false in order to start the simulation
                dataCalRun.setDone(false);
                simDisplayRun.setDone(false);
                
                //every time the start button is pressed, a new crystal needs to be created
                crystal = new CACrystal();
                //initialized with the the seed in the center
                crystal.initializeCrystal();
                dataCalRun.setCrystal(crystal);
                canvas.setCrystal(crystal);
                //pass the rule the user selected to the data calculation runnable
                dataCalRun.setRule(caRule);
                dataCalRun.setMaxSteps(maxStepsInput);
                if (!dataCalRun.isReady(ruleSelected, maxStepsInput)) {
       //             System.out.println("Please select a rule and max steps:");
                }
                simDisplayRun.setCrystal(crystal);
                simDisplayRun.setCanvas(canvas);
                simDisplayRun.setMaxSteps(maxStepsInput);
//                System.out.println("min Max Steps is: " + maxStepsInput);
//                System.out.println(dataCalRun);
//                System.out.println();
                
                //delegate the task of start calculation to data calculation thread
                dataCalRun.startCalculation();
  //              System.out.println(simDisplayRun);
                // delegate the simulation display to simulation display thread
                simDisplayRun.startDisplay();
                //adjust the button availability to avoid forbidden operations 
                setButtonStates();
            }
        });
        pauseOrResumeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
        //        System.out.println("PAUSE CALLED");
                //delegate the task of pause or resume simulation to other threads
                dataCalRun.pauseOrResumeCalculation();
                simDisplayRun.pauseOrResumeDisplay();
                //adjust the button availability to avoid forbidden operations 
                setButtonStates();
            }
        });
        stopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                //System.out.println("STOP CALLED");
                //delegate the task to associated threads
                dataCalRun.stopCalculation();
                simDisplayRun.stopDisplay();
              //adjust the button availability to avoid forbidden operations 
                setButtonStates();
            }
        });
        mainPanel.add(startBtn);
        mainPanel.add(pauseOrResumeBtn);
        mainPanel.add(stopBtn);
        mainPanel.add(comboItemSelectedLabel);
        mainPanel.add(maxStepsSlider);
        return mainPanel;
    }
  //adjust the button availability to avoid forbidden operations 
    private void setButtonStates() {
        ready = dataCalRun.isReady(this.ruleSelected, CAAppUI.maxStepsInput);
        done = dataCalRun.isDone();
        running = dataCalRun.isRunning();
        paused = dataCalRun.isPaused();
        //when parameters are ready and the process is not running, start button is enabled
        startBtn.setEnabled(ready && !running);
        //when the simulation is running or paused, the pause or resume button can be pressed 
        pauseOrResumeBtn.setEnabled(running || paused);
        //stop button can be pressed when running or paused or ready
        stopBtn.setEnabled(running || paused || ready);
    }
    
    public static void main(final String[] args) {
        new CAAppUI();
  //      System.out.println("caApp is running!");
    }
    //this main UI serve as an observer, monitor the CADataCalculationRunnable
    //whenever the status variable is changed, adjust the button availability to press.
    @Override
    public void update(final Observable o, final Object arg) {
        final boolean[] status = (boolean[])arg;
        running = status[0];
        paused = status[1];
        done = status[2];
    //    System.out.println("CAAppUI get message from CADataCalRun:, running, paused, done: " + this.running + ", " + this.paused + ", " + this.done);
        setButtonStates();
    }

    //get maxStepsInput of the user from the maxStepsSlider
	@Override
	public void stateChanged(ChangeEvent e) {
		maxStepsSlider = (JSlider)e.getSource();
		if(!maxStepsSlider.getValueIsAdjusting()) {
			maxStepsInput = (int)maxStepsSlider.getValue();
		}else {
			//set maxStepsInput to default value
			//if the user is adjust the slider, use the default max steps to avoid abormoly.
			maxStepsInput = maxStepsDefault;
		}
		
	}
    

}