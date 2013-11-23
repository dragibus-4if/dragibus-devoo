/*
 */
package view;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import model.RoadNode;

/**
 *
 * @author jmcomets
 */
public class DeliveryMap extends JPanel {

    private ArrayList<ArcView> mapArcs;
    private ArrayList<NodeView> mapNodes;

    public DeliveryMap() {
        super();
        mapArcs = new ArrayList<>();
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

    }

    public void update(List<RoadNode> nodes) {
        if (nodes == null) {
            return;
        }

        for (RoadNode rn : nodes) {
            if (rn.getNeighbors() == null) {
                break;
            }
            NodeView tempNode = new NodeView(rn.getX(), rn.getY());
            if (!mapNodes.contains(tempNode)) {
                mapNodes.add(tempNode);
            }
            for (RoadNode neighbor : rn.getNeighbors()) {

                ArcView temp = new ArcView(rn.getX(), rn.getY(), neighbor.getX(), neighbor.getY(), 4);
                if (!mapArcs.contains(temp)) {
                    mapArcs.add(temp);
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        draw(g);
    }

    private void draw(Graphics g) {
        for (ArcView arc : mapArcs) {
            arc.draw(g);
        }
        for (NodeView node : mapNodes) {
            node.draw(g);
        }
    }
}
