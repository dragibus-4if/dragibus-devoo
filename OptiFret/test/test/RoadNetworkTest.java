package test;

import java.io.IOException;

import java.io.StringReader;
import java.util.Iterator;
import junit.framework.TestCase;
import model.RoadNetwork;
import model.RoadNode;
import model.RoadSection;

public class RoadNetworkTest extends TestCase {

    public void testXMLSyntax() throws IOException {
        // Si la syntaxe XML est mauvaise, la fonction lance une IOException

        // Test d'une fermeture de balise manquante
        StringReader sr = new StringReader("<Reseau>");
        try {
            RoadNetwork.loadFromXML(sr);
            fail("Erreur de syntaxe: fermeture de balise manquante");
        } catch (IOException e) {
        }

        // Test d'une ouverture de balise manquante
        sr = new StringReader("<Reseau></Noeud><Reseau>");
        try {
            RoadNetwork.loadFromXML(sr);
            fail("Erreur de syntaxe: ouverture de balise manquante");
        } catch (IOException e) {
        }

        // Il n'y a pas tout les cas sur la syntaxe XML. La bibliothèque
        // utilisée doit pouvoir détecter les erreurs. Nous l'utilisons et ces
        // quelques tests permettent de montrer qu'on capte que la bibliothèque
        // a détecté une erreur.
    }

    public void testXMLSemantic() throws IOException {

        // Si la balise racine est un élément quelconque (différent de ce qui
        // est attendu), la fonction lance une erreur.
        StringReader sr = new StringReader("<root></root>");
        try {
            RoadNetwork.loadFromXML(sr);
            fail("Erreur roadNetwork.loadFromXML : \n"
                    + "Erreur syntaxique :\n"
                    + "\tLe noeud racine n'est pas <Reseau>");
        } catch (IOException e) {
        }

        // Si le document contient un élément non défini, la fonction renvoie
        // null.
        sr = new StringReader("<Reseau><autre></autre></Reseau>");
        try {
            RoadNetwork.loadFromXML(sr);
            fail("Erreur roadNetwork.loadFromXML : \n"
                    + "Erreur syntaxique :\n"
                    + "\tLe document ne contient pas de RoadNodes");
        } catch (IOException e) {
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
