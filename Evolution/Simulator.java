import java.util.ArrayList;
import java.util.*;
import java.awt.*;
/**
 * Write a description of class Simulator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Simulator
{

    private ArrayList<Critter> critters;
    private ArrayList<Point> food;
    private ArrayList<Point> barriers;
    public Board board;
    private int foodRate;
    private int foodValue;
    private int maxTimeSteps;
    private double mutationRate;
    private String[] commands = {"M", "Z", "<", ">", "r", "e", "E", "C", "0", "1", "2", "3", "4", "5", "6", "7", "U", "R", "D", "L", "H"};
    private int speed;
    public int pixelSize;
    //private HashMap<String[],Color> critterColors;
    //private ArrayList<String> genePool;
    //private ArrayList<Color> geneColors;
    private int boardSize;
    private int sleepC; 
    private int moveC; 
    private int turnC;
    private double colorVar;
    private int sightRange;
    private boolean paused = false;
   
    //------------------------------CONSTRUCTORS-------------------------------
    /**
     * Constructor for objects of class Simulator
     */
    public Simulator(int foodRate, int foodValue, double mutationRate, int speed, int pixelSize)
    {
        this.pixelSize = pixelSize;

        critters = new ArrayList<Critter>();
        food = new ArrayList<Point>();
        this.foodRate = foodRate;
        this.foodValue = foodValue;
        maxTimeSteps = 10;
        this.mutationRate = mutationRate;
        barriers = new ArrayList<Point>();
        this.speed = speed;
        //genePool = new ArrayList<String>();
        // geneColors = new ArrayList<Color>();
        colorVar = 0.5;
        sightRange = 10;

    }
    public Simulator(int bs,int sC, int mC, int tC)
    {   
        boardSize = bs;
        setSleepCost(sC);
        setMoveCost(mC);
        setTurnCost(tC);
        pixelSize = 4;
        board = new Board(4,bs);
        critters = new ArrayList<Critter>();
        food = new ArrayList<Point>();
        this.foodRate = bs/10;
        this.foodValue = bs/3;
        maxTimeSteps = 10;
        this.mutationRate = .2;
        barriers = new ArrayList<Point>();
        this.speed = 10;
        colorVar = 0.5;
        sightRange = 10;
        //genePool = new ArrayList<String> ();
        //geneColors = new ArrayList<Color>();
    }
    
    public void pause()
    {
        paused = true;
    }
    
    public void unpause()
    {
        paused = false;
    }
    
    public boolean isPaused()
    {
        return paused;
    }
    
    
    //-------------------------------------------------------------------------
    //Method used by UI to change the size.
    public void setBoardSize(int s)
    {
        boardSize = s;
    }
    public void setSleepCost(int sC)
    {
        sleepC = sC;
    }
    public void setMoveCost(int mC)
    {
        moveC = mC;
    }
    public void setTurnCost(int tC)
    {
        turnC = tC;
    }
    //MEthod to return data to the UI
    public int[] refresh()
    {
        int[] info = new int[]{speed, foodValue, foodRate};
        return info;
    }
    //Methods used by refresh()
    public double refreshMutationRate()
    {
        return mutationRate;
    }
    
    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public void setFoodRate(int foodRate)
    {
        this.foodRate = foodRate;
    }

    public void setFoodValue(int foodValue)
    {
        this.foodValue = foodValue;
    }

    public void setMutationRate(double mutationRate)
    {
        this.mutationRate = mutationRate;
    }
    
    public double getColorVar()
    {
        return colorVar;
    }
    
    public void setColorVar(double cVar)
    {
         colorVar = cVar;
    }
    
    public int getSightRange()
    {
        return sightRange;
    }
    
    public void setSightRange(int sightR)
    {
        sightRange = sightR;
    }
        

    //--------------------------------FOOD-------------------------------------
    public boolean isFood(int x, int y)
    {
        if (x < 0)
        {
            x += boardSize;
        }
        if (x > boardSize-1)
        {
            x -= boardSize;
        }
        if (y < 0)
        {
            y += boardSize;
        }
        if (y > boardSize-1)
        {
            y -= boardSize;
        }
        return food.contains(new Point(x,y));
    }
    
    public boolean isFood(Point point)
    {
        return isFood(point.x, point.y);
    }

    public void addFood(int x, int y)
    {
        Point point = new Point(x,y);
        if (!barriers.contains(point))
        {
            food.add(point);
            board.draw(point, Color.green);
        }
    }

    public void removeFood(int x, int y)
    {
        Point point = new Point(x,y);
        food.remove(point);
        board.erase(point);
    }

    public void clearFood()
    {
        for (Point i : food)
        {
            board.erase(i);
        }
        food.clear();
    }

    public boolean isFoodU(int x, int y)
    {
        Point thisPoint = new Point(x,y);
        for (int i = 1 ; i < sightRange; i++)
        {
            thisPoint.y = y-i;
            if (barriers.contains(thisPoint))
            {
                return false;
            }
            if (isFood(thisPoint))
            {
                return true;                
            }
        }
        return false;
    }     

    public boolean isFoodD(int x, int y)
    {
        Point thisPoint = new Point(x,y);
        for (int i = 1 ; i < sightRange; i++)
        {
            thisPoint.y = y+i;
            if (barriers.contains(thisPoint))
            {
                return false;
            }
            if (isFood(thisPoint))
            {
                return true;                
            }
        }
        return false;
    }  

    public boolean isFoodR(int x, int y)
    {
        Point thisPoint = new Point(x,y);
        for (int i = 1 ; i < sightRange; i++)
        {
            thisPoint.x = x+i;
            if (barriers.contains(thisPoint))
            {
                return false;
            }
            if (isFood(thisPoint))
            {
                return true;                
            }
        }
        return false;
    }  

    public boolean isFoodL(int x, int y)
    {
        Point thisPoint = new Point(x,y);
        for (int i = 1 ; i < sightRange; i++)
        {
            thisPoint.x = x-i;
            if (barriers.contains(thisPoint))
            {
                return false;
            }
            if (isFood(thisPoint))
            {
                return true;                
            }
        }
        return false;
    }
    //-------------------------------------------------------------------------
    public String toString(String[] string)
    {
        String newString = new String();
        for (int i = 0 ; i < string.length; i++)
        {
            newString += string[i];
        }
        return newString;
    }
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    public void addCritter(String dna)
    {
        Critter critter = new Critter(dna,(int)(Math.random()*boardSize),(int)(Math.random()*boardSize));
        addCritter(critter);
        board.repaint();
    }

    public void addCritter(String dna, int number)
    {
        for (int i = 0; i < number; i++)
        {
            addCritter(dna);
        }
    }

    public void addCritter(Critter critter)
    {
        critters.add(critter);
        int x = critter.getBody()[0].x;
        int y = critter.getBody()[0].y;
        Point point = new Point(x,y);
        barriers.add(point);
        board.draw(point, critter.getColor());
    }

    public void moveCritter(Critter critter)
    {
        Point newSpace;
        Point[] body = critter.getBody();
        int length = body.length;
        int x = body[0].x;
        int y = body[0].y;
        int lastX = body[length - 1].x;
        int lastY = body[length - 1].y;
        String facing = critter.getFacing();
        Point lastPoint = new Point(lastX, lastY);
        if (facing.equals("U"))
        {
            if (y == 0)
            {
                y = boardSize;
            }
            newSpace = new Point(x,y-1);
        }
        else if (facing.equals("D"))
        {
            newSpace = new Point(x,(y+1) % boardSize);
        }
        else if (facing.equals("L"))
        {
            if (x == 0)
            {
                x = boardSize;
            }
            newSpace = new Point(x-1,y);
        }
        else
        {
            newSpace = new Point((x+1) % boardSize,y);
        }

        if (!barriers.contains(newSpace))
        {
            if (length > 1)
            {
                int last2X = body[length - 2].x;
                int last2Y = body[length - 2].y;

                Point last2Point = new Point(last2X, last2Y);
                if (!(lastPoint.equals(last2Point)))
                {
                    barriers.remove(lastPoint);
                    if(lastPoint.x >= boardSize ||lastPoint.y >= boardSize)
                    {
                        int debuga = lastPoint.x;
                        int debugb = lastPoint.y;
                    }
                    board.erase(lastPoint);
                    if (critter.getEnergy() >= critter.getBaseEnergy() * 2)
                    {
                        
                        addCritter(critter.reproduce(mutationRate, lastPoint, colorVar));
                    }
                }
            }
            else 
            {
                barriers.remove(lastPoint);
                if(lastPoint.x >= boardSize ||lastPoint.y >= boardSize)
                    {
                        int debuga = lastPoint.x;
                        int debugb = lastPoint.y;
                    }
                board.erase(lastPoint);
                if (critter.getEnergy() >= critter.getBaseEnergy() * 2)
                {
                    addCritter(critter.reproduce(mutationRate, lastPoint, colorVar));
                }
            }
            critter.move(newSpace);
            barriers.add(newSpace);
            board.draw(newSpace, critter.getColor());
            critter.setBlocked(false);
        }
        else
        {
            critter.setBlocked(true);
        }
    }

    public void findElse(Critter critter)
    {
         int j = critter.getStep();
         boolean isElse = false;
         String[] dna = critter.getDNA();
         while (j < dna.length)
         {
             if (dna[j].equalsIgnoreCase("e") || dna[j].equals("C"))
             {
                 isElse = true;
                 break;
             }
             j++;
         }
         if (isElse)
         {
             critter.goToStep(j + 1);
         }
         else
         {
             critter.goToStep(0);
         }
    }
    
    public void findEnd(Critter critter)
    {
         int j = critter.getStep();
         boolean isEnd = false;
         String[] dna = critter.getDNA();
         while (j < dna.length)
         {
             if (dna[j].equals("E") || dna[j].equals("C"))
             {
                 isEnd = true;
                 break;
             }
             j++;
         }
         if (isEnd)
         {
             critter.goToStep(j + 1);
         }
         else
         {
             critter.goToStep(0);
         }
    }
    
    public void critterTimeStep(Critter critter)
    {
        if (critter.getTimeStep() >= maxTimeSteps)
        {
            critter.sleep();
            critter.spendEnergy(sleepC + 1);
            return;
        }
        critter.timeStep();
        String[] dna = critter.getDNA();
        int step = critter.getStep();
        String facing = critter.getFacing();
        Point[] body = critter.getBody();
        if (dna[step].equals("M"))
        {
            moveCritter(critter);
            critter.nextStep();
            int x = critter.getBody()[0].x;
            int y = critter.getBody()[0].y;
            Point point = new Point(x,y);
            if (food.contains(point))
            {
                critter.addEnergy(foodValue);
                food.remove(point);
            }
            critter.spendEnergy(moveC);
        }
        
        else if (dna[step].equals("H"))
        {
            moveCritter(critter);
            int x = critter.getBody()[0].x;
            int y = critter.getBody()[0].y;
            Point point = new Point(x,y);
            if (food.contains(point))
            {
                critter.addEnergy(foodValue);
                food.remove(point);
            }
            
            moveCritter(critter);
            x = critter.getBody()[0].x;
            y = critter.getBody()[0].y;
            point = new Point(x,y);
            if (food.contains(point))
            {
                critter.addEnergy(foodValue);
                food.remove(point);
            }
            
            critter.nextStep();
            critter.spendEnergy(2 * moveC);
        }
        
        else if (dna[step].equals("Z"))
        {
            critter.sleep();
            critter.nextStep();
            critter.spendEnergy(sleepC);
        }
        else if (dna[step].equals(">"))
        {
            critter.turnRight();
            critter.nextStep();
            critter.spendEnergy(turnC);
        }
        else if (dna[step].equals("<"))
        {
            critter.turnLeft();
            critter.nextStep();
            critter.spendEnergy(turnC);
        }
        else if (dna[step].equals("U"))
        {
            critter.changeFacing("U");
            critter.nextStep();
            critter.spendEnergy(turnC);
        }
        else if (dna[step].equals("R"))
        {
            critter.changeFacing("R");
            critter.nextStep();
            critter.spendEnergy(turnC);
        }
        else if (dna[step].equals("D"))
        {
            critter.changeFacing("D");
            critter.nextStep();
            critter.spendEnergy(turnC);
        }
        else if (dna[step].equals("L"))
        {
            critter.changeFacing("L");
            critter.nextStep();
            critter.spendEnergy(turnC);
        }
        else if (dna[step].equals("r"))
        {
            critter.goToStep(0);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("e"))
        {
            findEnd(critter);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("E"))
        {
            critter.nextStep();
            critterTimeStep(critter);
        }
        else if (dna[step].equals("C"))
        {
            critter.goToLastCondition();
            critterTimeStep(critter);
        }
        else if (dna[step].equals("0")) //If the critter sees food
        {
            boolean seeFood = 
                (facing.equals("U") && isFoodU(body[0].x, body[0].y)) ||   
                (facing.equals("R") && isFoodR(body[0].x, body[0].y)) ||   
                (facing.equals("D") && isFoodD(body[0].x, body[0].y)) ||  
                (facing.equals("L") && isFoodL(body[0].x, body[0].y));
            if (seeFood)
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("1")) //If the critter does not see food
        {
            boolean seeFood = 
                (facing.equals("U") && isFoodU(body[0].x, body[0].y)) ||   
                (facing.equals("R") && isFoodR(body[0].x, body[0].y)) ||   
                (facing.equals("D") && isFoodD(body[0].x, body[0].y)) ||  
                (facing.equals("L") && isFoodL(body[0].x, body[0].y));
            if (!seeFood)
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("2")) //If the critter is blocked
        {

            if (critter.isBlocked())
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("3")) //IF the critter is not blocked
        {

            if (!(critter.isBlocked()))
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("4"))
        {
            if (critter.getEnergy() < critter.getBaseEnergy())
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("5"))
        {
            if (critter.getEnergy() >= critter.getBaseEnergy())
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("6"))
        {
            if (critter.getAge() >= critter.getMaxAge() / 2)
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("7"))
        {
            if (critter.getAge() < critter.getMaxAge() / 2)
            {
                critter.nextStep();
            }
            else
            {
                findElse(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
    }
    //-------------------------------------------------------------------------

    public void addBarrier(Point point)
    {
        if (food.contains(point)){ food.remove(point);}
        barriers.add(point);
        board.draw(point, Color.gray);
    }
    
    public void removeBarrier(Point point)
    {
        barriers.remove(point);
        board.erase(point);
    }

    public void addBarrierLine(int x1, int y1, int x2, int y2)
    {
        if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 || x1 >= boardSize || y1 >= boardSize || x2 >= boardSize || y2 >= boardSize) return;
        if (Math.abs(y1 - y2) <= Math.abs(x1 - x2))
        {
            if (x1 > x2)
            {
                int temp;
                temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
            double slope = 1.0 * (y1 - y2) / (x1 - x2);
            int x = x1;
            double y = y1;
        
            while (x <= x2)
            {
                Point point = new Point(x, (int)(y + .5));
                if (!barriers.contains(point))
                {
                    addBarrier(point);
                }
                x++;
                y += slope; 
            }
        }
        else
        {
            
            if (y1 > y2)
            {
                int temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
                temp = x1;
                x1 = x2;
                x2 = temp;
            }
            double slope = 1.0 * (x1 - x2) / (y1 - y2);
            double x = x1;
            int y = y1;
        
            while (y <= y2)
            {
                Point point = new Point((int)(x + .5), y);
                if (!barriers.contains(point))
                {
                    addBarrier(point);
                }
                y++;
                x += slope; 
            }
            
        }
    }
    
    public void addBarrierGraph(Point p1, Point p2)
    {
        int i = p1.x;
      int j = p1.y;
     
       while (i < p2.x )
      {
          //j = (i % 9) / 3;
          j = 0;
          while (j < p2.y)
          {
              addBarrier(new Point(i, j));
              j += 2;
          }
          
          i += 2;
       }
    }
    
    public void removeBarrierLine(int x1, int y1, int x2, int y2)
    {
        if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 || x1 >= boardSize || y1 >= boardSize || x2 >= boardSize || y2 >= boardSize) return;
        if (Math.abs(y1 - y2) <= Math.abs(x1 - x2))
        {
            if (x1 > x2)
            {
                int temp;
                temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
            double slope = 1.0 * (y1 - y2) / (x1 - x2);
            int x = x1;
            double y = y1;
        
            while (x <= x2)
            {
                Point point = new Point(x, (int)(y + .5));
                if (barriers.contains(point))
                {
                    removeBarrier(point);
                }
                x++;
                y += slope; 
            }
        }
        else
        {
            
            if (y1 > y2)
            {
                int temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
                temp = x1;
                x1 = x2;
                x2 = temp;
            }
            double slope = 1.0 * (x1 - x2) / (y1 - y2);
            double x = x1;
            int y = y1;
        
            while (y <= y2)
            {
                Point point = new Point((int)(x + .5), y);
                if (barriers.contains(point))
                {
                    removeBarrier(point);
                }
                y++;
                x += slope; 
            }
            
        }
    }

    //-------------------------------------------------------------------------
    public void gameTimeStep()
    {
        if (!paused)
        {
            int i = 0; 
        
        while (i < critters.size())
        {
            Critter iCritter = critters.get(i);
            critterTimeStep(iCritter);
            iCritter.age();
            int iCritterEnergy = iCritter.getEnergy();
            Point[] iBody = iCritter.getBody();
            int length = iBody.length;
            if (iCritterEnergy <= 0 || iCritter.getAge() >= iCritter.getMaxAge())
            {
                int foodLeft = iCritterEnergy / foodValue;
                for (int j = 0; j < length; j++)
                {
                    barriers.remove(iBody[j]);
                    if (j < foodLeft)
                    {
                        addFood(iBody[j].x, iBody[j].y);
                    }
                    else
                    {
                        board.erase(iBody[j]);
                    }
                }
                critters.remove(i);
            }
            else
            {
                i++;
            }

        }
        for (int j = 0; j < foodRate; j ++)
        {
            addFood((int)(Math.random() * boardSize),(int)(Math.random() * boardSize));
        }
        }
        board.repaint();
        wait(speed);
        
    }

    public void gameTimeStep(int steps)
    {
        for (int i = 0; i < steps; i++)
        {
            gameTimeStep();
        }
    }

    public void wait(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        } 
        catch (Exception e)
        {
            // ignoring exception at the moment
        }
    }

}
