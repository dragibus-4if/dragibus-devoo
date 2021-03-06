package model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import tsp.AStar;

/**
 * Modèle de la feuille de route d'un livreur. {@literal DeliverySheet}
 * encapsule le chargement XML des demandes de livraisons pour définir la
 * tournée (représentée par {@literal DeliveryRound}) et le livreur associé
 * (représentée par {@literal DeliveryEmployee}).
 *
 * @author Patrizia
 * @author Jean-Marie
 * @author Julien
 * @author Pierre
 */
public class DeliverySheet {

    private List<Delivery> deliveries;

    private long warehouseAddress;
    private RoadNetwork network;

    public static final String ROOT_ELEM = "JourneeType";
    public static final String WAREHOUSE_NAME = "Entrepot";
    public static final String TIMETABLE_NAME = "Plage";
    public static final String DELIVERY_NAME = "Livraison";

    public static final String DELIVERY_ADDRESS = "adresse";
    public static final String DELIVERY_CLIENT_ID = "client";
    public static final String DELIVERY_ID = "id";

    public static final String WAREHOUSE_ADDRESS = "adresse";
    public static final String TIMESLOT_BEGIN = "heureDebut";
    public static final String TIMESLOT_END = "heureFin";

    private static final double CONE_FRONT = Math.PI * (1 / 4);
    private static final double CONE_BACK = Math.PI * (1 - 1 / 4);

    /**
     * Constructeur standard.
     */
    public DeliverySheet() {
        deliveries = null;
        network = null;
    }

    public List<RoadNode> getDeliveryRound() {
        List<RoadNode> l = new ArrayList<>();
        l.addAll(getWarehouseRound());
        for (Delivery d : deliveries) {
            l.addAll(getDeliveryRound(d));
        }
        return l;
    }

    public List<RoadNode> getWarehouseRound() {
        if (network == null) {
            return new ArrayList<>();
        }
        if (deliveries.isEmpty()) {
            return new ArrayList<>();
        }
        RoadNode n1 = network.getNodeById(warehouseAddress);
        RoadNode n2 = network.getNodeById(deliveries.get(0).getAddress());
        if (n1 == null || n2 == null) {
            return new ArrayList<>();
        }
        return AStar.findPath(n1, n2);
    }

    public List<RoadNode> getDeliveryRound(Delivery from) {
        if (network == null) {
            return new ArrayList<>();
        }
        int index = deliveries.indexOf(from) + 1;
        RoadNode n1 = network.getNodeById(from.getAddress());
        RoadNode n2 = null;
        if (index < deliveries.size()) {
            n2 = network.getNodeById(deliveries.get(index).getAddress());
        } else {
            n2 = network.getNodeById(warehouseAddress);
        }
        if (n1 == null || n2 == null) {
            return new ArrayList<>();
        }
        return AStar.findPath(n1, n2);
    }

    public long getWarehouseAddress() {
        return warehouseAddress;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    public Delivery findDeliveryById(long deliveryId) {
        for (Delivery delivery : deliveries) {
            if (delivery.getId() == deliveryId) {
                return delivery;
            }
        }
        return null;
    }

    public void setRoadNetwork(RoadNetwork rn) {
        network = rn;
    }

    /**
     * Méthode d'export d'une feuille de route en format texte. Cette méthode se
     * base sur la liste des livraisons et le chemin total des tournées. Elle
     * déclare la prochaine livraison à effectuer, donne quelques informations
     * appréciables la concernant, puis donne le chemin noeud par noeud jusqu'à
     * la livraison sus-citée. Elle recommence, jusqu'à la dernière livraison.
     *
     * @param writer
     * @throws IOException
     */
    public void export(Writer writer) throws IOException {
        if (writer == null) {
            throw new NullPointerException("'writer' ne doit pas être null");
        }

        List<Delivery> delv = deliveries;
        List<RoadNode> path = getDeliveryRound();

        if (path == null) {
            return;
        }

        int indexDelivs = 0;
        RoadNode old = null;
        RoadSection oldRs = null;

        String bufferRoad = "";      //Buffer de toute la route à effectuer

        writer.write("Nouvelle Tournée\n\n\n**********************\n\n\n\n");

        for (RoadNode liv : path) {
            if (old == null) {
                old = liv;
                continue;
            }
            Iterator secI = old.getSections().iterator();
            RoadSection rs = null;

            int gauche = 0;
            int droite = 0;

            while (secI.hasNext()) {
                rs = (RoadSection) secI.next();
                if (rs.getRoadNodeEnd() == liv) {

                    //Première rue à prendre
                    if (oldRs == null) {
                        bufferRoad += "Prendre la rue ";
                        bufferRoad += rs.getRoadName();
                        bufferRoad += "\n\n";
                    } else { //Calcul du "Prenez à gauche/droite, sur"
                        //Calcul de la longeur
                        bufferRoad += "Dans ";
                        bufferRoad += (int) rs.getLength();
                        bufferRoad += " mètres : \n";

                        /*int v1X = oldRs.getRoadNodeEnd().getX()
                         - oldRs.getRoadNodeBegin().getX();
                         int v1Y = oldRs.getRoadNodeEnd().getY()
                         - oldRs.getRoadNodeBegin().getY();
                         int v2X = rs.getRoadNodeEnd().getX()
                         - rs.getRoadNodeBegin().getX();
                         int v2Y = rs.getRoadNodeEnd().getY()
                         - rs.getRoadNodeBegin().getY();*/
                        //double angle1 = Math.atan2(v1Y, v1X);
                        //if ( angle1 <= 0 ) {angle1 += 2*Math.PI;}
                        //double angle2 = Math.atan2(v2Y, v2X);
                        //if ( angle2 <= 0 ) {angle2 += 2*Math.PI;}
                        //double angle = angle2 - angle1;
                        /*double angle = Math.atan2(v1X*v2Y - v1Y*v1X, v1X*v2X + v1Y*v2Y);
                         System.out.print("Angle : (");
                         System.out.print(v1X);
                         System.out.print(", ");
                         System.out.print(v1Y);
                         System.out.print(") ; (");
                         System.out.print(v2X);
                         System.out.print(", ");
                         System.out.print(v2Y);
                         System.out.print(") -> ");
                         System.out.println(angle);

                         if(Math.abs(angle) < CONE_FRONT) {
                         bufferRoad += "Prenez tout droit ";
                         }
                         else if(angle > CONE_FRONT && angle < CONE_BACK) {
                         bufferRoad += "Prenez à gauche ";
                         }
                         else if(angle < -CONE_FRONT && angle > -CONE_BACK) {
                         bufferRoad += "Prenez à droite ";
                         }
                         else {
                         bufferRoad += "Faite demi-tour ";
                         }*/
                        /*if (angle < -CONE_FRONT && angle > -CONE_BACK) {
                         bufferRoad += "Prenez à gauche ";
                         } else if (angle > CONE_FRONT && angle < CONE_BACK) {
                         bufferRoad += "Prenez à droite ";
                         } else if (angle >= CONE_BACK
                         || angle <= -CONE_BACK) {
                         bufferRoad += "Faites demi-tour ";
                         } else {
                         bufferRoad += "Prenez tout droit ";
                         }*/
                        //bufferRoad += "sur la rue ";
                        bufferRoad += "Prenez la rue ";
                        bufferRoad += rs.getRoadName();
                        bufferRoad += "\n\n";
                    }

                    oldRs = rs;
                    break;
                }
            }

            // Coupure dans le chemin
            if (rs == null) {
                throw new RuntimeException();
            }
            if (indexDelivs < delv.size() && liv.getId().equals(
                    delv.get(indexDelivs).getAddress())) {

                Delivery d = delv.get(indexDelivs);
                writer.write("Prochaine livraison : ");
                writer.write(rs.getRoadName() + "\n");
                writer.write("Horaire programmé : ");
                writer.write(d.getTimeSlot().getBegin().getHours() + "h" + d.getTimeSlot().getBegin().getMinutes() + "\n");
                writer.write("Horaire prévu : ");
                writer.write(d.getPredTimeSlot().getBegin().getHours() + "h" + d.getPredTimeSlot().getBegin().getMinutes() + "\n\n");
                writer.write(bufferRoad);
                writer.write("Arrivée à la livraison : ");
                writer.write(rs.getRoadName());
                writer.write("\n\n***\n\n");
                bufferRoad = "";
                indexDelivs++;
            } else if (indexDelivs == delv.size()) {
                writer.write(bufferRoad);
                bufferRoad = "";
            }
            old = liv;
        }
        // On est pas passé par toutes les livraisons
        if (indexDelivs != delv.size()) {
            throw new RuntimeException();
        }
        writer.flush();
    }

    private static long deliveryIdCursor;

    /**
     * Cette methode charger une liste de livraisons pour un plage horaire a
     * partir d'un fichier XML. Elle prend un Reader representant le fichier, le
     * parse et cree les entrepots et livraisons précisés dedans. En suite, ils
     * sont ajoute a l'instance de DeliveryRound du nouveau DeliverySheet. Si le
     * reader est null, une exception sera lance ainsi aue pour les erreurs de
     * structure du fichier XML.
     *
     * @param reader un Reader contenant le fichier XML a charger
     * @return le nouveau DeliverySheet avec les entrepots et les livraisons
     * @throws java.io.IOException
     */
    public static DeliverySheet loadFromXML(Reader reader) throws IOException {
        if (reader == null) {
            throw new NullPointerException("'reader' ne doit pas être nul");
        }

        deliveryIdCursor = 1;

        Element documentRoot;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(reader));
            documentRoot = document.getDocumentElement();
            documentRoot.normalize();
        } catch (ParserConfigurationException | SAXException ex) {
            throw new IOException(ex.getMessage());
        }

        DeliverySheet ds = new DeliverySheet();

        // Element racine different de "JourneeType" (erreur de syntaxe)
        if (!documentRoot.getTagName().equals(ROOT_ELEM)) {
            throw new IOException("Le noeud racine n'est pas '" + ROOT_ELEM + "'");
        }
        NodeList warehouseNodes = documentRoot.getElementsByTagName(WAREHOUSE_NAME);
        if (warehouseNodes == null || warehouseNodes.getLength() == 0) {
            throw new IOException("L'entrepôt doit être défini via l'élément '" + WAREHOUSE_NAME + "'");
        }
        ds.warehouseAddress = parseWarehouse(warehouseNodes.item(0));

        NodeList timeTable = documentRoot.getElementsByTagName(TIMETABLE_NAME);
        if (timeTable != null) {
            ds.deliveries = parseTimeTable(timeTable);
        }
        return ds;
    }

    private static long parseWarehouse(Node warehouseNode) throws IOException {
        NamedNodeMap attributs = warehouseNode.getAttributes();
        Node address = attributs.getNamedItem(WAREHOUSE_ADDRESS);
        if (address == null) {
            throw new IOException("Attribut '" + WAREHOUSE_ADDRESS + "' attendu pour l'entrepôt");
        }
        try {
            return Long.parseLong(address.getNodeValue());
        } catch (NumberFormatException e) {
            throw new IOException("Nombre attendu pour l'adresse de l'entrepôt, '"
                    + address.getNodeValue() + "' trouvé");
        }
    }

    /**
     * Cette methode s'occupe du traitement des elements d'une tournee. Elle
     * traverse le DOM du document XML à partir les elements "plage" pour
     * recuperer les details sur l'entrepot et le plage horaire. Elle cree tous
     * les plages d'apres les infos dans l'element XML correspondant ainsi que
     * les livraisons de chaque plage en transformant les childNodes des plage
     * et en les transformant en Element pour pouvoir acceder aux livraisons
     * facilement. Puis, le plage correspondant est ajoute a la livraison.
     *
     * @param journeyNodes la NodeListe de plages
     * @return la liste de livraisons crée
     */
    private static List<Delivery> parseTimeTable(NodeList timetable) throws IOException {
        List<Delivery> deliveries = new LinkedList<>();
        for (int i = 0; i < timetable.getLength(); i++) {
            Node node = timetable.item(i);
            Element plage;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                plage = (Element) node;
            } else {
                throw new IOException("Mauvais type de noeud pour le créneau horaire");
            }

            TimeSlot ts = parseTimeSlot(node);

            NodeList deliveryNodes = plage.getElementsByTagName(DELIVERY_NAME);

            // Traiter la liste des livraisons
            for (int j = 0; j < deliveryNodes.getLength(); j++, deliveryIdCursor++) {
                Node deliveryNode = deliveryNodes.item(j);
                Delivery del = parseDelivery(deliveryNode);
                deliveries.add(new Delivery(deliveryIdCursor, del.getAddress(), ts, del.getClient()));
            }

        }
        return deliveries;
    }

    /**
     * Cette methode cree un objet du type TimeSlot d'apres les informations
     * dans l'element XML. Elle lit les valeurs de tous les attributs,
     * transforme leurs types en types attendus et en cree le nouveau TimeSlot.
     *
     * @param node l'element XML precisant le TimeSlot
     * @return le nouveau TimeSlot
     * @throws IOException s'il y a un probleme avec le XML
     */
    private static TimeSlot parseTimeSlot(Node node) throws IOException {
        NamedNodeMap attributes = node.getAttributes();
        Node timeSlotBegin = attributes.getNamedItem(TIMESLOT_BEGIN);
        if (timeSlotBegin == null) {
            throw new IOException("Attribut '" + TIMESLOT_BEGIN + "' requis");
        }
        Node timeSlotEnd = attributes.getNamedItem(TIMESLOT_END);
        if (timeSlotEnd == null) {
            throw new IOException("Attribut '" + TIMESLOT_END + "' requis");
        }

        Date beginDate;
        Date endDate;

        // Récupérer l'heure de début de livraison en String, splitter le String
        // en ses trois parties pour les caster en int, la dernière étant optionnelle
        try {
            beginDate = parseHour(timeSlotBegin.getNodeValue());
            endDate = parseHour(timeSlotEnd.getNodeValue());
        } catch (DOMException ex) {
            throw new IOException(ex.getMessage());
        }

        return new TimeSlot(beginDate, endDate);
    }

    private static Date parseHour(String time) throws IOException {
        // Calendar initialisé à la date d'aujourd'hui
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String[] splitTime = time.split(":");
        if (splitTime.length < 2) {
            throw new IOException("Créneaux horaires attendus sous le format hh:mm[:ss], '" + time + "' trouvé");
        }

        // heures
        try {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTime[0]));
        } catch (NumberFormatException ex) {
            throw new IOException("Mauvais champ heures spécifié '" + splitTime[0] + "'");
        }

        // minutes
        try {
            calendar.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
        } catch (NumberFormatException ex) {
            throw new IOException("Mauvais champ minutes spécifié '" + splitTime[1] + "'");
        }

        // secondes
        try {
            calendar.set(Calendar.SECOND, Integer.parseInt(splitTime[2]));
        } catch (NumberFormatException ex) {
            throw new IOException("Mauvais champ secondes spécifié '" + splitTime[2] + "'");
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

        return calendar.getTime();
    }

    /**
     * Cette methode cree un objet du type Delivery d'apres les informations
     * dans l'element XML. Elle lit les valeurs de tous les attributs,
     * transforme leurs types en types attendus et en cree la nouvelle Delivery.
     *
     * @param node l'element XML precisant la Delivery
     * @return la nouvelle Delivery
     */
    private static Delivery parseDelivery(Node node) throws IOException {
        if (!node.getNodeName().equals(DELIVERY_NAME)) {
            throw new IOException("Noeud XML '" + DELIVERY_NAME + "' attendy, '"
                    + node.getNodeName() + "' trouvé");
        }

        NamedNodeMap attributs = node.getAttributes();

        // Non géré via XML du fait du mauvais format donné :
        //  -> un id doit être UNIQUE à travers tous les objets concernés
        // Id de la livraison
        //Node deliveryIdNode = attributs.getNamedItem(DELIVERY_ID);
        //if (deliveryIdNode == null) {
        //    throw new IOException("Attribut '" + DELIVERY_ID + "' requis");
        //}
        long deliveryId = 0;
        //try {
        //    deliveryId = Long.parseLong(deliveryIdNode.getNodeValue());
        //} catch (DOMException | NumberFormatException e) {
        //    throw new IOException("Erreur de format de l'id de la livraison");
        //}

        // Id du client
        Node clientIdNode = attributs.getNamedItem(DELIVERY_CLIENT_ID);
        if (clientIdNode == null) {
            throw new IOException("Attribut '" + DELIVERY_CLIENT_ID + "' requis");
        }
        long clientId;
        try {
            clientId = Long.parseLong(clientIdNode.getNodeValue());
        } catch (DOMException | NumberFormatException e) {
            throw new IOException("Erreur de format de l'id du client de la livraison");
        }
        clientId = Long.parseLong(clientIdNode.getNodeValue());

        // Addresse de la livraison
        Node addressNode = attributs.getNamedItem(DELIVERY_ADDRESS);
        if (addressNode == null) {
            throw new IOException("Attribut '" + DELIVERY_ADDRESS + "' requis");
        }
        long address;
        try {
            address = Long.parseLong(addressNode.getNodeValue());
        } catch (DOMException | NumberFormatException e) {
            throw new IOException("Erreur de format de l'adresse de la livraison");
        }

        return new Delivery(deliveryId, address, null, new Client(clientId));
    }

    /**
     * Méthode de création de l'horaire prévisionnel pour l'ensemble des
     * livraisons à effectuer.
     *
     */
    public void createPredTimeSlot() {
        TimeSlot departure = null;
        TimeSlot curTimeSlot = null;

        for (int it = 0; it < deliveries.size(); it++) {
            long tv = tempsVoyage(getDeliveryRound(deliveries.get(it)));

            if (departure == null) {
                curTimeSlot = deliveries.get(it).getTimeSlot();
                departure = curTimeSlot;
            } else if (!deliveries.get(it).getTimeSlot().getBegin().
                    equals(curTimeSlot.getBegin())) {
                curTimeSlot = deliveries.get(it).getTimeSlot();

                if (curTimeSlot.getBegin().getTime() > departure.getBegin().getTime() + tv) {
                    deliveries.get(it).setPredTimeSlot(new TimeSlot(new Date(departure.getBegin().getTime() + tv), 0l));
                    departure = curTimeSlot;
                    continue;
                }

            }
            deliveries.get(it).setPredTimeSlot(new TimeSlot(new Date(departure.getBegin().getTime() + tv), 0l));
            departure = deliveries.get(it).getPredTimeSlot();

        }
    }

    /**
     * Méthode de calcul du temps mis pour parcourir un ensemble de noeuds, en
     * fonction de la longueur et la vitesse des rues considérées.
     *
     * @param path la liste de noeuds dont on veut calculer le temps de parcourt
     * @return le temps de parcourt pour la liste de noeuds <code>path</code>
     */
    private long tempsVoyage(List<RoadNode> path) {
        long tv = 0l;
        for (int it = 0; it < path.size() - 1; it++) {
            for (RoadSection rs : path.get(it).getSections()) {
                if (rs.getRoadNodeEnd().equals(path.get(it + 1))) {
                    tv += (rs.getCost() * 1000l);
                    break;
                }

            }
        }
        return tv;
    }
}
