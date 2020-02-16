
/**
 * Write a description of class PopulationDisplay here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

public class PopulationDisplay extends JFrame implements MouseListener {
    // instance variables - replace the example below with your own
   private JLabel[] topDNA;
   private JLabel[] topPop;
   private JPanel[] topColor;
   private JLabel tickSpeed;
   private JPanel ancestorDisp;
   private JLabel[] ancestors;
   private JLabel ancCritter;
   private JPanel ancColor;
   
   

    /**
     * Constructor for objects of class PopulationDisplay
     */
    public PopulationDisplay() 
    {
      setSize(400,400);
      topColor = new JPanel[30];
      topDNA = new JLabel[30];
      topPop = new JLabel[30];
      tickSpeed = new JLabel("0 ticks / s");
      setBackground(Color.white);
      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      // setBorder(BorderFactory.createLineBorder(Color.black));
      gbc.anchor = GridBagConstraints.WEST;
      setAlwaysOnTop( true );

      for (int i = 0; i < 30; i++)
      {
          if (i < 15)
          {
          gbc.gridy = i + 1;
          topColor[i] = new JPanel();
          topColor[i].setBackground(Color.white);
          topColor[i].setSize(new Dimension(5, 5));
          topColor[i].setBorder(BorderFactory.createLineBorder(Color.black));
          gbc.gridx = 0;
          add(topColor[i],gbc);
          topDNA[i] = new JLabel("   ");
          gbc.gridx = 1;
          add(topDNA[i],gbc);
          topPop[i] = new JLabel("   ");
          gbc.gridx = 2;
          add(topPop[i],gbc);
          }
          else 
          {
          gbc.gridy = i - 14;
          topColor[i] = new JPanel();
          topColor[i].setBackground(Color.white);
          topColor[i].setSize(new Dimension(5, 5));
          topColor[i].setBorder(BorderFactory.createLineBorder(Color.black));
          gbc.gridx = 3;
          add(topColor[i],gbc);
          topDNA[i] = new JLabel("   ");
          gbc.gridx = 4;
          add(topDNA[i],gbc);
          topPop[i] = new JLabel("   ");
          gbc.gridx = 5;
          add(topPop[i],gbc); 
        }
          
          
      }
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridwidth = 5;
      gbc.gridx = 0;
      gbc.gridy = 0;
      add(tickSpeed, gbc);
      
      ancestorDisp = new JPanel();
      ancestorDisp.setLayout(new GridBagLayout());
      ancestorDisp.setBorder(BorderFactory.createLineBorder(Color.black));
      ancestors = new JLabel[10];
      ancCritter = new JLabel(" ");    
      gbc.gridx = 0;
      gbc.gridy = 0;
      ancestorDisp.add(ancCritter, gbc);
      for (int i = 0; i < 10; i++)
      {
          gbc.gridy = i + 1;
          ancestors[i] = new JLabel("");
          ancestorDisp.add(ancestors[i], gbc);
      }
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridwidth = 6;
      gbc.gridx = 0;
      gbc.gridy = 16;
      add(ancestorDisp, gbc);
      
      
      setVisible(true);
    }

    public void dispAncestors(String dna, ArrayList<String> anc)
    {
        ancCritter.setText("Ancestory of " + dna);
        for (int i = 0; i < anc.size(); i++)
        {
            ancestors[i].setText(anc.get(anc.size() - i - 1));
        }
    }
    
    public void refresh (Population[] top)
    {
       for (int i = 0; i < top.length ; i++)
       {
           
           Color color = top[i].color;
           topColor[i].setBackground(color);
           String dna = top[i].dna;

           if (top[i].color.equals(Color.gray))
           {
               topDNA[i].setText(" ");
               topPop[i].setText(" ");
           }
           else
           {
               topDNA[i].setText(" " + dna);
               topPop[i].setText(" " + top[i].size + " ");
           }
           ArrayList<String> ancestors = top[i].ancestors;
           topDNA[i].addMouseListener(new MouseAdapter()  
          {  
              public void mouseClicked(MouseEvent e)  
              {  
                 dispAncestors(dna, ancestors);
              }  
       }); 
       }
       
    }
    
    public void refreshTickSpeed(long time)
    {
        if (time != 0)
        {tickSpeed.setText(1000000000 / time + " ticks / s");}
        else
        {tickSpeed.setText("0 ticks / s");}
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
        
    }
 
    @Override
    public void mouseReleased(MouseEvent event) {
    }
}
