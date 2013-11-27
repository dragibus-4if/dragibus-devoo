/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author Quentin
 */
public class CustomJPanel extends JPanel{


    @Override
    protected void paintComponent(Graphics g) {
//        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setPaint(new GradientPaint(TOP_ALIGNMENT, TOP_ALIGNMENT, Color.white, BOTTOM_ALIGNMENT, BOTTOM_ALIGNMENT, Color.black));
////        g2d.setColor(new Color(0,0,0));
////        g2d.drawRect(10, 10, 100, 100);
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color color1 = Color.white;
        Color color2 = new Color(200, 200, 200);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);

        
        
        
    }

    public CustomJPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public CustomJPanel(LayoutManager layout) {
        super(layout);
    }

    public CustomJPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public CustomJPanel() {
    }
    
    
    
}
