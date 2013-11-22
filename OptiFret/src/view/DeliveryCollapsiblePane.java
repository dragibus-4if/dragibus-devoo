package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Delivery;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author Quentin
 */
public class DeliveryCollapsiblePane extends JPanel {

    public DeliveryCollapsiblePane(Delivery delivery) {

        JPanel minimal = new JPanel();
        minimal.setBackground(Color.white);
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));
        minimal.add(new JLabel("Livraison : " + delivery.getId()));
        
        final JXCollapsiblePane extend = new JXCollapsiblePane();
        
        minimal.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                extend.getActionMap().get("toggle")
                        .actionPerformed(new ActionEvent(e, WIDTH, TOOL_TIP_TEXT_KEY));
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        setLayout(new VerticalLayout());
        add(minimal);
        add(extend);
        validate();
    }
}
