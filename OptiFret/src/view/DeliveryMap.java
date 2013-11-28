package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import model.Delivery;
import model.RoadNode;
import model.TimeSlot;
import org.ini4j.MultiMap;
import view.NodeView.MODE;

/**
 *
 * @author jmcomets
 */
public class DeliveryMap extends NavigablePanel {

    private Map<Integer, ArcView> mapArcs;
    private Map<Long, NodeView> mapNodes;
    private WeakReference<NodeView> selectedNode;
    private CopyOnWriteArrayList<Listener> listeners;
    public static final int PADDING = 20;

    public enum NODE_RETURN {

        NOTHING_SELECTED,
        NODE_SELECTED,
        NODE_ALLREADY_SELECTED
    }

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
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), rn.getId(),
                    new WeakReference<>(this), MODE.CLASSIC);
            if (!mapNodes.containsKey(rn.getId())) {
                mapNodes.put(rn.getId(), tempNode);
            }
            for (RoadNode neighbor : rn.getNeighbors()) {
                ArcView temp = new ArcView(rn, neighbor, 0);
                if (!mapArcs.containsKey(temp.hashCode())) {
                    mapArcs.put(temp.hashCode(), temp);
                }
            }
        }
    }

    public void updateWhareHouse(Long id) {
        if (id != -1 && mapNodes.get(id) != null) {
            mapNodes.get(id).setMode(MODE.WAREHOUSE);
        }
    }

    public void updateDeliveryNodesPath(List<RoadNode> nodes) {
        if (nodes == null) {
            return;
        }
        for (ArcView arc : mapArcs.values()) {
            arc.resetNbLines();
            arc.resetColors();
        }
        for (int i = 0; i < nodes.size(); i++) {
            RoadNode rn = nodes.get(i);
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), rn.getId(),
                    new WeakReference<>(this), MODE.CLASSIC);
            for (Long j = new Long(0); j < mapNodes.size(); j++) {
                if (tempNode.equals(mapNodes.get(j))) {
                    mapNodes.get(j).setMode(MODE.DELIVERY_PATH);
                }
            }
            if (i >= 1) {
                RoadNode neighbor = nodes.get(i - 1);
                ArcView temp = new ArcView(neighbor, rn, 0);
                if (mapArcs.containsKey(temp.hashCode())) {
                    mapArcs.get(temp.hashCode()).incrementNbLines();
                }
            }
        }
    }

    public void updateTimeSlots(List<Delivery> dels, List<RoadNode> path) {
        if (dels == null) {
            return;
        }
        List<Integer> listPosDeliveries = new ArrayList<>();
        List<Integer> listPosPath = new ArrayList<>();

        TimeSlot currentTimeSlot;
        TimeSlot testedTimeSlot = new TimeSlot(new Date(0L), new Date(0L));
        for (Delivery del : dels) {
            currentTimeSlot = del.getTimeSlot();
            if (!currentTimeSlot.getBegin().equals(testedTimeSlot.getBegin())) {
                testedTimeSlot = currentTimeSlot;
                listPosDeliveries.add(dels.indexOf(del));
                System.out.println("CurrentTimeSlot : " + currentTimeSlot.toString());
            }
        }

        int nbTimeSlots = listPosDeliveries.size();
        System.out.println(nbTimeSlots);

        int itPath = 0, itDels = 1, itTS = 1;
        while (itPath < path.size()) {
            if (itDels<dels.size() && path.get(itPath).getId().equals(dels.get(itDels).getAddress()) ) {
                if (itTS<listPosDeliveries.size() && listPosDeliveries.get(itTS).equals(new Integer(itDels))) {
                    listPosPath.add(itPath);
                    itTS++;
                }
                itDels++;
            }

            itPath++;
        }
        itPath = 0;
        for (int i = 0; i < path.size()-1; i++) {
            if (itPath<listPosPath.size() &&listPosPath.get(itPath).equals(new Integer(i))) {
                itPath++;
            }
            ArcView arcTemp = new ArcView(path.get(i),
                    path.get(i + 1), 0);

            if (mapArcs.get(arcTemp.hashCode()) != null) {
                mapArcs.get(arcTemp.hashCode()).
                        updateColorPerTimeSlot(nbTimeSlots-1, itPath);
            }
        }

//        while(i<dels.size() && j<nbTimeSlots){
//            if(i<=listPosDeliveries.get(j)){
//                for(int k=savek ; k < path.size()-1 && k<i ; k++){
//                    if(i<dels.size() &&     
//                            path.get(k).getId()== dels.get(i+1).getAddress()){
//                        savek=k;
//                        System.out.println("SAVED : "+savek);
//                        break;
//                    }
//                    ArcView arcTemp = new ArcView (path.get(k), 
//                            path.get(k+1),0);
//                    
//                    if (mapArcs.get(arcTemp.hashCode()) != null  ) {
//                        System.out.println("j : " + j);
//
//                        mapArcs.get(arcTemp.hashCode()).
//                            updateColorPerTimeSlot(nbTimeSlots,j);
//                    }
//                }
//             
//                i++;
//                
//            }else{
//                j++;
//
//            }
//        }
    }

    public void updateDeliveryNodes(List<Delivery> dels) {
        if (dels == null) {
            return;
        }
        for (Delivery del : dels) {

            mapNodes.get(del.getAddress()).setMode(MODE.DELIVERY_NODE);
        }
    }

   
    
    public void clearNodeViewMode() {
        for (NodeView n : mapNodes.values()) {
            n.setMode(MODE.CLASSIC);
        }
    }
    
    public void clearArrowColors() {
        for(ArcView arc:mapArcs.values()){
            arc.resetColors();
        }
    }

    
    @Override
    public void notifyPressed(int x, int y) {
        //boolean voidClic = true;
        NODE_RETURN ret = NODE_RETURN.NOTHING_SELECTED;
        for (NodeView node : mapNodes.values()) {
            NODE_RETURN ret_node = node.onMouseDown(x, y);
            if (ret_node == NODE_RETURN.NODE_SELECTED || ret_node == NODE_RETURN.NODE_ALLREADY_SELECTED) {
                ret = ret_node;
                break;
                //voidClic = false; // TODO also break loop ?
            }
        }
        if (ret == NODE_RETURN.NOTHING_SELECTED) {
            if (getSelectedNode().get() != null) {
                getSelectedNode().clear();
            }
        }
        if (ret != NODE_RETURN.NODE_ALLREADY_SELECTED) {
            fireChangeEvent();
        }
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
            setSelectedNode(new WeakReference<NodeView>(null));
            repaint();
            return;
        }
        mapNodes.get(id).setSelection(true);
        setSelectedNode(new WeakReference<>(mapNodes.get(id)));
        repaint();
    }

    public void setSelectedNode(WeakReference<NodeView> selectedNode) {
        this.selectedNode = selectedNode;
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
