package model;

import java.util.*;

public class RoadNode {

    private Long id;
    private int x;
    private int y;
    private List<RoadSection> sections;

    public RoadNode() {
        // TODO - implement RoadNode.RoadNode
        throw new UnsupportedOperationException();
    }
    
    public void addNeighbor(RoadNode node, RoadSection section) {
        // TODO - implement RoadNode.addNeighbor
        throw new UnsupportedOperationException();
    }

    public List<RoadSection> getSections() {
        return sections;
    }

    public List<RoadNode> getNeighbors() {
        List<RoadNode> ls = new LinkedList<RoadNode>();
        for (RoadSection section : getSections()) {
            ls.add(section.getRoadNodeEnd());
        }
        return ls;
    }

}
