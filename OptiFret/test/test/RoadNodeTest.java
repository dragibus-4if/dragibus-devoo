package test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import model.RoadNode;
import model.RoadSection;

public class RoadNodeTest extends TestCase {

    public void testConstruction() {
        // Test de la construction
        RoadNode node = new RoadNode(0);
        assertNotNull(node);
        assertEquals(node.getNeighbors().size(), 0);
    }

    public void testAddNeighbors() {
        // Vérifie que la taille initiale est à 0
        RoadNode node = new RoadNode(0);

        // Ajout d'un voisin et vérification que la taille est à 1
        node.addNeighbor(new RoadSection(node, new RoadNode(1), 0, 0));
        assertEquals(node.getNeighbors().size(), 1);
        // ...propagation sur quelques autres essais
        for (int size = 2; size < 10; size++) {
            node.addNeighbor(new RoadSection(node, new RoadNode(size), 0, 0));
            assertEquals(node.getNeighbors().size(), size);
        }

        // Ajout d'un node null comme fils. Lève une exception et la taille ne
        // doit pas changer.
        RoadNode node1 = new RoadNode(0);
        try {
            node1.addNeighbor(null);
            fail("Ajout d'un pointeur vide comme voisin");
        } catch (NullPointerException e) {
        }
        assertEquals(node1.getNeighbors().size(), 0);
    }
};
