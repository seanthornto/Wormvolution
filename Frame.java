/**
 * Write a description of class Critter here.
 *
 * @author Sean Thornton and Sky Vercauteren
 * @version 1.0 December 2023
 */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class Frame extends JFrame implements MouseListener, MouseMotionListener {
	public Simulator sim;
	public int bs;
	public Point p1;
	public Point p2;
	public boolean p; //drop point barrier
	public boolean l; //draw line barrier
	public boolean g; // drag rectangle graph barrier
	public boolean e; // erase barrier
	public boolean z; // zoom boolean
	public boolean dragging;
	public Point zPoint1;
	public Point zPoint2;
	public int x1; 
	public int x2; 
	public int y1;
	public int y2;
	public int boardX;
	public int boardY;
	public int pixelSize;
	public int xSpace;
	public int xOff;
	public int ySpace;
	public int yOff;

	public Frame(String title) {
		super(title);
		addMouseListener(this);
		addMouseMotionListener(this);
		dragging = false;
	}

	@Override
	public void mouseClicked(MouseEvent event) {

	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		Point point = event.getPoint();
		p1 = point;
		dragging = true;
		adjustP1(p1);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		Point point = event.getPoint();
		p2 = point;
		adjustP2(point);
		dragging = false;
		if (p == true) {
			sim.addBarrierStamp(p1);
		} else if (l == true) {
			sim.addBarrierLine(p1, p2);
		} else if (g == true) {
			sim.addBarrierGraph(p1, p2, xSpace, ySpace, xOff, yOff);
		} else if (e == true) {
			sim.removeBarrierRect(p1, p2);
		} else if (z == true) {
			sim.zoom(zPoint1, zPoint2);
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		Point point = event.getPoint();
		adjustP2(point);
		
		//handle zoom rectangle
		if(z==true)
		{
			drawSquare(p1,p2, Color.WHITE);	
		}else if(g==true)
		{
			drawRectangle(p1,p2, Color.DARK_GRAY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void lTrue() {
		l = true;
		p = false;
		g = false;
		e = false;
		z = false;
	}

	public void pTrue() {
		p = true;
		l = false;
		g = false;
		e = false;
		z = false;
	}

	public void gTrue() {
		g = true;
		p = false;
		l = false;
		e = false;
		z = false;
	}

	public void eTrue() {
		e = true;
		p = false;
		g = false;
		l = false;
		z = false;
	}
	
	public void zTrue() {
		z = true;
		e = false;
		p = false;
		g = false;
		l = false;
	}
	
	public void allFalse() {
		e = false;
		p = false;
		g = false;
		l = false;
		z = false;
	}
	
	//adjusts the mouse event coordinates to line up with the simulated canvas.
	public void adjustP1(Point point)
	{
		p1 = point;
		boardX = sim.board.getX();
		boardY = sim.board.getY();
		boardX += this.getContentPane().getX();
		boardY += this.getContentPane().getY();
		p1.x -= boardX + 8;
		p1.y -= boardY + 32;
		x1 = p1.x / pixelSize;
		y1 = p1.y / pixelSize;
		p1.x = x1;
		p1.y = y1;
	}
	public void adjustP2(Point point)
	{
		p2 = point;
		boardX = sim.board.getX();
		boardY = sim.board.getY();
		boardX += this.getContentPane().getX();
		boardY += this.getContentPane().getY();
		p2.x -= boardX + 8;
		p2.y -= boardY + 32;
		x2 = p2.x / pixelSize;
		y2 = p2.y / pixelSize;
		p2.x = x2;
		p2.y = y2;
	}
	
	//draws a rectangle from point a to point b in given color.
	public void drawRectangle(Point a, Point b, Color color)
	{
		//flip rectangle so loops make sense. p1<p2 //WITHOUT changing the actual p1 or p2. 
		Point temp1 = new Point(a.x, a.y);
		Point temp2 = new Point(b.x, b.y);
		if (temp1.x > temp2.x) {
			temp1.x = b.x;
			temp2.x = a.x;
		}
		if (temp1.y > temp2.y) {
			temp1.y = b.y;
			temp2.y = a.y;
		}
		
		//paint it
		for(int i=temp1.x; i <= temp2.x; i++)
		{
			for (int j=temp1.y; j<= temp2.y; j++)
			{
				Point p = new Point(i,j);
				if(i == temp1.x || i == temp2.x){sim.board.draw(p,color);} //the left and right sides of the rectangle
				if(j == temp1.y || j == temp2.y) {sim.board.draw(p,color);} //the top and bottom of the rectangle
			}
		}
	}
	//draws a square based on change in y from point a to point b in a given color
	public void drawSquare(Point a, Point b, Color color)
	{
		//flip rectangle so loops make sense. p1<p2 //WITHOUT changing the actual p1 or p2. 
		Point temp1 = new Point(a.x, a.y);
		Point temp2 = new Point(b.x, b.y);
		if (temp1.x > temp2.x) {
			temp1.x = b.x;
			temp2.x = a.x;
		}
		if (temp1.y > temp2.y) {
			temp1.y = b.y;
			temp2.y = a.y;
		}
		
		//constrain the rectangle to be a square based on y distance change. 
		if(a.x < b.x) {temp2.x = temp1.x + (temp2.y - temp1.y);}
		else {temp1.x = temp2.x - (temp2.y - temp1.y); }
		
		//steal these for when zoom is using this method.
		zPoint1 = temp1;
		zPoint2 = temp2;
		
		//paint it
		for(int i=temp1.x; i <= temp2.x; i++)
		{
			for (int j=temp1.y; j<= temp2.y; j++)
			{
				Point p = new Point(i,j);
				if(i == temp1.x || i == temp2.x){sim.board.draw(p,color);} //the left and right sides of the rectangle
				if(j == temp1.y || j == temp2.y) {sim.board.draw(p,color);} //the top and bottom of the rectangle
			}
		}
		
	}

}