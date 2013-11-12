package test;

public class RoadNetwork extends TestCase {
    public void testFile() {
        // Si le filename est null, la fonction retourne null.
        assertNull(RoadNetwork.loadFromXML(Null));

        // Si le fichier n'existe pas, la fonction retourne Null
        // Normalement le fichier /4242 n'existe pas
        assertNull(RoadNetwork.loadFromXML("/4242"));

        // Si c'est un dossier, la fonction retourne Null
        assertNull(RoadNetwork.loadFromXML("/"));

        // Si le fichier n'est pas lisible, la fonction retourne Null
        // Normalement le fichier /root ne sont pas lisibles
        assertNull(RoadNetwork.loadFromXML("/root"));
    }

    private boolean writeInFile(String filename, String content) {
        try
        {
            FileWriter fw = new FileWriter(filename, false);
            BufferedWriter output = new BufferedWriter(fw);
            output.write(content);
            output.flush();
            output.close();
            return true;
        }
        catch(IOException ioe){
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
        assertNull(RoadNetwork.loadFromXML(filename));

        // Test d'une ouverture de balise manquante
        writeInFile(filename, "<root></balise></root>");
        assertNull(RoadNetwork.loadFromXML(filename));

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
        assertNull(RoadNetwork.loadFromXML(filename));

        // Si le document contient un élément non défini, la fonction renvoie
        // null.
        writeInFile(filename, "<road_network><autre></autre></road_network>");
        assertNull(RoadNetwork.loadFromXML(filename));

        // Si c'est la bonne balise racine, la fonction renvoie quelque chose de non
        // null.
        writeInFile(filename, "<road_network></road_network>");
        RoadNetwork rn = RoadNetwork.loadFromXML(filename);
        assertNotNull(rn);
        assertEquals(rn.getSize(), 0);

        // TODO tests sur l'intégrité du document
        // Voir le format des xmls à lire pour vérifier ça
    }

    public void testAddNSize() {
        // Si le réseau vient d'etre créé, sa taille est de 0
        RoadNetwork net = new RoadNetwork();
        assertEquals(net.getSize(), 0);

        // Ajoute un noeud unique
        net.addNode(new RoadNode());
        assertEquals(net.getSize(), 1);

        // Ajoute un noeud avec un fils
        RoadNode node = new RoadNode();
        node.addNeighbor(new RoadNode());
        net.addNode(node);
        assertEquals(net.getSize(), 3);

        // Ajoute un fils au noeud précédemment créé puis vérification que la
        // taille augmente de 1
        node.addNeighbor(new RoadNode());
        assertEquals(net.getSize(), 4);
    }

    public void testGetNodeById() {
        RoadNetwork net = new RoadNetwork();

        // Essaye d'acceder à élément n'existant pas (ou invalide)
        assertNull(net.getNodeById(-1));
        assertNull(net.getNodeById(0));

        // Ajout d'un noeud dans le graphe puis récupération de ce noeud par
        // l'id
        RoadNode node = new RoadNode();
        Long id = node.getId();
        net.addNode(node);
        assertNotNull(net.getNodeById(id));
        assertEquals(net.getNodeById(id).getId(), id);

        // Ajout d'un noeud comme fils du précédent puis recherche dans le
        // graphe
        RoadNode node2 = new RoadNode();
        Long id2 = node2.getId();
        node2.addNeighbor(node);
        assertNotNull(net.getNodeById(id2));
        assertEquals(net.getNodeById(id2).getId(), id2);

        // Essaye d'acceder à élément n'existant toujours pas (ou invalide)
        assertNull(net.getNodeById(-1));
    }
};
