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
    private DeliveryCollapsiblePane selected;
    private final CopyOnWriteArrayList<Listener> listeners;
    private Map<Long,DeliveryCollapsiblePane> panelList;
    public DeliveryList() {
        super();
        this.listeners = new CopyOnWriteArrayList<>();
        this.panelList= new LinkedHashMap<>();
        panel = new JPanel(new VerticalLayout());
        getViewport().add(panel);
    }

    public void setDeliveries(List<Delivery> deliveries) {
        if (deliveries == null) {
            return;
        }
        panel.removeAll();
        for (Delivery d : deliveries) {
            DeliveryCollapsiblePane dcp = new DeliveryCollapsiblePane(d, this);
            dcp.toggle();
            panelList.put(d.getAddress(), dcp);
            panel.add(dcp);
        }
        repaint();
    }

    public DeliveryCollapsiblePane getSelected() {
        return selected;
    }

    public void setSelected(DeliveryCollapsiblePane selected) {
        this.selected = selected;
    }
    public void setSelectionById(long id){
        if(panelList.get(id)==null){
            System.err.println("panelList.get(id) null");
            return;
        }
        panelList.get(id).select();
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
