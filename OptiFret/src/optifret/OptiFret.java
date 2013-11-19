package optifret;

import controller.DeliverySheetController;
import java.awt.EventQueue;
import view.DeliverySheetView;

/**
 * @author jmcomets
 */
public class OptiFret {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                DeliverySheetView view = new DeliverySheetView();
                DeliverySheetController controller = new DeliverySheetController();
                controller.setDeliverySheetView(view);
                view.setVisible(true);
            }
        });
    }
}
