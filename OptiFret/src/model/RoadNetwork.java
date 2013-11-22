package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RoadNetwork {

    private RoadNode root;

    public static RoadNetwork loadFromXML(File file) throws Exception { //TODO : check integrity of roadnetwork ( correspondance id des roadnodes des 
        //roadsections avec celle des roadNodes
        // TODO - implement RoadNetwork.loadFromXml
        //throw new UnsupportedOperationException();
        HashSet<RoadNode> roadNodes = new HashSet<RoadNode>();
        if (file != null) {
            try {
                // creation d'un constructeur de documents a l'aide d'une fabrique
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                // lecture du contenu d'un fichier XML avec DOM
                Document document = builder.parse(file);
                Element documentRoot = document.getDocumentElement();
                if (documentRoot.getTagName().equals("Reseau")) {
                    NodeList nodes = documentRoot.getElementsByTagName("Noeud");
                    if (nodes.getLength() > 0) {
                        RoadNetwork result = new RoadNetwork();

                        for (int i = 0; i < nodes.getLength(); i++) {
                            Node m = nodes.item(i);
                            Element n;
                            if ( m.getNodeType() == Node.ELEMENT_NODE){
                                n = ( Element )(m);
                            }else{
                        throw (new Exception("Erreur roadNetwork.loadFromXML : \nErreur syntaxique"));
                            }
                            if (n.getNodeName().equals("Noeud")) {
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
                                if (roadSections.getLength() > 0) {
                                    for (int j = 0; j < roadSections.getLength(); j++) {
                                        Node roadSectionNode = roadSections.item(j);
                                        if (roadSectionNode.getNodeName().equals("TronconSortant")) {
                                            NamedNodeMap roadSectionAttributes = roadSectionNode.getAttributes();
                                            //attributs à récupérer : 

                                            // nomRue, vitesse, longueur, destination


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
                                        } else { // noeud non defini ( n'est pas TronconSortant )
                                            throw (new Exception("Erreur roadNetwork.loadFromXML : \nErreur syntaxique :\n\tnom de noeud attend : TronconSortant\n\tnom de noeud trouvé : " + roadSectionNode.getNodeName()));

                                        }
                                    }
                                } else { // roadNode sans roadSections
                                    throw (new Exception("Erreur roadNetwork.loadFromXML : \nErreur syntaxique :\n\tRoadNode " + rn.getId() + " sans RoadSection."));

                                }
                            } else { // le reseau contient des elements qui ne sont pas des noeuds 
                                throw (new Exception("Erreur roadNetwork.loadFromXML : \nErreur syntaxique :\n\t\n\tnom de noeud attend : Noeud\n\tnom de noeud trouvé : " + n.getNodeName() + ""));

                            }
                        }
                        return result;
                    } else {// 0 nodes dans le document ... 
                        throw (new Exception("Erreur roadNetwork.loadFromXML : \nErreur syntaxique :\n\tLe document ne contient pas de RoadNodes"));

                    }
                } else { // element racine different de "Reseau" (erreur de syntaxe)
                    throw (new Exception("Erreur roadNetwork.loadFromXML : \nErreur syntaxique :\n\tLe noeud racine n'est pas <Reseau>"));


                }
                /*
                 * if (racine.getNodeName().equals("dessin")) {
                 int resultatConstruction = ConstruireToutAPartirDeDOMXML(racine);
                 if (resultatConstruction != Dessin.PARSE_OK) {
                 System.out.println("PB de lecture de fichier!");
                 } 
                 }
                 * */
                // todo : traiter les erreurs
            } catch (ParserConfigurationException pce) {
                System.out.println("Erreur de configuration du parseur DOM");
                System.out.println("lors de l'appel a fabrique.newDocumentBuilder();");
                return new RoadNetwork();
            } catch (SAXException se) {
                System.out.println("Erreur lors du parsing du document");
                System.out.println("lors de l'appel a construteur.parse(xml)");
                return new RoadNetwork();
            } catch (IOException ioe) {
                System.out.println("Erreur d'entree/sortie");
                System.out.println("lors de l'appel a construteur.parse(xml)");
                return new RoadNetwork();
            } catch (Exception e) { // exceptions avec des messages personalisés
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                return new RoadNetwork();
            }

        } else { // le fichier est null
            return new RoadNetwork();
        }
        //return new RoadNetwork();
    }

    public RoadNetwork() {
    }

    public RoadNode getRoot() {
        return root;
    }

    public RoadNode getNodeById(Long id) {
        // TODO - implement RoadNetwork.getNodeById
        throw new UnsupportedOperationException();
    }

    public List<RoadNode> getNodes() {
        // TODO - implement RoadNetwork.getNodes
        ArrayList<RoadNode> checked = new ArrayList<RoadNode>();
        Collection<RoadNode> n = this.getRoot().getNeighbors();
        checked.add(this.getRoot());
        Iterator<RoadNode> it = n.iterator();
        while( it.hasNext()){
            RoadNode r = it.next();
            if ( !checked.contains(r)){
                n.addAll(r.getNeighbors());
                checked.add(r);
                it = n.iterator();
            }
        }
        return checked;
        
    }

    public List<RoadNode> makeRoute(List<RoadNode> objectives) {
        // TODO - implement RoadNetwork.makeRoute
        throw new UnsupportedOperationException();

    }
    
    @Override
    public boolean equals( Object obj ){
        
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            final RoadNetwork other = (RoadNetwork) obj;
            List<RoadNode> nodes1 = this.getNodes();
            List<RoadNode> nodes2 = other.getNodes();
            if ( nodes1.size() != nodes2.size() ){
                return false;
            }
            Iterator it1 = nodes1.iterator();
            Iterator it2 = nodes2.iterator();
            while ( it1.hasNext() && it2.hasNext() ){
                if ( ! it1.next().equals(it2.next())){
                    return false;
                }
            }
            return true;
        }
        
        
    }
     

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.getNodes());
        return hash;
    }
    
    public static void main( String[] args){

        
        
        File f = new File("C:\\Users\\Mathis\\Documents\\Scolarité\\INSA 4IF\\DevOO\\DevOO2\\dragibus-devoo\\fixtures\\plan10x10.xml");
        try {
            RoadNetwork rn = RoadNetwork.loadFromXML(f);
            List<RoadNode> list = rn.getNodes();
            Iterator<RoadNode> it = list.iterator();
            while ( it.hasNext()){
                System.out.println(it.next());
            }
        } catch (Exception ex) {
            Logger.getLogger(RoadNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
