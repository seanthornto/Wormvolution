/**A class to customize the look and feel of the buttons since apparently JButtons make you choose between controlling the border or controlling the fill.
*@author: Sky Vercauteren
*@version: 1.0 August 2025
**/
package objects.ui_components; 

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.*;

public class Button extends JButton {

    private String _name;
    private String _tip;
    private Color _background;
    private Color _second_background;
    private Color _border;
    private int _borderWidth;
    private Color _text;

    public Button(String text, String hint, Color bgColor, Color bgGradient, Color borderColor, Color textColor, int borderWidth) {
        super(text);
        
        // Disable the default look and feel painting - Its crazy this even needs to be done
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        
        // Set the custom stuff here properties
        _background = bgColor;
        _second_background = bgGradient;
        _border = borderColor;
        _borderWidth = borderWidth;
        _name = text;
        _tip = hint;
        _text = textColor;
        setForeground(textColor);
        setToolTipText(hint);
        setBackground(bgColor);
        setBorder(new LineBorder(borderColor, borderWidth));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Cast to Graphics2D for gradient
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        //make a gradient
        Color startColor = _background;
        Color endColor = _second_background;
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(0, height);
        LinearGradientPaint p = new LinearGradientPaint(start, end, new float[]{0.4f, 1.0f}, new Color[]{startColor, endColor});
        g2.setPaint(p);
        g2.fillRoundRect(0, 0, width - 1, height - 1, 5, 5);

        // Border
        g2.setColor(_border);
        g2.drawRoundRect(0, 0, width - 1, height - 1, 5, 5);

        // Text
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D textBounds = fm.getStringBounds(getText(), g2);
        int textX = (int) (width - textBounds.getWidth()) / 2;
        int textY = (int) (height - textBounds.getHeight()) / 2 + fm.getAscent();
        g2.setColor(_text);
        g2.drawString(getText(), textX, textY);
        
        g2.dispose();
    }

}