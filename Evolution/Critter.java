import java.util.ArrayList;
import java.awt.*;
/**
 * Write a description of class Critter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Critter
{
    // instance variables - replace the example below with your own
    private Point[] body;
    private String[] dna;
    private int energy;
    private int baseEnergy;
    private String facing;
    private int dnaStep;
    private int age;
    private int maxAge;
    private int timeStep;
    private Color color;
    private boolean blocked;
    private String[] commands = {"M", "M","Z", "Z", "<", "<", ">",  ">", "U", "U", "R","R",  "D","D", "L", "L", "H", "H" , "v", "r", "e", "E", "C", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] facings = {"U", "R", "D", "L"};
    public int head;
    public int length;
    private boolean hasElse;
    private int lastCondition;
    public boolean curled;
    public boolean reproduced;
    


    /**
     * Constructor for objects of class Critter
     */
    public Critter(String newDNA, int xPos, int yPos)
    {
        length = newDNA.length();
        body = new Point[length];
        dna = new String[length];
        for (int i = 0; i < length; i++)
        {
            dna[i] = newDNA.substring(i, i + 1);
            
            body[i] = new Point(xPos, yPos);
        }
        baseEnergy = 400 + (length * 100);
        energy = baseEnergy;
        facing = facings[(int)(Math.random()*4)];
        dnaStep = 0;
        age = 0;
        maxAge = 200 + (length * 100);
        timeStep = 0;
        blocked = false;
        color = dnaToColor();
        head = 0;
        curled = true;
        reproduced = false;
        lastCondition = 0;
    }
    public Critter(String[] newDNA, int xPos, int yPos, String facing, Color color)
    {
        length = newDNA.length;
        body = new Point[length];
        dna = newDNA;
        for (int i = 0; i < length; i++)
        {
            body[i] = new Point(xPos, yPos);
        }
        baseEnergy = 400 + (length * 100);
        energy = baseEnergy;
        this.facing = facing;
        dnaStep = 0;
        age = 0;
        maxAge = 200 + (length * 100);
        timeStep = 0;
        blocked = false;
        this.color = color;
        head = 0;
        curled = true;
        reproduced = false;
        lastCondition = 0;
    }
    
    public int getMaxAge()
    {
        return maxAge;
    }
    
    public int getBaseEnergy()
    {
        return baseEnergy;
    }
    
    public void setLastCondition(int condition)
    {
        lastCondition = condition;
    }
    
    public void goToLastCondition()
    {
        dnaStep = lastCondition;
    }
    
    
    public void setHead(int newHead)
    {
        head = newHead;
    }
    
    public void setBlocked(boolean blocked)
    {
        this.blocked = blocked;
    }

    public boolean isBlocked()
    {
        return blocked;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public void goToStep(int step)
    {
        dnaStep = step % length;
    }

    public void nextStep()
    {
        dnaStep = ( dnaStep + 1 ) % length;
    }

    public void timeStep()
    {
        timeStep++;
    }

    public int getTimeStep()
    {
        return timeStep;
    }
    
    public void setTimeStep(int step)
    {
        timeStep = step;
    }

    public void resetTimeStep()
    {
        timeStep = 0;
    }

    public String getFacing()
    {
        return facing;
    }

    public int getEnergy()
    {
        return energy;
    }
    
    public Color dnaToColor()
    {
        double red = .5;
        double green = .5;
        double blue = .5;
        
        for (int i = 0; i < length; i++)
        {
            for (int j = 0; j < commands.length; j++)
            {
                if (dna[i].equals(commands[j]))
                {
                    red += (1 / (Math.pow(2, i + 2))) * Math.cos(20 * Math.PI * (j + 3 * i) / commands.length);
                    green += (1 / (Math.pow(2, i + 2))) * Math.cos(20 * Math.PI * ((j + 3 * i) + 20 * commands.length) / commands.length);
                    blue += (1 / (Math.pow(2, i + 2))) * Math.cos(20 * Math.PI * ((j + 3 * i) + 40 * commands.length) / commands.length);
                }
            }
        }
        
        return new Color((float)red,(float)green,(float)blue);
    }
    

    public String[] getDNA()
    {
        return dna;
    }

    public int getStep()
    {
        return dnaStep;
    }

    public Point[] getBody()
    {
        return body;
    }

    public void move(Point nextPoint)
    {
        int lastX = body[0].x;
        int lastY = body[0].y;
        body[0].move(nextPoint.x, nextPoint.y);
       
        for (int i = 1; i <body.length; i++)
        {
            int nextX = body[i].x; 
            int nextY = body[i].y; 
            body[i].move(lastX, lastY);
            lastX = nextX;
            lastY = nextY;
        }
        resetTimeStep();
    }

    public void sleep()
    {
        resetTimeStep();
    }

    public void turnRight()
    {
        if (facing.equals("U"))
        {
            facing = "R";
        }
        else if (facing.equals("R"))
        {
            facing = "D";
        }
        else if (facing.equals("D"))
        {
            facing = "L";
        }
        else
        {
            facing = "U";
        }
        resetTimeStep();
    }
    public void turnLeft()
    {
        if (facing.equals("U"))
        {
            facing = "L";
        }
        else if (facing.equals("R"))
        {             
            facing = "U";
        }
        else if (facing.equals("D"))
        {
            facing = "R";
        }
        else
        {
            facing = "D";
        }
        resetTimeStep();
    }

    public void changeFacing(String newFacing)
    {
        facing = newFacing;
        resetTimeStep();
    }

    public void addEnergy(int add)
    {
        energy = energy + add;
    }
    
    public void spendEnergy(int sub)
    {
        energy = energy - sub;
    }

    public void setEnergy(int set)
    {
        energy = set;
    }

    public void age()
    {
        age++;
    }

    public int getAge()
    {
        return age;
    }

    public Critter reproduce(double mutationRate, Point point, double colorVar)
    {
        reproduced = true;
        ArrayList<String> newDNA = new ArrayList<String>(); 
        float newR = (float)(color.getRed() / 255.0);
        float newG = (float)(color.getGreen() / 255.0);
        float newB = (float)(color.getBlue() / 255.0);
        
        for (int j = 0 ; j < length ; j++)
        {
            if (Math.random() < mutationRate / length)
            {
                int rand3 = (int)(Math.random() * 3);
                double rand = Math.random();
                if (rand < .5) {rand -= 1;}
                rand *= colorVar;
                if (rand3 == 0)
                {
                    newR += rand;
                    if (newR < .2) newR = (float).2;
                    else if (newR > 1) newR = 1;
                }
                else if (rand3 == 1)
                {
                    newG += rand;
                    if (newG < .2) newG = (float).2;
                    else if (newG > 1) newG = 1;
                }
                else
                {
                    newB += rand;
                    if (newB < .2) newB = (float).2;
                    else if (newB > 1) newB = 1;
                }
                
                int randCommand = (int)(Math.random() * commands.length);
                
                
                rand3 = (int)(Math.random() * 3);
                if (rand3 == 1)
                {
                    int rand2 = (int)(Math.random() * 2);
                    if (rand2 == 0)
                    {
                        newDNA.add(commands[randCommand]);
                        newDNA.add(dna[j]);
                    }
                    else
                    {
                        newDNA.add(dna[j]);
                        newDNA.add(commands[randCommand]);
                    }
                }
                else if (rand3 == 2 || (rand3 == 0 && newDNA.size() == 0))
                {
                    newDNA.add(commands[randCommand]);
                }
            }
            else
            {
                newDNA.add(dna[j]);
            }
        }
        String newFacing;
        if (facing.equals("U"))
        {
            newFacing = "R";
        }
        else if (facing.equals("R"))
        {
            newFacing = "D";
        }
        else if (facing.equals("D"))
        {
            newFacing = "L";
        }
        else
        {
            newFacing = "U";
        }
        String[] newDNAString = new String[newDNA.size()];
        int i = 0;
        for (String j : newDNA)
        {
            newDNAString[i] = j;
            i++;
        }
        Critter newCritter = new Critter(newDNAString, point.x,  point.y, newFacing, new Color(newR, newG, newB));
        
        spendEnergy(baseEnergy);
        return newCritter;
    }

}
