package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;
import model.RoadNode;
import view.NodeView.MODE;

/**
 *
 * @author jmcomets
 */
public class DeliveryMap extends JPanel {

    private Map<Integer, ArcView> mapArcs;
    private Map<Long, NodeView> mapNodes;
    private WeakReference<NodeView> selectedNode;
    private int maxX = 0;
    private int maxY = 0;
    private CopyOnWriteArrayList<Listener> listeners;
    private static final double SCALE_MAX = 3;
    private static final double SCALE_MIN = 0.1;
    private static final double SCALE_INC = 0.1;
    private static final double SCROLL_SPEED = 5;
    public static final int PADDING = 20;

    private double scale;
    private double vX;
    private double vY;
    private double offX;
    private double offY;

    public DeliveryMap() {
        super();
        setDoubleBuffered(true);
        setFocusable(true);
        reset();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                notifyPressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                notifyReleased(e);
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
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                notifyMoved(e);
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                notifyScrolled(e);
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
                        vY -= SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_DOWN:
                        vY += SCROLL_SPEED;
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
                        vY -= -SCROLL_SPEED;
                        break;

                    case KeyEvent.VK_DOWN:
                        vY += -SCROLL_SPEED;
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

    private void reset() {
        listeners = new CopyOnWriteArrayList<>();
        mapArcs = new LinkedHashMap<>();
        mapNodes = new LinkedHashMap<>();
        selectedNode = new WeakReference<>(null);
        scale = 1;
        offX = offY = 0;
        vX = vY = 0;
    }

    public void updateNetwork(List<RoadNode> nodes) {
        if (nodes == null) {
            reset();
            return;
        }
        mapArcs.clear();
        mapNodes.clear();
        selectedNode.clear();
        for (RoadNode rn : nodes) {
            if (rn.getNeighbors() == null) {
                break;
            }
            if (rn.getX() > maxX) {
                maxX = rn.getX();
            }
            if (rn.getY() > maxY) {
                maxY = rn.getY();
            }
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), rn.getId(), new WeakReference<>(this), MODE.CLASSIC);
            if (!mapNodes.containsKey(rn.getId())) {
                mapNodes.put(rn.getId(), tempNode);
            }
            for (RoadNode neighbor : rn.getNeighbors()) {
                ArcView temp = new ArcView(rn.getX(), rn.getY(), neighbor.getX(), neighbor.getY(), 0);
                if (!mapArcs.containsKey(temp.hashCode())) {
                    mapArcs.put(temp.hashCode(), temp);
                }
            }
        }
        setPreferredSize(new Dimension(maxX + PADDING, maxY + PADDING));
    }

    public void updateDeliveryNodes(List<RoadNode> nodes) {
        if (nodes == null) {
            return;
        }
        for (ArcView arc : mapArcs.values()) {
            arc.resetNbLines();
        }
        for (RoadNode rn : nodes) {
            if (rn.getNeighbors() == null) {
                break;
            }
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), rn.getId(), new WeakReference<>(this), MODE.CLASSIC);
            for (Long i = 0l; i < mapNodes.size(); i++) {
                if (tempNode.equals(mapNodes.get(i))) {
                    mapNodes.get(i).setMode(MODE.DELIVERY);
                }
            }

            for (RoadNode neighbor : rn.getNeighbors()) {
                ArcView temp = new ArcView(rn.getX(), rn.getY(), neighbor.getX(), neighbor.getY(), 0);
                if (mapArcs.containsKey(temp.hashCode())) {
                    mapArcs.get(temp.hashCode()).incrementNbLines();
                }
            }
        }
    }

    public void notifyPressed(MouseEvent e) {
        System.out.println("Début parcours nodes Pressed");
        boolean voidClic = true;
        for (NodeView node : mapNodes.values()) {
            int x = getActualX(e.getX());
            int y = getActualY(e.getY());
            if (!node.onMouseDown(x, y)) {
                voidClic = false; // TODO also break loop ?
            }
        }
        if (voidClic) {
            if (getSelectedNode().get() != null) {
                getSelectedNode().clear();
            }
        }
        fireChangeEvent();
        repaint();
    }

    private void notifyReleased(MouseEvent e) {
        int x = getActualX(e.getX());
        int y = getActualY(e.getY());
        for (NodeView node : mapNodes.values()) {
            node.onMouseUp(x, y);
        }
    }

    private void notifyMoved(MouseEvent e) {
        int x = getActualX(e.getX());
        int y = getActualY(e.getY());
        for (NodeView node : mapNodes.values()) {
            node.onMouseOver(x, y);
        }
        repaint();
    }

    private void notifyScrolled(MouseWheelEvent e) {
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(offX, offY);
        g2d.scale(scale, scale);
        draw(g);
    }

    private void draw(Graphics g) {
        for (ArcView arc : mapArcs.values()) {
            arc.draw(g);
        }
        for (NodeView node : mapNodes.values()) {
            node.draw(g);
        }
    }

    public WeakReference<NodeView> getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNodeById(long id) {
        if (selectedNode.get() != null) {
            selectedNode.get().setSelection(false);
        }
        if (id == -1l) {
            this.setSelectedNode(new WeakReference<NodeView>(null));
            repaint();
            return;
        }
        mapNodes.get(id).setSelection(true);
        this.setSelectedNode(new WeakReference<>(mapNodes.get(id)));
        repaint();
    }

    public void setSelectedNode(WeakReference<NodeView> selectedNode) {
        this.selectedNode = selectedNode;
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

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    // Event firing method.  Called internally by other class methods.
    protected void fireChangeEvent() {
        MyChangeEvent evt = new MyChangeEvent(this);
        for (Listener l : listeners) {
            l.changeEventReceived(evt);
        }
    }
}
