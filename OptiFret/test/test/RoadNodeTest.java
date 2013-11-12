package test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import model.RoadNode;

public class RoadNodeTest extends TestCase {
    public void testConstruction() {
        // Test de la construction
        assertNotNull(new RoadNode());
        assertEquals(new RoadNode().getNeighbors().size(), 0);
    }
    
    public void testAddNeighbors() {
        // Vérifie que la taille initiale est à 0
        RoadNode node = new RoadNode();
        assertEquals(node.getNeighbors().size(), 0);
        
        // Ajout d'un voisin et vérification que la taille est à 1
        node.addNeighbor(new RoadNode());
        assertEquals(node.getNeighbors().size(), 1);
        
        // Création d'un nouveau node et ajout de l'ancien comme voisin.
        // Le voisinnage est symétrique donc l'ancien node doit avoir 2
        // voisins après l'opération.
        RoadNode node2 = new RoadNode();
        node2.addNeighbor(node);
        assertEquals(node2.getNeighbors().size(), 1);
        assertEquals(node.getNeighbors().size(), 2);
        
        // Ajout d'un node null comme fils. Lève une exception et la taille ne
        // doit pas changer.
        try {
            node.addNeighbor(null);
            fail("Ajout d'un pointeur vide comme voisin");
        }
        catch(NullPointerException e) {
        }
        assertEquals(node.getNeighbors().size(), 2);
    }
};
