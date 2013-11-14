package model;

public class RoadNetwork {

    private RoadNode root;
    
    public RoadNetwork() {
        this.root = null;
    }
    
    public void setRoot(RoadNode root) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public RoadNode getRoot() {
        return this.root;
    }

    public RoadNode getNodeById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static RoadNetwork loadFromXML(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
