package view;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    private static final double SCALE_MAX = 3;
    private static final double SCALE_MIN = 0.1;
    private static final double SCALE_INC = 0.1;
    private static final double SCROLL_SPEED = 5;

    private double scale;

    private double vX;
    private double vY;
    private double offX;
    private double offY;

    private boolean dragging;
    private int grabX;
    private int grabY;
    private int dragX;
    private int dragY;

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
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                doScroll(e);
            }
        });
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        vY += SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_DOWN:
                        vY -= SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_LEFT:
                        vX += SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_RIGHT:
                        vX -= SCROLL_SPEED;
                        break;
                }
                updateOffset();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        vY += -SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_DOWN:
                        vY -= -SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_LEFT:
                        vX += -SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_RIGHT:
                        vX -= -SCROLL_SPEED;
                        break;
                }
            }
        });
    }

    public void resetTransform() {
        scale = 1;
        offX = offY = 0;
        vX = vY = 0;
        dragging = false;
        grabX = grabY = 0;
        dragX = dragY = 0;
    }

    public void applyTransform(Graphics2D g2d) {
        g2d.translate(offX, offY);
        g2d.translate(dragX, dragY);
        g2d.scale(scale, scale);
    }

    public abstract void notifyPressed(int x, int y);

    public abstract void notifyReleased(int x, int y);

    public abstract void notifyMoved(int x, int y);

    private void doScroll(MouseWheelEvent e) {
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
        grabX = e.getX();
        grabY = e.getY();
    }

    private void doMouseReleased(MouseEvent e) {
        notifyReleased(getActualX(e.getX()), getActualY(e.getY()));
        dragging = false;
    }

    private void doMouseDragged(MouseEvent e) {
        notifyMoved(getActualX(e.getX()), getActualY(e.getY()));
        if (dragging) {
            dragX = e.getX() - grabX;
            dragY = e.getY() - grabY;
            repaint();
        }
    }

    private void updateOffset() {
        offX += vX;
        offY += vY;
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
