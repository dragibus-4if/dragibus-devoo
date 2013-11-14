package optifret;

import java.awt.EventQueue;
import view.DeliverySheetFrame;

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
                DeliverySheetFrame frame = new DeliverySheetFrame();
                frame.setVisible(true);
            }
        });
    }
}
