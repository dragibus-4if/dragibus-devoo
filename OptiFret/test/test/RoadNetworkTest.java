package test;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;

import java.io.IOException;
import junit.framework.TestCase;
import model.RoadNetwork;
import model.RoadNode;
import model.RoadSection;

public class RoadNetworkTest extends TestCase {

    public void testFile() {
        // Si le filename est null, la fonction retourne null.
        assertNull(RoadNetwork.loadFromXML(null));

        // Si le fichier n'existe pas, la fonction retourne Null
        // Normalement le fichier /4242 n'existe pas
        assertNull(RoadNetwork.loadFromXML(new File("/4242")));

        // Si c'est un dossier, la fonction retourne Null
        assertNull(RoadNetwork.loadFromXML(new File("/")));

        // Si le fichier n'est pas lisible, la fonction retourne Null
        // Normalement le fichier /root ne sont pas lisibles
        assertNull(RoadNetwork.loadFromXML(new File("/root")));
    }

    private boolean writeInFile(String filename, String content) {
        try {
            FileWriter fw = new FileWriter(filename, false);
            try (BufferedWriter output = new BufferedWriter(fw)) {
                output.write(content);
                output.flush();
                return true;
            }
        } catch (IOException ioe) {
            System.out.print("Erreur : ");
            ioe.printStackTrace();
        }
        return false;
    }

    public void testXMLSyntax() {
        // Si la syntaxe XML est mauvaise, la fonction retourne Null
        String filename = "/tmp/dragibus-roadnetworktest-testxmlsyntax.xml";

        // Test d'une fermeture de balise manquante
        writeInFile(filename, "<root>");
        assertNull(RoadNetwork.loadFromXML(new File(filename)));

        // Test d'une ouverture de balise manquante
        writeInFile(filename, "<root></balise></root>");
        assertNull(RoadNetwork.loadFromXML(new File(filename)));

        // Il n'y a pas tout les cas sur la syntaxe XML. La bibliothèque
        // utilisée doit pouvoir détecter les erreurs. Nous l'utilisons et ces
        // quelques tests permettent de montrer qu'on capte que la bibliothèque
        // a détecté une erreur.
    }

    public void testXMLSemantic() {
        // Si la sémantique XML est mauvaise, la fonction retourne Null
        String filename = "/tmp/dragibus-roadnetworktest-testxmlsemantic.xml";

        // Si la balise racine est un élément quelconque (différent de ce qui
        // est attendu), la fonction renvoie null.
        writeInFile(filename, "<root></root>");
        assertNull(RoadNetwork.loadFromXML(new File(filename)));

        // Si le document contient un élément non défini, la fonction renvoie
        // null.
        writeInFile(filename, "<road_network><autre></autre></road_network>");
        assertNull(RoadNetwork.loadFromXML(new File(filename)));

        // Si c'est la bonne balise racine, la fonction renvoie quelque chose de non
        // null.
        writeInFile(filename, "<road_network></road_network>");
        RoadNetwork rn = RoadNetwork.loadFromXML(new File(filename));
        assertNotNull(rn);
        assertEquals(rn.getSize(), 0);

        // TODO tests sur l'intégrité du document
        // Voir le format des xmls à lire pour vérifier ça
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

    public void testGetNodeById() {
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
    }
};
