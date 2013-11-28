package view;

import config.Entry;
import config.Helper;
import config.Manager;
import config.MissingEntryException;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

/**
 *
 * @author jmcomets
 */
public abstract class NavigablePanel extends JPanel {

    private double SCALE_MAX = 3;
    private double SCALE_MIN = 0.1;
    private double SCALE_INC = 0.1;

    private double scale;

    private int offX;
    private int offY;

    private boolean dragging;
    private int grabX;
    private int grabY;

    public NavigablePanel() {
        super();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                doMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                doMouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                doMouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                doMouseMoved(e);
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                doScroll(e);
            }
        });
        String configEntryName = "navigable-panel";
        try {
            configure(Manager.getInstance().registerEntry(configEntryName));
        } catch (MissingEntryException e) {
            System.out.println("Aucune configuration trouvée pour " + configEntryName);
        }
    }

    private void configure(Entry entry) {
        Helper helper = new Helper(entry);
        SCALE_MAX = helper.getDouble("scale-max", SCALE_MAX);
        SCALE_MIN = helper.getDouble("scale-min", SCALE_MIN);
        SCALE_INC = helper.getDouble("scale-inc", SCALE_INC);
    }

    public void resetTransform() {
        scale = 1;
        offX = offY = 0;
        dragging = false;
        grabX = grabY = 0;
    }

    public void applyTransform(Graphics2D g2d) {
        g2d.translate(offX, offY);
        g2d.scale(scale, scale);
    }

    public abstract void notifyPressed(int x, int y);

    public abstract void notifyReleased(int x, int y);

    public abstract void notifyMoved(int x, int y);

    protected void doScroll(MouseWheelEvent e) {
        double diff = SCALE_INC * (double) e.getWheelRotation();
        offX += SCALE_INC * ((getWidth() / 2 - e.getX()) - offX);
        offY += SCALE_INC * ((getHeight() / 2 - e.getY()) - offY);
        if (diff < 0) {
            scale = Math.min(SCALE_MAX, scale - diff);
        } else {
            scale = Math.max(SCALE_MIN, scale - diff);
        }
        repaint();
    }

    private void doMousePressed(MouseEvent e) {
        notifyPressed(getActualX(e.getX()), getActualY(e.getY()));
        dragging = true;
        grabX = e.getX() - offX;
        grabY = e.getY() - offY;
    }

    private void doMouseReleased(MouseEvent e) {
        notifyReleased(getActualX(e.getX()), getActualY(e.getY()));
        dragging = false;
    }

    private void doMouseDragged(MouseEvent e) {
        if (dragging) {
            offX = e.getX() - grabX;
            offY = e.getY() - grabY;
            repaint();
        }
    }

    private void doMouseMoved(MouseEvent e){
        notifyMoved(getActualX(e.getX()), getActualY(e.getY()));
        repaint();
    }
    private int getActualX(int x) {
        return (int) ((x - offX) / scale);
    }

    private int getActualY(int y) {
        return (int) ((y - offY) / scale);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        if (scale <= 0) {
            throw new IllegalArgumentException("'scale' donnée ne doit pas être <= 0");
        }
        this.scale = scale;
    }
}
