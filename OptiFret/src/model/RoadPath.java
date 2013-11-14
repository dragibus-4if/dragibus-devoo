package model;

import java.util.List;

class RoadPath {

    private List<RoadNode> nodes;

    public void addNode(RoadNode node) {
        nodes.add(node);
    }

    public List<RoadNode> getNodes() {
        return nodes;
    }

}
