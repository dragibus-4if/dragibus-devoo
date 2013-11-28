package tsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.RoadNode;
import model.RoadSection;

/**
 * Méthode de recherche de chemin utilisant un algorithme A*.
 * L'utilisation est dédié au projet et n'implémente donc pas une interface
 * d'algorithme de recherche plus abstraite et générale.
 *
 * @author Pierre
 */
public class AStar {
    /**
     * Calcul et renvoie le cout entre deux noeuds voisins du graphe.
     * Le graphe est orienté. Le cout est donc calculé en prenant la section
     * entre {@code a} et {@code b}. Si cette section n'existe pas, la fonction
     * renvoie la valeur maximale {@code Double.MAX_VALUE}.
     * 
     * @param a
     * @param b
     * @return Cout entre deux noeuds voisins
     */
    public static Double cost(RoadNode a, RoadNode b) {
        if(a == null || b == null)
            throw new NullPointerException();
        for(RoadSection s : a.getSections()) {
            if(b == s.getRoadNodeEnd()) {
                return s.getCost();
            }
        }
        return Double.MAX_VALUE;
    }
    
    /**
     * Calcul et renvoie le cout heuristique entre deux noeuds du graphe.
     * L'heuristique est la distance euclidienne entre les deux noeuds calculée
     * grâce aux coordonnées x et y de ces derniers.
     * 
     * @param a
     * @param b
     * @return Cout heuristique entre deux noeuds voisins
     */
    private static Double heuristicCost(RoadNode a, RoadNode b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }
    
    /**
     * Cherche le chemin entre deux noeuds dans un graphe.
     * Effectue un parcourt de graphe utilisant l'algorithme A*. Retourne alors
     * une liste de noeud à parcourir pour accéder au noeud {@code goal} à
     * partir de {@code start}. Si ce chemin n'existe pas, {@code null} est
     * renvoyé.
     * 
     * @param start
     * @param goal
     * @return Chemin entre le début et la fin
     */
    static public List<RoadNode> findPath(RoadNode start, RoadNode goal) {
        if(start == null || goal == null)
            throw new NullPointerException();
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
                return reconstructPath(cameFrom, start, goal);
            }
            open.remove(current);
            close.add(current);
            for(RoadNode neighbor : current.getNeighbors()) {
                Double tryGScore = gScore.get(current) + cost(current, neighbor);
                Double tryFScore = tryGScore + heuristicCost(neighbor, goal);
                if(close.contains(neighbor) && fScore.containsKey(neighbor) && tryFScore >= fScore.get(neighbor))
                    continue;
                if(!open.contains(neighbor) || !fScore.containsKey(neighbor) || tryFScore < fScore.get(neighbor)) {
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

    /**
     * Reconstruit le chemin parcouru de puis l'arrivée.
     * Utilise le tableau associatif {@code cameFrom} pour retracer le chemin
     * depuis la fin jusqu'au début.
     * 
     * @param cameFrom
     * @param start
     * @param goal
     * @return Chemin entre le début et la fin
     */
    private static List<RoadNode> reconstructPath(Map<RoadNode, RoadNode> cameFrom, RoadNode start, RoadNode goal) {
        List<RoadNode> p = new ArrayList<>();
        if(goal != start && cameFrom.containsKey(goal)) {
            RoadNode n = cameFrom.get(goal);
            if(n != goal) {
                p = reconstructPath(cameFrom, start, cameFrom.get(goal));                
            }
        }
        p.add(goal);
        return p;
    }
}
