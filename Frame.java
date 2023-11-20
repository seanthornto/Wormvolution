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
	public boolean p;
	public boolean l;
	public boolean g;
	public boolean e;
	public boolean dragging;
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
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		Point point = event.getPoint();
		p2 = point;
		boardX = sim.board.getX();
		boardY = sim.board.getY();
		boardX += this.getContentPane().getX();
		boardY += this.getContentPane().getY();
		p1.x -= boardX + 8;
		p1.y -= boardY + 32;
		p2.x -= boardX + 8;
		p2.y -= boardY + 32;
		int x1 = p1.x / pixelSize;
		int y1 = p1.y / pixelSize;
		int x2 = p2.x / pixelSize;
		int y2 = p2.y / pixelSize;
		p1.x = x1;
		p1.y = y1;
		p2.x = x2;
		p2.y = y2;
		dragging = false;
		if (p == true) {
			sim.addBarrier(p1);
		} else if (l == true) {
			sim.addBarrierLine(x1, y1, x2, y2);
		} else if (g == true) {
			sim.addBarrierGraph(x1, y1, x2, y2, xSpace, ySpace, xOff, yOff);
		} else if (e == true) {
			sim.removeBarrierRect(x1, y1, x2, y2);
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void lTrue() {
		l = true;
		p = false;
		g = false;
		e = false;
	}

	public void pTrue() {
		p = true;
		l = false;
		g = false;
		e = false;
	}

	public void gTrue() {
		g = true;
		p = false;
		l = false;
		e = false;
	}

	public void eTrue() {
		e = true;
		p = false;
		g = false;
		l = false;
	}

}