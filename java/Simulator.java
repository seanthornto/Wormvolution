/**
 * Write a description of class Simulator here.
 *
 * @author Sean Thornton and Sky Vercauteren
 * @version 1.0 December 2023
 */

import java.util.ArrayList;

import javax.swing.JScrollPane;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

/**
 * GENES:
 * ------
 *
 * M : moves one space in facing direction
 * Z : sleep
 * < : Turns counter clockwise
 * > : turns clockwise
 * U : faces up
 * D : faces down
 * L : faces left
 * R : faces right
 * H : hop - moves 2 spaces
 * A : attack - steals energy from critter in front of it
 * -------------
 * conditionals
 * -------------
 * v : OR conditionals XvY is true if either is true
 * *any two conditionals behave as AND*
 * r : restart at head
 * e : else - skip to next e or E on false condition
 * E : end - skip to end on false condition
 * C : loop conditional - go back to last condition
 * 0 : if there is food in sight
 * -0: if there is not food
 * 1 : if the critter was blocked
 * -1: if the critter was not blocked
 * 2 : if the energy is above half
 * -2: if the energy is not above half
 * 3 : if the age is above half
 * -3: if the age is not above half
 * 4 : if it has reproduced
 * -4: if it has not reproduced
 * 5 : if it can see a critter
 * -5:if it can't see a critter
 * 6 : if it can see a same species 
 * -6: if it want see a same species
 *
 * @author Sean Thornton and Sky Vercauteren
 * @version Version 1.0 November 2023
 */

public class Simulator {
	//Admin stuff
	private static final long serialVersionUID = 1L;

	//worm vars
	private ArrayList<Critter> critters;
	private int foodRate;
	private int foodValue;
	private int maxTimeSteps;
	private long speed;
	private int maxSize;
	private int sleepC;
	private int moveC;
	private int turnC;
	private double colorVar;
	private int sightRange;

	//frame vars 
	public Board board;
	private Point[][] oldZoomViews; // each element is a "view". [0] is most recent, [3] is the oldest. 
	private int zoomIndex; 			// the index which points to whichever element in the zoom history is currently selected.
	private Point[] currentZoomView;// a tuple of points representing a square segment of board space. say a "view"
	public PopulationDisplay popDisp;
	public int pixelSize;
	private int boardSize;
	
	//simulation vars
	private boolean paused = false;
	private boolean[][] isFood;
	private boolean[][] isBarrier;
	private Critter[][] isCritter;
	public int boardTick;
	private Color barrierColor;
	private ArrayList<Population> populations;
	private ArrayList<String> inactiveGenes;
	private long tickTime;
	public boolean popDispVis;
	public boolean autoRefPop;

	// ------------------------------CONSTRUCTOR-------------------------------
	//needs board size, max size constraint, sleep cost, move cost and turn cost
	public Simulator(int bs, int max, int sC, int mC, int tC) {

		//set board vars
		boardSize = bs;
		maxSize = max;
		pixelSize = max / boardSize;
		barrierColor = Color.GRAY;
		board = new Board(pixelSize, bs, max);
		currentZoomView = new Point[2];
		currentZoomView[0] = new Point(0,0); currentZoomView[1] =new Point(boardSize,boardSize);
		oldZoomViews = new Point[4][2]; // the zoom memory is set to have 4 buffer slots.
		for(int i=0; i<4; i++)
		{
			oldZoomViews[i] = currentZoomView;
		}
		zoomIndex = 0;
		barrierColor = Color.gray;
		boardTick = 1;

		//set worm vars
		setSleepCost(sC);
		setMoveCost(mC);
		setTurnCost(tC);
		critters = new ArrayList<Critter>();
		populations = new ArrayList<Population>();
		inactiveGenes = new ArrayList<String>();

		//hard coded initial worm values
		this.foodRate = bs * bs / 200;
		this.foodValue = 50;
		maxTimeSteps = 10;
		this.speed = 30;
		colorVar = 0.5;
		sightRange = 10;
		
		// painting the initial board.
		isFood = new boolean[bs][bs];
		isCritter = new Critter[bs][bs];
		isBarrier = new boolean[bs][bs];
		popDispVis = false;
		for (int i = 0; i < bs; i++) {
			for (int j = 0; j < bs; j++) {
				isFood[i][j] = false;
				isBarrier[i][j] = false;
				isCritter[i][j] = null;
			}
		}
	}
	//helper
	public String toString(String[] string) {
		String newString = new String();
		for (int i = 0; i < string.length; i++)
			newString += string[i];
		return newString;
	}

	// clock controlls
	public void pause() {
		paused = true;
	}

	public void unpause() {
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public void boardTick() {
		if (boardTick == 100) {
			boardTick = 0;
		} else {
			boardTick++;
		}
	}

	
	//worm controlls. (getters and setters)
	//sleep, move, turn cost
	public void setSleepCost(int sC) {
		sleepC = sC;
	}

	public int getSleepCost()
	{
		return this.sleepC;
	}

	public void setMoveCost(int mC) {
		moveC = mC;
	}

	public int getMoveCost()
	{
		return this.moveC;
	}

	public void setTurnCost(int tC) {
		turnC = tC;
	}

	public int getTurnCost()
	{
		return this.turnC;
	}
	
	// Methods used by refresh()
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	
	public long getSpeed()
	{
		return this.speed;
	}

	public void setFoodRate(int foodRate) {
		this.foodRate = foodRate;
	}
	
	public int getFoodRate()
	{
		return this.foodRate;
	}

	public void setFoodValue(int foodValue) {
		this.foodValue = foodValue;
	}
	
	public int getFoodValue()
	{
		return this.foodValue;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public double getColorVar() {
		return colorVar;
	}

	public void setColorVar(double cVar) {
		colorVar = cVar;
	}

	public int getSightRange() {
		return sightRange;
	}

	public void setSightRange(int sightR) {
		sightRange = sightR;
	}

	public void addInactiveGene(String gene) {
		inactiveGenes.add(gene);
	}

	public void removeInactiveGene(String gene) {
		inactiveGenes.remove(gene);
	}

	public ArrayList<String> getInactiveGenes() {
		return inactiveGenes;
	}

	// --------------------------------NEXT SPACE- ("forsight") -------------------------
	//methods to check the next space  for food, barriers, and other critters
	//Add food, barriers,
	//move and get worm locations
	
	public boolean isFood(int x, int y) {
		if (x < 0) {
			x += boardSize;
		}
		if (x > boardSize - 1) {
			x -= boardSize;
		}
		if (y < 0) {
			y += boardSize;
		}
		if (y > boardSize - 1) {
			y -= boardSize;
		}
		return isFood[x][y];
	}

	public boolean isFood(Point point) {
		return isFood(point.x, point.y);
	}

	public boolean isBarrier(int x, int y) {
		if (x < 0) {
			x += boardSize;
		}
		if (x > boardSize - 1) {
			x -= boardSize;
		}
		if (y < 0) {
			y += boardSize;
		}
		if (y > boardSize - 1) {
			y -= boardSize;
		}
		return isBarrier[x][y];
	}

	public boolean isBarrier(Point point) {
		return isBarrier(point.x, point.y);
	}

	public boolean isCritter(int x, int y) {
		if (x < 0) {
			x += boardSize;
		}
		if (x > boardSize - 1) {
			x -= boardSize;
		}
		if (y < 0) {
			y += boardSize;
		}
		if (y > boardSize - 1) {
			y -= boardSize;
		}
		return !(isCritter[x][y] == null);
	}

	public boolean isCritter(Point point) {
		return isCritter(point.x, point.y);
	}

	//world state (should these be in "board"?)
	public void addCritterPoint(Point point, Critter critter) {
		isCritter[point.x][point.y] = critter;
		board.draw(point, critter.getColor());

	}

	public void addCritterPoint(int x, int y, Critter critter) {
		addCritterPoint(new Point(x, y), critter);
	}

	public void removeCritterPoint(Point point) {
		isCritter[point.x][point.y] = null;
		board.erase(point);
	}

	public void removeCritterPoint(int x, int y) {
		removeCritterPoint(new Point(x, y));
	}

	public Critter getCritterPoint(Point point) {
		return isCritter[point.x][point.y];
	}

	public Critter getCritterPoint(int x, int y) {
	    if (x < 0) {
            x += boardSize;
        }
        if (x > boardSize - 1) {
            x -= boardSize;
        }
        if (y < 0) {
            y += boardSize;
        }
        if (y > boardSize - 1) {
            y -= boardSize;
        }
		return getCritterPoint(new Point(x, y));
	}

	public void addFood(int x, int y) {
		if (!isBarrier(x, y) && !isCritter(x, y)) {
			isFood[x][y] = true;
			board.draw(new Point(x, y), Color.green);
		}
	}

	public void addFood(Point point) {
		addFood(point.x, point.y);
	}

	public void removeFood(int x, int y) {
		isFood[x][y] = false;
	}

	public void removeFood(Point point) {
		removeFood(point.x, point.y);
	}

	public boolean lookFood(int x, int y, String facing) {
		if (facing.equals("U")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isFood(x, y - i))
					return true;
				if (isBarrier(x, y - i) || isCritter(x, y - i))
					return false;
			}
			return false;
		} else if (facing.equals("D")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isFood(x, y + i))
					return true;
				if (isBarrier(x, y + i) || isCritter(x, y + i))
					return false;
			}
			return false;
		} else if (facing.equals("L")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isFood(x - i, y))
					return true;
				if (isBarrier(x - i, y) || isCritter(x - i, y))
					return false;
			}
			return false;
		} else {
			for (int i = 1; i <= sightRange; i++) {
				if (isFood(x + i, y))
					return true;
				if (isBarrier(x + i, y) || isCritter(x + i, y))
					return false;
			}
			return false;
		}
	}

	public boolean lookBarrier(int x, int y, String facing) {
		if (facing.equals("U")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x, y - i))
					return true;
				if (isCritter(x, y - i))
					return false;
			}
		} else if (facing.equals("D")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x, y + i))
					return true;
				if (isCritter(x, y + i))
					return false;
			}
		} else if (facing.equals("L")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x - i, y))
					return true;
				if (isCritter(x - i, y))
					return false;
			}
		} else {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x + i, y))
					return true;
				if (isCritter(x + i, y))
					return false;
			}
		}
		return false;
	}

	public Critter lookCritter(int x, int y, String facing) {
		if (facing.equals("U")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x, y - i))
					return null;
				if (isCritter(x, y - i))
					return getCritterPoint(x, y - i);
			}
		} else if (facing.equals("D")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x, y + i))
					return null;
				if (isCritter(x, y + i))
					return getCritterPoint(x, y + i);
			}
		} else if (facing.equals("L")) {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x - i, y))
					return null;
				if (isCritter(x - i, y))
					return getCritterPoint(x - i, y);
			}
		} else {
			for (int i = 1; i <= sightRange; i++) {
				if (isBarrier(x + i, y))
					return null;
				if (isCritter(x + i, y))
					return getCritterPoint(x + i, y);
			}
		}
		return null;
	}



	// -------------------------------------------------------------------------
		//DNA SEQUENCE EXECUTION
	// -------------------------------------------------------------------------
	//should these be in the critter class themselves?

	public void addCritter(String dna) {
		Critter critter = new Critter(dna, (int) (Math.random() * boardSize), (int) (Math.random() * boardSize));
		addCritter(critter);
		board.repaint();
	}

	public void addCritter(String dna, int number) {
		for (int i = 0; i < number; i++)
			addCritter(dna);
	}

	public void addCritter(Critter critter) {
		critters.add(critter);
		addCritterPoint(critter.getBody()[0], critter);
		board.draw(critter.getBody()[0], critter.getColor());
	}

	public void moveCritter(Critter critter) {
		Point newSpace;
		Point[] body = critter.getBody();
		int length = body.length;
		int x = body[0].x;
		int y = body[0].y;
		String facing = critter.getFacing();
		Point lastPoint = body[length - 1];

		if (facing.equals("U")) {
			if (y == 0)
				y = boardSize;
			newSpace = new Point(x, y - 1);
		} else if (facing.equals("D")) {
			newSpace = new Point(x, (y + 1) % boardSize);
		} else if (facing.equals("L")) {
			if (x == 0)
				x = boardSize;
			newSpace = new Point(x - 1, y);
		} else
			newSpace = new Point((x + 1) % boardSize, y);

		if (!isBarrier(newSpace) && !isCritter(newSpace)) {
			if (critter.curled > 0)
				critter.curled--;
			else {
				removeCritterPoint(lastPoint);
				if (critter.getEnergy() >= critter.getBaseEnergy() * 2)
					critterReproduce(critter, lastPoint);
			}

			critter.move(newSpace);
			addCritterPoint(newSpace, critter);
			critter.setBlocked(false);
		} else
			critter.setBlocked(true);
	}

	public void critterAttack(Critter critter) {
		Point attackSpace;
		Point[] body = critter.getBody();
		int x = body[0].x;
		int y = body[0].y;
		String facing = critter.getFacing();

		if (facing.equals("U")) {
			if (y == 0)
				y = boardSize;
			attackSpace = new Point(x, y - 1);
		} else if (facing.equals("D")) {
			attackSpace = new Point(x, (y + 1) % boardSize);
		} else if (facing.equals("L")) {
			if (x == 0)
				x = boardSize;
			attackSpace = new Point(x - 1, y);
		} else
			attackSpace = new Point((x + 1) % boardSize, y);

		Critter attacked = getCritterPoint(attackSpace);

		if (attacked != null) {
			if (attacked.length * 2 <= critter.length) {
				critter.addEnergy(attacked.getEnergy());
				attacked.setEnergy(0);
			} else {
				critter.addEnergy(moveC * 10);
				attacked.spendEnergy(moveC * 10);
			}
		}
	}

	public void critterReproduce(Critter critter, Point lastPoint) {
		Critter newCritter = critter.reproduce(lastPoint, colorVar);
		Population pop = new Population(newCritter.getColor(), toString(newCritter.getDNA()));
		Population parPop = new Population(critter.getColor(), toString(critter.getDNA()));
		boolean exists = false;
		boolean parFound = false;
		boolean different = !(pop.equals(parPop));
		for (Population p : populations) {
			if (different && !parFound && parPop.equals(p)) {
				parPop = p;
				parFound = true;
			}
			if (pop.equals(p)) {
				p.addMember();
				newCritter.setColor(p.color);
				exists = true;
				break;
			}
		}
		if (!exists) {
			pop.addAncestors(parPop);
			populations.add(pop);
		}

		addCritter(newCritter);
	}

	public void findElse(Critter critter) {
		if (critter.hasElse) {
			int j = critter.getStep();
			boolean isElse = false;
			String[] dna = critter.getDNA();
			while (j < dna.length) {
				if (dna[j].equalsIgnoreCase("e") || dna[j].equals("C")) {
					isElse = true;
					break;
				}
				j++;
			}
			if (isElse)
				critter.goToStep(j + 1);
			else
				critter.goToStep(0);
		} else
			critter.goToStep(0);
	}

	public void findEnd(Critter critter) {
		int j = critter.getStep();
		boolean isEnd = false;
		String[] dna = critter.getDNA();
		while (j < dna.length) {
			if (dna[j].equals("E") || dna[j].equals("C")) {
				isEnd = true;
				break;
			}
			j++;
		}
		if (isEnd)
			critter.goToStep(j + 1); // Go to step after END
		else
			critter.goToStep(0);
	}

	public void checkOr(Critter critter) {
		int step = critter.getStep();
		String[] dna = critter.getDNA();
		if (step + 2 < dna.length) {
			if (dna[step + 1].equals("v")) { // If next step is an OR
				try {
					int temp = Integer.parseInt(dna[step + 2]); // Test if the step after OR is a conditional
					critter.goToStep(step + 2); // If it is, go to that step
				} catch (Exception e) {
					findElse(critter); // Otherwise, proceed as normal
				}
			} else
				findElse(critter); // No OR, proceed as normal
		} else
			findElse(critter);
	}

	public void skipOr(Critter critter) {
		int step = critter.getStep() + 1;
		String[] dna = critter.getDNA();
		boolean done = false;
		boolean lastOr = false;
		while (!done && step < dna.length) {
			if (dna[step].equals("v")) { // If this step is OR
				lastOr = true;
				step++; // Skips through all ORs.
			} else if (lastOr) // If last step was OR
			{
				lastOr = false;
				try {
					int temp = Integer.parseInt(dna[step]); // Tests if step is a conditional
					step++; // If it is, skips step
				} catch (Exception e) {
					critter.goToStep(step); // Otherwise, go to this step
					done = true;
				}
			} else // If neither this step nor last step were OR
			{
				critter.goToStep(step); // Go to this step
				done = true;
			}
		}
		if (step == dna.length) {
			critter.goToStep(0);
		}
	}

	public void critterTimeStep(Critter critter) // This is a recursive function, calling itself until the critter takes
													// an action. (Move, Sleep, Turn)
	{
		if (critter.getTimeStep() >= maxTimeSteps) { // Check if critter has gone too many steps without taking an action
			critter.sleep();
			critter.spendEnergy(sleepC);
			return;
		}

		critter.timeStep();
		String[] dna = critter.getDNA();
		int step = critter.getStep();
		String facing = critter.getFacing();
		Point[] body = critter.getBody();

		if (inactiveGenes.contains(dna[step])) { // Checks if the current gene is an inactive one.
			critter.nextStep();
			critterTimeStep(critter);
		} else if (dna[step].equals("M")) {
			moveCritter(critter);
			critter.nextStep();
			Point point = critter.getBody()[0];
			if (isFood(point)) {
				critter.addEnergy(foodValue);
				removeFood(point);
			}
			critter.spendEnergy(moveC);
		}

		else if (dna[step].equals("H")) {
			moveCritter(critter);
			Point point = critter.getBody()[0];
			if (isFood(point)) {
				critter.addEnergy(foodValue);
				removeFood(point.x, point.y);
			}

			moveCritter(critter);
			point = critter.getBody()[0];
			if (isFood(point)) {
				critter.addEnergy(foodValue);
				removeFood(point.x, point.y);
			}

			critter.nextStep();
			critter.spendEnergy(2 * moveC);
		}

		else if (dna[step].equals("Z")) {
			critter.sleep();
			critter.nextStep();
			critter.spendEnergy(sleepC);
		}

		else if (dna[step].equals(">")) {
			critter.turnRight();
			critter.nextStep();
			critter.spendEnergy(turnC);
		}

		else if (dna[step].equals("<")) {
			critter.turnLeft();
			critter.nextStep();
			critter.spendEnergy(turnC);
		}

		else if (dna[step].equals("U")) {
			critter.changeFacing("U");
			critter.nextStep();
			critter.spendEnergy(turnC);
		}

		else if (dna[step].equals("R")) {
			critter.changeFacing("R");
			critter.nextStep();
			critter.spendEnergy(turnC);
		}

		else if (dna[step].equals("D")) {
			critter.changeFacing("D");
			critter.nextStep();
			critter.spendEnergy(turnC);
		}

		else if (dna[step].equals("L")) {
			critter.changeFacing("L");
			critter.nextStep();
			critter.spendEnergy(turnC);
		}

		else if (dna[step].equals("A")) {
			critterAttack(critter);
			critter.nextStep();
			critter.spendEnergy(moveC * 5);
		}

		else if (dna[step].equals("r")) {
			critter.goToStep(0);
			critterTimeStep(critter);
		}

		else if (dna[step].equals("e")) {
			findEnd(critter);
			critterTimeStep(critter);
		}

		else if (dna[step].equals("E") || dna[step].equals("v")) {
			critter.nextStep();
			critterTimeStep(critter);
		}

		else if (dna[step].equals("C")) {
			critter.goToLastCondition();
			critterTimeStep(critter);
		}

		else if (dna[step].equals("0")) { // If the critter sees food
			if (lookFood(body[0].x, body[0].y, facing))
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("-0")) { // If the critter does not see food
			if (!lookFood(body[0].x, body[0].y, facing))
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("1")) { // If the critter is blocked
			if (critter.isBlocked())
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("-1")) {// If the critter is not blocked
			if (!(critter.isBlocked()))
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("2")) {
			if (critter.getEnergy() >= critter.getBaseEnergy())
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("-2")) {
			if (critter.getEnergy() < critter.getBaseEnergy())
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("3")) {
			if (critter.getAge() >= critter.getMaxAge() / 2)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("-3")) {
			if (critter.getAge() < critter.getMaxAge() / 2)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("4")) {
			if (critter.reproduced)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		} else if (dna[step].equals("-4")) {
			if (!critter.reproduced)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		}

		else if (dna[step].equals("5")) {
			Critter lookCritter = lookCritter(body[0].x, body[0].y, facing);
			if (lookCritter != null && lookCritter != critter)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		}

		else if (dna[step].equals("-5")) {
			Critter lookCritter = lookCritter(body[0].x, body[0].y, facing);
			if (lookCritter == null || lookCritter == critter)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		}

		else if (dna[step].equals("6")) {
			Critter lookCritter = lookCritter(body[0].x, body[0].y, facing);
			if (lookCritter != null && lookCritter.getDNA().equals(critter.getDNA()) && lookCritter != critter)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		}

		else if (dna[step].equals("-6")) {
			Critter lookCritter = lookCritter(body[0].x, body[0].y, facing);
			if (lookCritter == null || !lookCritter.getDNA().equals(critter.getDNA()) || lookCritter == critter)
				skipOr(critter);
			else
				checkOr(critter);
			critter.setLastCondition(step);
			critterTimeStep(critter);
		}

	}
	
	// -------------------------------------------------------------------------
	//BARRIER STUFF
	// -------------------------------------------------------------------------
	//(should this be in the board object?)

	public void setBarrierColor(Color color)
	{
		barrierColor = color;
	}
	public Color getBarrierColor()
	{
		return barrierColor;
	}
	public void setAllBarrierColor(Color color) {
		barrierColor = color;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++)
				if (isBarrier[i][j])
					board.draw(new Point(i, j), color);
		}
	}
	
	//used only by other barrier tools
	private void addBarrier(Point point) {
		if (point.x < 0 || point.y < 0 || point.x >= boardSize || point.y >= boardSize)
			return;
		if (!isCritter(point)) {
			if (isFood(point)){removeFood(point);}
			for(int i = point.x; i < point.x + board.getBarrierWidth(); i++)
			{
				for(int j = point.y; j < point.y + board.getBarrierWidth(); j++)
				{
					if(i < board.getWidth() && j < board.getHeight())
					{
						isBarrier[i][j] = true;
						board.draw(new Point(i,j), barrierColor);
					}
				}
			}
		}
	}
	
	//used only by other barrier tools
	public void removeBarrier(Point point) {
		if (point.x < 0 || point.y < 0 || point.x >= boardSize || point.y >= boardSize)
			return;
		if (isBarrier[point.x][point.y] == true) {
			isBarrier[point.x][point.y] = false;
			board.erase(point);
		}
	}
	
	//creates a small "plus" shaped barrier TODO: other shapes?
	public void addBarrierStamp(Point point) {
		if (point.x < 0 || point.y < 0 || point.x >= boardSize || point.y >= boardSize) {return;}
		
		//translate the whole thing so the mouse click will represent the center of the stamp.
		int w = board.getBarrierWidth();
		point.x = point.x - (w/2);
		point.y = point.y - (w/2);
		
		//expand to a 3 x 3 grid of points instead of 1x1.
		int minX = point.x - w; //leftmost point of quadrant 1, 4, 7
		int minY = point.y - w; //topmost point of quadrant 1, 2, 3
								//point = center x and y. top left of quadrant 5
		int midX = point.x + w; //leftmost of quadrant 3,6,9
		int midY = point.y + w; //topmost of quadrant 7,8,9
								//midX + w = rightmost point of quadrant 3, 6, 9
								//midY + w = bottommost point of quadrant 7,8,9
		for(int i = minX; i < midX + w; i++)
		{
			for(int j = minY; j < midY + w; j++)
			{
				if(i < board.getWidth() && j < board.getHeight())
				{
					if(j < point.y && i >= point.x && i < midX)//The top square of the plus.
					{
						isBarrier[i][j] = true;
						board.draw(new Point(i,j), barrierColor);
					}
					else if(j >= point.y && j < midY) //the full middle section
					{
						isBarrier[i][j] = true;
						board.draw(new Point(i,j), barrierColor);
					}
					else if(j >= midY && i >= point.x && i < midX)//the bottom square of the plus
					{
						isBarrier[i][j] = true;
						board.draw(new Point(i,j), barrierColor);
					}
					
				}
			}
		}
	}
	

	public void addBarrierLine(Point a, Point b) {
		if (Math.abs(a.y - b.y) <= Math.abs(a.x - b.x)) { // If slope is less than 1, iterate on x and add to y.
			if (a.x > b.x) { // If x1 > x2, switch values.
				Point temp = a;
				a = b;
				b = temp;
			}
			
			double slope = 1.0 * (a.y - b.y) / (a.x - b.x);
			int x = a.x;
			double y = a.y;
			while (x <= b.x) {
				Point point = new Point(x, (int) (y + .5));
				if (!isBarrier(point) && !isCritter(point))
				{
					addBarrier(point);
				}
				x++;
				y += slope;
			}
		} else // If slope is greater than 1, iterate on y and add to x.
		{
			if (a.y > b.y) { // If x1 > x2, switch values.
				Point temp = a;
				a = b;
				b = temp;
			}
			double slope = 1.0 * (a.x - b.x) / (a.y - b.y);
			double x = a.x;
			int y = a.y;

			while (y <= b.y) {
				Point point = new Point((int) (x + .5), y);
				if (!isBarrier(point) && !isCritter(point))
				{
					addBarrier(point);
				}
				y++;
				x += slope;
			}
		}
	}
	
	public void addBarrierRect(Point a, Point b)
	{
		Point[] slope = justifySlope(a,b);
		a = slope[0];
		b = slope[1];
		
		for(int i=a.x; i <= b.x; i++)
		{
			for (int j=a.y; j<= b.y; j++)
			{
				Point p = new Point(i,j);
				if(i == a.x || i == b.x){board.draw(p,barrierColor); addBarrier(p);} //the left and right sides of the rectangle
				if(j == a.y || j == b.y) {board.draw(p,barrierColor); addBarrier(p);} //the top and bottom of the rectangle
			}
		}
	}

	public void addBarrierGraph(Point a, Point b) {
		Point[] slope = justifySlope(a,b);
		a = slope[0];
		b = slope[1];
		
		int i = a.x;
		int j = a.y;

		while (i < b.x) {
			while (j < b.y) {
				addBarrier(new Point(i, j));
				j += 2;
			}
			j = a.y;
			i += 2;
		}
	}

	public void addBarrierGraph(Point a, Point b, int xSpace, int ySpace, int xOff, int yOff) {
		Point[] slope = justifySlope(a,b);
		a = slope[0];
		b = slope[1];
		
		// Don't ask how any of this works.
		int i = 0;
		int j = 0;
		int xRange = b.x - a.x;
		int yRange = b.y - a.y;

		while (i < xRange) {
			while (j < yRange) {
				addBarrier(new Point(a.x + i + (xOff * ((j / (ySpace + 1)) % (xSpace + 1))), a.y + j)); // Yeah, this look s like math
				j += ySpace + 1;
			}
			i += xSpace + 1;
			j = yOff * (i / (xSpace + 1)) % (ySpace + 1);
		}
	}

	public void removeBarrierLine(Point a, Point b) {

		if (Math.abs(a.y - b.y) <= Math.abs(a.x - b.x)) {
			if (a.x > b.x) { // If x1 > x2, switch values.
				Point temp = a;
				a = b;
				b = temp;
			}
			double slope = 1.0 * (a.y - b.y) / (a.x - b.x);
			int x = a.x;
			double y = a.y;

			while (x <= b.x) {
				Point point = new Point(x, (int) (y + .5));
				if (isBarrier(point)) {
					removeBarrier(point);
				}
				x++;
				y += slope;
			}
		} else {

			if (a.y > b.y) { // If x1 > x2, switch values.
				Point temp = a;
				a = b;
				b = temp;
			}
			double slope = 1.0 * (a.x - b.x) / (a.y - b.y);
			double x = a.x;
			int y = a.y;

			while (y <= b.y) {
				Point point = new Point((int) (x + .5), y);
				if (isBarrier(point)) {
					removeBarrier(point);
				}
				y++;
				x += slope;
			}
		}
	}

	public void removeBarrierRect(Point a, Point b) {
		Point[] slope = justifySlope(a,b);
		a = slope[0];
		b = slope[1];

		int i = a.x;
		int j = a.y;

		while (i <= b.x) {
			while (j <= b.y) {
				removeBarrier(new Point(i, j));
				j++;
			}
			i++;
			j = a.y;
		}
	}
	
	//helper that returns the slope of a rectangle so that point A << point B
	private Point[] justifySlope(Point a, Point b)
	{
		Point temp1 = new Point(a.x,a.y);
		Point temp2 = new Point(b.x,b.y);
		
		
		if(a.x > b.x)
		{
			temp1.x = b.x; 
			temp2.x = a.x;
		}
		if(a.y>b.y)
		{
			temp1.y = b.y; 
			temp2.y = a.y;
		}
		
		Point[] line = {temp1,temp2};
		return line;
	}
	
	
	//-------------------------------------------------------------------------
	//ZOOM STUFF
	//-------------------------------------------------------------------------
	// should this be in the board stuff?
	
	// stores the current view in the zoom history
	public void storeZoom(Point p1, Point p2)
	{
		// push all the elements down by one and discard the last element.
		oldZoomViews[3] = oldZoomViews[2].clone();
		oldZoomViews[2] = oldZoomViews[1].clone();
		oldZoomViews[1] = oldZoomViews[0].clone();

		// set the new zoom as the current, and whatever was current as old
		oldZoomViews[0] = currentZoomView.clone();
		currentZoomView[0] = p1;
		currentZoomView[1] = p2;
	}

	//displays the zoom view either forward or backward in the zoom history.
	public void recallZoom(int step)
	{
		// "next" if at 4, or "back" if at 0 wont do anything.
		if((zoomIndex + step) <4 && (zoomIndex + step > -1))
		{
			zoomIndex += step;
		}
		zoom(oldZoomViews[zoomIndex][0], oldZoomViews[zoomIndex][1]);
	}

	public void zoom(Point p1, Point p2)
	{
		// find the new origin and figure the scale
		double scale = 0;
		board.setZoomed(true);
		scale = (double)(boardSize) / ((p2.y) - (p1.y));
		board.setOrigin(p1.x, p1.y);
		board.setScale(scale);
		board.revalidate();
	}
	

	// -------------------------------------------------------------------------
	//GAME STUFF//ENVIRONMENT STUFF
	//--------------------------------------------------------------------------
	// Idk I think maybe the clock should be its own class. 
	
	public void gameTimeStep() {
		long startTime = 0;
		if (!paused) {
			boardTick();
			if (boardTick == 0) {
				startTime = System.nanoTime();
				if (popDispVis) {
					popDisp.refresh(getTopPopulations());
				}
			}
			int i = 0;

			while (i < critters.size()) {
				Critter iCritter = critters.get(i);
				if(iCritter != null) 
				{
					critterTimeStep(iCritter);
					iCritter.age();
					int iCritterEnergy = iCritter.getEnergy();
					Point[] iBody = iCritter.getBody();
					int length = iBody.length;
					if (iCritterEnergy <= 0 || iCritter.getAge() >= iCritter.getMaxAge()) {
					    if (foodValue == 0) 
					        foodValue = 1;
						int foodLeft = iCritterEnergy / foodValue;
						for (int j = 0; j < length; j++) {
							removeCritterPoint(iBody[j]);
							if (j < foodLeft) {
								addFood(iBody[j]);
							} else {
								board.erase(iBody[j]);
							}

						}
						Population pop = new Population(iCritter.getColor(), toString(iCritter.getDNA()));
						for (Population p : populations) {
							if (pop.equals(p)) {
								p.removeMember();
								if (p.size <= 0) {
									populations.remove(p);
								}
								break;
							}
						}
						critters.remove(i);
					} else {
						i++;
					}
				}

			}
			for (int j = 0; j < foodRate; j++) {
				addFood((int) (Math.random() * boardSize), (int) (Math.random() * boardSize));
			}

		}
		board.repaint();
		hold(speed);
		if (paused) {
			hold(250000000);
		}
		if (boardTick == 0) {
			long endTime = System.nanoTime();
			tickTime = endTime - startTime;
			if (popDispVis)
				popDisp.refreshTickSpeed(tickTime);
		}
	}

	public void gameTimeStep(int steps) {
		for (int i = 0; i < steps; i++)
			{
			gameTimeStep();
			}
	}

	public void hold(long nano) {
		try {
			long milliseconds = nano / 1000000;
			int nanoseconds = (int) (nano - milliseconds * 1000000);
			Thread.sleep(milliseconds, nanoseconds);
		} catch (Exception e) {
			// ignoring exception at the moment
		}
	}

	public void dispPopulations() {
		popDisp = new PopulationDisplay();
		popDisp.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				popDispVis = false;
			}
		});
		popDispVis = true;
	}

	public Population[] getTopPopulations() {
		Population[] top = new Population[30];
		Collections.sort(populations);
		for (int i = 0; i < 30; i++) {
			if (i < populations.size()) {
				top[i] = populations.get(i);
			} else {
				String dna = "";
				top[i] = new Population(Color.gray, dna);
			}

		}
		return top;
	}
}
