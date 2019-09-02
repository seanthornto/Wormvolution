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
        if(p == true)
        {
            sim.addBarrier(p1);
            p = false;
        }
    }
 
    @Override
    public void mouseReleased(MouseEvent event) {
        Point point = event.getPoint();
        p2 = point;
        dragging = false;
        if(l == true)
        {
            sim.addBarrierLine(p1.x,p1.y,p2.x,p2.y);
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