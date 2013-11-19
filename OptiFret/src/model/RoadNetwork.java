package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tsp.RegularGraph;
import tsp.SolutionState;
import tsp.TSP;

public class RoadNetwork {

    private RoadNode root;
    
    public static RoadNetwork loadFromXML(File file) {
        // TODO - implement RoadNetwork.loadFromXml
        throw new UnsupportedOperationException();
    }

    public RoadNetwork() {
        root = null;
    }

    public RoadNode getRoot() {
        return root;
    }
    
    public RoadNode getNodeById(Long id) {
        // TODO - implement RoadNetwork.getNodeById
        throw new UnsupportedOperationException();
    }
    
    public List<RoadNode> getNodes() {
        if(root == null)
            return new ArrayList<>();
        Set<RoadNode> open = new HashSet<>();
        Set<RoadNode> close = new HashSet<>();
        List<RoadNode> l = new ArrayList<>();
        open.add(root);
        while (!open.isEmpty()) {
            RoadNode current = open.iterator().next();
            open.remove(current);
            close.add(current);
            for (RoadNode n : current.getNodes()) {
                if (!close.contains(n)) {
                    open.add(n);
                }
            }
            l.add(current);
        }
        return l;
    }
    
    public List<RoadNode> makeRoute(List<RoadNode> objectives) {
        RegularGraph graph = RegularGraph.loadFromRoadNetwork(this);
        TSP tsp = new TSP(graph);
        SolutionState s = tsp.solve(1000000, 100000);
        if(s == SolutionState.OPTIMAL_SOLUTION_FOUND || s == SolutionState.SOLUTION_FOUND) {
            int[] ls = tsp.getNext();
            return graph.getLsNode(ls);
        }
        return new ArrayList<>();
    }

    public void setRoot(RoadNode root) {
        this.root = root;
    }

    public int getSize() {
        return getNodes().size();
    }

}
