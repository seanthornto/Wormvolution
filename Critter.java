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
	private static String[] genes = {"M", "M","Z", "Z", "<", "<", ">",  ">", "U", "U", "R","R",  "D","D", "L", "L", "H", "H" , "A", "v", "r", "e", "E", "C", "0", "-0", "1", "-1", "2", "-2", "3", "-3", "4", "-4", "5", "-5"};
    private static String[] facings = {"U", "R", "D", "L"};
    private static double mutationRate = .2;
	
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
    public int length;
    public boolean hasElse;
    private int lastCondition;
    public boolean curled;
    public boolean reproduced;
    
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
        baseEnergy = 200 + (length * 100);
        energy = baseEnergy;
        facing = facings[(int)(Math.random()*4)];
        dnaStep = 0;
        age = 0;
        maxAge = 200 + (length * 200);
        timeStep = 0;
        blocked = false;
        color = Color.red;
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
        baseEnergy = 400 + (length * 200);
        energy = baseEnergy;
        this.facing = facing;
        dnaStep = 0;
        age = 0;
        maxAge = 200 + (length * 100);
        timeStep = 0;
        blocked = false;
        this.color = color;
        curled = true;
        reproduced = false;
        lastCondition = 0;
    }
    
    public static void setMutationRate(double set)
    {
    	mutationRate = set;
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
    
    /*
    public Color dnaToColor()
    {
        double red = .5;
        double green = .5;
        double blue = .5;
        
        for (int i = 0; i < length; i++)
        {
            for (int j = 0; j < genes.length; j++)
            {
                if (dna[i].equals(genes[j]))
                {
                    red += (1 / (Math.pow(2, i + 2))) * Math.cos(20 * Math.PI * (j + 3 * i) / genes.length);
                    green += (1 / (Math.pow(2, i + 2))) * Math.cos(20 * Math.PI * ((j + 3 * i) + 20 * genes.length) / genes.length);
                    blue += (1 / (Math.pow(2, i + 2))) * Math.cos(20 * Math.PI * ((j + 3 * i) + 40 * genes.length) / genes.length);
                }
            }
        }
        
        return new Color((float)red,(float)green,(float)blue);
    }
    */

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
       
        for (int i = 1; i <body.length; i++) {
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
        if (facing.equals("U"))  facing = "R";
        else if (facing.equals("R")) facing = "D";
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

    public Critter reproduce(Point point, double colorVar)
    {
        reproduced = true;
        ArrayList<String> newDNA = new ArrayList<String>(); 
        float newR = (float)(color.getRed() / 255.0);
        float newG = (float)(color.getGreen() / 255.0);
        float newB = (float)(color.getBlue() / 255.0);
        
        for (int j = 0 ; j < length ; j++) //For each gene of the dna sequence, check if it mutates or copies.
        {
            if (Math.random() < mutationRate / length) //If it mutates
            {
                int rand3 = (int)(Math.random() * 3); // Adjust the color of the new critter
                double rand = Math.random();
                if (rand < .5) rand -= 1;
                rand *= colorVar;
                if (rand3 == 0) {
                    newR += rand;
                    if (newR < .2) newR = (float).2;
                    else if (newR > 1) newR = 1;
                }
                else if (rand3 == 1) {
                    newG += rand;
                    if (newG < .2) newG = (float).2;
                    else if (newG > 1) newG = 1;
                }
                else {
                    newB += rand;
                    if (newB < .2) newB = (float).2;
                    else if (newB > 1) newB = 1;
                }
                
                int randGene = (int)(Math.random() * genes.length);
                
                rand3 = (int)(Math.random() * 3);
                // if (rand3 == 0) Delete gene. (Does nothing)
                if (rand3 == 1) { //Add gene. Randomly assign to left or right of original gene.
                    int rand2 = (int)(Math.random() * 2);
                    if (rand2 == 0) {
                        newDNA.add(genes[randGene]);
                        newDNA.add(dna[j]);
                    }
                    else { 
                        newDNA.add(dna[j]);
                        newDNA.add(genes[randGene]);
                    }
                }
                else if (rand3 == 2 || (rand3 == 0 && newDNA.size() == 0 && j == length - 1)) newDNA.add(genes[randGene]); //Replace gene. Also replaces if deleting makes the dna sequence empty
            }
            else newDNA.add(dna[j]); //If no mutation, copy as normal.
        }
        String newFacing;
         
        if (facing.equals("U"))  newFacing = "R";
        else if (facing.equals("R"))  newFacing = "D";
        else if (facing.equals("D"))  newFacing = "L";
        else newFacing = "U";
        
        String[] newDNAString = new String[newDNA.size()];
        int i = 0;
        boolean newElse = false;
        
        for (String j : newDNA) {
        	if (j.equals("e")) newElse = true;
            newDNAString[i] = j;
            i++;
        }
        Critter newCritter = new Critter(newDNAString, point.x,  point.y, newFacing, new Color(newR, newG, newB));
        
        spendEnergy(baseEnergy);
        newCritter.hasElse = newElse;
        return newCritter;
    }

}
