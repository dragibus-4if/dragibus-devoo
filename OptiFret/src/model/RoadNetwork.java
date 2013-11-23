package model;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import tsp.RegularGraph;
import tsp.SolutionState;
import tsp.TSP;

public class RoadNetwork {

    private static final String ROOT_ELEM = "Reseau";
    private static final String NODE_ELEM = "Noeud";
    private static final String DEST_ATTR = "destination";
    private static final String LENGTH_ATTR = "longueur";
    private static final String SPEED_ATTR = "vitesse";
    private static final String ROAD_ATTR = "nomRue";
    private static final String SECTION_ELEM = "TronconSortant";
    private static final String Y_ELEM = "y";
    private static final String X_ELEM = "x";
    private static final String ID_ATTR = "id";
    private RoadNode root;

    public static RoadNetwork loadFromXML(Reader input) throws IOException {
        if (input == null) {
            throw new NullPointerException("Fichier chargé null");
        }
        HashSet<RoadNode> roadNodes = new HashSet<>();
        HashSet<RoadNode> destinationNodes = new HashSet<>();
        Element documentRoot = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(input));
            documentRoot = document.getDocumentElement();
        } catch (ParserConfigurationException pce) {
            System.out.println("Erreur de configuration du parseur DOM");
            System.out.println("lors de l'appel a fabrique.newDocumentBuilder();");
            throw new IOException(pce.getMessage(), pce.getCause());
        } catch (SAXException se) {
            System.out.println("Erreur lors du parsing du document");
            System.out.println("lors de l'appel a construteur.parse(xml)");
            throw new IOException(se.getMessage(), se.getCause());
        } catch (IOException ioe) {
            System.out.println("Erreur d'entree/sortie");
            System.out.println("lors de l'appel a construteur.parse(xml)");
            throw new IOException(ioe.getMessage(), ioe.getCause());
        }

        // Element racine different de "Reseau" (erreur de syntaxe)
        if (!documentRoot.getTagName().equals(ROOT_ELEM)) {
            throw new IOException(
                    "Erreur roadNetwork.loadFromXML : \n"
                    + "Erreur syntaxique :\n"
                    + "\tLe noeud racine n'est pas <Reseau>"
            );
        }

        NodeList nodes = documentRoot.getElementsByTagName(NODE_ELEM);
        if (nodes.getLength() == 0) {// 0 nodes dans le document ...
            throw new IOException(
                    "Erreur roadNetwork.loadFromXML : \n"
                    + "Erreur syntaxique :\n"
                    + "\tLe document ne contient pas de RoadNodes"
            );
        }
        RoadNetwork result = new RoadNetwork();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node m = nodes.item(i);
            if (m.getNodeType() != Node.ELEMENT_NODE) {
                throw new IOException(
                        "Erreur roadNetwork.loadFromXML : \n"
                        + "Erreur syntaxique"
                );
            }

            Element n = (Element) m;
            if (!n.getNodeName().equals(NODE_ELEM)) {
                throw new IOException(
                        "Erreur roadNetwork.loadFromXML : \n"
                        + "Erreur syntaxique :\n"
                        + "\t\n\tnom de noeud attend : Noeud\n"
                        + "\tnom de noeud trouvé : "
                        + n.getNodeName() + ""
                );
            }

            NamedNodeMap roadNodeAttributes = n.getAttributes();

            // ID du roadNode
            Node id = roadNodeAttributes.getNamedItem(ID_ATTR);
            String stringFormNodeId = id.getNodeValue();
            long nodeId = Long.parseLong(stringFormNodeId);

            RoadNode rn = new RoadNode(nodeId);
            RoadNode beginRoadNode = null;
            if (roadNodes.add(rn)) {
                beginRoadNode = rn;
            } else {
                for (RoadNode node : roadNodes) {
                    if (node.getId() == nodeId) {
                        beginRoadNode = node;
                    }
                }
            }
            assert beginRoadNode != null;

            if (i == 0) {
                result.root = beginRoadNode;
            }

            // Coordonnées x et y du roadNode
            try {
                String stringFormX = roadNodeAttributes.getNamedItem(X_ELEM).getNodeValue();
                String stringFormY = roadNodeAttributes.getNamedItem(Y_ELEM).getNodeValue();
                beginRoadNode.setX(Integer.parseInt(stringFormX));
                beginRoadNode.setY(Integer.parseInt(stringFormY));
            } catch (NumberFormatException e) {
                throw new IOException("Les attribute 'x' et 'y' d'un node devraient être des entiers");
            }

            // Sections
            NodeList roadSections = n.getElementsByTagName(SECTION_ELEM);
            if (roadSections.getLength() == 0) {
                throw new IOException(
                        "Erreur roadNetwork.loadFromXML : \n"
                        + "Erreur syntaxique :\n"
                        + "\tRoadNode " + beginRoadNode.getId() + " sans RoadSection."
                );
            }

            for (int j = 0; j < roadSections.getLength(); j++) {
                Node roadSectionNode = roadSections.item(j);
                NamedNodeMap roadSectionAttributes = roadSectionNode.getAttributes();

                // roadNode indéfini
                if (!roadSectionNode.getNodeName().equals(SECTION_ELEM)) {
                    throw new IOException(
                            "Erreur roadNetwork.loadFromXML : \n"
                            + "Erreur syntaxique :\n"
                            + "\tnom de noeud attend : TronconSortant\n"
                            + "\tnom de noeud trouvé : "
                            + roadSectionNode.getNodeName());
                }

                // roadNode de destination
                String destinationIdStr = roadSectionAttributes.getNamedItem(DEST_ATTR).getNodeValue();
                long destinationId;
                try {
                    destinationId = Long.parseLong(destinationIdStr);
                } catch (NumberFormatException e) {
                    throw new IOException(e.getMessage(), e.getCause());
                }

                RoadNode dest = new RoadNode(destinationId);
                if (!destinationNodes.contains(dest)) {
                    destinationNodes.add(dest);
                } else {
                    Iterator<RoadNode> it = destinationNodes.iterator();
                    while (it.hasNext()) {
                        RoadNode nNode = it.next();
                        if (nNode.getId() == destinationId) {
                            dest = nNode;
                        }
                    }
                }

                RoadSection roadSection = new RoadSection(beginRoadNode, dest);

                // nomRue, vitesse, longueur
                String roadName = roadSectionAttributes.getNamedItem(ROAD_ATTR).getNodeValue();
                roadSection.setRoadName(roadName);

                // roadSection speed
                try {
                    String speedStr = roadSectionAttributes.getNamedItem(SPEED_ATTR).getNodeValue();
                    roadSection.setSpeed(Double.parseDouble(speedStr));
                } catch (NumberFormatException e) {
                    throw new IOException(e.getMessage(), e.getCause());
                }

                // roadSection length
                try {
                    String lengthStr = roadSectionAttributes.getNamedItem(LENGTH_ATTR).getNodeValue();
                    roadSection.setLength(Double.parseDouble(lengthStr));
                } catch (NumberFormatException e) {
                    throw new IOException(e.getMessage(), e.getCause());
                }

                beginRoadNode.addNeighbor(roadSection);
            }
        }
        return result;
    }

    public RoadNetwork() {
    }

    public RoadNode getRoot() {
        return root;
    }

    public List<RoadNode> getNodes() {
        if (root == null) {
            return new ArrayList<>();
        }
        Set<RoadNode> open = new HashSet<>();
        Set<RoadNode> close = new HashSet<>();
        List<RoadNode> l = new ArrayList<>();
        open.add(root);
        while (!open.isEmpty()) {
            RoadNode current = open.iterator().next();
            open.remove(current);
            close.add(current);
            for (RoadNode n : current.getNodes()) {
                if (!close.contains(n)) {
                    open.add(n);
                }
            }
            l.add(current);
        }
        return l;
    }

    public List<RoadNode> makeRoute(List<RoadNode> objectives) {
        RegularGraph graph = RegularGraph.loadFromRoadNetwork(this);
        TSP tsp = new TSP(graph);
        SolutionState s = tsp.solve(1000000, 100000);
        if (s == SolutionState.OPTIMAL_SOLUTION_FOUND || s == SolutionState.SOLUTION_FOUND) {
            int[] ls = tsp.getNext();
            return graph.getLsNode(ls);
        }
        return new ArrayList<>();
    }

    public int getSize() {
        return getNodes().size();
    }

}
