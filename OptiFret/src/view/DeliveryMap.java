package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import model.RoadNode;
import view.NodeView.MODE;

/**
 *
 * @author jmcomets
 */
public class DeliveryMap extends NavigablePanel {

    private Map<Integer, ArcView> mapArcs;
    private Map<Long, NodeView> mapNodes;
    private WeakReference<NodeView> selectedNode;
    private int maxX = 0;
    private int maxY = 0;
    private CopyOnWriteArrayList<Listener> listeners;
    public static final int PADDING = 20;

    public DeliveryMap() {
        super();
        setDoubleBuffered(true);
        setFocusable(true);
        reset();
    }

    private void reset() {
        listeners = new CopyOnWriteArrayList<>();
        mapArcs = new LinkedHashMap<>();
        mapNodes = new LinkedHashMap<>();
        selectedNode = new WeakReference<>(null);
        resetTransform();
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

    @Override
    public void notifyPressed(int x, int y) {
        boolean voidClic = true;
        for (NodeView node : mapNodes.values()) {
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

    @Override
    public void notifyReleased(int x, int y) {
        for (NodeView node : mapNodes.values()) {
            node.onMouseUp(x, y);
        }
    }

    @Override
    public void notifyMoved(int x, int y) {
        for (NodeView node : mapNodes.values()) {
            node.onMouseOver(x, y);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        applyTransform((Graphics2D) g);
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
