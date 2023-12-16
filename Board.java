/**
 * Write a description of class Board here.
 *
 * @author Sean Thornton and Sky Vercauteren
 * @version 1.0 November 2023
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.swing.JPanel;

public class Board extends JPanel {

	private BufferedImage canvas;
	private int pixelSize;
	private int boardSize;
	private int maxConstraint;
	private Boolean misaligned;
	private int[] origin = {0,0};
	private int barrierWidth;
	private double scale = 1;
	private Dimension preferredSize = new Dimension(850,850);

	public Board(int pixelSize, int bs, int max) {
		this.pixelSize = pixelSize;
		boardSize = bs;
		maxConstraint = max;
		misaligned = false;
		canvas = new BufferedImage(boardSize * pixelSize, boardSize * pixelSize, BufferedImage.TYPE_INT_ARGB);
				
		fillCanvas(Color.black);
		barrierWidth = 1;
	}

	public void setBoardSize(int s) {
		boardSize = s;
	}
	
	public void setPreferredSize(Dimension d)
	{
		preferredSize = d;
	}

	public Dimension getPreferredSize() {
		return preferredSize;
	}
	
	public void setMisaligned(Boolean b)
	{
		misaligned = b;
	}
	
	public void setOrigin(int x, int y)
	{
		origin[0] = x;
		origin[1] = y;
	}
	
	public void setBarrierWidth(int w)
	{
		barrierWidth =w;
	}
	
	public int getBarrierWidth()
	{
		return barrierWidth;
	}
	
	public void setScale(double s)
	{
		scale=s;
	}
	
	public double getScale()
	{
		return scale;
	}
	
	public int getPixelSize()
	{
		return pixelSize;
	}
	
	public BufferedImage getCanvas()
	{
		return canvas;
	}
	
	public void setCanvas(BufferedImage img)
	{
		canvas = img;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform at = new AffineTransform();
        double x = (maxConstraint - (canvas.getWidth() * scale))/2;
        if(misaligned == false) {
        	at.translate(x,x);
        }
        else { at.translate(origin[0], origin[1]); }
        at.scale(scale, scale);
        g2d.drawImage(canvas, at, this);
        g2d.dispose();
	}

	public void fillCanvas(Color c) {
		int color = c.getRGB();
		for (int x = 0; x < canvas.getWidth(); x++) {
			for (int y = 0; y < canvas.getHeight(); y++) {
				canvas.setRGB(x, y, color);
			}
		}
	}

	public void draw(Point point, Color c) {
		int color = c.getRGB();
		int x1 = point.x;
		int y1 = point.y;
		for (int x = x1 * pixelSize; x < pixelSize * (x1 + 1); x++) {
			for (int y = y1 * pixelSize; y < pixelSize * (y1 + 1); y++) {
				canvas.setRGB(x, y, color);
			}
		}
	}
	

	public void erase(Point point) {
		draw(point, Color.black);
	}
	
	public BufferedImage zoom(float p)
	{
		BufferedImage after = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(p,p);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(canvas, after);
		
		return after;
	}
}