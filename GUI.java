/**
 * Basically just a constructor/interface to glue all the different components together.
 * Gets Everything Going!!
 *
 * @author Sean Thornton and Sky Vercauteren
 * @version 1.0 December 2023
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

public class GUI {
	
	//Panels and frames
	private static Dimension screenSize;
	private static Dimension windowSize = new Dimension(1200,1100);
	private static Frame mainFrame = new Frame("Worm Evolution");
	private static JPanel all = new JPanel();
	
	//Board Sizes
	private static int sizeConstraint;
	private static int boardSize;
    private static double scale = 1;
    
    
    //Build a simulator
    private static Simulator simulator;
    

    
    //MAIN
    //Finds default starting information. Starts the tick loop.
    public static void main(String[] args) {
    	
    	//find board size by finding the smallest dimension, and extrapolate size information
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	screenSize = new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
    	sizeConstraint = (screenSize.width > screenSize.height ? screenSize.height : screenSize.width);
    	sizeConstraint = (sizeConstraint - (int)(sizeConstraint * 0.06)); //regardless of screen size, every board and frame needs to be padded.
    	boardSize = getSizeConstraint();
    	
    	//instantiate simulator
    	simulator = new Simulator(boardSize, sizeConstraint, 0, 3, 1); //DEFAULT VALUES FOR SleepCost, MoveCost, TurnCost.
    	simulator.board.setPreferredSize(new Dimension(boardSize, boardSize));
    	
    	//instantiate control panel.
    	Controls controls = new Controls(simulator, mainFrame, boardSize);
    	
    	//instantiate main window
    	windowSize.width = boardSize + 15 + (boardSize/3);
    	windowSize.height = (int)(boardSize*0.06) + boardSize;
        mainFrame.setSize(windowSize);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.xSpace = 1; mainFrame.ySpace = 1;
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        //pack and render the main window
        all.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //CONTROLS
        gbc.fill = GridBagConstraints.HORIZONTAL;	
        gbc.insets = new Insets(0,5,0,5);
        JPanel tools = controls.getControls();
        all.add(tools,gbc);
        //SIMULATOR - The big enchilada
        gbc.gridx = 1;
        gbc.insets = new Insets(0,5,5,0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridheight = 3;
        gbc.gridwidth = 3;
        all.add(simulator.board, gbc);
        
        start(boardSize);

        //Start the main loop!! :D
        while (true) {

            simulator.gameTimeStep();
        }
    }
    
    
    public static void start(int bs)
    {
    	//this is to let the board populate before creating worms so no one gets "unlucky"
        int startingPopulation = (boardSize/10);
        simulator.gameTimeStep(boardSize/3);
        simulator.addCritter("M", startingPopulation);
    	System.out.println("start");
    	mainFrame.sim = simulator;
        mainFrame.bs = bs;
        mainFrame.setContentPane(all);
        mainFrame.setVisible(true);
        mainFrame.pixelSize = simulator.pixelSize;
    }

	public static int getSizeConstraint() {
		return sizeConstraint;
	}
	public static void setSizeConstraint(int sizeConstraint) {
		GUI.sizeConstraint = sizeConstraint;
	}
	public static Dimension getWindowSize()
	{
		return windowSize;
	}
	public void setBoardSize(int size)
    {
    	boardSize = size;
    }
	public static int getBoardSize() {
		return boardSize;
	}
	public static Simulator getSimulator() {
        return simulator;
    }
	public static void setSimulator(Simulator sim)
	{
		simulator = sim;
	}
}
