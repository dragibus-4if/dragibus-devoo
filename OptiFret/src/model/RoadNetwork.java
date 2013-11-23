package model;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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

    private RoadNode root;

    //TODO : check integrity of roadnetwork ( correspondance id des roadnodes des 
    public static RoadNetwork loadFromXML(Reader input) throws Exception {
        if (input == null) {
            throw new NullPointerException("Fichier chargé null");
        }
        HashSet<RoadNode> roadNodes = new HashSet<>();
        Element documentRoot = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(input));
            documentRoot = document.getDocumentElement();
        } catch (ParserConfigurationException pce) {
            System.out.println("Erreur de configuration du parseur DOM");
            System.out.println("lors de l'appel a fabrique.newDocumentBuilder();");
            throw new Exception(pce);
        } catch (SAXException se) {
            System.out.println("Erreur lors du parsing du document");
            System.out.println("lors de l'appel a construteur.parse(xml)");
            throw new Exception(se);
        } catch (IOException ioe) {
            System.out.println("Erreur d'entree/sortie");
            System.out.println("lors de l'appel a construteur.parse(xml)");
            throw new Exception(ioe);
        }

        // Element racine different de "Reseau" (erreur de syntaxe)
        if (!documentRoot.getTagName().equals("Reseau")) {
            throw new Exception(
                    "Erreur roadNetwork.loadFromXML : \n"
                    + "Erreur syntaxique :\n"
                    + "\tLe noeud racine n'est pas <Reseau>"
            );
        }

        NodeList nodes = documentRoot.getElementsByTagName("Noeud");
        if (nodes.getLength() == 0) {// 0 nodes dans le document ...
            throw new Exception(
                    "Erreur roadNetwork.loadFromXML : \n"
                    + "Erreur syntaxique :\n"
                    + "\tLe document ne contient pas de RoadNodes"
            );
        }
        RoadNetwork result = new RoadNetwork();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node m = nodes.item(i);
            if (m.getNodeType() == Node.ELEMENT_NODE) {
                throw new Exception(
                        "Erreur roadNetwork.loadFromXML : \n"
                        + "Erreur syntaxique"
                );
            }

            Element n = (Element) m;
            if (!n.getNodeName().equals("Noeud")) {
                throw new Exception(
                        "Erreur roadNetwork.loadFromXML : \n"
                        + "Erreur syntaxique :\n"
                        + "\t\n\tnom de noeud attend : Noeud\n"
                        + "\tnom de noeud trouvé : "
                        + n.getNodeName() + ""
                );
            }

            NamedNodeMap roadNodeAttributes = n.getAttributes();

            // recupere l'id du roadNode
            Node id = roadNodeAttributes.getNamedItem("id");
            String stringFormNodeId = id.getNodeValue();
            long nodeId = Long.parseLong(stringFormNodeId);

            // TODO : erreurs sur le parsing ?
            RoadNode tmp = new RoadNode(nodeId);
            RoadNode rn = new RoadNode(-1);
            if (!roadNodes.contains(tmp)) {
                roadNodes.add(tmp);
                rn = tmp;
            } else {
                Iterator<RoadNode> iterator = roadNodes.iterator();
                while (iterator.hasNext()) {
                    RoadNode nNode = iterator.next();
                    if (nNode.getId() == nodeId) {
                        rn = nNode;
                    }
                }
            }
            // RoadNode rn = new RoadNode(nodeId);
            if (i == 0) {
                result.root = rn;
            }

            // recupere les coordonnees x et y du roadNode
            Node x = roadNodeAttributes.getNamedItem("x");
            Node y = roadNodeAttributes.getNamedItem("y");
            String stringFormX = x.getNodeValue();
            String stringFormY = y.getNodeValue();
            int nodeX = Integer.parseInt(stringFormX);
            int nodeY = Integer.parseInt(stringFormY);
            rn.setX(nodeX);
            rn.setY(nodeY);
            NodeList roadSections = n.getElementsByTagName("TronconSortant");

            // roadNode sans roadSections
            if (roadSections.getLength() == 0) {
                throw new Exception(
                        "Erreur roadNetwork.loadFromXML : \n"
                        + "Erreur syntaxique :\n"
                        + "\tRoadNode " + rn.getId() + " sans RoadSection."
                );
            }

            for (int j = 0; j < roadSections.getLength(); j++) {
                Node roadSectionNode = roadSections.item(j);

                // noeud non defini (n'est pas TronconSortant)
                if (!roadSectionNode.getNodeName().equals("TronconSortant")) {
                    throw new Exception(
                            "Erreur roadNetwork.loadFromXML : \n"
                            + "Erreur syntaxique :\n"
                            + "\tnom de noeud attend : TronconSortant\n"
                            + "\tnom de noeud trouvé : "
                            + roadSectionNode.getNodeName());

                }

                // nomRue, vitesse, longueur, destination
                NamedNodeMap roadSectionAttributes = roadSectionNode.getAttributes();
                Node nodeFormRoadName = roadSectionAttributes.getNamedItem("nomRue");
                String roadName = nodeFormRoadName.getNodeValue();
                Node nodeFormSpeed = roadSectionAttributes.getNamedItem("vitesse");
                String stringFormSpeed = nodeFormSpeed.getNodeValue();
                double speed = Double.parseDouble(stringFormSpeed);
                Node nodeFormLength = roadSectionAttributes.getNamedItem("longueur");
                String stringFormLength = nodeFormLength.getNodeValue();
                double length = Double.parseDouble(stringFormLength);
                Node nodeFormDestinationId = roadSectionAttributes.getNamedItem("destination");
                String stringFormDestinationId = nodeFormDestinationId.getNodeValue();
                long destinationId = Long.parseLong(stringFormDestinationId);

                RoadNode dest = new RoadNode(destinationId);
                if (!roadNodes.contains(dest)) {
                    roadNodes.add(dest);
                } else {
                    Iterator<RoadNode> it = roadNodes.iterator();
                    while (it.hasNext()) {
                        RoadNode nNode = it.next();
                        if (nNode.getId() == destinationId) {
                            dest = nNode;
                        }
                    }

                }
                RoadSection roadSection = new RoadSection(rn, dest, speed, length);
                rn.addNeighbor(roadSection);
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
        if(this.root == null)
            return new ArrayList<>();
        ArrayList<RoadNode> checked = new ArrayList<>();
        Collection<RoadNode> n = this.getRoot().getNeighbors();
        checked.add(this.getRoot());
        Iterator<RoadNode> it = n.iterator();
        while (it.hasNext()) {
            RoadNode r = it.next();
            if (!checked.contains(r)) {
                n.addAll(r.getNeighbors());
                checked.add(r);
                it = n.iterator();
            }
        }
        return checked;
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

    public void setRoot(RoadNode root) {
        this.root = root;
    }

    public int getSize() {
        return getNodes().size();
    }
}
