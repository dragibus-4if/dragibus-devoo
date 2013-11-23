package view;

import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import model.Delivery;

public class DeliveryList extends JScrollPane {
    
    public DeliveryList() {
    }
    
    public void setDeliveries(List<Delivery> deliveries) {
        if (deliveries == null) {
            return;
        }
        JViewport vp = getViewport();
        vp.removeAll();
        for (Delivery d : deliveries) {
            //vp.add(new DeliveryCollapsiblePane(d));
        }
    }
}