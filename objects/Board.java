/**
 * Write a description of class Board here.
 *
 * @author Sean Thornton and Sky Vercauteren
 * @version 1.0 December 2023
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.JPanel;

public class Board extends JPanel {

	private BufferedImage canvas;
	private int pixelSize;
	private int boardSize;
	private int maxConstraint;
	private Boolean zoomed;
	private Boolean cropped;
	private Point origin = new Point(0,0);
	private int barrierWidth;
	private double scale = 1;
	private Dimension preferredSize = new Dimension(850,850);

	public Board(int pixelSize, int bs, int max) {
		this.setBackground(GUI.color_background);
		this.pixelSize = pixelSize;
		boardSize = bs;
		maxConstraint = max;
		zoomed = false;
		cropped = false;
		canvas = new BufferedImage(bs * pixelSize, bs * pixelSize, BufferedImage.TYPE_INT_ARGB);
				
		fillCanvas(Color.black);
		barrierWidth = pixelSize;
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
	
	public void setZoomed(Boolean b)
	{
		zoomed = b;
	}
	
	public boolean getZoomed()
	{
		return zoomed;
	}
	
	public void setCropped(Boolean b)
	{
		cropped = b;
	}
	
	public void setOrigin(double x, double y)
	{
		origin.setLocation(x, y);;
	}
	
	public Point getOrigin()
	{
		return origin;
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
        if(zoomed == true) 
        {
        	double x = (0 - (origin.x * scale)) * pixelSize;
        	double y = (0 - (origin.y * scale)) * pixelSize;
        	at.translate(x, y); 
        }
        else 
        {
        	double x = (maxConstraint - (canvas.getWidth() * scale))/2;
        	at.translate(x,x);
        }
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

	
	//I believe this is isnt being used by anything. 
	/* public BufferedImage zoom(double p)
	{
		BufferedImage after = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(p,p);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(canvas, after);
		
		return after;
	} */
}