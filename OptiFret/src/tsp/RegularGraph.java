package tsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import model.RoadNetwork;
import model.RoadNode;
import model.RoadSection;

/**
 * @author Christine Solnon
 *
 */
public class RegularGraph implements Graph {

    private final int nbVertices;
    private final int maxArcCost;
    private final int minArcCost;
    private final int[][] cost;
    private final ArrayList<ArrayList<Integer>> succ;

    /**
     *
     * @param net
     * @return
     */
    public static RegularGraph loadFromRoadNetwork(RoadNetwork net) {
        if (net == null) {
            throw new NullPointerException();
        }
        if (net.getRoot() == null) {
            return new RegularGraph(0, 0, 0, new int[0][0], new ArrayList<ArrayList<Integer>>());
        }

        Set<RoadNode> open = new HashSet<>();
        Set<RoadNode> close = new HashSet<>();
        Long index = new Long(0);
        Map<RoadNode, Long> indexMap = new HashMap<>();
        open.add(net.getRoot());
        while (!open.isEmpty()) {
            RoadNode current = open.iterator().next();
            open.remove(current);
            close.add(current);
            for (RoadSection section : current.getSections()) {
                RoadNode n = section.getRoadNodeEnd();
                if (!close.contains(n)) {
                    open.add(n);
                }
            }
            indexMap.put(current, index);
            index++;
        }
        
        open = new HashSet<>();
        open.add(net.getRoot());
        close = new HashSet<>();
        Long size = index;
        int[][] costs = new int[size.intValue()][size.intValue()];
        ArrayList<ArrayList<Integer>> succ = new ArrayList<>();
        for(int i = 0 ; i < size ; i++) {
            succ.add(new ArrayList<Integer>());
        }
        while (!open.isEmpty()) {
            RoadNode current = open.iterator().next();
            Long currentIndex = indexMap.get(current);
            open.remove(current);
            close.add(current);
            for (RoadSection section : current.getSections()) {
                RoadNode n = section.getRoadNodeEnd();
                if (!close.contains(n)) {
                    open.add(n);
                }
                Long nIndex = indexMap.get(n);
                succ.get(currentIndex.intValue()).add(new Integer(nIndex.intValue()));
                costs[currentIndex.intValue()][nIndex.intValue()] = (int)section.getCost();
            }
        }

        return null;
    }

    public RegularGraph(int nbVertices, int maxArcCost, int minArcCost, int[][] cost, ArrayList<ArrayList<Integer>> succ) {
        this.nbVertices = nbVertices;
        this.maxArcCost = maxArcCost;
        this.minArcCost = minArcCost;
        this.cost = cost;
        this.succ = succ;
    }

    @Override
    public int getMaxArcCost() {
        return maxArcCost;
    }

    @Override
    public int getMinArcCost() {
        return minArcCost;
    }

    @Override
    public int getNbVertices() {
        return nbVertices;
    }

    @Override
    public int[][] getCost() {
        return cost;
    }

    @Override
    public int[] getSucc(int i) throws ArrayIndexOutOfBoundsException {
        if ((i < 0) || (i >= nbVertices)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int[] tab = new int[succ.get(i).size()];
        for (int j = 0; j < tab.length; j++) {
            tab[j] = succ.get(i).get(j);
        }
        return tab;
    }

    @Override
    public int getNbSucc(int i) throws ArrayIndexOutOfBoundsException {
        if ((i < 0) || (i >= nbVertices)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return succ.get(i).size();
    }

}
