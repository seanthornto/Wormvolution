import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTaskPane;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GUI {
	
	//Panels and frames
    private Frame mainFrame = new Frame("Worm Evolution");
    private JPanel controlPanel = new JPanel();
    private JPanel tools = new JPanel();
    private JScrollPane scrollTools;
    //Build a simulator
    private static Simulator simulator;
    public Simulator getSimulator() {
        return simulator;
    }
    //GUI constructor
    public GUI() {
        prepareGUI();
    }
    public void prepareGUI() {
        mainFrame.setSize(1200, 1100);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.xSpace = 1; mainFrame.ySpace = 1;
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
    
    //Behavior variables
    private int sleepCost;
    private int moveCost;
    private int turnCost;
    private long speed;
    private double mutation;
    private boolean barrierVis = true;
    
    //simulator environment variables
    private int foodVal;
    private int foodRate;
    private double colorVar;
    private static int boardSize = 160;
    private boolean isFullscreen = false; 
    
    //COMPLETELY UNUSED VARIABLES - LMAO
    //TODO: ??
    private static int startsC = 0;
    private static int startmC = 3;
    private static int starttC = 1;
    private int sightRange;
    private int startSpeed = 10;
    private double startMutate = 0.2;
    private int startfVal = boardSize / 2;
    private int startfRate = boardSize / 10;
    private boolean run = true;
    private JPanel displayTop;
    
    
    //Control panel variables
    private String[] genes = { "M", "Z", "<", ">", "U", "R", "D", "L", "H", "A",
            "v", "r", "e", "E", "C", "0", "-0", "1", "-1", "2", "-2", "3", "-3",
            "4", "-4", "5", "-5", "6", "-6" };
    private String[] commInfo = {
            "Moves critter one space in the direction it is facing",
            "Critter rests (Does nothing)",
            "Changes critter facing counter-clockwise.",
            "Changes critter facing clockwise.",
            "Changes critter facing to up.", "Changes critter facing to right.",
            "Changes critter facing to down.",
            "Changes critter facing to left.",
            "Moves critter two spaces in the direction it is facing.",
            "Steals energy from a critter directly in front of the head.",
            "OR: for conditionals X and Y, the code XvY continues if either X or Y are true.",
            "Restart at head of DNA",
            "ELSE: if a condition is checked to be false, go to the next ELSE (or END). Go to next END if stepping into ELSE.",
            "END: if a condition is checked to be false, go to the next END (or ELSE). Continue on if stepping into END.",
            "Go back to last conditional checked. If there is none, restart. Also acts as an END.",
            "IF there is food within sight range in the direction the critter is facing",
            "IF there is NOT food",
            "IF the critter was blocked when it last attempted to move",
            "IF the critter was NOT blocked",
            "IF the critter's energy is above half base energy",
            "IF the critter's energy is below half base energy",
            "IF the critter's age is above half max value",
            "IF the critter's age is below half max value",
            "IF the critter has reproduced",
            "IF the critter has NOT reproduced",
            "IF there is a critter within sight",
            "IF there is NOT a critter within sight",
            "IF there is a critter with the same DNA within sight",
            "If there is NOT a critter with the same DNA in sight" };


    //prompts the user for information, then starts the tick loop.
    public static void main(String[] args) {
        prompt();
        while (true) {

            simulator.gameTimeStep();
        }
    }
    
    //creates a pop-up to collect information from the user, then starts the game
    private static void prompt() {
        JFrame frame = new JFrame("Board Size");
        boardSize = Integer.parseInt(JOptionPane.showInputDialog(frame,
                "Input board size between 50 and 900"));
        start(0, 3, 1, boardSize);
    }

    // builds a fresh GUI and populates the simulation from defaults
    // takes(sleep Cost, movement Cost, turn cost, board size)
    private static void start(int sc, int mc, int tc, int bs) {
        GUI gui = new GUI();
        gui.showGUI(sc, mc, tc, bs);
        simulator.gameTimeStep(bs);
        int startingPopulation = (bs / 10);
        simulator.addCritter("M", startingPopulation);
    }
    
    //displays the main window
    //contains event listeners
    //controls the UX 
    private void showGUI(int sleepC, int moveC, int turnC, int bs) {

        sleepCost = sleepC;
        moveCost = moveC;
        turnCost = turnC;
        colorVar = 0.5;

        GridBagConstraints gbc = new GridBagConstraints();
        controlPanel.setLayout(new GridBagLayout());
        tools.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0,5,0,5);
        
        //----------
        //COMPONENTS
        //----------
        
        //PLAY - toggles pause/play by stopping and resuming the simulation.
        JButton play = new JButton("Stop");
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!simulator.isPaused()) {
                    simulator.pause();
                    play.setText("Start");
                    run = true;
                } else {
                    simulator.unpause();
                    play.setText("Stop");
                    run = false;
                }
            }
        });
        gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        //gbc.anchor = GridBagConstraints.SOUTH;
        controlPanel.add(play, gbc);
        
        //RESET - closes window, starts over from prompt();
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                mainFrame.dispose();
                prompt();
            }
        });
        gbc.gridy = 0;
        controlPanel.add(reset, gbc);

      //SIMULATOR - The big enchilada
        gbc.weighty = 0.0;
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.insets = new Insets(0,0,0,0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridheight = 3;
        simulator = new Simulator(bs, sleepCost, moveCost, turnCost);
        controlPanel.add(simulator.board, gbc);
        
        //reset gridbag
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,15,5,15);
        
        //ENVIRONMENT
        
        //Environment tools
        JXTaskPaneContainer environmentComponents = new JXTaskPaneContainer();
        environmentComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane environmentTools = new JXTaskPane();
        environmentTools.setTitle("Environment Tools");

        //SPEED - Adjusts the tick speed of the game
        String isNeg = (int)simulator.getSpeed() == 0 ? "" : "-";
        JLabel spd = new JLabel("Tick Speed: " + isNeg +(int) simulator.getSpeed());
        environmentTools.add(spd);
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
        speedSlider.setValue((int) (27067056 + (Math.log(simulator.getSpeed()/0.02)/200000000)));
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sp = ((JSlider) e.getSource()).getValue();
                speed = (long) (200000000 * Math.exp(-sp * 0.02)) - 27067056;
                simulator.setSpeed(speed);
                String isNeg = (int)simulator.getSpeed() == 0 ? "" : "-";
                spd.setText("Tick Speed: "+ isNeg + (int) simulator.getSpeed());
            }
        });
        environmentTools.add(speedSlider);
        
        //FOOD VALUE - adjusts the amount of energy a worm gets from 1 piece of food.
        JLabel fVal = new JLabel("Food Value: " + simulator.getFoodValue());
        environmentTools.add(fVal);
        JSlider foodValueSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 250);
        foodValueSlider.setValue(simulator.getFoodValue());
        foodValueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int foodV = ((JSlider) e.getSource()).getValue();
                foodVal = foodV;
                simulator.setFoodValue(foodVal);
                fVal.setText("Food Value: " + simulator.getFoodValue());
            }
        });
        environmentTools.add(foodValueSlider);
        
        //FOOD RATE - Adjusts the amount of food that spawns per tick
        JLabel fR8 = new JLabel("Food Rate: "+simulator.getFoodRate());
        environmentTools.add(fR8);
        JSlider foodRateSlider = new JSlider(JSlider.HORIZONTAL, 0, bs * bs / 200, bs * bs / 500);
        foodRateSlider.setValue(simulator.getFoodRate());
        foodRateSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int foodR = ((JSlider) e.getSource()).getValue();
                foodRate = foodR;
                simulator.setFoodRate(foodRate);
                fR8.setText("Food Rate: "+simulator.getFoodRate());
            }
        });
        environmentTools.add(foodRateSlider);
        
      //finishing up the environment task pane
        environmentTools.setCollapsed(true);
        environmentComponents.add(environmentTools);
        gbc.gridx = 0;
        gbc.gridy = 0;
        tools.add(environmentComponents,gbc);
        
        
        //BEHAVIOR
        
        //Behavior cost task panel
        JXTaskPaneContainer behaviorComponents = new JXTaskPaneContainer();
        behaviorComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane behaviorTools = new JXTaskPane();
        behaviorTools.setTitle("Behavior Tools");
        
        
        //SLEEP - adjusts the amount of energy a worm spends during a sleep tick
        JLabel sc = new JLabel("Sleep Cost: "+simulator.getSleepCost());
        behaviorTools.add(sc);
        JSlider sleepCostSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, sleepC);
        sleepCostSlider.setValue(simulator.getSleepCost());
        sleepCostSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sCost = ((JSlider) e.getSource()).getValue();
                sleepCost = sCost;
                simulator.setSleepCost(sleepCost);
                sc.setText("Sleep Cost: "+simulator.getSleepCost());
            }
        });
        behaviorTools.add(sleepCostSlider);
        
        //MOVEMENT - adjusts the amount of energy a worm spends during a movement tick
        JLabel mc = new JLabel("Move Cost: " + simulator.getMoveCost());
        behaviorTools.add(mc);
        JSlider moveCostSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, moveC);
        moveCostSlider.setValue(simulator.getMoveCost());
        moveCostSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int mCost = ((JSlider) e.getSource()).getValue();
                moveCost = mCost;
                simulator.setMoveCost(moveCost);
                mc.setText("Move Cost: " + simulator.getMoveCost());
            }
        });
        behaviorTools.add(moveCostSlider);

        
        //TURN - adjusts the amount of energy a worm spends during a turning tick
        JLabel tc = new JLabel("Turn Cost: " + simulator.getTurnCost());
        behaviorTools.add(tc);
        JSlider turnCostSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, turnC);
        turnCostSlider.setValue(simulator.getTurnCost());
        turnCostSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int tCost = ((JSlider) e.getSource()).getValue();
                turnCost = tCost;
                simulator.setTurnCost(turnCost);
                tc.setText("Turn Cost: " + simulator.getTurnCost());
            }
        });
        behaviorTools.add(turnCostSlider);
        
      //SIGHT - Adjusts the range a worm can detect food, barriers or other worms in front of it
        JLabel sr = new JLabel("Sight Range: "+simulator.getSightRange());
        behaviorTools.add(sr);
        JSlider sightRangeSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        sightRangeSlider.setValue(simulator.getSightRange());
        sightRangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sightR = ((JSlider) e.getSource()).getValue();
                sightRange = sightR;
                simulator.setSightRange(sightR);
                sr.setText("Sight Range: "+simulator.getSightRange());
            }
        });
        behaviorTools.add(sightRangeSlider);
        
      //finishing up the behavior task pane
        behaviorTools.setCollapsed(true);
        behaviorComponents.add(behaviorTools);
        gbc.gridy = 1;
        tools.add(behaviorComponents,gbc);   
        
        
       //GENETICS TOOLS
        
        //Genetics tools task pane
        JXTaskPaneContainer geneticsComponents = new JXTaskPaneContainer();
        geneticsComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane geneticsTools = new JXTaskPane();
        geneticsTools.setTitle("Genetics Tools");
        
        //MUTATION - adjusts the rate of DNA change in new worms
        JLabel mut8 = new JLabel("Mutation Rate: "+ Critter.getMutationRate());
        geneticsTools.add(mut8);
        JSlider mutationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
        mutationSlider.setValue((int)Critter.getMutationRate());
        mutationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int mutate = ((JSlider) e.getSource()).getValue();
                mutation = mutate / 100.0;
                Critter.setMutationRate(mutation);
                mut8.setText("Mutation Rate: " + Critter.getMutationRate());
            }
        });
        geneticsTools.add(mutationSlider);
        
      //COLOR - Adjusts the amount of color variation in new worm species
        JLabel cv = new JLabel("Color Variance: "+simulator.getColorVar());
        geneticsTools.add(cv);
        JSlider colorVarSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        colorVarSlider.setValue((int) simulator.getColorVar());
        colorVarSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int cVar = ((JSlider) e.getSource()).getValue();
                colorVar = cVar / 100.0;
                simulator.setColorVar(colorVar);
                cv.setText("Color Variance: "+simulator.getColorVar());
            }
        });
        geneticsTools.add(colorVarSlider);
        
        //DNA - opens pop-up sub-menu that allows user to select genes in the gene pool. 
        JButton comm = new JButton("Genes");
        comm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispGene();
            }
        });
        geneticsTools.add(comm);
        
        //POPULATION - opens pop-up info-graphic showing the most populous worm species.
        JButton dispPop = new JButton("Disp Populations");
        dispPop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!simulator.popDispVis)
                    simulator.dispPopulations();
            }
        });
        geneticsTools.add(dispPop);
        
        //finishing up genetics task pane
        geneticsTools.setCollapsed(true);
        geneticsComponents.add(geneticsTools);
        gbc.gridy = 2;
        tools.add(geneticsComponents, gbc);

        //-------------------
        //DRAWING COMPONENTS
        //-------------------
        
        //All of these components exist inside a task panel within the gridBag
        JXTaskPaneContainer drawingComponents = new JXTaskPaneContainer();
        drawingComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane drawTools = new JXTaskPane();
        drawTools.setTitle("Drawing Tools");
        
      // BARRIER WIDTH / BRUSH TYPE 
        JLabel bwl = new JLabel("Brush Size: " + simulator.board.getBarrierWidth());
        drawTools.add(bwl);
        int min = simulator.pixelSize;
        int max = simulator.pixelSize * 100;
        JSlider barrierWidth = new JSlider(JSlider.HORIZONTAL,min,max,1);
        barrierWidth.setValue(simulator.pixelSize);
        barrierWidth.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int bw = ((JSlider) e.getSource()).getValue();
        		simulator.board.setBarrierWidth(bw);
        		bwl.setText("Brush Size: " + simulator.board.getBarrierWidth());
        	}
        });
        drawTools.add(barrierWidth);
        
      //the following buttons are beveled, and should stay indented if selected
        Border raised = BorderFactory.createRaisedBevelBorder();
        JButton dragLine = new JButton("Drag Line");
        JButton dropPoint = new JButton("Drop Point");
        JButton dragGraph = new JButton("Drag Grid");
        JButton eraseRect = new JButton("Erase");
        
        
        //LINE - Drags a line from mouse position
        dragLine.setBorder(raised);
        dragLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.lTrue();
                toggleUtencil(dragLine, dropPoint, dragGraph, eraseRect);
            }
        });
        drawTools.add(dragLine);
        
        //POINT - Drops a single point barrier at mouse click
        dropPoint.setBorder(raised);
        dropPoint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.pTrue();
                toggleUtencil(dropPoint, dragGraph, dragLine, eraseRect);
            }
        });
        drawTools.add(dropPoint);
        
        //GRAPH - Drags a "screen" of points in a rectangle based on a line from mouse position
        dragGraph.setBorder(raised);
        dragGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.gTrue();
                toggleUtencil(dragGraph, dragLine, dropPoint, eraseRect);
            }
        });
        drawTools.add(dragGraph);
        
      //ERASE - Erases a barriers based on a line from mouse position.
        eraseRect.setBorder(raised);
        eraseRect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.eTrue();
                toggleUtencil(eraseRect, dragGraph, dragLine, dropPoint);
            }
        });
        drawTools.add(eraseRect);
        
      //All of these components exist inside a task panel within the gridBag
        JXTaskPaneContainer gridComponents = new JXTaskPaneContainer();
        gridComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane gridTools = new JXTaskPane();
        gridTools.setTitle("Grid Tools");
        
        //X SPACE - 
        JLabel xspLabel = new JLabel("X Space: 3");
        gridTools.add(xspLabel);
        JSlider xSpace = new JSlider(JSlider.HORIZONTAL,0,10,1);
        xSpace.setValue(mainFrame.xSpace);
        xSpace.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int xsp = ((JSlider) e.getSource()).getValue();
        		mainFrame.xSpace = xsp;
        		xspLabel.setText("X Space: "+xsp);
        	}
        });
        gridTools.add(xSpace);
        
        //X OFFSET -
        JLabel xoffLabel = new JLabel("X Offset: 3");
        gridTools.add(xoffLabel);
        JSlider xOff = new JSlider(JSlider.HORIZONTAL,0,10,1);
        xOff.setValue(mainFrame.xOff);
        xOff.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int xof = ((JSlider) e.getSource()).getValue();
        		mainFrame.xOff = xof;
        		xoffLabel.setText("X Offset: "+xof);
        	}
        });
        gridTools.add(xOff);
        
        //Y SPACE -
        JLabel yspLabel = new JLabel("Y Space: 3");
        gridTools.add(yspLabel);
        JSlider ySpace = new JSlider(JSlider.HORIZONTAL,0,10,1);
        ySpace.setValue(mainFrame.ySpace);
        ySpace.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int ysp = ((JSlider) e.getSource()).getValue();
        		mainFrame.ySpace = ysp;
        		yspLabel.setText("Y Space: "+ysp);
        	}
        });
        gridTools.add(ySpace);
        
        //Y OFFSET - 
        JLabel yoffLabel = new JLabel("Y Offset: 3");
        gridTools.add(yoffLabel);
        JSlider yOff = new JSlider(JSlider.HORIZONTAL,0,10,1);
        yOff.setValue(mainFrame.yOff);
        yOff.addChangeListener(new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int yof = ((JSlider) e.getSource()).getValue();
        		mainFrame.xOff = yof;
        		yoffLabel.setText("Y Offset: "+yof);
        	}
        });
        gridTools.add(yOff);
        
        //add grid tools to larger panel
        gridTools.setCollapsed(true);
        drawTools.add(gridTools);
        
      //INVISIBLE - Toggles visibility of barriers
        JButton toggleInv = new JButton("Hide Barriers");
        toggleInv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (barrierVis) {
                    simulator.setBarrierColor(Color.black);
                    barrierVis = false;
                    toggleInv.setText("Show Barriers");
                } else {
                    simulator.setBarrierColor(Color.gray);
                    barrierVis = true;
                    toggleInv.setText("Hide Barriers");
                }
            }
        });
        drawTools.add(toggleInv);
       
        
        //drawing task pane
        drawTools.setCollapsed(true);
        drawingComponents.add(drawTools);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 3;
        tools.add(drawingComponents, gbc);
        
        
        
        //-------------------
        //ZOOM COMPONENTS
        //-------------------
        
        //All of these components exist inside a task panel within the gridBag
        JXTaskPaneContainer zoomComponents = new JXTaskPaneContainer();
        zoomComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane zoomTools = new JXTaskPane();
        zoomTools.setTitle("Zoom Tools");
        
        //Full Screen / Windowed
        JButton fullscreen = new JButton("Fullscreen");
        fullscreen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String fsName = null;
        		if(isFullscreen == true)
        		{
        			//make it windowed
        			mainFrame.setSize(1200, 1100);
        			mainFrame.setLocationRelativeTo(null);
        			fsName = "Fullscreen";
        			isFullscreen = false;
        		}else
        		{
        			//make it fullscreen
        			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        			fsName = "Windowed";
        			isFullscreen = true;
        		}
        		fullscreen.setText(fsName);		
        	}
        });
        zoomTools.add(fullscreen);
        
        //
        
      //drawing task pane
        zoomTools.setCollapsed(true);
        zoomComponents.add(zoomTools);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 4;
        tools.add(zoomComponents, gbc);
        
        
        //--------------
        //FINISHING UP!
        //--------------
        
        //tools scroll bar
        scrollTools = new JScrollPane(tools);
        scrollTools.setHorizontalScrollBarPolicy(scrollTools.HORIZONTAL_SCROLLBAR_NEVER);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.5;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0,5,0,5);
        controlPanel.add(scrollTools, gbc);
        
        //main frame
        mainFrame.sim = simulator;
        mainFrame.bs = bs;
        mainFrame.setContentPane(controlPanel);
        mainFrame.setVisible(true);
        mainFrame.pixelSize = simulator.pixelSize;
        
    }
    
    //helper function used by the drawing tools
    public void toggleUtencil(JButton selected, JButton other, JButton other1, JButton other2)
    {
    	Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        if(selected.getBorder() == raised)
        {
        	selected.setBorder(lowered);
            other.setBorder(raised);
    		other1.setBorder(raised);	
    		other2.setBorder(raised);
        }else
        {
        	selected.setBorder(raised);
        	mainFrame.allFalse();
        }
    }
    
    //helper function used by the displayPopulation button
    public void dispGene() {
        simulator.pause();
        JDialog comDialog = new JDialog(mainFrame, "Genes");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        comDialog.setLayout(new GridBagLayout());
        ArrayList<String> inactiveGenes = simulator.getInactiveGenes();
        JCheckBox[] boxes = new JCheckBox[genes.length];
        for (int i = 0; i < genes.length; i++) {
            String gene = genes[i];
            if (inactiveGenes.contains(gene)) {
                boxes[i] = new JCheckBox(gene + " : " + commInfo[i], false);
            } else {
                boxes[i] = new JCheckBox(gene + " : " + commInfo[i], true);
            }
            boxes[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == 1) {
                        simulator.removeInactiveGene(gene);
                    } else {
                        simulator.addInactiveGene(gene);
                    }
                }
            });
            gbc.gridy = i;
            comDialog.add(boxes[i], gbc);
        }

        comDialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                simulator.unpause();
            }
        });
        comDialog.setSize(700, 700);
        comDialog.setVisible(true);

    }

    //TODO: finish and implement!
    public void save(String filename) {
        // write object to file
        try {
            if (run = false) {
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(simulator);
                oos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //TODO: finish and implement!
    public void load(String filename) {
        // read object from file
        try {
            if (run = false) {
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Simulator loadedSim = (Simulator) ois.readObject();
                ois.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
