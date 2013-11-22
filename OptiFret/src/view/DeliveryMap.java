/*
 */
package view;

/**
 *
 * @author jmcomets
 */
public class DeliveryMap extends Canvas {

    private ArrayList<ArcView> mapArcs;
    private ArrayList<NodeView> mapNodes;

    public MapCanvas(List<RoadNode> nodes) {
        super();
        mapArcs = new ArrayList<>();
        mapNodes = new ArrayList<>();

        if (nodes != null) {
            for (RoadNode rn : nodes) {
                if (rn.getNeighbors() != null) {
                    if (!mapNodes.contains(rn)) {
                        mapNodes.add(new NodeView(rn.getX(), rn.getY()));
                    }
                    for (RoadNode neighbor : rn.getNeighbors()) {
                        ArcView temp = new ArcView(rn.getX(), rn.getY(), neighbor.getX(), neighbor.getY());
                        if (!mapArcs.contains(temp)) {
                            mapArcs.add(temp);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
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
