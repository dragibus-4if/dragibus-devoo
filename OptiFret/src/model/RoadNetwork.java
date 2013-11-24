package model;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private static final String Y_ATTR = "y";
    private static final String X_ATTR = "x";
    private static final String ID_ATTR = "id";
    private RoadNode root;

    public static RoadNetwork loadFromXML(Reader input) throws IOException {
        if (input == null) {
            throw new NullPointerException("Fichier chargé null");
        }
        HashMap<Long, RoadNode> roadNodes = new HashMap<>();
        HashMap<Long, RoadNode> destinationNodes = new HashMap<>();
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
        RoadNetwork roadNetwork = new RoadNetwork();

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
            Long nodeId;
            try {
                Node id = roadNodeAttributes.getNamedItem(ID_ATTR);
                String stringFormNodeId = id.getNodeValue();
                nodeId = Long.parseLong(stringFormNodeId);
            } catch (NumberFormatException e) {
                throw new IOException("L'attribut '" + ID_ATTR + "' d'un RoadNode devrait être un entier");
            } catch (NullPointerException e) {
                throw new IOException("L'attribut '" + ID_ATTR + "' d'un RoadNode est requis");
            }

            RoadNode beginRoadNode;
            if (destinationNodes.containsKey(nodeId)) {
                beginRoadNode = destinationNodes.remove(nodeId);
            } else {
                beginRoadNode = new RoadNode(nodeId);
                if (i == 0) {
                    roadNetwork.root = beginRoadNode;
                }
            }
            roadNodes.put(nodeId, beginRoadNode);

            // Coordonnées x et y du roadNode
            try {
                String x = roadNodeAttributes.getNamedItem(X_ATTR).getNodeValue();
                String y = roadNodeAttributes.getNamedItem(Y_ATTR).getNodeValue();
                beginRoadNode.setX(Integer.parseInt(x));
                beginRoadNode.setY(Integer.parseInt(y));
            } catch (NumberFormatException e) {
                throw new IOException("Les attributs '" + X_ATTR + "' et '" + Y_ATTR + "' d'un RoadNode devraient être des entiers");
            } catch (NullPointerException e) {
                throw new IOException("Les attributs '" + X_ATTR + "' et '" + Y_ATTR + "' d'un RoadNode sont requis");
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
                Long destId;
                try {
                    String destinationIdStr = roadSectionAttributes.getNamedItem(DEST_ATTR).getNodeValue();
                    destId = Long.parseLong(destinationIdStr);
                } catch (NumberFormatException e) {
                    throw new IOException(e.getMessage(), e.getCause());
                } catch (NullPointerException e) {
                    throw new IOException("L'attribut '" + DEST_ATTR + "' est requis");
                }

                RoadNode destRoadNode = roadNodes.get(destId);
                if (destRoadNode == null) {
                    destRoadNode = destinationNodes.get(destId);
                    if (destRoadNode == null) {
                        destinationNodes.put(destId, new RoadNode(destId));
                        destRoadNode = destinationNodes.get(destId);
                    }
                }

                RoadSection roadSection = new RoadSection(beginRoadNode, destRoadNode);

                // nomRue, vitesse, longueur
                String roadName = roadSectionAttributes.getNamedItem(ROAD_ATTR).getNodeValue();
                roadSection.setRoadName(roadName);

                // roadSection speed
                try {
                    String speedStr = roadSectionAttributes.getNamedItem(SPEED_ATTR).getNodeValue();
                    roadSection.setSpeed(Double.parseDouble(speedStr));
                } catch (NumberFormatException e) {
                    throw new IOException(e.getMessage(), e.getCause());
                } catch (NullPointerException e) {
                    throw new IOException("L'attribut '" + SPEED_ATTR + "' de RoadSection est requis");
                }

                // roadSection length
                try {
                    String lengthStr = roadSectionAttributes.getNamedItem(LENGTH_ATTR).getNodeValue();
                    roadSection.setLength(Double.parseDouble(lengthStr));
                } catch (NumberFormatException e) {
                    throw new IOException(e.getMessage(), e.getCause());
                } catch (NullPointerException e) {
                    throw new IOException("L'attribut '" + LENGTH_ATTR + "' de RoadSection est requis");
                }

                beginRoadNode.addNeighbor(roadSection);
            }
        }

        // Vérification : si on a ajouté des RoadSection dont la
        // destination n'est pas un RoadNode défini, grosse erreur
        if (!destinationNodes.isEmpty()) {
            throw new IOException("Destination définie mais noeud correspondant non défini");
        }

        return roadNetwork;
    }

    public RoadNetwork() {
    }

    public RoadNode getRoot() {
        return root;
    }
    
    public void setRoot(RoadNode root) {
        this.root = root;
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

    public List<RoadNode> makeRoute(List<Delivery> deliveries) {
        RegularGraph graph = RegularGraph.loadFromRoadNetwork(this, deliveries);
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
