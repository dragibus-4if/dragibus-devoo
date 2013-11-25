package tsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.RoadNode;
import model.RoadSection;

public class AStar {
    public static Double cost(RoadNode a, RoadNode b) {
        for(RoadSection s : a.getSections()) {
            if(b == s.getRoadNodeEnd()) {
                return s.getCost();
            }
        }
        return Double.MAX_VALUE;
    }
    
    private static Double heuristicCost(RoadNode a, RoadNode b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }
    
    static public List<RoadNode> findPath(RoadNode start, RoadNode goal) {
        Set<RoadNode> open = new HashSet<>();
        Set<RoadNode> close = new HashSet<>();
        Map<RoadNode, RoadNode> cameFrom = new HashMap<>();
        Map<RoadNode, Double> gScore = new HashMap<>();
        Map<RoadNode, Double> fScore = new HashMap<>();
        
        open.add(start);
        gScore.put(start, new Double(0));
        fScore.put(start, gScore.get(start) + heuristicCost(start, goal));
        
        while(!open.isEmpty()) {
            RoadNode current = null;
            Double min = Double.MAX_VALUE;
            for(RoadNode n : open) {
                if(fScore.containsKey(n) && fScore.get(n) < min) {
                    min = fScore.get(n);
                    current = n;
                }
            }
            if(current == goal) {
                return reconstructPath(cameFrom, goal);
            }
            open.remove(current);
            close.add(current);
            for(RoadNode neighbor : current.getNeighbors()) {
                Double tryGScore = gScore.get(current) + cost(current, neighbor);
                Double tryFScore = tryGScore + heuristicCost(current, neighbor);
                if(!fScore.containsKey(neighbor) || tryFScore < fScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tryGScore);
                    fScore.put(neighbor, tryFScore);
                    if(!open.contains(neighbor))
                        open.add(neighbor);
                }
            }
        }
        return null;
    }

    private static List<RoadNode> reconstructPath(Map<RoadNode, RoadNode> cameFrom, RoadNode goal) {
        if(cameFrom.containsKey(goal)) {
            List<RoadNode> p = reconstructPath(cameFrom, cameFrom.get(goal));
            p.add(goal);
            return p;
        }
        List<RoadNode> p = new ArrayList<>();
        p.add(goal);
        return p;
    }
}
