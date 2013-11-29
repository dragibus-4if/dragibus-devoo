package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import model.Delivery;
import model.RoadNode;
import model.TimeSlot;
import view.NodeView.MODE;

/**
 * Vue générale de la carte dans laquelle sont tracés le réseau routier et les
 * chemins de livraison
 *
 * @author Sylvain
 */
public class DeliveryMap extends NavigablePanel {

    private Map<Integer, ArcView> mapArcs;
    private Map<Long, NodeView> mapNodes;
    private WeakReference<NodeView> selectedNode;
    private CopyOnWriteArrayList<Listener> listeners;
    public static final int PADDING = 20;

    /**
     * Enum de retour lors d'un clic sur la carte afin de savoir l'état de ce
     * noeud
     *     
*/
    public enum NODE_RETURN {

        NOTHING_SELECTED,
        NODE_SELECTED,
        NODE_ALLREADY_SELECTED
    }

    /**
     * Constructeur
     */
    public DeliveryMap() {
        super();
        setDoubleBuffered(true);
        setFocusable(true);
        reset();
    }

    /**
     * Méthode qui vide entièrement la carte et la réinitialise
     */
    private void reset() {
        listeners = new CopyOnWriteArrayList<>();
        mapArcs = new LinkedHashMap<>();
        mapNodes = new LinkedHashMap<>();
        selectedNode = new WeakReference<>(null);
        resetTransform();
    }

    /**
     * Méthode qui génère le réseau routier (composé de NodeView et d'ArcView) à
     * partir de la liste des noeuds <nodes> du réseau
     *
     * @param nodes liste des noeuds du réseau routier
     */
    public void updateNetwork(List<RoadNode> nodes) {
        if (nodes == null) {
            reset();
            return;
        }
        mapArcs.clear();
        mapNodes.clear();
        selectedNode.clear();
        for (RoadNode rn : nodes) {
            if (rn.getNeighbors() == null) {
                break;
            }
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), rn.getId(),
                    new WeakReference<>(this), MODE.CLASSIC);
            if (!mapNodes.containsKey(rn.getId())) {
                mapNodes.put(rn.getId(), tempNode);
            }
            for (RoadNode neighbor : rn.getNeighbors()) {
                ArcView temp = new ArcView(rn, neighbor, 0);
                if (!mapArcs.containsKey(temp.hashCode())) {
                    mapArcs.put(temp.hashCode(), temp);
                }
            }
        }
    }

    /**
     * Méthode de mise à jour visuelle du noeud correspondant à l'entrepot grâce
     * à l'adresse de l'entrepot.
     *
     * @param id identifiant de l'adresse de l'entrepot
     */
    public void updateWhareHouse(Long id) {
        if (id != -1 && mapNodes.get(id) != null) {
            mapNodes.get(id).setMode(MODE.WAREHOUSE);
        }
    }

    /**
     * Méthode de tracé du chemin qu'emprunte le livreur. Met à jour les noeuds
     * et les arcs parcourus à partir de la liste de noeuds passée en paramètre
     *
     * @param nodes liste des noeuds qu'emprunte le livreur
     */
    public void updateDeliveryNodesPath(List<RoadNode> nodes) {
        if (nodes == null) {
            return;
        }
        for (ArcView arc : mapArcs.values()) {
            arc.resetNbLines();
            arc.resetColors();
        }
        for (int i = 0; i < nodes.size(); i++) {
            RoadNode rn = nodes.get(i);
            NodeView tempNode = new NodeView(rn.getX(), rn.getY(), rn.getId(),
                    new WeakReference<>(this), MODE.CLASSIC);
            for (Long j = new Long(0); j < mapNodes.size(); j++) {
                if (tempNode.equals(mapNodes.get(j))) {
                    mapNodes.get(j).setMode(MODE.DELIVERY_PATH);
                }
            }
            if (i >= 1) {
                RoadNode neighbor = nodes.get(i - 1);
                ArcView temp = new ArcView(neighbor, rn, 0);
                if (mapArcs.containsKey(temp.hashCode())) {
                    mapArcs.get(temp.hashCode()).incrementNbLines();
                }
            }
        }
    }

    /**
     * Méthode de mise à jour des couleurs par plage horraire du chemin
     * qu'emprunte le livreur.
     *
     * @param dels liste des points de livraison (liste de <Delivery>)
     * @param path liste des points du chemin de livraison (liste de <RoadNode>)
     */
    public void updateTimeSlots(List<Delivery> dels, List<RoadNode> path) {
        if (dels == null) {
            return;
        }
        List<Integer> listPosDeliveries = new ArrayList<>();
        List<Integer> listPosPath = new ArrayList<>();

        TimeSlot currentTimeSlot;
        TimeSlot testedTimeSlot = new TimeSlot(new Date(0L), new Date(0L));
        for (Delivery del : dels) {
            currentTimeSlot = del.getTimeSlot();
            if (!currentTimeSlot.getBegin().equals(testedTimeSlot.getBegin())) {
                testedTimeSlot = currentTimeSlot;
                listPosDeliveries.add(dels.indexOf(del));

            }
        }

        int nbTimeSlots = listPosDeliveries.size();


        int itPath = 0, itDels = 1, itTS = 1;
        while (itPath < path.size()) {
            if (itDels < dels.size() && path.get(itPath).getId().equals(dels.get(itDels).getAddress())) {
                if (itTS < listPosDeliveries.size() && listPosDeliveries.get(itTS).equals(new Integer(itDels))) {
                    listPosPath.add(itPath);
                    itTS++;
                }
                itDels++;
            }

            itPath++;
        }
        itPath = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            if (itPath < listPosPath.size() && listPosPath.get(itPath).equals(new Integer(i))) {
                itPath++;
            }
            ArcView arcTemp = new ArcView(path.get(i),
                    path.get(i + 1), 0);

            if (mapArcs.get(arcTemp.hashCode()) != null) {
                mapArcs.get(arcTemp.hashCode()).
                        updateColorPerTimeSlot(nbTimeSlots - 1, itPath);
            }
        }
    }

    /**
     * Méthode qui met à jour les noeuds de livraison pour les faire apparaitre
     * en rouge.
     *
     * @param dels liste des noeuds à faire apparaitre en rouge (Liste de
     * <RoadNode>)
     */
    public void updateDeliveryNodes(List<Delivery> dels) {
        if (dels == null) {
            return;
        }
        for (Delivery del : dels) {

            mapNodes.get(del.getAddress()).setMode(MODE.DELIVERY_NODE);
        }
    }

    /**
     * Méthode qui remet tous les noeuds de la carte en mode normal (noir)
     */
    public void clearNodeViewMode() {
        for (NodeView n : mapNodes.values()) {
            n.setMode(MODE.CLASSIC);
        }
    }

    /**
     * Méthode qui remet tous les arcs de la carte en mode normal (noir sans
     * flèche)
     */
    public void clearArrowColors() {
        for (ArcView arc : mapArcs.values()) {
            arc.resetColors();
        }
    }

    /**
     * Méthode qui parcourt la liste des noeuds de la carte et détecte si les
     * coordonnées <x> et <y> du clic passé en paramètre tombent dans un noeud
     * de la carte.Détecte également si aucun noeud n'a été sélectionné ou si le
     * noeud cliqué était déjà sélectionné grâce à la valeur de retour
     * <NODE_RETURN>.
     *
     * @param x coordonnée horizontale du clic
     * @param y coordonnée verticale du clic
     */
    @Override
    public void notifyPressed(int x, int y) {
        //boolean voidClic = true;
        NODE_RETURN ret = NODE_RETURN.NOTHING_SELECTED;
        for (NodeView node : mapNodes.values()) {
            NODE_RETURN ret_node = node.onMouseDown(x, y);
            if (ret_node == NODE_RETURN.NODE_SELECTED || ret_node == NODE_RETURN.NODE_ALLREADY_SELECTED) {
                ret = ret_node;
                break;
                //voidClic = false; // TODO also break loop ?
            }
        }
        if (ret == NODE_RETURN.NOTHING_SELECTED) {
            if (getSelectedNode().get() != null) {
                getSelectedNode().clear();
            }
        }
        if (ret != NODE_RETURN.NODE_ALLREADY_SELECTED) {
            fireChangeEvent();
        }
        repaint();
    }

    /**
     * Méthode qui notifie les noeuds de la carte que le clic en cours a été
     * relaché.
     *
     * @param x coordonnée horizontale du clic
     * @param y coordonnée verticale du clic
     */
    @Override
    public void notifyReleased(int x, int y) {
        for (NodeView node : mapNodes.values()) {
            node.onMouseUp(x, y);
        }
    }

    /**
     * Méthode qui notifie les noeuds de la carte que la souris a été bougée aux
     * coordonnées <x> et <y>.
     *
     * @param x coordonnée horizontale
     * @param y coordonnée verticale
     */
    @Override
    public void notifyMoved(int x, int y) {
        for (NodeView node : mapNodes.values()) {
            node.onMouseOver(x, y);
        }
        repaint();
    }

    /**
     * Override de la méthode de dessin du JPanel pour dessiner la carte
     *
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
        applyTransform((Graphics2D) g);
        draw(g);
    }

    /**
     * Méthode qui dit aux arcs et aux noeuds de la carte de se dessiner sur le
     * JPanel
     *
     * @param g Graphics
     */
    private void draw(Graphics g) {
        for (ArcView arc : mapArcs.values()) {
            arc.draw(g, getScale());
        }
        for (NodeView node : mapNodes.values()) {
            node.draw(g);
        }
    }
    
/**
 * Getter qui renvoie le noeud actuellement sélectionné
 * @return 
 */
    public WeakReference<NodeView> getSelectedNode() {
        return selectedNode;
    }
/**
 * Méthode qui met le noeud dont l'adresse est <id> dans l'attribut <selectedNode> car il a été sélectionné  
 * @param id adresse du noeud sélectionné
 */
    public void setSelectedNodeById(long id) {
        if (selectedNode.get() != null) {
            selectedNode.get().setSelection(false);
        }
        if (id == -1l) {
            setSelectedNode(new WeakReference<NodeView>(null));
            repaint();
            return;
        }
        mapNodes.get(id).setSelection(true);
        setSelectedNode(new WeakReference<>(mapNodes.get(id)));
        repaint();
    }
/**
 * Setter de la weakReference vers le noeud sélectionné
 * @param selectedNode 
 */
    public void setSelectedNode(WeakReference<NodeView> selectedNode) {
        this.selectedNode = selectedNode;
    }
/**
 * Ajoute le listener <l> aux listeners de cette classe.
 * @param l 
 */
    public void addListener(Listener l) {
        listeners.add(l);
    }
/**
 * Enlève le listener <l> des listeners de cette classe.
 * @param l 
 */
    public void removeListener(Listener l) {
        listeners.remove(l);
    }

/**
 * Notifie les listeners de la classe d'un évènement
 */
    protected void fireChangeEvent() {
        MyChangeEvent evt = new MyChangeEvent(this);
        for (Listener l : listeners) {
            l.changeEventReceived(evt);
        }
    }
}
