package view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.Delivery;
import org.jdesktop.swingx.VerticalLayout;

public class DeliveryList extends JScrollPane {

    private final JPanel panel;
    private DeliveryView selected;
    private final CopyOnWriteArrayList<Listener> listeners;
    private Map<Long, DeliveryView> panelList;

    public DeliveryList() {
        super();
        this.listeners = new CopyOnWriteArrayList<>();
        this.panelList = new LinkedHashMap<>();
        panel = new JPanel(new VerticalLayout());
        getViewport().add(panel);
    }

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

    public DeliveryView getSelected() {
        return selected;
    }

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

    public void setSelectionById(long id) {
        DeliveryView newSelected = panelList.get(id);

        if (newSelected != null) {
            newSelected.onEventSelect();
        } else {
            setSelected(null);
        }
    }

    public void addListener(Listener l) {
        this.listeners.add(l);
    }

    public void removeListener(Listener l) {
        this.listeners.remove(l);
    }

    // Event firing method.  Called internally by other class methods.
    protected void fireChangeEvent() {
        MyChangeEvent evt = new MyChangeEvent(this);
        for (Listener l : listeners) {
            l.changeEventReceived(evt);
        }
    }
}
