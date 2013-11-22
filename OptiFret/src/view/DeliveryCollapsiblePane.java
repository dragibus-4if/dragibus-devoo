package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Client;
import model.Delivery;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author Quentin
 */
public class DeliveryCollapsiblePane extends JPanel {

    private Delivery delivery;

    private JPanel minimal = new JPanel();
    private JXCollapsiblePane extend = new JXCollapsiblePane();
    private JLabel idDeliveryLabel = new JLabel();
    private JLabel clientAdressLabel = new JLabel();
    private JLabel clientPhoneNumLabel = new JLabel();
    private JLabel clientNameLabel = new JLabel();

    public DeliveryCollapsiblePane(Delivery delivery) {
        this.delivery = delivery;

        // Labels
        idDeliveryLabel.setText("Livraison : " + delivery.getId());
        Client client = delivery.getClient();
        if (client != null) {
            clientPhoneNumLabel.setText("Téléphone du client : " + client.getPhoneNum());
            clientNameLabel.setText("Client : " + client.getName());
            clientAdressLabel.setText("Adresse :" + client.getAddress());
        }

        setLayout(new VerticalLayout());
        add(makeMinimal());
        add(makeExtend());
        validate();
    }

    private JXCollapsiblePane makeExtend() {
        extend.setLayout(new FlowLayout());

        extend.add(clientAdressLabel);
        extend.add(clientNameLabel);
        extend.add(clientPhoneNumLabel);

        return extend;
    }

    private JPanel makeMinimal() {

        minimal.setBackground(Color.white);
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));
        minimal.add(idDeliveryLabel);

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
        return minimal;
    }
}
