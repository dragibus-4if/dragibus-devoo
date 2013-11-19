package tsp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
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
    private final Map<Integer, RoadNode> index2Node;

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
            return new RegularGraph(0, 0, 0, new int[0][0], new ArrayList<ArrayList<Integer>>(), new TreeMap<Integer, RoadNode>());
        }

        Set<RoadNode> open = new HashSet<>();
        Set<RoadNode> close = new HashSet<>();
        Integer index = new Integer(0);
        Map<Integer, RoadNode> indexMap = new TreeMap<>();
        open.add(net.getRoot());
        while (!open.isEmpty()) {
            RoadNode current = open.iterator().next();
            open.remove(current);
            close.add(current);
            for (RoadNode n : current.getNodes()) {
                if (!close.contains(n)) {
                    open.add(n);
                }
            }
            indexMap.put(index, current);
            index++;
        }
        
        open = new HashSet<>();
        open.add(net.getRoot());
        close = new HashSet<>();
        Integer size = index;
        int[][] costs = new int[size.intValue()][size.intValue()];
        ArrayList<ArrayList<Integer>> succ = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        int max = 0;
        for(int i = 0 ; i < size ; i++) {
            succ.add(new ArrayList<Integer>());
        }
        while (!open.isEmpty()) {
            RoadNode current = open.iterator().next();
            Integer currentIndex = null;
            for(Entry<Integer, RoadNode> e : indexMap.entrySet()) {
                if(e.getValue().equals(current)) {
                    currentIndex = e.getKey();
                    break;
                }
            }
            open.remove(current);
            close.add(current);
            for (RoadNode n : current.getNodes()) {
                if (!close.contains(n)) {
                    open.add(n);
                }
            }
            for (RoadSection section : current.getSections()) {
                RoadNode n = section.getRoadNodeEnd();
                Integer nIndex = null;
                for(Entry<Integer, RoadNode> e : indexMap.entrySet()) {
                    if(e.getValue().equals(n)) {
                        nIndex = e.getKey();
                        break;
                    }
                }
                succ.get(currentIndex).add(new Integer(nIndex));
                costs[currentIndex][nIndex] = (int)section.getCost();
                if(section.getCost() < min)
                    min = (int) section.getCost();
                if(section.getCost() > max)
                    max = (int) section.getCost();
            }
        }

        return new RegularGraph(indexMap.size(), max, min, costs, succ, indexMap);
    }

    public RegularGraph(int nbVertices, int maxArcCost, int minArcCost,
            int[][] cost, ArrayList<ArrayList<Integer>> succ,
            Map<Integer, RoadNode> index2Node) {
        this.nbVertices = nbVertices;
        this.maxArcCost = maxArcCost;
        this.minArcCost = minArcCost;
        this.cost = cost;
        this.succ = succ;
        this.index2Node = index2Node;
    }
    
    public List<RoadNode> getLsNode(int[] indexes) {
        List<RoadNode> l = new ArrayList<>();
        for(int i : indexes) {
            l.add(index2Node.get(new Integer(i)));
        }
        return l;
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
