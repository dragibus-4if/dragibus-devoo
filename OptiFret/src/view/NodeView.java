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
import java.awt.geom.Ellipse2D;
import java.lang.ref.WeakReference;
import view.DeliveryMap.NODE_RETURN;

/**
 *
 * @author Sylvain
 */
public class NodeView {

    public static final int DIAMETER = 10;
    private static final int BASIC_NODE_STROKE = 2;
    private static final int STROKE_ENLIGHT = 5;
    private static final int PATH_NODE_DIAMETER = 12;
    private static final int DEL_NODE_DIAMETER = 14;
    private static final int WHAREHOUSE_DIAMETER = 14;
    private final BasicStroke basicNodeStroke = new BasicStroke(BASIC_NODE_STROKE);
    private final BasicStroke myStrokeEnlight = new BasicStroke(STROKE_ENLIGHT);
    private Ellipse2D circle;
    private static final Color cBasic = new Color(100, 100, 100);
    private static final Color cSelectedBasic = new Color(255, 204, 0);
    private static final Color cBasicDelPath = new Color(0, 151, 202);
    private static final Color cSelectedBasicDelPath = new Color(0, 151, 202);
    private static final Color cBasicDelNode = new Color(255, 100, 100);
    private static final Color cSelectedBasicDelNode = new Color(255, 100, 100);
    private static final Color cBasicWharehouse = new Color(0, 0, 0);
    private static final Color cSelectedWhareHouse = new Color(0, 0, 0);
    private static final Color cMouseOver = new Color(204, 204, 204);
    private final WeakReference<DeliveryMap> parent;
    private int x1;
    private int y1;
    private final Long address;
    private boolean mouseOver = false;
    private boolean selected = false;

    public enum MODE {

        CLASSIC, DELIVERY_PATH, DELIVERY_NODE, WAREHOUSE
    };
    private MODE mode;

    public NodeView(int x1, int y1, Long address, WeakReference<DeliveryMap> ref, MODE mode) {
        this.x1 = x1;
        this.y1 = y1;
        this.address = address;
        this.mode = mode;
        circle = generateCircle();
        parent = ref;
    }

    public Ellipse2D generateCircle() {
        Ellipse2D circle;
        switch (mode) {
            case DELIVERY_NODE:
                circle = new Ellipse2D.Double((float) x1, (float) y1,
                        (float) DEL_NODE_DIAMETER, (float) DEL_NODE_DIAMETER);
                break;
            case DELIVERY_PATH:
                circle = new Ellipse2D.Double((float) x1, (float) y1,
                        (float) PATH_NODE_DIAMETER, (float) PATH_NODE_DIAMETER);
                break;
            case WAREHOUSE:
                circle = new Ellipse2D.Double((float) x1, (float) y1,
                        (float) WHAREHOUSE_DIAMETER, (float) WHAREHOUSE_DIAMETER);
                break;
            default:
                circle = new Ellipse2D.Double((float) x1, (float) y1,
                        (float) DIAMETER, (float) DIAMETER);
        }
        return circle;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof NodeView) {
            if (x1 == ((NodeView) object).x1 && y1 == ((NodeView) object).y1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + x1;
        hash = 47 * hash + y1;
        return hash;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setStroke(basicNodeStroke);
        g2d.translate(-circle.getWidth() / 2, -circle.getWidth() / 2);
        g2d.setColor(parent.get().getBackground());
        g2d.fill(circle);
        switch (mode) {
            case CLASSIC:
                if (selected) {
                    g2d.setColor(cSelectedBasic);
                    g2d.fill(circle);
                } else {
                    g2d.setColor(cBasic);
                }
                break;
            case DELIVERY_PATH:
                if (selected) {
                    g2d.setColor(cSelectedBasicDelPath);
                    g2d.fill(circle);
                } else {
                    g2d.setColor(cBasicDelPath);
                }
                break;
            case DELIVERY_NODE:
                if (selected) {
                    g2d.setColor(cSelectedBasicDelNode);
                    g2d.fill(circle);
                } else {
                    g2d.setColor(cBasicDelNode);
                }
                break;
            case WAREHOUSE:
                g2d.setColor(cSelectedWhareHouse);
                g2d.fill(circle);
                break;
        }

        if (mouseOver) {
            g2d.setColor(cMouseOver);
            g2d.setStroke(myStrokeEnlight);
            g2d.draw(circle);
            g2d.setStroke(basicNodeStroke);
        } else {
            g2d.draw(circle);
        }
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

    public NODE_RETURN onMouseDown(int x, int y) {
        NODE_RETURN ret = NODE_RETURN.NOTHING_SELECTED;
        if (circle.contains(x + DIAMETER / 2, y + DIAMETER / 2) && (parent.get() != null) && this.mode!= MODE.WAREHOUSE) {
            selected = true;
            ret = NODE_RETURN.NODE_SELECTED;
            if (parent.get().getSelectedNode().get() != null) {
                if (parent.get().getSelectedNode().get().equals(this)) {
                    ret = NODE_RETURN.NODE_ALLREADY_SELECTED;
                } else {
                    parent.get().getSelectedNode().get().setSelection(false);
                    parent.get().getSelectedNode().clear();
                }
            }
            parent.get().setSelectedNode(new WeakReference<>(this));
        } else {
            selected = false;
        }
        return ret;
    }

    public void onMouseUp(int x, int y) {
    }

    public void onMouseOver(int x, int y) {
        mouseOver = circle.contains(x + DIAMETER / 2, y + DIAMETER / 2) && (parent.get() != null);
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
        circle = generateCircle();
    }

    public Long getAddress() {
        return address;
    }
}
