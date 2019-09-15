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
    private static final long serialVersionUID = 1L;
    private ArrayList<Critter> critters;
    public Board board;
    public PopulationDisplay popDisp;
    private int foodRate;
    private int foodValue;
    private int maxTimeSteps;
    private double mutationRate;
    private int speed;
    public int pixelSize;
    private int boardSize;
    private int sleepC; 
    private int moveC; 
    private int turnC;
    private double colorVar;
    private int sightRange;
    private boolean paused = false;
    private boolean[][] isFood;
    private boolean[][] isBarrier;
    private boolean[][] isCritter;
    public int boardTick;
    private Color barrierColor;
    private ArrayList<Population> populations;
    private ArrayList<String> inactiveCommands;
    private long tickTime;
    //------------------------------CONSTRUCTORS-------------------------------
    /**
     * Constructor for objects of class Simulator
     */
    
    //This one
    public Simulator(int bs,int sC, int mC, int tC)
    {   
        boardSize = bs;
        setSleepCost(sC);
        setMoveCost(mC);
        setTurnCost(tC);
        pixelSize = 900 / boardSize;
        board = new Board(pixelSize,bs);
        popDisp = new PopulationDisplay();
        critters = new ArrayList<Critter>();
        populations = new ArrayList<Population>();
        inactiveCommands = new ArrayList<String>();
        this.foodRate = bs/10;
        this.foodValue = bs/3;
        maxTimeSteps = 10;
        this.mutationRate = .2;
        this.speed = 10;
        colorVar = 0.5;
        sightRange = 10;
        boardTick = 1;
        barrierColor = Color.gray;
        isFood = new boolean[bs][bs];
        isCritter = new boolean[bs][bs];
        isBarrier = new boolean[bs][bs];
        for(int i = 0; i < bs; i++)
        {
            for (int j = 0; j < bs; j++)
            {
                isFood[i][j] = false;
                isCritter[i][j] = false;
                isBarrier[i][j] = false;
            }
        }
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
    
    public void boardTick()
    {
        if (boardTick == 50) {boardTick = 0;}
        else {boardTick++;}
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
    
    public int getBoardSize()
    {
        return boardSize;
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
        
    public void addInactiveCommand(String command)
    {
        inactiveCommands.add(command);
    }
    
    public void removeInactiveCommand(String command)
    {
        inactiveCommands.remove(command);
    }
    
    public ArrayList<String> getInactiveCommands()
    {
        return inactiveCommands;
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
        return isFood[x][y];
    }
    
    public boolean isFood(Point point)
    {
        return isFood(point.x, point.y);
    }
    
    public boolean isBarrier(int x, int y)
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
        return isBarrier[x][y];
    }
    
    public boolean isBarrier(Point point)
    {
        return isBarrier(point.x, point.y);
    }
    
    public boolean isCritter(int x, int y)
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
        return isCritter[x][y];
    }
    
    public boolean isCritter(Point point)
    {
        return isCritter(point.x, point.y);
    }
    
    public void addCritterPoint(Point point)
    {
        isCritter[point.x][point.y] = true;
    }
    
    public void addCritterPoint(int x,int y)
    {
        isCritter[x][y] = true;
    }
    
    public void removeCritterPoint(int x,int y)
    {
        isCritter[x][y] = false;
    }
    
    public void removeCritterPoint(Point point)
    {
        isCritter[point.x][point.y] = false;
    }

    public void addFood(int x, int y)
    {
        if (!isBarrier(x,y) && !isCritter(x,y))
        {
            isFood[x][y] = true;
            board.draw(new Point(x,y), Color.green);
        }
    }
    
    public void addFood(Point point)
    {
        addFood(point.x,point.y);
    }

    public void removeFood(int x, int y)
    {
        isFood[x][y] = false;
    }
    
    public void removeFood(Point point)
    {
        removeFood(point.x, point.y);
    }
    
    public boolean lookFood(int x, int y, String facing)
    {
        if (facing.equals("U"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isFood(x,y-i))
                {
                    return true;
                }
                if (isBarrier(x,y-i)|| isCritter(x,y-i))
                {
                    return false;
                }
            }
            return false;
        }
        else if (facing.equals("D"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isFood(x,y+i))
                {
                    return true;
                }
                if (isBarrier(x,y+i)|| isCritter(x,y+i))
                {
                    return false;
                }
            }
            return false;
        }
        else if (facing.equals("L"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isFood(x-i,y))
                {
                    return true;
                }
                if (isBarrier(x-i,y)|| isCritter(x-i,y))
                {
                    return false;
                }
            }
            return false;
        }
        else
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isFood(x+i,y))
                {
                    return true;
                }
                if (isBarrier(x+i,y)|| isCritter(x+i,y))
                {
                    return false;
                }
            }
            return false;
        }
    }
     public boolean lookBarrier(int x, int y, String facing)
    {
        if (facing.equals("U"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x,y-i))
                {
                    return true;
                }
                if (isCritter(x,y-i))
                {
                    return false;
                }
            }
            return false;
        }
        else if (facing.equals("D"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x,y+i))
                {
                    return true;
                }
                if (isCritter(x,y+i))
                {
                    return false;
                }
            }
            return false;
        }
        else if (facing.equals("L"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x-i,y))
                {
                    return true;
                }
                if (isCritter(x-i,y))
                {
                    return false;
                }
            }
            return false;
        }
        else
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x+i,y))
                {
                    return true;
                }
                if (isCritter(x+i,y))
                {
                    return false;
                }
            }
            return false;
        }
    }
    
    public boolean lookCritter(int x, int y, String facing)
    {
        if (facing.equals("U"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x,y-i))
                {
                    return false;
                }
                if (isCritter(x,y-i))
                {
                    return true;
                }
            }
            return false;
        }
        else if (facing.equals("D"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x,y+i))
                {
                    return false;
                }
                if (isCritter(x,y+i))
                {
                    return true;
                }
            }
            return false;
        }
        else if (facing.equals("L"))
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x-i,y))
                {
                    return false;
                }
                if (isCritter(x-i,y))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            for (int i = 1; i < sightRange; i++)
            {
                if (isBarrier(x+i,y))
                {
                    return false;
                }
                if (isCritter(x+i,y))
                {
                    return true;
                }
            }
            return false;
        }
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
        addCritterPoint(point);
        Population pop = new Population(critter.getColor(), critter.getDNA());
        boolean exists = false;
        for (Population p : populations)
        {
            if (pop.equals(p))
            {
                p.addMember();
                critter.setColor(p.color);
                exists = true;
                break;
            }
        }
        if (!exists)
        {
            populations.add(pop);
        }
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

        if (!isBarrier(newSpace) && !isCritter(newSpace))
        {
            if (critter.curled && length > 1)
            {

                Point last2Point = body[length - 2];
                if (!(lastPoint.equals(last2Point)))
                {
                    critter.curled = false;
                    removeCritterPoint(lastPoint);
                    board.erase(lastPoint);
                    if (critter.getEnergy() >= critter.getBaseEnergy() * 2)
                    {
                        addCritter(critter.reproduce(mutationRate, lastPoint, colorVar));
                    }
                }
            }
            else 
            {
                removeCritterPoint(lastPoint);
                board.erase(lastPoint);
                if (critter.getEnergy() >= critter.getBaseEnergy() * 2)
                {
                    addCritter(critter.reproduce(mutationRate, lastPoint, colorVar));
                }
            }
            critter.move(newSpace);
            addCritterPoint(newSpace);
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
             critter.goToStep(j + 1); //Go to step after END
         }
         else
         {
             critter.goToStep(0);
         }
    }
    
    public void checkOr(Critter critter)
    {
        int step = critter.getStep();
        String[] dna = critter.getDNA();
        if (step + 2 < dna.length)
        {
            if (dna[step + 1].equals("v")) //If next step is an OR
            {
                try {
                        int temp = Integer.parseInt(dna[step + 2]); //Test if the step after OR is a conditional
                        critter.goToStep(step + 2); //If it is, go to that step
                    }   catch(Exception e) {
                        findElse(critter);  //Otherwise, proceed as normal
                    }
                }
                else
                {findElse(critter);} //No OR, proceed as normal
            }
            else
            {findElse(critter);}
    }
    public void skipOr(Critter critter)
    {
        int step = critter.getStep() + 1;
        String[] dna = critter.getDNA();
        boolean done = false;
        boolean lastOr = false;
        while (!done && step < dna.length)
        {   
            if (dna[step].equals("v")) //If this step is OR
            {
                lastOr = true;
                step++; //Skips through all ORs.
            }
            else if (lastOr) //If last step was OR
            {
                lastOr = false;
                try {
                        int temp = Integer.parseInt(dna[step]); //Tests if step is a conditional
                        step++; //If it is, skips step
                    } catch(Exception e) {
                        critter.goToStep(step); //Otherwise, go to this step
                        done = true;
                    }
            }
            else //If neither this step nor last step were OR
            {
                critter.goToStep(step); //Go to this step
                done = true;
            }
        }
        if (step == dna.length)
        {
            critter.goToStep(0);
        }
    }
    
    public void critterTimeStep(Critter critter)
    {
        if (critter.getTimeStep() >= maxTimeSteps)
        {
            critter.sleep();
            critter.spendEnergy(sleepC);
            return;
        }
        critter.timeStep();
        String[] dna = critter.getDNA();
        int step = critter.getStep();
        String facing = critter.getFacing();
        Point[] body = critter.getBody();
        if (inactiveCommands.contains(dna[step]))
        {
            critter.nextStep();
            critterTimeStep(critter);
        }
        else if (dna[step].equals("M"))
        {
            moveCritter(critter);
            critter.nextStep();
            int x = critter.getBody()[0].x;
            int y = critter.getBody()[0].y;
            Point point = new Point(x,y);
            if (isFood(point))
            {
                critter.addEnergy(foodValue);
                removeFood(point);
            }
            critter.spendEnergy(moveC);
        }
        
        else if (dna[step].equals("H"))
        {
            moveCritter(critter);
            int x = critter.getBody()[0].x;
            int y = critter.getBody()[0].y;
            Point point = new Point(x,y);
            if (isFood(point))
            {
                critter.addEnergy(foodValue);
                removeFood(point.x, point.y);
            }
            
            moveCritter(critter);
            x = critter.getBody()[0].x;
            y = critter.getBody()[0].y;
            point = new Point(x,y);
            if (isFood(point))
            {
                critter.addEnergy(foodValue);
                removeFood(point.x, point.y);
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
        else if (dna[step].equals("E") || dna[step].equals("v"))
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
            if (lookFood(body[0].x, body[0].y, facing))
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("1")) //If the critter does not see food
        {
            if (!lookFood(body[0].x, body[0].y, facing))
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("2")) //If the critter is blocked
        {

            if (critter.isBlocked())
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("3")) //IF the critter is not blocked
        {

            if (!(critter.isBlocked()))
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("4"))
        {
            if (critter.getEnergy() < critter.getBaseEnergy())
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("5"))
        {
            if (critter.getEnergy() >= critter.getBaseEnergy())
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("6"))
        {
            if (critter.getAge() >= critter.getMaxAge() / 2)
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("7"))
        {
            if (critter.getAge() < critter.getMaxAge() / 2)
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("8"))
        {
            if (critter.reproduced)
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
        else if (dna[step].equals("9"))
        {
            if (!critter.reproduced)
            {
                skipOr(critter);
            }
            else
            {
                checkOr(critter);
            }
            critter.setLastCondition(step);
            critterTimeStep(critter);
        }
    }
    //-------------------------------------------------------------------------

    public void setBarrierColor(Color color)
    {
        barrierColor = color;
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            if (isBarrier[i][j])
            {
                board.draw(new Point(i,j), color);
            }
        }
    }
    
    public void addBarrier(Point point)
    {
        if (point.x < 0 || point.y < 0 || point.x >= boardSize || point.y >= boardSize) return;
        if (!isCritter(point))
        {
            if (isFood(point)){removeFood(point);}
            isBarrier[point.x][point.y] = true;
            board.draw(point, Color.gray);
        }
    }
    
    public void removeBarrier(Point point)
    {
        if (point.x < 0 || point.y < 0 || point.x >= boardSize || point.y >= boardSize) return;
        if (isBarrier[point.x][point.y] == true) 
        {
            isBarrier[point.x][point.y] = false;
            board.erase(point);
        }
    }

    public void addBarrierLine(int x1, int y1, int x2, int y2)
    {
        
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
                if (!isBarrier(point) && !isCritter(point))
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
                if (!isBarrier(point))
                {
                    addBarrier(point);
                }
                y++;
                x += slope; 
            }
            
        }
    }
    
    public void addBarrierGraph(int x1, int y1, int x2, int y2)
    {
      
      int temp;
      if (x1 > x2)
      {
          temp = x1;
          x1 = x2;
          x2 = temp;
      }
      if (y1 > y2)
      {
          temp = y1;
          y1 = y2;
          y2 = temp;
      }
      int i = x1;
      int j = y1;
     
       while (i < x2 )
      {
          while (j < y2)
          {
              addBarrier(new Point(i, j));
              j += 2;
          }
          j = y1;
          i += 2;
       }
    }
    
    public void addBarrierGraph(int x1, int y1, int x2, int y2, int xSpace, int ySpace, int xOff, int yOff)
    {
      
      int temp;
      if (x1 > x2)
      {
          temp = x1;
          x1 = x2;
          x2 = temp;
      }
      if (y1 > y2)
      {
          temp = y1;
          y1 = y2;
          y2 = temp;
      }
      
      int i = 0;
      int j = 0;
      int xRange = x2 - x1;
      int yRange = y2 - y1;
     
      while (i < xRange)
      {
          while (j < yRange)
          {
              addBarrier(new Point(x1 + i + (xOff * ((j / (ySpace + 1)) % (xSpace + 1))), y1 + j));
              j += ySpace + 1;
          }
          i += xSpace + 1;
          j = yOff * (i / (xSpace + 1)) % (ySpace + 1);
       }
    }
    
    public void removeBarrierLine(int x1, int y1, int x2, int y2)
    {
        
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
                if (isBarrier(point))
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
                if (isBarrier(point))
                {
                    removeBarrier(point);
                }
                y++;
                x += slope; 
            }
            
        }
    }
    
    public void removeBarrierRect(int x1, int y1, int x2, int y2)   
    {
      int temp;
      if (x1 > x2)
      {
          temp = x1;
          x1 = x2;
          x2 = temp;
      }
      if (y1 > y2)
      {
          temp = y1;
          y1 = y2;
          y2 = temp;
      }
      
      int i = x1;
      int j = y1;
      
      while (i <= x2)
      {
          while (j <= y2)
          {
              removeBarrier(new Point(i, j));
              j++;
          }
          i++;
          j = y1;
      }
    }

    //-------------------------------------------------------------------------
    public void gameTimeStep()
    {
        long startTime = 0;
        if (!paused)
        {
            boardTick();
            if (boardTick == 0)
            {
                startTime = System.nanoTime();
                popDisp.refresh(getTopPopulations());
                popDisp.refreshTickSpeed(tickTime);
            }
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
                    removeCritterPoint(iBody[j]);
                    if (j < foodLeft)
                    {
                        addFood(iBody[j]);
                    }
                    else
                    {
                        board.erase(iBody[j]);
                    }
                    
                }
                Population pop = new Population(iCritter.getColor(), iCritter.getDNA());
                for (Population p : populations)
                {
                    if (pop.equals(p))
                    {
                        p.removeMember();
                        if (p.size <= 0) {populations.remove(p);}
                        break;
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
        if (boardTick == 0)
        {
            long endTime = System.nanoTime();
            tickTime = endTime - startTime;
        }
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
    
    
    public Population[] getTopPopulations()
    {
        Population[] top = new Population[30];
        Collections.sort(populations);
        for (int i = 0; i < 30; i++)
        {
            if (i < populations.size())
                {top[i] = populations.get(i);}
            else
            {
                String[] dna = new String[1];
                dna[0] = "";
                top[i] = new Population(Color.gray, dna);
            }

        }
        return top;
    }
}
