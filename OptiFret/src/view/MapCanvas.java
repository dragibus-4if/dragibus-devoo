/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

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
        ArrayList <ArcView> temp=new ArrayList<ArcView> ();
        draw(g,temp);
    }
    
    private void draw(Graphics g,ArrayList<ArcView> arcs) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString("Java 2D", 50, 50);
        g2d.drawOval(10, 10, 20, 20);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(0, 0, getWidth(), getHeight());
        g2d.drawRect(0,0,getWidth()-1,getHeight()-1);

    }
}
