package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Client;
import model.Delivery;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXImageView;
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
    private JLabel arrow;
    private ImageIcon arrowUp;
    private ImageIcon arrowDown;
    //private Image arrowUp;
    //private Image arrowDown;

    private DeliveryList parent;

    private boolean folded =true;
    public DeliveryCollapsiblePane(Delivery delivery, DeliveryList parent) {
        this.parent = parent;
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

        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();

        minimal.setBackground(Color.white);
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));

        arrow = new JLabel();
        arrow.setDoubleBuffered(true);
        
        
        arrowDown = new ImageIcon(toolkit.getImage("src/flechedown.jpg"));
        arrowUp = new ImageIcon(toolkit.getImage("src/flecheup.jpg"));

        arrow.setIcon(arrowDown);
       
        minimal.add(idDeliveryLabel);
        minimal.add(arrow);
        minimal.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                select();
                
                if(parent.getSelected()!=null)
                {
                System.out.println(parent.getSelected().getDelivery().getId());
                }
                else{
                    System.out.println("RIEN RIEN RIEN");
                }
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
        arrow.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                toggle(e);
                if (folded) {
                    arrow.setIcon(arrowUp);
                    folded = false;
                } else {
                     arrow.setIcon(arrowDown);
                     folded=true;
                }
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
        return minimal;
    }

    public void toggle() {
        toggle(new MouseEvent(this, 0, 0, 0, 0, 0, 0, false, 0));
    }

    private void toggle(MouseEvent e) {
        extend.getActionMap().get("toggle")
                .actionPerformed(new ActionEvent(e, WIDTH, TOOL_TIP_TEXT_KEY));
    }

    public void select() {
        minimal.setBackground(Color.gray);

        if (parent.getSelected() != null) {
            parent.getSelected().unselect();
        }

        if (parent.getSelected() != this) {
            parent.setSelected(this);
        } else {
            parent.setSelected(null);
        }

    }

    public void unselect() {
        minimal.setBackground(Color.white);
    }

    public Delivery getDelivery() {
        return delivery;
    }

    
}
