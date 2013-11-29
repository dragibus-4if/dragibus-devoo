package view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JScrollPane;
import model.Delivery;
import org.jdesktop.swingx.VerticalLayout;

public class DeliveryList extends JScrollPane {

    /**
     * JPanel contenant l'ensemble des DeliveryView
     *
     * @see DeliveryView
     */
    private final CustomJPanel panel;
    /**
     * contient le DeliveryView actuellemnt selectionné
     *
     * @see DeliveryView
     */
    private DeliveryView selected;
    /**
     * Contient la liste des listeners ecoutant les events envoyés
     */
    private final CopyOnWriteArrayList<Listener> listeners;
    /**
     * Contient toutes les DeliveryView présent dans le panel
     *
     * @see DeliveryView
     */
    private Map<Long, DeliveryView> panelList;

    public DeliveryList() {
        super();
        this.listeners = new CopyOnWriteArrayList<>();
        this.panelList = new LinkedHashMap<>();
        panel = new CustomJPanel(new VerticalLayout());


        getViewport().add(panel);


    }

    /**
     * Charge une liste de Delivery dans la DeliveryList
     *
     * @param deliveries
     */
    public void setDeliveries(List<Delivery> deliveries) {
        if (deliveries == null) {
            return;
        }
        panel.removeAll();
        for (Delivery d : deliveries) {
            DeliveryView dcp = new DeliveryView(d, this);
            dcp.toggle();
            panelList.put(d.getAddress(), dcp);
            panel.add(dcp);
        }
        validate();
        //repaint();
    }

    /**
     * Getter pour selected
     *
     * @return Renvoie la DeliveryView selectionnée
     */
    public DeliveryView getSelected() {
        return selected;
    }

    /**
     * Permet de mettre a jour la DeliveryView seéctionnée
     *
     * @param newSelected
     */
    public void setSelected(DeliveryView newSelected) {

        if (selected == null) {
            selected = newSelected;
        } else {
            if (selected == newSelected) {
                selected.unselect();
                selected = null;
            } else {
                selected.unselect();
                selected = newSelected;
            }
        }
    }


    /**
     * Permet de mettre à jour la DeliveryView seléctionnée
     *
     * @param id id de la livraison lié à la DeliveryView
     */
    public void setSelectionById(long id) {
        DeliveryView newSelected = panelList.get(id);

        if (newSelected != null) {
            newSelected.onEventSelect();
        } else {
            setSelected(null);
        }
    }

    /**
     * Ajoute un listener d'event
     *
     * @param l
     */
    public void addListener(Listener l) {
        this.listeners.add(l);
    }

    /**
     * Retire un listerner d'event
     *
     * @param l
     */
    public void removeListener(Listener l) {
        this.listeners.remove(l);
    }

    /**
     * Envoie une notification a tous les listeners d'event ajouté à listerners
     */
    protected void fireChangeEvent() {
        MyChangeEvent evt = new MyChangeEvent(this);
        for (Listener l : listeners) {
            l.changeEventReceived(evt);

        }
    }
}

