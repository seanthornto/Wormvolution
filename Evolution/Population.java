import java.awt.*;
/**
 * Write a description of class Population here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Population implements Comparable<Population>
{
    // instance variables - replace the example below with your own
    public int size;
    public Color color;
    public String[] dna;

    /**
     * Constructor for objects of class Population
     */
    public Population(Color color, String[] dna)
    {
        this.color = color;
        this.dna = dna;
        size = 1;
    }

    public void addMember()
    {
        size++;
    }
    
    public void removeMember()
    {
        size--;
    }
    
    public boolean equals(Population pop)
    {
        if(dna.length != pop.dna.length)
        {
            return false;
        }
        else
        {
            for (int i = 0; i < dna.length; i++)
            {
                if (!(dna[i].equals(pop.dna[i])))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(Population pop)
    {
        return Integer.compare(pop.size, size);
    }
}
