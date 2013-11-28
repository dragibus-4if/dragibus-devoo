package tsp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import model.Delivery;
import model.RoadNetwork;
import model.RoadNode;

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
    private final List<Delivery> objectives;
    
    
    /*
     * Permet de convertir <code>net</code> (graphe entier) et <code>objectives</code>
     *  (la liste des livraisons) en un <code>RegularGraph</code> qui est un graphe
     *  qui pourra être traité par le module TSP, et execute l'algorithm AStar
     *  afin d'établir un plus court chemin parmi le graphe entier.
     * 
     * @param net le graphe des noeuds entier. 
     * @param objectives la liste des livraisons à effectuer
     * @return RegularGraph le graphe formatté que TSP est capable de traiter.
     *  Réprésente le graphe des livraisons liées par des arcs de plus faible coût.
     */
    public static RegularGraph loadFromRoadNetwork(RoadNetwork net, List<Delivery> objectives) {
        if (net == null || objectives == null) {
            throw new NullPointerException();
        }
        if (net.getRoot() == null) {
            return new RegularGraph(0, 0, 0, new int[0][0], new ArrayList<ArrayList<Integer>>(), new TreeMap<Integer, RoadNode>(), new ArrayList<Delivery>());
        }

        Set<RoadNode> open = new HashSet<>();
        Set<RoadNode> close = new HashSet<>();
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
            indexMap.put(current.getId().intValue(), current);
        }
        
        ArrayList<ArrayList<Integer>> succ = new ArrayList<>();
        for(int i = 0 ; i < objectives.size() ; i++) {
            Delivery d1 = objectives.get(i);
            ArrayList<Integer> succEq = new ArrayList<>();
            ArrayList<Integer> succNext = new ArrayList<>();
            Date minDate = null;
            for(int j = 0 ; j < objectives.size() ; j++) {
                Delivery d2 = objectives.get(j);
                if(d1 != d2) {
                    if(d1.getTimeSlot().getBegin().equals(d2.getTimeSlot().getBegin())) {
                        succEq.add(j);
                    }
                    else if(d1.getTimeSlot().getBegin().before(d2.getTimeSlot().getBegin())) {
                        if(minDate != null && d2.getTimeSlot().getBegin().equals(minDate)) {
                            succNext.add(j);
                        }
                        else if(minDate == null || d2.getTimeSlot().getBegin().before(minDate)) {
                            minDate = d2.getTimeSlot().getBegin();
                            succNext.clear();
                            succNext.add(j);
                        }
                    }
                }
            }
            succEq.addAll(succNext);
            if(succNext.isEmpty()) {
                succEq.add(0);
            }
            succ.add(succEq);
        }

        // Calcul du Dijstrak pour chaque "paire de livraison" parmis les successeurs
        int size = objectives.size();
        int min = Integer.MAX_VALUE;
        int max = 0;
        int[][] distances = new int[size][size];
        for (int i = 0 ; i < succ.size() ; i++) {
            for (int j = 0 ; j < succ.get(i).size() ; j++) {
                // Effectuer le AStar
                // Récupérer la longueur qui correspond au cout de cheminement.
                Integer succIndex = succ.get(i).get(j);
                Integer addr1 = objectives.get(i).getAddress().intValue();
                Integer addr2 = objectives.get(succIndex).getAddress().intValue();
                List<RoadNode> pathNode = AStar.findPath(indexMap.get(addr1), indexMap.get(addr2));
                Double c = new Double(0);
                for (int k = 1; k < pathNode.size(); k++) {
                    c += AStar.cost(pathNode.get(k - 1), pathNode.get(k));
                }
                distances[i][succ.get(i).get(j)] = c.intValue();
                if(distances[i][succ.get(i).get(j)] > max) {
                    max = distances[i][succ.get(i).get(j)];
                }
                if(distances[i][succ.get(i).get(j)] < min) {
                    min = distances[i][succ.get(i).get(j)];
                }
            }
        }

        return new RegularGraph(size, new Double(max).intValue(), new Double(min).intValue(), distances, succ, indexMap, objectives);
    }

    /**
     * Constructeur du <code>RegularGraph</code>
     * @param nbVertices le nombre de points d'un <code>RegularGraph</code> 
     * @param maxArcCost le cout maximal d'un arc dans le graphe
     * @param minArcCost le cout minimal d'un arc dans le graphe
     * @param cost matrice de cout du graphe
     * @param succ la liste des successeurs possibles pour un point donné
     * @param index2Node map des liens entre les indexes des nodes et leur Id
     * @param objectives la liste des livraisons à effectuer
     */
    public RegularGraph(int nbVertices, int maxArcCost, int minArcCost,
            int[][] cost, ArrayList<ArrayList<Integer>> succ,
            Map<Integer, RoadNode> index2Node,
            List<Delivery> objectives) {
        this.nbVertices = nbVertices;
        this.maxArcCost = maxArcCost;
        this.minArcCost = minArcCost;
        this.cost = cost;
        this.succ = succ;
        this.index2Node = index2Node;
        this.objectives = objectives;
    }
    
    /**
     * 
     * 
     * @param from 
     * @param to 
     * @return la liste des <code>RoadNode</code> entre <code>from</code> et 
     * <code>to</code>, parmis le chemin de la tournée.
     */
    private List<RoadNode> getPath(int from, int to) {
        Integer addr1 = objectives.get(from).getAddress().intValue();
        Integer addr2 = objectives.get(to).getAddress().intValue();
        List<RoadNode> l = new ArrayList<>();
        l.add(index2Node.get(addr1));
        List<RoadNode> path = AStar.findPath(index2Node.get(addr1), index2Node.get(addr2));
        l.addAll(path);
        return l;
    }

    /**
     * 
     * 
     * @param indexes
     * @return la liste des <code>RoadNode</code> , parmis le chemin de la tournée.
     */
    public HashMap<Delivery, List<RoadNode>> getPaths(int[] indexes) {
        HashMap<Delivery, List<RoadNode>> l = new HashMap<>();
        int index = 0;
        do {
            l.put(objectives.get(index), getPath(index, indexes[index]));
            index = indexes[index];
        } while(index != 0);
        return l;
    }

    
    /**
     * @return the maximal cost of an arc of <code>this</code>
     */
    @Override
    public int getMaxArcCost() {
        return maxArcCost;
    }

    /**
     * @return the minimal cost of an arc of <code>this</code>
     */
    @Override
    public int getMinArcCost() {
        return minArcCost;
    }

    /**
     * @return the number of vertices of <code>this</code>
     */
    @Override
    public int getNbVertices() {
        return nbVertices;
    }

    /**
     * @return the <code>cost</code> matrix of <code>this</code>: for all
     * vertices <code>i</code> and <code>j</code>, if <code>(i,j)</code> is an
     * arc of <code>this</code>, then <code>cost[i][j]</code> = cost of
     * <code>(i,j)</code>, otherwise
     * <code>cost[i][j] = this.getMaxArcCost()+1</code>
     */
    @Override
    public int[][] getCost() {
        return cost;
    }

    /**
     * @param i a vertex such that <code>0 <= i < this.getNbVertices()</code>
     * @return an array
     * containing all successor vertices of <code>i</code> in <code>this</code>
     * @throws ArrayIndexOutOfBoundsException If <code>i<0</code> or
     * <code>i>=this.getNbVertices()</code>
     */
    @Override
    public int[] getSucc(int i) throws ArrayIndexOutOfBoundsException {
        if ((i < 0) || (i >= nbVertices)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if(succ.get(i).isEmpty())
            return new int[0];
        int[] tab = new int[succ.get(i).size()];
        for (int j = 0; j < tab.length; j++) {
            tab[j] = succ.get(i).get(j);
        }
        return tab;
    }

    /**
     * @param i a vertex such that <code>0 <= i < this.getNbVertices()</code>
     * @return the numbe
     * r of successor vertices of <code>i</code> in <code>this</code>
     * @throws ArrayIndexOutOfBoundsException If <code>i<0</code> or
     * <code>i>=this.getNbVertices()</code>
     */
    @Override
    public int getNbSucc(int i) throws ArrayIndexOutOfBoundsException {
        if ((i < 0) || (i >= nbVertices)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return succ.get(i).size();
    }

}
