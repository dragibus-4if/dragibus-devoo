package tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import model.Delivery;
import model.DeliveryRound;
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

    /**
     *
     * @param net
     * @return
     */
    public static RegularGraph loadFromRoadNetwork(RoadNetwork net, DeliveryRound thatRound) {
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
        
        //Ancien calcul du RegularGraph
//        open = new HashSet<>();
//        open.add(net.getRoot());
//        close = new HashSet<>();
//        Integer size = index;
//        int[][] costs = new int[size.intValue()][size.intValue()];
//        ArrayList<ArrayList<Integer>> succ = new ArrayList<>();
//        double min = Double.MAX_VALUE;
//        double max = 0;
//        for(int i = 0 ; i < size ; i++) {
//            succ.add(new ArrayList<Integer>());
//        }
//        while (!open.isEmpty()) {
//            RoadNode current = open.iterator().next();
//            Integer currentIndex = null;
//            for(Entry<Integer, RoadNode> e : indexMap.entrySet()) {
//                if(e.getValue().equals(current)) {
//                    currentIndex = e.getKey();
//                    break;
//                }
//            }
//            open.remove(current);
//            close.add(current);
//            for (RoadNode n : current.getNodes()) {
//                if (!close.contains(n)) {
//                    open.add(n);
//                }
//            }
//            for (RoadSection section : current.getSections()) {
//                RoadNode n = section.getRoadNodeEnd();
//                Integer nIndex = null;
//                for(Entry<Integer, RoadNode> e : indexMap.entrySet()) {
//                    if(e.getValue().equals(n)) {
//                        nIndex = e.getKey();
//                        break;
//                    }
//                }
//                succ.get(currentIndex).add(new Integer(nIndex));
//                costs[currentIndex][nIndex] = new Double(section.getCost()).intValue();
//                if(section.getCost() < min)
//                    min = (double) section.getCost();
//                if(section.getCost() > max)
//                    max = (double) section.getCost();
//            }
//        }
        
        
        //Nouveau calcul du RegularGraph (limité aux livraisons)
        
        List<RoadNode> path = thatRound.getPath();
        List<Delivery> objectives = thatRound.getDeliveries();
        
        
        int size = objectives.size();
        int min = Integer.MAX_VALUE;
        int max = 0;
        int [][] distances = new int [size][size];
        open = new HashSet<>();
        close = new HashSet<>();
        ArrayList<ArrayList<Integer>> succ = new ArrayList<>();
        ArrayList<Integer> tsList = new ArrayList <>();
        
        /*
         * Cette List n'est pas read pour le moment, 
         * mais fait correspondre des Id de livraisons 
         * (dans la DeliveryRound) avec leurs adresses      
        */
        ArrayList<Long> adrList = new ArrayList <>();
        
        //Premier parsing des timeslots + adresses
        adrList.add(objectives.get(0).getAddress());
        long currentTimeSlot = objectives.get(1).getTimeSlot().getBegin().getTime();
        tsList.add(1);
        adrList.add(objectives.get(1).getAddress());
        for (int j = 2 ; j < size ; j++ ) {
            adrList.add(objectives.get(j).getAddress());
            if (objectives.get(j).getTimeSlot().getBegin().getTime()!= 
                    currentTimeSlot) {
                tsList.add(j);
                currentTimeSlot = objectives.get(j).getTimeSlot().
                        getBegin().getTime();
            }
        }
        
        
        //Etablissement de la liste des successeurs (parmis les livraisons)
        int progress = 0;   //Progres general dans la liste des livraisons
        int progTSB = 0;    //Debut des timeSlot a pointer pour une adresse
        int progNTS = 1;    //Prochaine timeSlot a traiter
        int progTSE = 2;    //Fin des timeSlot a pointer pour une adresse
        
        while ( progress < size ) {
            //Pour chaque livraison, on fait une liste de successeurs
            succ.add(new ArrayList<Integer>());
            //On parcourt les livraisons au sein des timeSlots ciblées.
            for (int j = tsList.get(progTSB) ; j < tsList.get(progTSE) ; j++ ) {
                //Ne doit pas pointer vers sois même
                if ( j != progress ) {
                    succ.get(progress).add(j);
                }
            }
            progress++;
            if (progress == tsList.get(progNTS)) {
                progTSB++;
                progNTS++;
                progTSE++;
            }
        }
        
        
        // Calcul du Dijstrak pour chaque "paire de livraison" parmis les successeurs
        for ( int i = 0 ; i < succ.size() ; i++ ) {
            for ( int j = 0 ; j < succ.get(i).size() ; j++ ) {
                // Effectuer le AStar
                // AStar entre indexMap[i] et indexMap[j].
                // Récupérer la longueur qui correspond au cout de cheminement.
                List<RoadNode> pathNode = AStar.findPath(indexMap.get(i), indexMap.get(j));
                Double c = new Double(0);
                for(int k = 1 ; k < pathNode.size() ; k++) {
                    c += AStar.cost(pathNode.get(i - 1), pathNode.get(i));
                }
                distances[i][j] = c.intValue();
            }
        }
        
        
        
        return new RegularGraph(indexMap.size(), new Double(max).intValue(), new Double(min).intValue(), distances, succ, indexMap);
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
