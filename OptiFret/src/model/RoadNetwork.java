/*
 */

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
    
    public RoadNode getRoadNodeById(Long id) {
        return null;
    }
}
