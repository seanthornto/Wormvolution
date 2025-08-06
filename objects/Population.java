import java.awt.*;
import java.util.ArrayList;

/**
 * Write a description of class Population here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Population implements Comparable<Population> {
	// instance variables - replace the example below with your own
	public int size;
	public Color color;
	public String dna;
	public ArrayList<String> ancestors;

	/**
	 * Constructor for objects of class Population
	 */
	public Population(Color color, String dna) {
		this.color = color;
		this.dna = dna;
		size = 1;
		ancestors = new ArrayList<String>();
	}

	public void addMember() {
		size++;
	}

	public void removeMember() {
		size--;
	}

	public void addAncestor(String ancestor) {
		ancestors.add(ancestor);
		if (ancestors.size() > 10)
			ancestors.remove(0);
	}

	public void addAncestors(Population pop) {
		for (String anc : pop.ancestors) {
			addAncestor(anc);
		}
		addAncestor(pop.dna);
	}

	public boolean equals(Population pop) {
		return dna.equals(pop.dna);
	}

	@Override
	public int compareTo(Population pop) {
		return Integer.compare(pop.size, size);
	}
}
