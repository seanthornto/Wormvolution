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

public class GUI     {
   private Frame mainFrame;
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
   private boolean barrierVis = true;
   
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
      boardSize = simulator.getBoardSize();
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
       System.out.println("Please enter a size for the simulator between 50 and 900: ");
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
            restart(0,3,1,200);
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
      mainFrame = new Frame("Worm Evolution");
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
      JSlider foodValueSlider = new JSlider(JSlider.VERTICAL, 0, 300,150 );
      JSlider foodRateSlider = new JSlider(JSlider.VERTICAL, 0, bs, (bs/5));
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
      JTextField xSpace = new JTextField("x Space");
      JTextField xOff = new JTextField("x Offset");
      JTextField ySpace = new JTextField("y Space");
      JTextField yOff = new JTextField("y Offset");
      JButton dragLine = new JButton("Drag Line");
      JButton dropPoint = new JButton("Drop Point");
      JButton dragGraph = new JButton("Drag Grid");
      JButton eraseRect = new JButton("Erase");
      
      JTextField saveLoad = new JTextField("Save/Load");
      JButton play = new JButton("Stop");
      JButton save = new JButton("Save");
      JButton load = new JButton("Load");
      
      JButton toggleInv = new JButton("Make Barr Inv");
      
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
      
    
 
    
      play.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            if (!simulator.isPaused())
            {
                simulator.pause();
                play.setText("Start");
                run = true;
            }
            else
            {
                simulator.unpause();
                play.setText("Stop");
                run = false;
            }
        } 
      });
      
     /* save.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            save(saveLoad.getText());
        } 
      });
      
      load.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            load(saveLoad.getText());
        } 
      });*/
      toggleInv.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
              if (barrierVis)
              {
                  simulator.setBarrierColor(Color.black);
                  barrierVis = false;
                  toggleInv.setText("Make Barr Vis");
              }
              else
              {
                  simulator.setBarrierColor(Color.gray);
                  barrierVis = true;
                  toggleInv.setText("Make Barr Inv");
              }
        } 
      });
      
      dragLine.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            mainFrame.lTrue();
        }  
      });
      
      dropPoint.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
            mainFrame.pTrue();
            }
      }); 
      
      dragGraph.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
           mainFrame.gTrue();
           mainFrame.xSpace = Integer.parseInt(xSpace.getText());
           mainFrame.xOff = Integer.parseInt(xOff.getText());
           mainFrame.ySpace = Integer.parseInt(ySpace.getText());
           mainFrame.yOff = Integer.parseInt(yOff.getText());
            }
      });
      
      eraseRect.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent e){  
           mainFrame.eTrue();

            }
      });
      
      
      
      cSpeed = new JLabel(" "+speed);
      cMutate = new JLabel(" "+mutation);
      cFval = new JLabel(" "+foodVal);
      cFrate = new JLabel (" "+foodRate);
      int comp = -1;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridx = 0;
      gbc.gridy = 0;
      //controlPanel.add(cSpeed,gbc);
      gbc.gridx = 1;
      gbc.gridy = 0;
      //controlPanel.add(cMutate,gbc);
      controlPanel.add(reset, gbc);
      comp++;
      gbc.gridx = 2;
      gbc.gridy = 0;
      //controlPanel.add(cFval,gbc);
      gbc.gridx = 3;
      gbc.gridy = 0;
      //controlPanel.add(cFrate,gbc);
      gbc.gridx = 0;
      gbc.gridy = 1;
      controlPanel.add(speedSlider,gbc);
      comp++;
      gbc.gridx = 1;
      gbc.gridy = 1;
      controlPanel.add(mutationSlider,gbc);
      comp++;
      gbc.gridx = 2;
      gbc.gridy = 1;
      controlPanel.add(foodValueSlider,gbc);
      comp++;
      gbc.gridx = 3;
      gbc.gridy = 1;
      controlPanel.add(foodRateSlider,gbc);
      comp++;
      gbc.gridx = 0;
      gbc.gridy = 2;
      controlPanel.add(spd,gbc);
      comp++;
      gbc.gridx = 1;
      gbc.gridy = 2;
      controlPanel.add(mut8,gbc);
      comp++;
      gbc.gridx = 2;
      gbc.gridy = 2;
      controlPanel.add(fVal,gbc);
      comp++;
      gbc.gridx = 3;
      gbc.gridy = 2;
      controlPanel.add(fR8,gbc);
      comp++;
      gbc.gridx = 0;
      gbc.gridy = 3;
      controlPanel.add(sleepCostSlider,gbc);
      comp++;
      gbc.gridx = 1;
      gbc.gridy = 3;
      controlPanel.add(moveCostSlider,gbc);
      comp++;
      gbc.gridx = 2;
      gbc.gridy = 3;
      controlPanel.add(turnCostSlider,gbc);
      comp++;
      gbc.gridx = 3;
      gbc.gridy = 3;
      controlPanel.add(colorVarSlider,gbc);
      comp++;
      gbc.gridx = 4;
      gbc.gridy = 3;
      controlPanel.add(sightRangeSlider,gbc);
      comp++;
      gbc.gridx = 0;
      gbc.gridy = 4;
      controlPanel.add(sc,gbc);
      comp++;
      gbc.gridx = 1;
      gbc.gridy = 4;
      controlPanel.add(mc,gbc);
      comp++;
      gbc.gridx = 2;
      gbc.gridy = 4;
      controlPanel.add(tc,gbc);
      comp++;
      gbc.gridx = 3;
      gbc.gridy = 4;
      controlPanel.add(cv,gbc);
      comp++;
      gbc.gridx = 4;
      gbc.gridy = 4;
      controlPanel.add(sr,gbc);
      comp++;
      gbc.gridx = 0;
      gbc.gridy = 5;
      controlPanel.add(xSpace,gbc);
      comp++;
      gbc.gridx = 1;
      gbc.gridy = 5;
      controlPanel.add(ySpace,gbc);
      comp++;
      gbc.gridx = 2;
      gbc.gridy = 5;
      controlPanel.add(eraseRect,gbc);
      comp++;
      gbc.gridx = 3;
      gbc.gridy = 5;
      controlPanel.add(dropPoint,gbc);
      comp++;
      gbc.gridx = 0;
      gbc.gridy = 6;
      controlPanel.add(xOff,gbc);
      comp++;
      gbc.gridx = 1;
      gbc.gridy = 6;
      controlPanel.add(yOff,gbc);
      comp++;
      gbc.gridx = 2;
      gbc.gridy = 6;
      controlPanel.add(dragGraph,gbc);
      comp++;
      gbc.gridx = 3;
      gbc.gridy = 6;
      controlPanel.add(dragLine,gbc);
      comp++;
      gbc.gridx = 3;
      gbc.gridy = 0;
      controlPanel.add(play,gbc);
      comp++;
      
      /*controlPanel.add(saveLoad,gbc);
      gbc.gridx = 1;
      gbc.gridy = 7;
      controlPanel.add(save,gbc);
      gbc.gridx = 2;
      gbc.gridy = 7;
      controlPanel.add(load,gbc);
      */
      gbc.gridx = 2;
      gbc.gridy = 7;
      controlPanel.add(toggleInv,gbc);

      
      gbc.gridx = 5;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.VERTICAL;
      gbc.gridheight = 9;
      simulator = new Simulator(bs, sleepCost, moveCost, turnCost);
      controlPanel.add(simulator.board,gbc);
      comp++;
      mainFrame.sim = simulator;
      mainFrame.bs = bs;
      mainFrame.setContentPane(controlPanel);
      mainFrame.setVisible(true); 
      mainFrame.pixelSize = simulator.pixelSize;
   } 
   
   public void save(String filename)
   {        
       // write object to file
       try{
           if (run = false)
           {
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

   public void load(String filename)
   {
            // read object from file
       try{
           if (run = false)
           {
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