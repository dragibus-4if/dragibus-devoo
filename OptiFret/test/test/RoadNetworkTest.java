package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.StringReader;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import model.RoadNetwork;
import model.RoadNode;
import model.RoadSection;

public class RoadNetworkTest extends TestCase {

    public void testSimpleIO() {
        // Argument nul => NullPointerException
        try {
            RoadNetwork.loadFromXML(null);
            throw new Exception();
        } catch (NullPointerException e) {
        } catch (Exception e) {
            fail("loadFromXML(null) devrait causer une NullPointerException");
        }
        
        // Fichier inexistant => Exception
        try {
            RoadNetwork.loadFromXML(new FileReader(new File("C://frthi/f4242")));
            throw new Exception();
        } catch (IOException e) {
        } catch (Exception e) {
            fail("loadFromXML(fichier inexistant) devrait causer une IOException");
        }

        // Dossier => Exception
        try {
            RoadNetwork.loadFromXML(new FileReader(new File(".")));
            throw new Exception();
        } catch (IOException e) {
            System.out.println("Test error while generating temporary files for test purposes.");
        } catch (Exception e) {
            fail("loadFromXML(dossier) devrait causer une IOException");
        }
    }

    public void testXMLSyntax() {
        try {
            // Test d'une fermeture de balise manquante
            assertNull(RoadNetwork.loadFromXML(new StringReader("<root>")));
            
            // Test d'une ouverture de balise manquante
            assertNull(RoadNetwork.loadFromXML(new StringReader("<root></balise></root>")));
        } catch (IOException ex) {
            Logger.getLogger(RoadNetworkTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    public void testXMLSemantic() {
        try {
            // Si la balise racine est un élément quelconque (différent de ce qui
            // est attendu), la fonction renvoie null.
            assertNull(RoadNetwork.loadFromXML(new StringReader("<root></root>")));
            
            // Si le document contient un élément non défini, la fonction renvoie
            // null.
            String s1 = "<road_network><autre></autre></road_network>";
            assertNull(RoadNetwork.loadFromXML(new StringReader(s1)));
            
            // Si c'est la bonne balise racine, la fonction renvoie quelque chose de non
            // null.
            String s2 = "<road_network></road_network>";
            RoadNetwork rn = RoadNetwork.loadFromXML(new StringReader(s2));
            assertNotNull(rn);
            assertEquals(rn.getSize(), 0);
            
            // Voir le format des xmls à lire pour vérifier ça
        } catch (IOException ex) {
            Logger.getLogger(RoadNetworkTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    public void testRoot() {
        // Si le réseau vient d'etre créé, sa taille est de 0
        RoadNetwork net = new RoadNetwork();
        assertEquals(net.getSize(), 0);

        // Ajoute un noeud unique
        net.setRoot(new RoadNode(42));
        assertEquals(net.getSize(), 1);

        // Ajoute un noeud avec un fils
        RoadNode node = new RoadNode(1337);
        node.addNeighbor(new RoadSection(node, new RoadNode(31415), 0.0, 0.0));
        net.setRoot(node);
        assertSame(net.getRoot(), node);
        assertEquals(net.getSize(), 2);

        // Ajoute un fils au noeud précédemment créé puis vérification que la
        // taille augmente de 1
        node.addNeighbor(new RoadSection(node, new RoadNode(1234), 0.0, 0.0));
        assertEquals(net.getSize(), 3);

        // Ajout d'un noeud directement depuis le getter
        net.getRoot().addNeighbor(new RoadSection(net.getRoot(), new RoadNode(9876), 0.0, 0.0));
        assertEquals(net.getSize(), 4);
    }

    /*public void testGetNodeById() {
        RoadNetwork net = new RoadNetwork();

        // Essaye d'acceder à élément n'existant pas (ou invalide)
        assertNull(net.getNodeById(null));
        assertNull(net.getNodeById(new Long(-1)));
        assertNull(net.getNodeById(new Long(0)));

        // Ajout d'un noeud dans le graphe puis récupération de ce noeud par
        // l'id
        RoadNode node = new RoadNode(0);
        Long id = node.getId();
        net.setRoot(node);
        assertNotNull(net.getNodeById(id));
        assertEquals(net.getNodeById(id).getId(), id);

        // Ajout d'un noeud comme fils du précédent puis recherche dans le
        // graphe
        RoadNode node2 = new RoadNode(1);
        Long id2 = node2.getId();
        node2.addNeighbor(new RoadSection(node2, node, 0.0, 0.0));
        assertNotNull(net.getNodeById(id2));
        assertEquals(net.getNodeById(id2).getId(), id2);

        // Essaye d'acceder à élément n'existant toujours pas (ou invalide)
        assertNull(net.getNodeById(null));
        assertNull(net.getNodeById(new Long(-1)));
    }*/
    
    public void testGetNodes() {
        RoadNetwork net = new RoadNetwork();

        // Récupère la liste des nodes sans avoir défini une racine.
        assertNotNull(net.getNodes());
        assertEquals(net.getNodes().size(), 0);

        // Ajout d'un noeud dans le graphe puis récupération de ce noeud dans
        // une liste
        RoadNode node = new RoadNode(0);
        net.setRoot(node);
        assertEquals(net.getNodes().size(), 1);
        assertEquals(net.getNodes().iterator().next(), net.getRoot());
        assertEquals(net.getNodes().iterator().next(), node);

        // Ajout d'un noeud comme fils du précédent puis recherche dans le
        // graphe et vérification que les deux nodes sont dans la liste des
        // noeuds. Il n'y a pas d'ordre défini dans le parcours.
        RoadNode node2 = new RoadNode(1);
        node2.addNeighbor(new RoadSection(node2, node, 0.0, 0.0));
        assertEquals(net.getNodes().size(), 2);
        Iterator<RoadNode> it = net.getNodes().iterator();
        RoadNode n1 = it.next();
        RoadNode n2 = it.next();
        assertEquals(it.hasNext(), false);
        if (!((n1 == node && n2 == node2) || (n1 == node2 && n2 == node))) {
            fail("Les 2 noeuds ne sont pas trouvés");
        }
    }
};
