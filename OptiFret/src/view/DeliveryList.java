package view;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.Delivery;
import org.jdesktop.swingx.VerticalLayout;

public class DeliveryList extends JScrollPane {

    private final JPanel panel;
    
    public DeliveryList() {
        panel = new JPanel(new VerticalLayout());
        getViewport().add(panel);
    }
    
    public void update(List<Delivery> deliveries) {
        if (deliveries == null) {
            return;
        }
        panel.removeAll();
        for (Delivery d : deliveries) {
            DeliveryCollapsiblePane dcp = new DeliveryCollapsiblePane(d);
            dcp.toggle();
            panel.add(dcp);
        }
    }
}
