/*
 */
package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import model.RoadNode;
import view.NodeView.MODE;

/**
 *
 * @author jmcomets
 */
public class DeliveryMap extends JPanel {

    private Map<Integer ,ArcView> mapArcs;
    private ArrayList<NodeView> mapNodes;
    
    private WeakReference<NodeView> selectedNode;

    private int maxX=0;

   
    private int maxY=0;
    
    public DeliveryMap() {
        super();
        this.setDoubleBuffered(true);
        mapArcs = new LinkedHashMap<>();
        mapNodes = new ArrayList<>();
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(String.valueOf(e.getX()));
                System.out.println(String.valueOf(e.getY()));

                notifyPressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println(String.valueOf(e.getX()));
                System.out.println(String.valueOf(e.getY()));

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
        
    }

    public void updateNetwork(List<RoadNode> nodes) {
        if (nodes == null) {
            return;
        }
        mapArcs.clear();
        mapNodes.clear();
        for (RoadNode rn : nodes) {
            if (rn.getNeighbors() == null) {
                break;
            }
            if(rn.getX()>maxX){
                maxX=rn.getX();
            }
            if(rn.getY()>maxY){
                maxY=rn.getY();
            }
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), new WeakReference<>(this), MODE.CLASSIC );
            if (!mapNodes.contains(tempNode)) {
                mapNodes.add(tempNode);
            }
            for (RoadNode neighbor : rn.getNeighbors()) {
                ArcView temp = new ArcView(rn.getX(), rn.getY(), neighbor.getX(), neighbor.getY(), 0);
                if (!mapArcs.containsKey(temp.hashCode())) {
                    mapArcs.put(temp.hashCode(), temp);
                }
            }
        }
        this.setPreferredSize(new Dimension(maxX+20, maxY+20));
    }
    
    public void updateDeliveryNodes(List<RoadNode> nodes){
        if (nodes == null) {
            return;
        }
        
        for (RoadNode rn : nodes) {
            if (rn.getNeighbors() == null) {
                break;
            }
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), new WeakReference<>(this), MODE.CLASSIC );
            for(int i=0;i<mapNodes.size();i++){
                if(tempNode.equals(mapNodes.get(i))){
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
        for (NodeView node : mapNodes) {
            node.onMouseDown(e.getX(), e.getY());
        }
        System.out.println("Fin parcours nodes Pressed");
        repaint();
    }

    private void notifyReleased(MouseEvent e) {
        System.out.println("Début parcours nodes Released");
        for (NodeView node : mapNodes) {
            node.onMouseUp(e.getX(), e.getY());
        }
        System.out.println("Fin parcours nodes Released");
    }
    
    private void notifyMoved(MouseEvent e){
         for (NodeView node : mapNodes) {
            node.onMouseOver(e.getX(), e.getY());
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        draw(g);
    }

   
    
    private void draw(Graphics g) {
        for (ArcView arc : mapArcs.values()) {
            arc.draw(g);
        }
        for (NodeView node : mapNodes) {
            node.draw(g);
        }
    }

    public WeakReference<NodeView> getSelectedNode() {
        return selectedNode;
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
}
