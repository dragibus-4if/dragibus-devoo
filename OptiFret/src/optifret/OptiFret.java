package optifret;

import controller.MainController;
import java.awt.EventQueue;
import view.MainFrame;

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
                MainFrame frame = new MainFrame();
                MainController controller = new MainController(frame);
                frame.setVisible(true);
            }
        });
    }
}
