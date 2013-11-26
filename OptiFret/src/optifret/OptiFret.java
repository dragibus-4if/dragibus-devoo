package optifret;

import controller.MainController;
import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    MainFrame frame = new MainFrame();
                    MainController controller = new MainController(frame);
                    controller.run();
                } catch (ClassNotFoundException | InstantiationException |
                        IllegalAccessException | UnsupportedLookAndFeelException e) {
                    
                }
            }
        });
    }
}
