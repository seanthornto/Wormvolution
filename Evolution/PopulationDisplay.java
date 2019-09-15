
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

    /**
     * Constructor for objects of class PopulationDisplay
     */
    public PopulationDisplay()
    {
      topColor = new JPanel[30];
      topDNA = new JLabel[30];
      topPop = new JLabel[30];
      setBackground(Color.white);
      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      
      for (int i = 0; i < 30; i++)
      {
          if (i < 15){
          gbc.gridy = i;
          topColor[i] = new JPanel();
          topColor[i].setBackground(Color.white);
          topColor[i].setSize(new Dimension(5, 5));
          gbc.gridx = 0;
          add(topColor[i],gbc);
          topDNA[i] = new JLabel("   ");
          gbc.gridx = 1;
          add(topDNA[i],gbc);
          topPop[i] = new JLabel("   ");
          gbc.gridx = 2;
          add(topPop[i],gbc); }
          else {
          gbc.gridy = i - 15;
          topColor[i] = new JPanel();
          topColor[i].setBackground(Color.white);
          topColor[i].setSize(new Dimension(5, 5));
          gbc.gridx = 3;
          add(topColor[i],gbc);
          topDNA[i] = new JLabel("   ");
          gbc.gridx = 4;
          add(topDNA[i],gbc);
          topPop[i] = new JLabel("   ");
          gbc.gridx = 5;
          add(topPop[i],gbc); }
          
      }
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
           topDNA[i].setText("          " + temp);
           topPop[i].setText("          " + top[i].size + "     ");
       }
    }
}
