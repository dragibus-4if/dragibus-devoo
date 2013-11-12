package model;

import java.util.List;

/**
 *
 * @author jmcomets
 */
public class RoadNetwork {

    private List<RoadNode> nodes;

    public void addNode(RoadNode node) {
        nodes.add(node);
    }

    public RoadNode getNodeById(Long id) {
        return null;
    }

    public int getSize() {
        return nodes.size();
    }

    public static RoadNetwork loadFromXML(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
