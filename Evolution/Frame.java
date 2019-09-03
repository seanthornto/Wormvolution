import java.util.ArrayList;
import java.util.*;
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
    public boolean dragging;
    public int boardX;
    public int boardY;
    public int pixelSize;
    
    public Frame(String title)
    {
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
        boardY= sim.board.getY();
        boardX += this.getContentPane().getX();
        boardY += this.getContentPane().getY();
        p1.x -= boardX;
        p1.y -= boardY;
        p2.x -= boardX;
        p2.y -= boardY;
        int x1 = (int) p1.x/pixelSize;
        int y1 = (int) p1.y/pixelSize;
        int x2 = (int) p2.x/pixelSize;
        int y2 = (int) p2.y/pixelSize;
        p1.x = x1;
        p1.y = y1;
        p2.x = x2;
        p2.y = y2;
        dragging = false;
        if(p == true)
        {
            sim.addBarrier(p1);
            p = false;
        }
        else if(l == true)
        {
            sim.addBarrierLine(x1,y1,x2,y2);
            l = false;
        }
        else if(g == true)
        {
            sim.addBarrierGraph(p1,p2);
            g = false;
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
    }
    
     @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    public void lTrue()
    {
        l = true;
    }
    public void pTrue()
    {
        p = true;
    }
    public void gTrue()
    {
        g = true;
    }
   
}