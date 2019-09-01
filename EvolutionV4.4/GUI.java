import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Scanner;

public class GUI     {
   private JFrame mainFrame;
   private JPanel controlPanel;
   private static Simulator simulator;
   private int sleepCost;
   private int moveCost;
   private int turnCost;
   private int speed;
   private double mutation;
   private int foodVal;
   private int foodRate;
   private double colorVar;
   private int sightRange;
   private static int startsC=0;
   private static int startmC=3;
   private static int starttC=1;
   private int startSpeed = 10;
   private double startMutate = 0.2;
   private static int boardSize = 160;
   private int startfVal = boardSize/2;
   private int startfRate = boardSize/10;
   private boolean run = true;
   
   public GUI(){
      prepareGUI();
   }
   public Simulator getSimulator()
   {
       return simulator;
   }
   
   public static void main(String[] args){
      prompt();
      int refreshRate = 2;
      int i = 0;
      int j = 0;
     
       while (i < boardSize / 2 )
      {
          //j = (i % 9) / 3;
          j = 0;
          while (j < boardSize)
          {
              simulator.addBarrier(new Point(i, j));
              j += 3;
          }
          
          i += 3;
       }
      
      
      while (true)
      {
          simulator.gameTimeStep();
      }
      
      
      /* while (true)
      {
          if(refreshRate>0){
          simulator.gameTimeStep();
          refreshRate--;
        }else{
            refreshRate = 2;
        }
      } */
   }
   
   //method to prompt the user for initial values
   private static void prompt()
   {
       int s, sc,mc,tc;
       try
        { 
            Scanner obj = new Scanner(System.in);
            // This line of code throws NullPointerException when its not run from a console. 
       //System.out.flush();
       System.console().printf("--------------------------- ------------------------------");
       System.out.println("");
       System.out.println("");
       System.out.println("");
       System.out.println("Please enter a size for the simulator between 24 and 318: ");
       s = obj.nextInt();
       System.out.println("Starting number of Critters = " + s/10);
       System.out.println("Starting Food Value = " + s/3);
       System.out.println("Starting Food Rate = " + s/10);
       System.out.println("Please enter the costs for the following initial starting values.");
       System.out.println("Movement Cost: ");
       mc = obj.nextInt();
       System.out.println("Turning Cost: ");
       tc = obj.nextInt();
       System.out.println("Sleeping Cost: ");
       sc = obj.nextInt();
       System.out.println("Enjoy!");  
       //System.out.flush();
       System.out.println(" ");
       System.out.println(" ");
       System.out.println("--------------------------- ------------------------------ ------------");
       System.out.println(" ");
       System.out.println(" ");
       //obj.close();
       restart(sc,mc,tc,s);
        } 
        catch(NullPointerException e) 
        { 
            restart(0,3,1,160);
            System.out.print("NullPointerException Caught"); 
        } 
       
    }
   //method to quit the window and start again
   //takes(sleep Cost, movement Cost, turn cost, board size)
  private static void restart(int sc, int mc, int tc, int bs)
   {
      GUI gui = new GUI();
      gui.showGUI(sc,mc,tc,bs);
      simulator.gameTimeStep(bs);
      int startingPopulation = (bs /10);
      simulator.addCritter("M", startingPopulation);
   }
   public void prepareGUI(){
      mainFrame = new JFrame("Worm Evolution");
      mainFrame.setSize(1200,1200);
      //mainFrame.setLayout(new FlowLayout());
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      
      //mainFrame.add(controlPanel);
      //mainFrame.setVisible(true);  
   }
   private void showGUI(int sleepC, int moveC, int turnC, int bs){
       sleepCost = sleepC;
       moveCost = moveC;
       turnCost = turnC;
       colorVar = 0.5;
      controlPanel = new JPanel();
      controlPanel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      JButton reset = new JButton("Reset");
      JSlider speedSlider = new JSlider(JSlider.VERTICAL, 0,100,20);
      JSlider mutationSlider = new JSlider(JSlider.VERTICAL, 0,100,20);
      JSlider foodValueSlider = new JSlider(JSlider.VERTICAL, 0, 160, bs/2);
      JSlider foodRateSlider = new JSlider(JSlider.VERTICAL, 0, 30, (bs/10));
      JSlider sleepCostSlider = new JSlider(JSlider.VERTICAL, 0,10,sleepC);
      JSlider moveCostSlider = new JSlider(JSlider.VERTICAL, 0,10,moveC);
      JSlider turnCostSlider = new JSlider(JSlider.VERTICAL, 0,10,turnC);
      JSlider colorVarSlider = new JSlider(JSlider.VERTICAL, 0, 100, 50);
      JSlider sightRangeSlider = new JSlider(JSlider.VERTICAL, 0, 20, 10);
      JLabel spd = new JLabel("Tick Speed  ");
      JLabel mut8 = new JLabel("Mutation Rate  ");
      JLabel fVal = new JLabel("Food Value  ");
      JLabel fR8 = new JLabel("    Food Rate ");
      JLabel sc = new JLabel("Sleep Cost   ");
      JLabel mc = new JLabel(" Move Cost ");
      JLabel tc = new JLabel("Turn Cost  ");
      JLabel cv = new JLabel("Color Variance    ");
      JLabel sr = new JLabel("Sight Range");
      JLabel cSpeed = new JLabel(" x");
      JLabel cMutate = new JLabel(" x");
      JLabel cFval = new JLabel(" x");
      JLabel cFrate = new JLabel(" x");
      
      int cs;
      JButton drawLine = new JButton("Draw Line");
      JButton drawPoint = new JButton("Draw Point");
      JTextField x1 = new JTextField("x1");
      JTextField x2 = new JTextField("x2");
      JTextField y1 = new JTextField("y1");
      JTextField y2 = new JTextField("y2");
      
      JButton play = new JButton("Stop");
      
      reset.addActionListener(new ActionListener(){
          public void actionPerformed (ActionEvent e){
              mainFrame.setVisible(false);
              mainFrame.dispose();
              prompt();
            }
        });
      speedSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int spd = ((JSlider)e.getSource()).getValue();
            speed = (int)(100 * Math.exp(-spd * 2 / 43));
                simulator.setSpeed(speed);
            
         }
         
      });
      mutationSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int mutate = ((JSlider)e.getSource()).getValue();
            mutation = mutate/100.0;
                simulator.setMutationRate(mutation);
            
         }
         
      });
            foodValueSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int foodV = ((JSlider)e.getSource()).getValue();
            foodVal = foodV;
                simulator.setFoodValue(foodVal);
            
         }
         
      });
      
            foodRateSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int foodR= ((JSlider)e.getSource()).getValue();
            foodRate = foodR;
                simulator.setFoodRate(foodRate);
            
         }
         
      });
      sleepCostSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int sCost = ((JSlider)e.getSource()).getValue();
            sleepCost = sCost;
                simulator.setSleepCost(sleepCost);
            
         }
         
      });
      moveCostSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int mCost = ((JSlider)e.getSource()).getValue();
            moveCost = mCost;
                simulator.setMoveCost(moveCost);
         }
         
      });
      turnCostSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int tCost = ((JSlider)e.getSource()).getValue();
            turnCost = tCost;
                simulator.setTurnCost(turnCost);
            
         }
         
      });   
      colorVarSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int cVar = ((JSlider)e.getSource()).getValue();
           colorVar = cVar / 100.0;
                simulator.setColorVar(colorVar);
            
         }
         
      });
       sightRangeSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            int sightR = ((JSlider)e.getSource()).getValue();
           sightRange = sightR;
                simulator.setSightRange(sightR);
            
         }
         
      });
      
      drawLine.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            simulator.addBarrierLine(Integer.parseInt(x1.getText()),Integer.parseInt(y1.getText()),Integer.parseInt(x2.getText()),Integer.parseInt(y2.getText()));
        }  
      });
      
      drawPoint.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            simulator.addBarrier(new Point(Integer.parseInt(x1.getText()),Integer.parseInt(y1.getText())));
        }  
      }); 
    
      play.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            if (!simulator.isPaused())
            {
                simulator.pause();
                play.setText("Start");
            }
            else
            {
                simulator.unpause();
                play.setText("Stop");
            }
        }  
      });
      
      cSpeed = new JLabel(" "+speed);
      cMutate = new JLabel(" "+mutation);
      cFval = new JLabel(" "+foodVal);
      cFrate = new JLabel (" "+foodRate);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridx = 0;
      gbc.gridy = 0;
      //controlPanel.add(cSpeed,gbc);
      gbc.gridx = 1;
      gbc.gridy = 0;
      //controlPanel.add(cMutate,gbc);
      controlPanel.add(reset, gbc);
      gbc.gridx = 2;
      gbc.gridy = 0;
      //controlPanel.add(cFval,gbc);
      gbc.gridx = 3;
      gbc.gridy = 0;
      //controlPanel.add(cFrate,gbc);
      gbc.gridx = 0;
      gbc.gridy = 1;
      controlPanel.add(speedSlider,gbc);
      gbc.gridx = 1;
      gbc.gridy = 1;
      controlPanel.add(mutationSlider,gbc);
      gbc.gridx = 2;
      gbc.gridy = 1;
      controlPanel.add(foodValueSlider,gbc);
      gbc.gridx = 3;
      gbc.gridy = 1;
      controlPanel.add(foodRateSlider,gbc);
      gbc.gridx = 0;
      gbc.gridy = 2;
      controlPanel.add(spd,gbc);
      gbc.gridx = 1;
      gbc.gridy = 2;
      controlPanel.add(mut8,gbc);
      gbc.gridx = 2;
      gbc.gridy = 2;
      controlPanel.add(fVal,gbc);
      gbc.gridx = 3;
      gbc.gridy = 2;
      controlPanel.add(fR8,gbc);
      gbc.gridx = 0;
      gbc.gridy = 3;
      controlPanel.add(sleepCostSlider,gbc);
      gbc.gridx = 1;
      gbc.gridy = 3;
      controlPanel.add(moveCostSlider,gbc);
      gbc.gridx = 2;
      gbc.gridy = 3;
      controlPanel.add(turnCostSlider,gbc);
      gbc.gridx = 3;
      gbc.gridy = 3;
      controlPanel.add(colorVarSlider,gbc);
      gbc.gridx = 4;
      gbc.gridy = 3;
      controlPanel.add(sightRangeSlider,gbc);
      gbc.gridx = 0;
      gbc.gridy = 4;
      controlPanel.add(sc,gbc);
      gbc.gridx = 1;
      gbc.gridy = 4;
      controlPanel.add(mc,gbc);
      gbc.gridx = 2;
      gbc.gridy = 4;
      controlPanel.add(tc,gbc);
      gbc.gridx = 3;
      gbc.gridy = 4;
      controlPanel.add(cv,gbc);
      gbc.gridx = 4;
      gbc.gridy = 4;
      controlPanel.add(sr,gbc);
      gbc.gridx = 0;
      gbc.gridy = 5;
      controlPanel.add(x1,gbc);
      gbc.gridx = 1;
      gbc.gridy = 5;
      controlPanel.add(y1,gbc);
      gbc.gridx = 0;
      gbc.gridy = 6;
      controlPanel.add(x2,gbc);
      gbc.gridx = 1;
      gbc.gridy = 6;
      controlPanel.add(y2,gbc);
      gbc.gridx = 2;
      gbc.gridy = 6;
      controlPanel.add(drawLine,gbc);
      gbc.gridx = 2;
      gbc.gridy = 5;
      controlPanel.add(drawPoint,gbc);
      gbc.gridx = 3;
      gbc.gridy = 0;
      controlPanel.add(play,gbc);
      gbc.gridx = 6;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.VERTICAL;
      gbc.gridheight = 6;
      simulator = new Simulator(bs, sleepCost, moveCost, turnCost);
      controlPanel.add(simulator.board,gbc);
      mainFrame.setContentPane(controlPanel);
      mainFrame.setVisible(true); 
   } 
}