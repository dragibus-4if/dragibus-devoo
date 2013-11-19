package model;

import java.io.File;
import java.io.IOException;

import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RoadNetwork {

    private RoadNode root;

    public static RoadNetwork loadFromXML(File file) {
        // TODO - implement RoadNetwork.loadFromXml
        //throw new UnsupportedOperationException();
        if (file != null) {
             try {
                 // creation d'un constructeur de documents a l'aide d'une fabrique
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
                // lecture du contenu d'un fichier XML avec DOM
                Document document = builder.parse(file);
                Element documentRoot = document.getDocumentElement();
                if ( documentRoot.getTagName().equals("Reseau")){
                    
                }
                else{ // element racine different de "Reseau" (erreur de syntaxe)
                    NodeList roadNodes =  documentRoot.getChildNodes();
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
            } catch (SAXException se) {
                System.out.println("Erreur lors du parsing du document");
                System.out.println("lors de l'appel a construteur.parse(xml)");
            } catch (IOException ioe) {
                System.out.println("Erreur d'entree/sortie");
                System.out.println("lors de l'appel a construteur.parse(xml)");
            }
             
        }
        else{ // le fichier est null
            return null;   
        }
        return new RoadNetwork();
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
        throw new UnsupportedOperationException();
    }

    public List<RoadNode> makeRoute(List<RoadNode> objectives) {
        // TODO - implement RoadNetwork.makeRoute
        throw new UnsupportedOperationException();

    }
}
