package model;

import java.util.List;
import java.util.Objects;

public class RoadNode {

    private Long id;
    private List<RoadNode> neighbors;

    public Long getId() {
        return id;
    }

    public List<RoadNode> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(RoadNode node) {
        neighbors.add(node);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RoadNode other = (RoadNode) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
