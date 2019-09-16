
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

public class PopulationDisplay extends JPanel {
    // instance variables - replace the example below with your own
   private JLabel[] topDNA;
   private JLabel[] topPop;
   private JPanel[] topColor;
   private JLabel tickSpeed;

    /**
     * Constructor for objects of class PopulationDisplay
     */
    public PopulationDisplay()
    {
      topColor = new JPanel[30];
      topDNA = new JLabel[30];
      topPop = new JLabel[30];
      tickSpeed = new JLabel("0 ticks / s");
      setBackground(Color.white);
      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      setBorder(BorderFactory.createLineBorder(Color.black));
      gbc.anchor = GridBagConstraints.WEST;
      
      for (int i = 0; i < 30; i++)
      {
          if (i < 15){
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
          add(topPop[i],gbc); }
          else {
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
          add(topPop[i],gbc); }
          
      }
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridwidth = 5;
      gbc.gridx = 0;
      gbc.gridy = 0;
      add(tickSpeed, gbc);
    }

    public void refresh (Population[] top)
    {
       for (int i = 0; i < top.length ; i++)
       {
           topColor[i].setBackground(top[i].color);
           String temp = "";
           for (int j = 0; j < top[i].dna.length; j++)
           {
               temp += top[i].dna[j];
           }
           if (top[i].color.equals(Color.gray))
           {
               topDNA[i].setText(" ");
               topPop[i].setText(" ");
           }
           else
           {
               topDNA[i].setText(" " + temp);
               topPop[i].setText(" " + top[i].size + " ");
           }
       }
    }
    
    public void refreshTickSpeed(long time)
    {
        if (time != 0)
        {tickSpeed.setText(1000000000 / time + " ticks / s");}
        else
        {tickSpeed.setText("0 ticks / s");}
    }
}
