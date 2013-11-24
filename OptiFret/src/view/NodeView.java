/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.lang.ref.WeakReference;

/**
 *
 * @author Sylvain
 */
public class NodeView {

    public static final int DIAMETER = 10;
    private static final int STROKE = 2;
    private static final int STROKE_ENLIGHT = 5;
    private int x1;
    private int y1;
    private Long address;

   
    private final BasicStroke myStroke = new BasicStroke(STROKE);
    private final BasicStroke myStrokeEnlight = new BasicStroke(STROKE_ENLIGHT);
    private Ellipse2D circle;
    private final Color cBasic = new Color(100, 100, 100);
    private final Color cSelectedBasic = new Color(255, 204, 0);
    private final Color cBasicDel = new Color(0, 51, 102);
    private final Color cSelectedBasicDel = new Color(255, 204, 100);
    private final Color cMouseOver = new Color(204, 204, 204);
    private boolean mouseOver = false;
    private boolean selected = false;
    private WeakReference<DeliveryMap> parent;

    public enum MODE {

        CLASSIC, DELIVERY
    };
    private MODE mode;

    public NodeView(int x1, int y1,Long address, WeakReference<DeliveryMap> ref, MODE mode) {
        this.x1 = x1;
        this.y1 = y1;
        this.address=address;
        this.circle = new Ellipse2D.Double((float) x1, (float) y1, (float) DIAMETER, (float) DIAMETER);
        this.mode = mode;
        parent = ref;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof NodeView) {
            if (this.x1 == ((NodeView) object).x1 && this.y1 == ((NodeView) object).y1) {
                sameSame = true;
            }
        }

        return sameSame;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.x1;
        hash = 47 * hash + this.y1;
        return hash;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setStroke(myStroke);
        g2d.translate(-DIAMETER / 2, -DIAMETER / 2);
        switch (mode) {
            case CLASSIC:
                if (selected) {
                    g2d.setColor(cSelectedBasic);
                    g2d.fill(circle);
                } else {
                    g2d.setColor(cBasic);
                }
                break;
            case DELIVERY:
                if (selected) {
                    g2d.setColor(cSelectedBasicDel);
                    g2d.fill(circle);
                } else {
                    g2d.setColor(cBasicDel);
                }
                break;
        }
        if (mouseOver) {
            g2d.setColor(cMouseOver);
            g2d.setStroke(myStrokeEnlight);
            g2d.draw(circle);
            g2d.setStroke(myStroke);

        } else {
            g2d.draw(circle);

        }

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

    public boolean onMouseDown(int x, int y) {
        boolean voidClic=true;
        if (circle.contains(x + DIAMETER / 2, y + DIAMETER / 2) && (parent.get() != null)) {
            selected = true;
            voidClic=false;
            if (parent.get().getSelectedNode() != null) {
                parent.get().getSelectedNode().clear();
            }
            parent.get().setSelectedNode(new WeakReference<>(this));
        } else {
            selected = false;            
        }
        return voidClic;
    }

    public void onMouseUp(int x, int y) {
    }

    public void onMouseOver(int x, int y) {
        if (circle.contains(x + DIAMETER / 2, y + DIAMETER / 2) && (parent.get() != null)) {
            mouseOver = true;
        } else {
            mouseOver = false;
        }
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }
    
     public Long getAddress() {
        return address;
    }
}
