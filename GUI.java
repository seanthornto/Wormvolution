import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.*;

public class GUI {
	
	//Panels and frames
    private Frame mainFrame = new Frame("Worm Evolution");
    private JPanel controlPanel = new JPanel();
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
        mainFrame.setSize(1500, 1500);
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
        int comp = -1; //key value for components [CURRENTLY UNUSED]

        GridBagConstraints gbc = new GridBagConstraints();
        controlPanel.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        //----------
        //COMPONENTS
        //----------
        
        
        //RESET - closes window, starts over from prompt();
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                mainFrame.dispose();
                prompt();
            }
        });
        gbc.gridx = 7;
        gbc.gridy = 10;
        controlPanel.add(reset, gbc);
        comp++;
        
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
        gbc.gridx = 7;
        gbc.gridy = 0;
        controlPanel.add(play, gbc);
        comp++;
        
      //SIMULATOR - The big enchilada
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridheight = 7;
        gbc.gridwidth = 2;
        simulator = new Simulator(bs, sleepCost, moveCost, turnCost);
        controlPanel.add(simulator.board, gbc);
        comp++;
        
        //SPEED - Adjusts the tick speed of the game
        JSlider speedSlider = new JSlider(JSlider.VERTICAL, 0, 100, 20);
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int spd = ((JSlider) e.getSource()).getValue();
                speed = (long) (200000000 * Math.exp(-spd * 0.02)) - 27067056;
                simulator.setSpeed(speed);
            }
        });
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(speedSlider, gbc);
        comp++;
        JLabel spd = new JLabel("Tick Speed: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        controlPanel.add(spd, gbc);
        comp++;
        
        //MUTATION - adjusts the rate of DNA change in new worms
        JSlider mutationSlider = new JSlider(JSlider.VERTICAL, 0, 100, 20);
        mutationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int mutate = ((JSlider) e.getSource()).getValue();
                mutation = mutate / 100.0;
                Critter.setMutationRate(mutation);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        controlPanel.add(mutationSlider, gbc);
        comp++;
        JLabel mut8 = new JLabel("Mutation Rate: ");
        gbc.gridx = 1;
        gbc.gridy = 1;
        controlPanel.add(mut8, gbc);
        comp++;
        
        
        //FOOD VALUE - adjusts the amount of energy a worm gets from 1 piece of food.
        JSlider foodValueSlider = new JSlider(JSlider.VERTICAL, 0, 500, 250);
        foodValueSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int foodV = ((JSlider) e.getSource()).getValue();
                foodVal = foodV;
                simulator.setFoodValue(foodVal);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        controlPanel.add(foodValueSlider, gbc);
        comp++;
        JLabel fVal = new JLabel("Food Value: ");
        gbc.gridx = 2;
        gbc.gridy = 1;
        controlPanel.add(fVal, gbc);
        comp++;
        
        //FOOD RATE - Adjusts the amount of food that spawns per tick
        JSlider foodRateSlider = new JSlider(JSlider.VERTICAL, 0, bs * bs / 500, bs * bs / 1000);
        foodRateSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int foodR = ((JSlider) e.getSource()).getValue();
                foodRate = foodR;
                simulator.setFoodRate(foodRate);
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 0;
        controlPanel.add(foodRateSlider, gbc);
        comp++;
        JLabel fR8 = new JLabel("Food Rate: ");
        gbc.gridx = 3;
        gbc.gridy = 1;
        controlPanel.add(fR8, gbc);
        comp++;
        
        //SLEEP - adjusts the amount of energy a worm spends during a sleep tick
        JSlider sleepCostSlider = new JSlider(JSlider.VERTICAL, 0, 10, sleepC);
        sleepCostSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sCost = ((JSlider) e.getSource()).getValue();
                sleepCost = sCost;
                simulator.setSleepCost(sleepCost);
            }
        });
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        controlPanel.add(sleepCostSlider, gbc);
        comp++;
        JLabel sc = new JLabel("Sleep Cost: ");
        gbc.gridx = 0;
        gbc.gridy = 3;
        controlPanel.add(sc, gbc);
        comp++;
        
        //MOVEMENT - adjusts the amount of energy a worm spends during a movement tick
        JSlider moveCostSlider = new JSlider(JSlider.VERTICAL, 0, 10, moveC);
        moveCostSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int mCost = ((JSlider) e.getSource()).getValue();
                moveCost = mCost;
                simulator.setMoveCost(moveCost);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        controlPanel.add(moveCostSlider, gbc);
        comp++;
        JLabel mc = new JLabel("Move Cost: ");
        gbc.gridx = 1;
        gbc.gridy = 3;
        controlPanel.add(mc, gbc);
        comp++;
        
        //TURN - adjusts the amount of energy a worm spends during a turning tick
        JSlider turnCostSlider = new JSlider(JSlider.VERTICAL, 0, 10, turnC);
        turnCostSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int tCost = ((JSlider) e.getSource()).getValue();
                turnCost = tCost;
                simulator.setTurnCost(turnCost);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 2;
        controlPanel.add(turnCostSlider, gbc);
        comp++;
        JLabel tc = new JLabel("Turn Cost: ");
        gbc.gridx = 2;
        gbc.gridy = 3;
        controlPanel.add(tc, gbc);
        comp++;
        
        //COLOR - Adjusts the amount of color variation in new worm species
        JSlider colorVarSlider = new JSlider(JSlider.VERTICAL, 0, 100, 50);
        colorVarSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int cVar = ((JSlider) e.getSource()).getValue();
                colorVar = cVar / 100.0;
                simulator.setColorVar(colorVar);
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 2;
        controlPanel.add(colorVarSlider, gbc);
        comp++;
        JLabel cv = new JLabel("Color Variance: ");
        gbc.gridx = 3;
        gbc.gridy = 3;
        controlPanel.add(cv, gbc);
        comp++;
        
        //SIGHT - Adjusts the range a worm can detect food, barriers or other worms in front of it
        JSlider sightRangeSlider = new JSlider(JSlider.VERTICAL, 0, 20, 10);
        sightRangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sightR = ((JSlider) e.getSource()).getValue();
                sightRange = sightR;
                simulator.setSightRange(sightR);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        controlPanel.add(sightRangeSlider, gbc);
        comp++;
        JLabel sr = new JLabel("Sight Range: ");
        gbc.gridx = 0;
        gbc.gridy = 5;
        controlPanel.add(sr, gbc);
        comp++;
        
        //DNA - opens pop-up sub-menu that allows user to select genes in the gene pool. 
        JButton comm = new JButton("Genes");
        comm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispGene();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        controlPanel.add(comm, gbc);
        comp++;
        
        //POPULATION - opens pop-up info-graphic showing the most populous worm species.
        JButton dispPop = new JButton("Disp Populations");
        dispPop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!simulator.popDispVis)
                    simulator.dispPopulations();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 6;
        controlPanel.add(dispPop, gbc);
        comp++;

        //-------------------
        //DRAWING COMPONENTS
        //-------------------
        
        //INVISIBLE - Toggles visibility of barriers
        JButton toggleInv = new JButton("Make Barr Inv");
        toggleInv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (barrierVis) {
                    simulator.setBarrierColor(Color.black);
                    barrierVis = false;
                    toggleInv.setText("Make Barr Vis");
                } else {
                    simulator.setBarrierColor(Color.gray);
                    barrierVis = true;
                    toggleInv.setText("Make Barr Inv");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 7;
        controlPanel.add(toggleInv, gbc);
        comp++;
        
        //LINE - Drags a line from mouse position
        JButton dragLine = new JButton("Drag Line");
        dragLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.lTrue();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 7;
        controlPanel.add(dragLine, gbc);
        comp++;
        
        //POINT - Drops a single point barrier at mouse click
        JButton dropPoint = new JButton("Drop Point");
        dropPoint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.pTrue();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 7;
        controlPanel.add(dropPoint, gbc);
        comp++;
        
        //GRAPH - Drags a "screen" of points in a rectangle based on a line from mouse position
        JButton dragGraph = new JButton("Drag Grid");
        dragGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.gTrue();
            }
        });
        gbc.gridx = 3;
        gbc.gridy = 7;
        controlPanel.add(dragGraph, gbc);
        comp++;
        
        //ERASE - Erases a barriers based on a line from mouse position.
        JButton eraseRect = new JButton("Erase");
        eraseRect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.eTrue();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        controlPanel.add(eraseRect, gbc);
        comp++;
        
        //X SPACE - 
        JTextField xSpace = new JTextField("x Space");
        xSpace.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		int xs  = xSpace.getText() == null ?  0 : Integer.parseInt(xSpace.getText());
        		mainFrame.xSpace = xs;
        	}
        });
        gbc.gridx = 0;
        gbc.gridy = 9;
        controlPanel.add(xSpace, gbc);
        comp++;
        
        //X OFFSET -
        JTextField xOff = new JTextField("x Offset");
        xOff.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		int xo  = xOff.getText() == null ?  0 : Integer.parseInt(xOff.getText());
        		mainFrame.xOff = xo;
        	}
        });
        gbc.gridx = 1;
        gbc.gridy = 9;
        controlPanel.add(xOff, gbc);
        comp++;
        
        //Y SPACE -
        JTextField ySpace = new JTextField("y Space");
        ySpace.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		int ys  = ySpace.getText() == null ?  0 : Integer.parseInt(ySpace.getText());
        		mainFrame.ySpace = ys;
        	}
        });
        gbc.gridx = 0;
        gbc.gridy = 10;
        controlPanel.add(ySpace, gbc);
        comp++;
        
        //Y OFFSET - 
        JTextField yOff = new JTextField("y Offset");
        yOff.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		int yo  = yOff.getText() == null ?  0 : Integer.parseInt(yOff.getText());
        		mainFrame.yOff = yo;
        	}
        });
        gbc.gridx = 1;
        gbc.gridy = 10;
        controlPanel.add(yOff, gbc);
        comp++; 
        
        
        //--------------
        //FINISHING UP!
        //--------------
        mainFrame.sim = simulator;
        mainFrame.bs = bs;
        mainFrame.setContentPane(controlPanel);
        mainFrame.setVisible(true);
        mainFrame.pixelSize = simulator.pixelSize;
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
