/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Sylvain
 */
public class NodeView {

    public static final int DIAMETER = 10;
    private static final int STROKE = 2;
    private int x1;
    private int y1;
    private BasicStroke myStroke;
    private Ellipse2D circle;
    private Color cBasic = new Color(100, 100, 100);
    private Color cSelectedBasic = new Color(200, 200, 0);
    private boolean selected = false;

    public NodeView(int x1, int y1) {
        this.myStroke = new BasicStroke(STROKE);
        this.x1 = x1;
        this.y1 = y1;
        this.circle = new Ellipse2D.Double((float) x1, (float) y1, (float) DIAMETER, (float) DIAMETER);
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof ArcView) {
            if (this.x1 == ((NodeView) object).x1 && this.y1 == ((NodeView) object).y1) {
                sameSame = true;
            }
        }

        return sameSame;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.x1;
        hash = 47 * hash + this.y1;
        return hash;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(myStroke);
        g2d.translate(-DIAMETER / 2, -DIAMETER / 2);
        if (selected) {
            g2d.setColor(cSelectedBasic);
            g2d.fill(circle);
        } else {
            g2d.setColor(cBasic);
        }
        g2d.draw(circle);

        g2d.translate(DIAMETER / 2, DIAMETER / 2);
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public Ellipse2D getCircle() {
        return circle;
    }

    public void setSelection(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    void onMouseDown(int x, int y) {
        if (circle.contains(x + DIAMETER / 2, y + DIAMETER / 2)) {
            selected = true;
        } else {
            selected = false;
        }
    }

    void onMouseUp(int x, int y) {
    }
}
