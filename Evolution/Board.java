import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board extends JPanel {

    private BufferedImage canvas;
    private int pixelSize; 
    private int boardSize;

    public Board(int pixelSize, int bs) {
        this.pixelSize = pixelSize; 
        boardSize = bs;
        canvas = new BufferedImage(boardSize * pixelSize, boardSize * pixelSize, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.black);
    }
    
    public void setBoardSize(int s)
    {
        boardSize = s;
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }


    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++)
        {
            for (int y = 0; y < canvas.getHeight(); y++)
            {
                canvas.setRGB(x, y, color);
            }
        }
    }
    
    public void draw(Point point, Color c)
    {
        int color = c.getRGB();
        int x1 = point.x;
        int y1 = point.y;
        // Implement rectangle drawing
        for (int x = x1 * pixelSize; x < pixelSize * (x1 + 1); x++)
        {
            for (int y = y1 * pixelSize; y < pixelSize * (y1 + 1); y++)
            {
                canvas.setRGB(x, y, color);
            }
        }
    }

    public void erase(Point point)
    {
        draw(point, Color.black);
    }
}