/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Sylvain
 */
public class MapCanvas extends Canvas {
    public MapCanvas(){
        super();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }
    
    private void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRect(0,0,getWidth(),getHeight());
        g2d.drawString("Java 2D", 0, 0);

    }
}
