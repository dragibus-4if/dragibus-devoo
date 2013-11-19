package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    }

    public RoadNode getRoot() {
        return root;
    }
    
    public RoadNode getNodeById(Long id) {
        // TODO - implement RoadNetwork.getNodeById
        throw new UnsupportedOperationException();
    }
    
    public List<RoadNode> getNodes() {
        // TODO - implement RoadNetwork.getNodes
        throw new UnsupportedOperationException();
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

}
