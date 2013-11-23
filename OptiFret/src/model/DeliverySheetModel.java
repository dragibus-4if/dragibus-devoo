package model;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DeliverySheetModel {

    private static final String ROOT_ELEM = "JourneeType";
    private static final String NAME_WAREHOUSE = "Entrepot";
    private static final String NAME_TIMETABLE = "Plage";
    private static final String NAME_DELIVERY = "Livraison";

    private static final String DELIVERY_ADRESSE = "adresse";
    private static final String DELIVERY_CLIENT = "client";
    private static final String DELIVERY_ID = "id";

    private static final String ROADNODE_ID = "adresse";
    private static final String TIMESLOT_DEBUT = "heureDebut";
    private static final String TIMESLOT_FIN = "heureFin";

    private DeliveryRound deliveryRound = new DeliveryRound();
    private DeliveryEmployee deliveryEmployee = new DeliveryEmployee();

    public DeliverySheetModel() {
    }

    public DeliveryRound getDeliveryRound() {
        return this.deliveryRound;
    }

    public DeliveryEmployee getDeliveryEmployee() {
        return deliveryEmployee;
    }

    public void setDeliveryEmployee(DeliveryEmployee deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
    }

    public static DeliverySheetModel loadFromXML(File file) {
        // TODO - implement DeliverySheetModel.loadFromXML
        //throw new UnsupportedOperationException();
        DeliverySheetModel dsm = new DeliverySheetModel();
        
        if (file != null) {
            try {
                // creation d'un constructeur de documents a l'aide d'une fabrique
                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                // lecture du contenu d'un fichier XML avec DOM
                Document document = builder.parse(file);
                Element documentRoot = document.getDocumentElement();

                // normalizer la représentation textuelle des elements
                documentRoot.normalize();

                // recuperer la liste de sous-elements
                if (documentRoot.getTagName().equals(ROOT_ELEM)) {
                    NodeList entrepots = documentRoot.getElementsByTagName(NAME_WAREHOUSE);
                    List<RoadNode> rnListe = treatWarehouse(entrepots);
                    
                    // ajouter la liste des entrepots a la deliveryRound du DSM
                    dsm.deliveryRound.setPath(rnListe);
                    
                    NodeList plages = documentRoot.getElementsByTagName(NAME_TIMETABLE);
                    List<Delivery> livListe = treatTimetable(plages);
                    
                    // parcourir la liste des livraisons pour les ajouter a la
                    // deliveryRound du DSM
                    for (Delivery delivery : livListe) {
                        dsm.deliveryRound.addDelivery(delivery);
                    }
                } else { // element racine different de "JourneeType" (erreur de syntaxe)
                    throw new Exception("La structure du document n'est pas la bonne!");
                }

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
            } catch (Exception ex) {
                System.out.println("Erreur de type d'element XML");
                System.out.println("lors de l'appel a Node.getNodeType()");
            }

        } else { // le fichier est null
            return null;
        }
        return dsm;
    }

    /**
     * 
     * @param entrepots 
     */
    private static List<RoadNode> treatWarehouse(NodeList entrepots) {
        // TODO - traiter les entrepots, on va dire que l'id des RoadNodes
        // correspond à l'adresse précisé dans l'élément entrepot
        List<RoadNode> nodes = new LinkedList<>();
        
        for (int i = 0; i < entrepots.getLength(); i++) {
            NamedNodeMap attributs = entrepots.item(i).getAttributes();
            Node adresse = attributs.getNamedItem(ROADNODE_ID);

            String adresseString = adresse.getNodeValue();
            RoadNode entrepot = new RoadNode(Long.parseLong(adresseString));
            
            // ajouter l'entrepot à la liste de RoadNodes
            nodes.add(entrepot);

            System.out.println(entrepot);
        }
        
        return nodes;
    }

    /**
     * Cette methode s'occupe du traitement des elements d'une tournee. Elle
     * traverse le DOM du document XML pour recuperer les details sur l'entrepot
     * et le plage horaire. Pour le plage horaire elle cree tous les plages
     * d'apres les infos dans l'element XML correspondant ainsi que les
     * livraisons de chaque plage.
     *
     * @param journeyNodes la liste d'elements d'une tournee, normalement un
     * entrepot et un plage horaire
     */
    private static List<Delivery> treatTimetable(NodeList timetable) throws Exception {
        // traiter chaque plage
        List<Delivery> livs = new LinkedList<>();
        
        for (int i = 0; i < timetable.getLength(); i++) {
            Node node = timetable.item(i);
            Element plage;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                plage = (Element) node;
            } else {
                throw new Exception("Wrong node type");
            }

            TimeSlot ts = createTimeSlotFromXMLNode(node);

            NodeList livraisons = plage.getElementsByTagName(NAME_DELIVERY);
            System.out.println(ts);

            // traiter la liste des livraisons
            for (int k = 0; k < livraisons.getLength(); k++) {
                Node livraison = livraisons.item(k);
                Delivery liv = createDeliveryFromXMLNode(livraison);

                // ajouter le plage a la livraison
                liv.setTimeSlot(ts);
                System.out.println(liv);
                
                // ajouter la livraison à la liste
                livs.add(liv);
            }
            
        }
        return livs;
    }

    /**
     * Cette methode cree un objet du type TimeSlot d'apres les informations
     * dans l'element XML. Elle lit les valeurs de tous les attributs,
     * transforme leurs types en types attendus et en cree le nouveau TimeSlot.
     *
     * @param node l'element XML precisant le TimeSlot
     * @return le nouveau TimeSlot
     * @throws DOMException s'il y a un probleme avec le XML
     */
    private static TimeSlot createTimeSlotFromXMLNode(Node node) throws DOMException {
        NamedNodeMap attributs = node.getAttributes();
        Node timeslotDebut = attributs.getNamedItem(TIMESLOT_DEBUT);
        Node timeslotFin = attributs.getNamedItem(TIMESLOT_FIN);

        // recuperer les valeurs des attributs
        String tsDebutString = timeslotDebut.getNodeValue();
        String[] debutHeureParties = tsDebutString.split(":");
        int debutHeure = Integer.parseInt(debutHeureParties[0]);
        int debutMin = Integer.parseInt(debutHeureParties[1]);
        int debutSec = Integer.parseInt(debutHeureParties[2]);

        String tsFinString = timeslotFin.getNodeValue();
        String[] finHeureParties = tsFinString.split(":");
        int finHeure = Integer.parseInt(finHeureParties[0]);
        int finMin = Integer.parseInt(finHeureParties[1]);
        int finSec = Integer.parseInt(finHeureParties[2]);

        // convertir les attributs de String en Date
        Date today = new Date();
        GregorianCalendar calDebut = new GregorianCalendar(
                today.getYear(), today.getMonth(), today.getDay(), 
                debutHeure, debutMin, debutSec);
        GregorianCalendar calFin = new GregorianCalendar(
                today.getYear(), today.getMonth(), today.getDay(), 
                finHeure, finMin, debutSec);

        Date tsDebutDate = calDebut.getTime();
        Date tsFinDate = calFin.getTime();

        // creer nouveau TimeSlot
        return new TimeSlot(tsDebutDate, tsFinDate);
    }

    /**
     * Cette methode cree un objet du type Delivery d'apres les informations
     * dans l'element XML. Elle lit les valeurs de tous les attributs,
     * transforme leurs types en types attendus et en cree la nouvelle Delivery.
     *
     * @param node l'element XML precisant la Delivery
     * @return la nouvelle Delivery
     */
    private static Delivery createDeliveryFromXMLNode(Node node) {
        if (node.getNodeName().equals(NAME_DELIVERY)) {
            NamedNodeMap attributs = node.getAttributes();
            Node idLiv = attributs.getNamedItem(DELIVERY_ID);
            Node clientLiv = attributs.getNamedItem(DELIVERY_CLIENT);
            Node adresseLiv = attributs.getNamedItem(DELIVERY_ADRESSE);

            // recuperer les valeurs des attributs
            String idLivString = idLiv.getNodeValue();
            String clientLivString = clientLiv.getNodeValue();
            String adresseLivString = adresseLiv.getNodeValue();

            // convertir les attributs de String en long
            long id = Long.parseLong(idLivString);
            long client = Long.parseLong(clientLivString);
            long adresse = Long.parseLong(adresseLivString);

            // creer nouvelle livraison et nouvelle client
            Delivery liv = new Delivery(id);
            Client c = new Client(client);

            // ajouter adresse et client a la livraison
            liv.setAddress(adresse);
            liv.setClient(c);

            return liv;
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * TODO - a supprimer après le testing
     * @param args 
     */
    public static void main(String[] args) {
        File testfile = null;
        try {
            testfile = new File("/Users/pellerpatrizia/Documents/INSA_Lyon/DevOO/fixtures/livraison20x20-2.xml");
        } catch (Exception e) {
            System.out.println("Sorry, file could not be created, try another path.");
        }

        DeliverySheetModel dsm = DeliverySheetModel.loadFromXML(testfile);
    }
}
