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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class GUI {
	
	//Panels and frames
	private static Dimension screenSize;
	private static Dimension windowSize = new Dimension(1200,1100);
	private static Frame mainFrame;
	private static JPanel all;
	public static Color[] themeColors = {new Color(34, 46, 56), new Color(7, 3, 28), Color.decode("#131b33ff")};
	public static Color background_color;
	public static Color panel_primary;
	public static Color panel_secondary;
	
	//Board Sizes
	private static int sizeConstraint;
	private static int boardSize;
    private static double scale = 1;
    
    //Build a simulator
    private static Simulator simulator;
    
    //Build a control panel
    private static Controls controls;
    
    
    //MAIN
    //Finds default starting information. Starts the tick loop.
    public static void main(String[] args) {

		try {
            // Set the Look and Feel to the system's native L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace(); // Handle any exceptions that occur
        }
        
    	
    	//find board size by finding the smallest dimension, and extrapolate size information
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	screenSize = new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
    	sizeConstraint = (screenSize.width > screenSize.height ? screenSize.height : screenSize.width);
    	sizeConstraint = (sizeConstraint - (int)(sizeConstraint * 0.06)); //regardless of screen size, every board and frame needs to be padded.
    	boardSize = sizeConstraint;
    	
    	//build initial window frame
    	windowSize.width = boardSize + 15 + (boardSize/3);
    	windowSize.height = (int)(boardSize*0.06) + boardSize;

		//dark mode as default
		background_color = themeColors[0];
		panel_primary = themeColors[1]; 	

        //build sim and controls, pack and display!
        start(boardSize);
        
      //Start the main loop!! :D
        while (true) {

            simulator.gameTimeStep();
        }
    }
    
    //creates a simulator and a control panel based on given board size.
    //packs and displays the main frame.
    public static void start(int bs)
    {
    	Controls.setScale(1);
    	
    	//instantiate main window
    	mainFrame = new Frame("Worm Evolution");
        mainFrame.setSize(windowSize);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.xSpace = 1; mainFrame.ySpace = 1;
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    	
    	//instantiate simulator
    	simulator = new Simulator(bs, sizeConstraint, 0, 3, 1); //DEFAULT VALUES FOR SleepCost, MoveCost, TurnCost.
    	simulator.board.setPreferredSize(new Dimension(boardSize, boardSize));
    	mainFrame.sim = simulator;
    	mainFrame.pixelSize = simulator.pixelSize;
    	mainFrame.bs = bs;
    	Controls.setSimulator(simulator);
    	
    	//instantiate control panel.
    	controls = new Controls(simulator, mainFrame);

    	//pack simulator and controls together
    	all = new JPanel();
		all.setBackground(background_color);
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
        
        //Add everything to the frame -> display!
        mainFrame.setContentPane(all);
        mainFrame.setVisible(true);
        
    	//this is to let the board populate before creating worms so no one gets "unlucky"
        int startingPopulation = (bs/10);
        //simulator.gameTimeStep(boardSize/3);
        Controls.setMainSliders();
        simulator.addCritter("M", startingPopulation);
    }

	public static int getSizeConstraint() {
		return sizeConstraint;
	}
	public static Dimension getScreenSize()
	{
		return screenSize;
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
	
	//This is used to adjust the discrepancy between pixel sizes when the board is resized.
	//i.e. if the board is sized to 75%, We can't fit two pixels/square, so it's just a smaller board.
	//this should fix that.
    public static void autoscale(int newBoardSize)
	{
		scale = (double)sizeConstraint/(double)(newBoardSize * simulator.pixelSize);
		
		//leave a little room for the remainder.
		scale = scale - 0.005;
		
		Controls.setScale(scale);
		simulator.board.setScale(scale);
		simulator.board.revalidate();
		simulator.board.repaint();
	}
}
