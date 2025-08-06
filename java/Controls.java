/**
 * A file to handle all the various controls and settings.
 *  @author Sean Thornton and Sky Vercauteren
 *  @version 1.0 January 2024
 */

import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTaskPane;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.event.*;
import javax.swing.border.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;


public class Controls {
	
	//Behavior variables
	//Default Starting Values.
    private static int sleepCost = 0;
    private static int moveCost = 3;
    private static int turnCost = 1;
    private static int sightRange = 0;
    private int sc, mc, tc, sr; // temp variables for reset.
    private boolean barrierVis = true;
    
    //simulator environment variables
    private static Simulator simulator;
    private static int foodVal;
    private static int foodRate;
    private static double colorVar = 0.5;
    private double cv = 0.5;
    private long speed;
    private static double mutation;
    private double mt;
    private int fv, fr;
    public static void setSimulator(Simulator sim)
    {
    	simulator = sim;
    }
    
    //Sizing and scale variables
    private int boardSize = GUI.getBoardSize();
    public int newBoardSize =  boardSize;
    private int sizeConstraint = GUI.getSizeConstraint();
    private boolean isFullscreen = false;
    private static double scale = 1;
    public static void setScale(double ss)
    {
    	scale = ss;
    }
    
    //Panels and Frames
    public JPanel getControls()
    {
    	return controlPanel;
    }
    private JPanel controlPanel = new JPanel();
    private static JPanel tools = new JPanel();
    public static void setTools(JPanel t)
    {
    	tools = t;
    }
    public JPanel getTools()
    {
    	return tools;
    }
    private JScrollPane scrollTools;
    JPanel resetTools = new JPanel();
    JFrame resetFrame = new JFrame("Reset Options");
    Frame mainFrame = new Frame("INIT");
    //gbc for main frame
    GridBagConstraints gbc = new GridBagConstraints();
  //to reference the gridbag itself
    GridBagLayout gbl = new GridBagLayout();
    
    //Genes: Expand comment for more info.
    /*
     * ------
     *
     * M : moves one space in facing direction
     * Z : sleep
     * < : Turns counter clockwise
     * > : turns clockwise
     * U : faces up
     * D : faces down
     * L : faces left
     * R : faces right
     * H : hop - moves 2 spaces
     * A : attack - steals energy from critter in front of it
     * -------------
     * conditionals
     * -------------
     * v : OR conditionals XvY is true if either is true
     * *any two conditionals behave as AND*
     * r : restart at head
     * e : else - skip to next e or E on false condition
     * E : end - skip to end on false condition
     * C : loop conditional - go back to last condition
     * 0 : if there is food in sight
     * -0: if there is not food
     * 1 : if the critter was blocked
     * -1: if the critter was not blocked
     * 2 : if the energy is above half
     * -2: if the energy is not above half
     * 3 : if the age is above half
     * -3: if the age is not above half
     * 4 : if it has reproduced
     * -4: if it has not reproduced
     * 5 : if it can see a critter
     * -5:if it can't see a critter
     * 6 : if it can see a same species 
     * -6: if it want see a same species
     * 
     */
    //
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

    //displays the main window
    //contains event listeners
    //controls the UX 
    public Controls(Simulator sim, Frame mf) {
        
    	//Set sim
    	simulator = sim; 	
    	mainFrame = mf;
    	
    	//Create Layout
        controlPanel.setLayout(gbl);
        controlPanel.setBackground(GUI.background_color);
        tools.setLayout(new GridBagLayout());
        tools.setBackground(GUI.panel_primary);
        //reset pane
        resetTools.setLayout(new GridBagLayout());
        resetFrame.setSize(boardSize/3,(int)(boardSize/2));
    	resetTools.setSize(resetFrame.getSize());
    	resetFrame.setLocationRelativeTo(null);
    	
    	//initialize global vars
    	foodVal = simulator.getFoodValue();
    	foodRate = simulator.getFoodRate();
    	sightRange = simulator.getSightRange();
    	sleepCost = simulator.getSleepCost();
    	moveCost = simulator.getMoveCost();
    	turnCost = simulator.getTurnCost();
    	mutation = Critter.getMutationRate();
    	colorVar = simulator.getColorVar();
        
      //----------
      //COMPONENTS
      //----------
       
      //RESET - prompts user with different kinds of reset options;
    	JButton reset = newButton("Reset", "Prompts the user with different reset variations.");
    	reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	//throw pop up for reset options
            	resetFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        resetFrame.dispose();
                        simulator.unpause();
                    }
                });
            	resetFrame.setVisible(true);
            	
            	//Pause the simulation and collect current values.
            	simulator.pause();
            	fv = simulator.getFoodValue();
            	fr = simulator.getFoodRate();
            	sr = simulator.getSightRange();
            	sc = simulator.getSleepCost();
            	mc = simulator.getMoveCost();
            	tc = simulator.getTurnCost();
            	mt = Critter.getMutationRate();
            	cv = simulator.getColorVar();
            }
        });
    	packResetMenu();
    	addControl(reset, 0);
    	
      //ALL TOOLS - scroll bar panel
        scrollTools = new JScrollPane(tools);
        scrollTools.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollTools.setPreferredSize(new Dimension(GUI.getWindowSize().width-boardSize -70, boardSize - 100));
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,5,0,5);
        gbc.gridy = 1;
        controlPanel.add(scrollTools, gbc);
        
        // -- ENVIRONMENT
        
        //Environment tools
        JXTaskPaneContainer environmentComponents = new JXTaskPaneContainer();
        environmentComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane environmentTools = newScroll("Environment Tools", "Tools: tick speed, food rate, and food value.");
        environmentComponents.add(packEnvironment(environmentTools));
        addTool(environmentComponents, 0);
        
        // -- BEHAVIOR
        
        //Behavior tools: Sleep Cost, Movement Cost, Turn Cost, Sight Range
        JXTaskPaneContainer behaviorComponents = new JXTaskPaneContainer();
        behaviorComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane behaviorTools = newScroll("Behavior Tools", "Tools: Sleep Cost, Movement Cost, Turn Cost, Sight Range.");
        behaviorComponents.add(packBehavior(behaviorTools));
        addTool(behaviorComponents, 1);
        
        // -- GENETICS TOOLS
        
        //Genetics tools task pane
        JXTaskPaneContainer geneticsComponents = new JXTaskPaneContainer();
        geneticsComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane geneticsTools = newScroll("Genetics Tools", "Tools: Mutation Rate, Color Varience, Genes Selection, Population Display.");
        geneticsComponents.add(packGenetics(geneticsTools));
        addTool(geneticsComponents, 2);
        
        // -- DRAWING COMPONENTS
        
        //Draw line, rect, grid and stamp - also erase.
        JXTaskPaneContainer drawingComponents = new JXTaskPaneContainer();
        drawingComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane drawTools = newScroll("Drawing Tools", "Place barriers of various shapes, sizes and colors that worms can't move through.");
        drawingComponents.add(packDrawing(drawTools));
        addTool(drawingComponents, 3);
        
        // -- ZOOM COMPONENTS
        
     	//Zoom tools: windowed vs fullscreen. Scale down, Zoom in, zoom in again, return to fullsize.
        JXTaskPaneContainer zoomComponents = new JXTaskPaneContainer();
        zoomComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane zoomTools = newScroll("Zoom Tools", "Scale, zoom, zoom agian, full size, fullscreen or windowed.");
        zoomComponents.add(packZoom(zoomTools));
        addTool(zoomComponents, 4);
        
      //PLAY - toggles pause/play by stopping and resuming the simulation.
        JButton play = newButton("Stop", "Pauses and resumes the simulation.");
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//toggle pause/play
                if (!simulator.isPaused()) {
                    simulator.pause();
                    play.setText("Start");
                } else {
                    simulator.unpause();
                    play.setText("Stop");
                }
            }
        });
       addControl(play,2);
    }
    
    //
    //PACKERS
    //
    
    //This helper is literally JUST for readability. Come down here to edit the reset menu!!
    private void packResetMenu()
    {
    	//Presets - minimum, maximum, default, "saved"?
    	String presetTip = "Presets simply sets a specific combination of the below sliders. Feel free to set them yourself.";
        JLabel presetLabel = newLabel("Easy Presets:", presetTip);
    	addReset(presetLabel,0);
    	String[] choices = {"Easy","Hard","Default","Current"};
    	JComboBox<String> presets = new JComboBox<String>(choices);
    	presets.setToolTipText(presetTip);
    	presets.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			//Temp values! 
    			int s_c =0, m_c=0, t_c=0, f_r=0, f_v=0, s_r=0, b_s = 0;
    			double m_t = 0, c_v = 0;
    			switch((String)presets.getSelectedItem())
    			{
    			//set reset sliders: 
    			//sleep cost, move cost, turn cost, food rate (divisor), food value, sight range, mutation, color variance, new board size
    			case "Easy":
    				s_c = 0; m_c = 0; t_c = 0;  f_r = 200; f_v= 500; s_r = 20;
    				m_t = 0.2; c_v = 0.5;
    				b_s = sizeConstraint;
    				//Easy: Worms have life so good.
    				//Minimum: sleep, move, turn. Maximum: food value, rate, and sight range.
    				//Default mutation and color variation and largest board size.
    				break;
    			case "Hard":
    				s_c = 10; m_c = 10; t_c = 10;  f_r = 800; f_v= 25; s_r = 0;
    				m_t = 0.2; c_v = 0.5;
    				b_s = 50;
    				//Hard: Life is hell for a worm.
					//Maximum: sleep, move, turn. Minimum: food value, rate, and sight range.
    				//Default mutation and color variation and small board size.
    				break;
    			case "Current":
    				int r = boardSize*boardSize/fr;
    				s_c = sc; m_c = mc; t_c = tc;  f_r = r; f_v= fv; s_r = sr;
    				m_t = mt; c_v = cv;
    				b_s = boardSize;
    				//whatever the sliders where at before reset was clicked.
    				break;
    			case "Default":
    				s_c = 0; m_c = 03; t_c = 1;  f_r = 200; f_v= 50; s_r = 10;
    				m_t = 0.2; c_v = 0.5;
    				b_s = sizeConstraint;
    				//as far as I know these are all the constructor defaults.
    				break;
    			}
    			setResetSliders(s_c, m_c, t_c, f_r, f_v, s_r, m_t, c_v, b_s);
    		}
    	});
    	addReset(presets,1);
    	
    	
    	//Board Size
    	JLabel bsLabel = newLabel("Board Size", "The size of the new board.");
    	ChangeListener bsListen = new ChangeListener() {
    		public void stateChanged(ChangeEvent e) {
        		int bw = ((JSlider) e.getSource()).getValue();
        		newBoardSize=(bw> 30 ? bw : 30);
        		bsLabel.setText("Board Size: " + newBoardSize);
        	}
    	};
    	JPanel boardSizeSlider = newSlider(bsLabel, 30, sizeConstraint, boardSize, bsListen);
    	addReset(boardSizeSlider,2);
    	
    	
    	//Submit - reset board with current slider values
    	JButton reset = newButton("   ===RESET===   ", "Commit to these changes and start a new game");
    	reset.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    	reset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//TODO: Bug! - reset only works one time?? - and it resizes the tool panel when values are changed??
				
				//destroy old board
				mainFrame.setVisible(false);
				mainFrame.dispose();
				
		        GUI.start(newBoardSize); 
				GUI.autoscale(newBoardSize);	
				
				//Ok we need to keep track of:
				//food rate, food value, sight range, mutation rate, color variation, genes.
				simulator.setSleepCost(sleepCost);
				simulator.setMoveCost(moveCost);
				simulator.setTurnCost(turnCost);
				simulator.setFoodRate(foodRate);
				simulator.setFoodValue(foodVal);
				simulator.setSightRange(sightRange);
				Critter.setMutationRate(mutation);
				simulator.setColorVar(colorVar);
				
				//and a new tick speed, if you happened to change it.
				simulator.setSpeed(speed);
				
				resetFrame.dispose();
			}
		});
    	addReset(reset, 14);
    	
    	resetFrame.setContentPane(resetTools);
    }
    
    
    //This helper is literally JUST for readability. Come down here to edit the environment components!!
    public JXTaskPane packEnvironment(JXTaskPane tools)
    {
    	GridBagConstraints c = new GridBagConstraints();
    	
    	//SPEED - Adjusts the tick speed of the game
    	int spdValue = 100;
    	JLabel spdLabel = newLabel("Tick Speed", "Adjust the speed of game ticks.");
    	ChangeListener spdListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sp = ((JSlider) e.getSource()).getValue();
                speed = (long) (200000000 * Math.exp(-sp * 0.02)) - 27067056;
                simulator.setSpeed(speed);
                spdLabel.setText("Tick Speed: "+ sp);
            }
        };
        JPanel speedSlider = newSlider(spdLabel,0,100,spdValue, spdListen);
        c.gridy = 0;
        tools.add(speedSlider,c);
        addReset(copySlider(speedSlider), 3);
        
        //FOOD VALUE - adjusts the amount of energy a worm gets from 1 piece of food.
        int fVal = simulator.getFoodValue();
    	JLabel fValLabel = newLabel("Food Value", "adjusts the amount of energy a worm gets from 1 piece of food." );
    	ChangeListener fvListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int foodV = ((JSlider) e.getSource()).getValue();
                foodVal = foodV;
                simulator.setFoodValue(foodVal);
                fValLabel.setText("Food Value: " + simulator.getFoodValue());
            }
    	};
        JPanel fValSlider = newSlider(fValLabel,0,500,fVal, fvListen);
        c.gridy++;
        tools.add(fValSlider, c);
        addReset(copySlider(fValSlider),4);
        
        //FOOD RATE - Adjusts the amount of food that spawns per tick
        int frVal = simulator.getFoodRate();
        JLabel frLabel = newLabel("Food Rate", "Adjusts the amount of food that spawns per tick");
        ChangeListener frListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int foodR = ((JSlider) e.getSource()).getValue();
                foodRate = foodR;
                simulator.setFoodRate(foodRate);
                frLabel.setText("Food Rate: "+foodRate);
            }
        };
        JPanel frSlider = newSlider(frLabel,0, boardSize * boardSize / 200, frVal, frListen);
        c.gridy++;
        tools.add(frSlider,c);
        addReset(copySlider(frSlider), 5);
        
        tools.setCollapsed(true);
        return tools;
    }
    
    
    //This helper is literally JUST for readability. Come down here to edit the behavior components!!
    public JXTaskPane packBehavior(JXTaskPane tools)
    {
    	GridBagConstraints c = new GridBagConstraints();
    	
    	//SLEEP - adjusts the amount of energy a worm spends during a sleep tick
    	int scVal = simulator.getSleepCost();
    	JLabel scLabel = newLabel("Sleep Cost", "adjusts the amount of energy a worm spends during a sleep tick");
    	ChangeListener scListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sCost = ((JSlider) e.getSource()).getValue();
                sleepCost = sCost;
                simulator.setSleepCost(sleepCost);
                scLabel.setText("Sleep Cost: "+simulator.getSleepCost());
            }
        };
        JPanel scSlider = newSlider(scLabel, 0, 10, scVal, scListen);
        c.gridy = 0;
        tools.add(scSlider, c);
        addReset(copySlider(scSlider), 6);
        
        //MOVEMENT - adjusts the amount of energy a worm spends during a movement tick
        int mcVal = simulator.getMoveCost();
        JLabel mcLabel = newLabel("Move Cost", "adjusts the amount of energy a worm spends during a movement tick");
        ChangeListener mcListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int mCost = ((JSlider) e.getSource()).getValue();
                moveCost = mCost;
                simulator.setMoveCost(moveCost);
                mcLabel.setText("Move Cost: " + simulator.getMoveCost());
            }
        };
        JPanel mcSlider = newSlider(mcLabel, 0, 10, mcVal, mcListen);
        c.gridy++;
        tools.add(mcSlider,c);
        addReset(copySlider(mcSlider),7);
        
        //TURN - adjusts the amount of energy a worm spends during a turning tick
        int tcVal = simulator.getTurnCost();
        JLabel tcLabel = newLabel("Turn Cost", "adjusts the amount of energy a worm spends during a turning tick");
        ChangeListener tcListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int tCost = ((JSlider) e.getSource()).getValue();
                turnCost = tCost;
                simulator.setTurnCost(turnCost);
                tcLabel.setText("Turn Cost: " + simulator.getTurnCost());
            }
        };
        JPanel tcSlider = newSlider(tcLabel, 0, 10, tcVal, tcListen);
        c.gridy++;
        tools.add(tcSlider, c);
        addReset(copySlider(tcSlider), 8);
        
        //SIGHT - Adjusts the range a worm can detect food, barriers or other worms in front of it
        int srVal = simulator.getSightRange();
        JLabel srLabel = newLabel("Sight Range", "Adjusts the range a worm can detect food, barriers or other worms in front of it");
        ChangeListener srListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int sightR = ((JSlider) e.getSource()).getValue();
                sightRange = sightR;
                simulator.setSightRange(sightR);
                srLabel.setText("Sight Range: "+simulator.getSightRange());
            }
        };
        JPanel srSlider = newSlider(srLabel, 0, 20, srVal, srListen);
        c.gridy++;
        tools.add(srSlider, c);
        addReset(copySlider(srSlider), 9);
        
        //Finishing Up
        tools.setCollapsed(true);
    	return tools;
    }
    
    
    //This helper is literally JUST for readability. Come down here to edit the Genetics Components!!
    public JXTaskPane packGenetics(JXTaskPane tools)
    {
    	GridBagConstraints c = new GridBagConstraints();
    	
    	//MUTATION - adjusts the rate of DNA change in new worms
    	double mtVal = Critter.getMutationRate() * 100;
    	JLabel mtLabel = newLabel("Mutation Rate", "adjusts the rate of DNA change in new worms");
    	ChangeListener mtListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int mutate = ((JSlider) e.getSource()).getValue();
                mutation = mutate / 100.0;
                Critter.setMutationRate(mutation);
                mtLabel.setText("Mutation Rate: " + Critter.getMutationRate());
            }
        };
        JPanel mtSlider = newSlider(mtLabel, 0, 100, (int)mtVal, mtListen);
        c.gridy++;
        tools.add(mtSlider,c);
        addReset(copySlider(mtSlider),10);
        
        //COLOR - Adjusts the amount of color variation in new worm species
        double cVal = simulator.getColorVar() * 100;
        JLabel cLabel = newLabel("Color Variance", "Adjusts the amount of color variation in new worm species");
        ChangeListener cListen = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int cVar = ((JSlider) e.getSource()).getValue();
                colorVar = cVar / 100.0;
                simulator.setColorVar(colorVar);
                cLabel.setText("Color Variance: "+simulator.getColorVar());
            }
        };
        JPanel cSlider = newSlider(cLabel, 0, 100, (int)cVal, cListen);
        c.gridy++;
        tools.add(cSlider,c);
        addReset(copySlider(cSlider),11);
        
        //DNA - opens pop-up sub-menu that allows user to select genes in the gene pool
        JButton dna = newButton("Genes", "opens pop-up sub-menu that allows user to select genes in the gene pool");
        JButton dnaCopy = newButton("Genes", "opens pop-up sub-menu that allows user to select genes in the gene pool");
        ActionListener dnaListen = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispGene();
            }
        };
        dna.addActionListener(dnaListen);
        dnaCopy.addActionListener(dnaListen);
        c.gridy++;
        tools.add(dna,c);
        addReset(dnaCopy,12);
        
        //POPULATION - opens pop-up info-graphic showing the most populous worm species.
        JButton pop = newButton("Display Populations", "opens pop-up info-graphic showing the most populous worm species.");
        pop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!simulator.popDispVis)
                    simulator.dispPopulations();
            }
        });
        c.gridy++;
        tools.add(pop,c);
    	
    	//Finishing Up
        tools.setCollapsed(true);
    	return tools;
    }
    
    
    //This helper is literally JUST for readability. Come down here to edit the drawing components!!
    public JXTaskPane packDrawing(JXTaskPane tools)
    {
    	GridBagConstraints c = new GridBagConstraints();
    	
    	//BARRIER\BRUSH THICKNESS - adjust the thickness of the barrier to be drawn.
    	int bwVal = simulator.board.getBarrierWidth();
    	JLabel bwLabel = newLabel("Brush Size", "adjust the thickness of the barrier to be drawn.");
    	ChangeListener bwListen = new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int bw = ((JSlider) e.getSource()).getValue();
        		simulator.board.setBarrierWidth(bw);
        		bwLabel.setText("Brush Size: " + simulator.board.getBarrierWidth());
        	}
        };
        JPanel bwSlider = newSlider(bwLabel, 1, ((int) simulator.getBoardSize()/20), bwVal, bwListen);
        c.gridy = 0;
        tools.add(bwSlider,c);
        
        //All of these components exist inside a task panel within the gridBag
        JXTaskPaneContainer gridColorComponents = new JXTaskPaneContainer();
        gridColorComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane gridColorTools = newScroll("Barrier Color Settings","Adjusts the color and visibility of barriers.");
        
        // -- COLOR - Change Barrier Color
        JButton bColor = newButton("Set Color", "Change Barrier Color");
        bColor.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e)
        	{
        		Color newColor = JColorChooser.showDialog(
                        bColor,
                        "Choose Barrier Color",
                        Color.GRAY);
        		simulator.setBarrierColor(newColor);
        	}
        });
        gridColorTools.add(bColor);
        
        // -- REPAINT OLD BARRIERS - Paints existing barriers with the new set color.
        JButton reColor = newButton("Repaint Barriers", "Paints existing barriers with the new set color.");
        reColor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		simulator.setAllBarrierColor(simulator.getBarrierColor());
        	}
        });
        gridColorTools.add(reColor);
        
        // -- INVISIBLE - Toggles visibility of barriers
        JButton inv = newButton("Hide Barriers", "Toggles visibility of barriers");
        inv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (barrierVis) {
                    simulator.setAllBarrierColor(Color.black);
                    barrierVis = false;
                    inv.setText("Show Barriers");
                } else {
                    simulator.setAllBarrierColor(simulator.getBarrierColor());
                    barrierVis = true;
                    inv.setText("Hide Barriers");
                }
            }
        });
        gridColorTools.add(inv);
        
        //add grid tools to larger panel
        gridColorTools.setCollapsed(true);
        c.gridy++;
        tools.add(gridColorTools, c);
        
        //the following buttons are beveled, and should stay indented if selected
        Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        JButton eraseRect = newButton("Erase", "Erases a barriers based on a line from mouse position.");
        JButton dragLine = newButton("Line","Drags a line from mouse position");
        JButton dropPoint = newButton("Stamp", "Drops a stamp barrier at mouse click");
        JButton dragRect = newButton("Rectangle", "creates a rectangular barrier on mouse drag");
        JButton dragGraph = newButton("Grid", "Creates a rectangle of square barriers in a grid formation from mouse click to mouse release");
        
        JButton[] drawButtons = {dragLine,dropPoint,dragRect,dragGraph, eraseRect};
        
        
        //Visually group the toggled utencil tools together
        GridBagConstraints dc = new GridBagConstraints();
        dc.fill = GridBagConstraints.HORIZONTAL;
        dc.weightx = 1;
        JPanel utencilPadding = new JPanel(new GridBagLayout());
        utencilPadding.setBorder(lowered);
        
        // - ERASE - Erases a barriers based on a line from mouse position.
        eraseRect.setBorder(raised);
        eraseRect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mainFrame.setListener('e');
                toggleUtencil(4, drawButtons);
            }
        });
        JPanel erasePadding = new JPanel(new GridBagLayout());
        erasePadding.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        dc.insets = new Insets(5,5,5,5);
        erasePadding.add(eraseRect,dc);
        dc.insets = new Insets(10,10,1,10);
        utencilPadding.add(erasePadding,dc);
        
        // - LINE - Drags a line from mouse position
        dragLine.setBorder(raised);
        dragLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.setListener('l');
                toggleUtencil(0, drawButtons);
            }
        });
        dc.insets = new Insets(1,10,1,10);
        dc.gridy=1;
        utencilPadding.add(dragLine,dc);
        
        // - STAMP - Drops a stamp barrier at mouse click TODO: add other stamp shapes
        dropPoint.setBorder(raised);
        dropPoint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mainFrame.setListener('p');
                toggleUtencil(1, drawButtons);
            }
        });
        dc.gridy=2;
        utencilPadding.add(dropPoint,dc);
        
        // - RECTANGLE - creates a rectangular barrier on mouse drag
        dragRect.setBorder(raised);
        dragRect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mainFrame.setListener('r');
                toggleUtencil(2, drawButtons);
            }
        });
        dc.gridy=3;
        utencilPadding.add(dragRect,dc);
        
        // - GRAPH - Drags a "screen" of points in a rectangle based on a line from mouse position
        dragGraph.setBorder(raised);
        dragGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mainFrame.setListener('g');
                toggleUtencil(3, drawButtons);
            }
        });
        dc.insets = new Insets(1,10,10,10);
        dc.gridy=4;
        utencilPadding.add(dragGraph,dc);
        
        //and add this submenue into tools as well.
        c.gridy++;
        tools.add(utencilPadding, c);
        
        //Yet ANOTHER subMenu - All of these components exist inside a task panel within the gridBag
        JXTaskPaneContainer gridComponents = new JXTaskPaneContainer();
        gridComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane gridTools = newScroll("Grid Settings","Adjusts the spacing and alignment of square barriers within a grid.");
        
        // -- X SPACE - "Adjusts the horizontal space between square barriers in each row of a grid."
        int xsVal = mainFrame.xSpace;
        JLabel xsLabel = newLabel("X Space","Adjusts the horizontal space between square barriers in each row of a grid.");
        ChangeListener xsListen = new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int xsp = ((JSlider) e.getSource()).getValue();
        		mainFrame.xSpace = xsp;
        		xsLabel.setText("X Space: "+xsp);
        	}
        };
        JPanel xsSlider = newSlider(xsLabel, 0, 10, xsVal, xsListen);
        gridTools.add(xsSlider);
        
        // -- X OFFSET - "Adjusts the vertical alignment of each row in a grid."
        int xoVal = mainFrame.xOff;
        JLabel xoLabel = newLabel("X Offset", "Adjusts the vertical alignment of each row in a grid.");
        ChangeListener xoListen = new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int xof = ((JSlider) e.getSource()).getValue();
        		mainFrame.xOff = xof;
        		xoLabel.setText("X Offset: "+xof);
        	}
        };
        JPanel xoSlider = newSlider(xoLabel, 0, 10, xoVal, xoListen);
        gridTools.add(xoSlider);
        
        // -- Y SPACE - "Adjusts the vertical space between square barriers in each column of a grid."
        int ysVal = mainFrame.ySpace;
        JLabel ysLabel= newLabel("Y Space", "Adjusts the vertical space between square barriers in each column of a grid.");
        ChangeListener ysListen = new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int ysp = ((JSlider) e.getSource()).getValue();
        		mainFrame.ySpace = ysp;
        		ysLabel.setText("Y Space: "+ysp);
        	}
        };
        JPanel ysSlider = newSlider(ysLabel, 0, 10, ysVal, ysListen);
        gridTools.add(ysSlider);
        
        // -- Y OFFSET - "Adjusts the horizontal alignment of each column in a grid."
        int yoVal = mainFrame.yOff;
        JLabel yoLabel = newLabel("Y Offset", "Adjusts the horizontal alignment of each column in a grid.");
        ChangeListener yoListen = new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int yof = ((JSlider) e.getSource()).getValue();
        		mainFrame.xOff = yof;
        		yoLabel.setText("Y Offset: "+yof);
        	}
        };
        JPanel yoSlider = newSlider(yoLabel, 0, 10, yoVal, yoListen);
        gridTools.add(yoSlider);
        
      //add grid tools to larger panel
        gridTools.setCollapsed(true);
        c.gridy++;
        tools.add(gridTools, c);
        
    	//Finishing Up
        tools.setCollapsed(true);
    	return tools;
    }
    
    
    //This helper is literally JUST for readability. Come down here to edit the zoom components!!
    public JXTaskPane packZoom(JXTaskPane tools)
    {
    	GridBagConstraints c = new GridBagConstraints();
    	
    	JButton[] zoomModes = new JButton[3];

        // Dark Mode
        JCheckBox darkMode = new JCheckBox("Dark Mode",true);
        darkMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int width = GUI.getWindowSize().width-boardSize-70;
                if(!isFullscreen){
                    width = GUI.getWindowSize().width-boardSize-70;
                }else{
                    width = GUI.getScreenSize().width-boardSize-150;
                }

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    GUI.background_color = GUI.themeColors[0];
		            GUI.panel_primary = GUI.themeColors[1];
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    GUI.background_color = null;
		            GUI.panel_primary = null;
                }
                simulator.board.setBackground(GUI.background_color);
                controlPanel.setBackground(GUI.background_color);
                toggleFullscreen(width);
            }
        });
        c.gridy=0;
        tools.add(darkMode);

    	
    	//Fullscreen vs Windowed
    	JButton fullscreen = newButton("Fullscreen", "Toggles between fullscreen and windowed views.");
    	fullscreen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String fsName = null;
        		if(isFullscreen == true)
        		{
        			//make it windowed
        			mainFrame.setSize(GUI.getWindowSize());
        			mainFrame.setLocationRelativeTo(null);
        			fsName = "Fullscreen";
        			isFullscreen = false;
        			
                    toggleFullscreen(GUI.getWindowSize().width-boardSize-70);
        		}else
        		{
        			//make it fullscreen
        			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        			fsName = "Windowed";
        			isFullscreen = true;

                    toggleFullscreen(GUI.getScreenSize().width-boardSize - 150);
        		}
        		fullscreen.setText(fsName);		
        	}
        });
    	c.gridy++;
        tools.add(fullscreen);
        
        //DISPLAY SIZE - "Adjusts the scale of the simulation. DOES NOT CHANGE BOARD SIZE."
        //scale = 1;
        int mx = 10000;
        int scl = (int) scale * mx;
        
        JLabel dispLabel = newLabel("Scale", "Adjusts the scale of the simulation. DOES NOT CHANGE BOARD SIZE.");
        ChangeListener dispListen = new ChangeListener(){
        	public void stateChanged(ChangeEvent e) {
        		int sc = ((JSlider) e.getSource()).getValue();
        		double s = (double)sc /( mx);
        		dispLabel.setText("Scale: "+(double)sc /mx);
                zoom100();
                toggleZoom(0, zoomModes);
        		simulator.board.setScale(scale * s);
				simulator.board.revalidate();
        	}
        };
        JPanel dispSlider = newSlider(dispLabel, 1, mx, scl, dispListen);
        c.gridy++;
        tools.add(dispSlider, c);
        
        //100% - returns to normal scale and zoom
        JButton zoomReturn = newButton("100%", "Returns to full scale view.");
        zoomReturn.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomReturn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoom100();
                toggleZoom(0, zoomModes);
            }
        });
        JPanel zoomReturnBox = new JPanel(new GridBagLayout());
        zoomReturnBox.setBorder(BorderFactory.createEtchedBorder());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10,8,10,8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.gridwidth = 2;
        zoomReturnBox.add(zoomReturn,gc);
        zoomModes[0] = zoomReturn;

        //Back button used to toggle back to the previous zoom selection
        JButton zoomRecall = newButton("<- Previous Zoom", "Returns to the previous zoom selection");
        zoomRecall.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomRecall.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                mainFrame.sim.recallZoom(1);
            }
        });
        gc.gridy++;
        gc.gridwidth = 1;
        gc.insets = new Insets(0,8,10,3);
        zoomReturnBox.add(zoomRecall, gc);

        //forward button used to toggle back to the previous zoom selection
        JButton zoomRecallNext = newButton("Next Zoom ->", "Goes to the previous previous zoom selection");
        zoomRecallNext.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomRecallNext.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                mainFrame.sim.recallZoom(-1);
            }
        });

        gc.insets = new Insets(0,3,10,8);
        zoomReturnBox.add(zoomRecallNext, gc);
        c.gridy++;
        tools.add(zoomReturnBox, c);
        
        //ZOOM MODES submenu - All of these components exist inside a task panel within the gridBag
        JXTaskPaneContainer selectZoomComponents = new JXTaskPaneContainer();
        selectZoomComponents.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
        JXTaskPane selectZoomTools = newScroll("Zoom Selection Tools","Select an area to focus on.");
        selectZoomTools.setCollapsed(true);
        
        // -- SQUARE SELECT - "Zooms in on the square created from mouse click to mouse release."
        JButton sqZoom = newButton("Square Zoom", "Zooms in on the square created from mouse click to mouse release.");
        sqZoom.setBorder(BorderFactory.createRaisedBevelBorder());
        sqZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	mainFrame.setListener('z');
            	toggleZoom(1, zoomModes);
            }
        });
        selectZoomTools.add(sqZoom);
        zoomModes[1] = sqZoom;
        
        //adding submenu
        c.gridy++;
        tools.add(selectZoomTools);
    	
    	//Finishing Up
        tools.setCollapsed(true);
    	return tools;
    }
    
    //
    //HELPERS
    //
    public void toggleFullscreen(int width)
    {
        //re -add components to extended frame
        JPanel all = new JPanel();
        all.setLayout(new GridBagLayout());
        all.setBackground(GUI.background_color);
        GridBagConstraints newGbc = new GridBagConstraints();
        
        //tools
        //remove and readd tools from control panel
        controlPanel.remove(scrollTools);
        tools.setBackground(GUI.panel_primary);
        scrollTools.setPreferredSize(new Dimension(width, boardSize - 100));
        newGbc.fill = GridBagConstraints.HORIZONTAL;
        newGbc.insets = new Insets(0,5,0,5);
        newGbc.gridy = 1;
        controlPanel.add(scrollTools, gbc);
        controlPanel.revalidate();

        //Not sure what this inset was doing, but on a small screen it breaks fullscreen.
        //int left = Math.round(sizeConstraint/7);
    	//int top = Math.round(sizeConstraint/90);
    	//newGbc.insets = new Insets(top,left,0,5);
        newGbc.insets = new Insets(0,5,0,5);
    	newGbc.weighty = 0;
    	newGbc.gridy = 0;
        newGbc.gridx=0;
        newGbc.gridwidth=2;
        newGbc.weightx=0.4;
        newGbc.ipadx = 
        newGbc.fill = GridBagConstraints.BOTH;
    	newGbc.anchor = GridBagConstraints.CENTER;
        all.add(controlPanel, newGbc);

        //baord
    	newGbc.gridx = 2;
    	newGbc.gridheight = 3;
    	newGbc.gridwidth = 3;
        newGbc.weightx=0.2;
        all.add(simulator.board, newGbc);

        //repaint
        mainFrame.setContentPane(all);
    	mainFrame.revalidate();
    	mainFrame.repaint();
    }

    public void zoom100(){
        mainFrame.allFalse();
        mainFrame.sim.storeZoom(new Point(0,0), new Point(boardSize,boardSize));
    	mainFrame.sim.board.setScale(scale);
        mainFrame.sim.board.setOrigin(0,0);
    	mainFrame.sim.board.setZoomed(false);
        controlPanel.revalidate();
		controlPanel.repaint();
    }
    
    //NEW COMPONENT HELPERS:
    //----------------------
    
    //Adds a component to the control panel!!
    //Default grid bag only! don't use if you want custom constraint values!!
    private void addTool(Component c, int y)
    {
    	GridBagConstraints tempGBC = new GridBagConstraints();
    	//reset gridbag
    	tempGBC.weighty = 0;
        tempGBC.weightx=0.8;
    	tempGBC.gridheight = 1;
    	tempGBC.gridwidth = 1;
    	tempGBC.fill = GridBagConstraints.BOTH;
    	tempGBC.insets = new Insets(5,15,5,15);
    	tempGBC.gridy = y;
    	tools.add(c,tempGBC);
    }
    
    //Same as above! use only for listed components! not custom values!
    private void addControl(Component c, int y)
    {
    	GridBagConstraints tempGBC = new GridBagConstraints();
    	tempGBC.fill = GridBagConstraints.HORIZONTAL;
    	tempGBC.insets = new Insets(12,5,12,5);
    	tempGBC.gridy = y;
    	controlPanel.add(c,tempGBC);
    }
    
    //Same as above! use only for listed components! not custom values!
    private void addReset(Component c, int y)
    {
    	GridBagConstraints tempGBC = new GridBagConstraints();
    	tempGBC.gridy = y;
    	resetTools.add(c,tempGBC);
    }
    
    //itty bitty time saver
    private JXTaskPane newScroll(String name, String tip)
    {
    	JXTaskPane hold = new JXTaskPane();
    	hold.setTitle(name);
    	hold.setToolTipText(tip);
    	return hold;
    }
    
    //nifty time saver.
    private JButton newButton(String name, String tip)
    {
    	JButton butt = new JButton(name);
    	butt.setToolTipText(tip);
    	return butt;
    }
    
    //another nifty time saver. Could be used IN the argument for newSlider :3
    private JLabel newLabel(String name, String tip)
    {
    	JLabel label = new JLabel(name);
    	label.setToolTipText(tip);
    	return label;
    }
    
    //returns a JPanel with a nicely formatted label and slider. :)
    private JPanel newSlider(JLabel label, int min, int max, int value, ChangeListener l)
    {
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridy=0;
    	JPanel hold = new JPanel();
    	hold.setLayout(new GridBagLayout());
    	label.setText(label.getText() + ": "+value);
    	hold.add(label,c);
    	JSlider slider = new JSlider(min, max, min);
    	slider.setName(label.getText().split(":", 2)[0]);
    	slider.setToolTipText(label.getToolTipText());
    	slider.setValue(value);
    	slider.addChangeListener(l);
    	c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=1;
    	hold.add(slider, c);
    	return hold;
    }
    
    private JPanel copySlider(JPanel panel)
    {
    	
    	//initial values for new slider and label
    	String text = ""; 
    	String tip = "";
    	String name = "";
    	int min = 0; int max = 0; int value = 0;
    	ChangeListener l = new ChangeListener() {public void stateChanged(ChangeEvent e) {}};

    	//set the values based on the components of the JPanel
    	Component[] stuff = panel.getComponents();
    	for (Component field : stuff)
    	{
    		if(field instanceof JLabel)
    		{
    			text = ((JLabel) field).getText();
    			tip = ((JLabel) field).getToolTipText();
    		}
    		else if(field instanceof JSlider)
    		{
    			name = ((JSlider) field).getName();
    			min = ((JSlider) field).getMinimum();
    			max = ((JSlider) field).getMaximum();
    			value = ((JSlider) field).getValue();
    			l = ((JSlider) field).getChangeListeners()[0];
    		}
    	}
    	
    	//init new component
    	JPanel hold = new JPanel();
    	hold.setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.gridy=0;

    	//set values.
    	JLabel label = new JLabel();
    	label.setText(text);
    	label.setToolTipText(tip);
    	hold.add(label,c);
    	
    	JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
    	slider.setName(name);
    	slider.setToolTipText(tip);
    	slider.setValue(value);
    	slider.addChangeListener(l);
    	slider.addChangeListener(new ChangeListener(){
    		public void stateChanged(ChangeEvent e)
    		{
    			label.setText(slider.getName()+": "+slider.getValue());
    			setMainSlider(slider.getName(), slider.getValue());
    		}
    			});
    	c.gridy++;
        c.weightx=1;
    	hold.add(slider,c);
    	return hold;
    }
    
    //MISC HELPERS
    //-------------------------------------
    
    //called on reset to update new INITIAL sliders to reflect updated reset values
    public static void setMainSliders()
    {
    	//make sure the new window reflects the correct slider values. 
		setMainSlider("Sleep Cost", sleepCost);
		setMainSlider("Move Cost", moveCost);
		setMainSlider("Turn Cost", turnCost);
		setMainSlider("Food Rate", foodRate);
		setMainSlider("Food Value", foodVal);
		setMainSlider("Sight Range", sightRange);
		setMainSlider("Mutation Rate", (int)mutation * 100);
		setMainSlider("Color Variance", (int)colorVar * 100);
    }
    
    //used by reset menu action listeners to update the corresponding slider on the main control panel - kinda useless. kinda cools to see.
    public static void setMainSlider(String name, int value)
    {
    	Component[] main = tools.getComponents();
    	
    	for (Component container : main)
    	{
    		Component[] taskContainer = ((JXTaskPaneContainer) container).getComponents();
    		JXTaskPane pane = (JXTaskPane) taskContainer[0];
    		String title = ((JXTaskPane) pane).getTitle();
    		if(title == "Environment Tools" || title == "Behavior Tools" || title == "Genetics Tools")
    		{
    			Component[] taskTools = pane.getContentPane().getComponents();
    			for(Component t : taskTools)
    			{
    				if (t instanceof JPanel)
    				{
    					Component[] sliders = ((JPanel) t).getComponents();
    					for (Component s : sliders)
    					{
    						if(s instanceof JSlider && s.getName() == name)
    						{
    							((JSlider) s).setValue(value);
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    //sets all the reset menu slider values to given args - used by the preset dropdpwn
    //r is JUST THE DENOMINATOR of the food rate fraction. where the numerator is the boardSize squared. 
    public void setResetSliders(int sc, int mc, int tc, int r, int fv, int sr, double mt, double cv, int nbs)
    {
    	//Grab ALL panels we need to find sliders in.
    	Component[] reset = resetTools.getComponents();
    	
    	
    	for(Component comp : reset)
    	{
    		if(comp instanceof JPanel)
    		{
    			Component subComps[] = ((JPanel) comp).getComponents();
    			
    			Component l = subComps[0];
    			Component c = subComps[1];
    			if(c instanceof JSlider)
        		{
        			switch(c.getName())
        			{
        			case "Board Size": 
        				((JSlider) c).setValue(nbs); 
        				((JLabel) l).setText("Board Size: "+ nbs);
        				break;
        			case "Food Value": 
        				((JSlider) c).setValue(fv);
        				((JLabel) l).setText("Food Value: " + fv);
        				break;
        			case "Food Rate": 
        				((JSlider) c).setValue((nbs * nbs)/r);
        				((JLabel) l).setText("Food Rate: "+ (nbs*nbs)/r);
        				break;
        			case "Sight Range": 
        				((JSlider) c).setValue(sr);
        				((JLabel) l).setText("Sight Range: "+ sr);
        				break;
        			case "Sleep Cost": 
        				((JSlider) c).setValue(sc);
        				((JLabel) l).setText("Sleep cost: "+ sc);
        				break;
        			case "Move Cost": 
        				((JSlider) c).setValue(mc);
        				((JLabel) l).setText("Move Cost: "+mc);
        				break;
        			case "Turn Cost": 
        				((JSlider) c).setValue(tc);
        				((JLabel) l).setText("Turn Cost: "+tc);
        				break;
        			case "Mutation Rate": 
        				((JSlider) c).setValue((int)mt*100);
        				((JLabel) l).setText("Mutation Rate: "+(int)mt*100);
        				break;
        			case "Color Variance": 
        				((JSlider) c).setValue((int)cv*100);
        				((JLabel) l).setText("Color Variance: "+ (int)cv*100);
        				break;
        			}
        		}
    		}
    	}
    }
    
    //helper function used by the drawing tools . also Butts xD.
    public void toggleUtencil(int index, JButton[] butt)
    {
    	Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        JButton selected = butt[index];
        if(selected.getBorder() == raised)
        {
        	selected.setBorder(lowered);
            for(int i = 0; i < butt.length; i++)
            {
            	if(i != index) {butt[i].setBorder(raised);}
            }
        }else
        {
        	selected.setBorder(raised);
        	mainFrame.allFalse();
        }
    }
    
    //helper function used by zoom tools.
    public void toggleZoom(int index, JButton[] butt)
    {
    	Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        JButton selected = butt[index];
        if(selected.getBorder() == raised)
        {
        	selected.setBorder(lowered);
            for(int i = 0; i < butt.length; i++)
            {
            	if(i != index) {butt[i].setBorder(raised);}
            }
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
}


