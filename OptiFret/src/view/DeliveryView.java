package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
public class DeliveryView extends JPanel {

    private static final Color MINIMAL_BG_COLOR = new Color(0, 0, 0, 0);
    private static final Color MINIMAL_BG_OVER_COLOR = new Color(240, 240, 240, 175);
    //private static final Color MINIMAL_BG_SELECT_COLOR = new Color(170, 170, 170, 100);
    private static final Color MINIMAL_BG_SELECT_COLOR = new Color(0,0,0,40);
    private static final Color MINIMAL_BG_UNCOLLAPSED_COLOR = new Color(200, 200, 200, 100);
    private final Delivery delivery;
    private final JPanel minimal;
    private final JXCollapsiblePane extend;
    private final JLabel idDeliveryLabel;
    private final JLabel clientAddressLabel;
    private final JLabel clientPhoneNumLabel;
    private final JLabel clientNameLabel;
    private JButton toggleButton;
    private final DeliveryList parent;
    private boolean folded;
    private static final String ARROW_UP = "\u2191";
    private static final String ARROW_DOWN = "\u2193";

    public DeliveryView(Delivery delivery, DeliveryList parent) {
        super(new VerticalLayout());
        folded = true;
        clientNameLabel = new JLabel();
        clientPhoneNumLabel = new JLabel();
        clientAddressLabel = new JLabel();
        idDeliveryLabel = new JLabel();
        extend = new JXCollapsiblePane();
        minimal = new JPanel();
        this.parent = parent;
        this.delivery = delivery;

        // Labels
        idDeliveryLabel.setText("Livraison : " + delivery.getId());
        Client client = delivery.getClient();
        if (client != null) {
            String unspecified = "non spécifié";
            String clientName = (client.getName() != null) ? client.getName() : unspecified;
            String clientPhoneNum = (client.getPhoneNum() != null) ? client.getPhoneNum() : unspecified;
            String clientAddress = (client.getAddress() != null) ? client.getAddress() : unspecified;
            clientPhoneNumLabel.setText("Tél. client : " + clientPhoneNum);
            clientNameLabel.setText("Client : " + clientName);
            clientAddressLabel.setText("Adresse : " + clientAddress);
        }

        add(makeMinimal());
        add(makeExtend());
        extend.addPropertyChangeListener("collapsed", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                repaintall();
            }
        });
        setOpaque(false);
        
        validate();
        repaint();
    }

    private void repaintall() {
        this.parent.validate();
        this.parent.repaint();

    }

    private JXCollapsiblePane makeExtend() {
        extend.setLayout(new GridLayout(3, 1));
        extend.add(clientNameLabel);
        extend.add(clientAddressLabel);
        extend.add(clientPhoneNumLabel);


        //extend.getContentPane().setBackground(MINIMAL_BG_OVER_COLOR);
        extend.setOpaque(false);
        for(Component c : extend.getComponents()){
            c.setBackground(MINIMAL_BG_COLOR);
        }
        extend.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        extend.validate();
        extend.repaint();
        return extend;
    }

    private JPanel makeMinimal() {

        minimal.setBackground(MINIMAL_BG_COLOR);
        minimal.setOpaque(true);
        minimal.setBackground(new Color(0, 0, 0, 0));
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));

        toggleButton = new JButton();
        updateToggleButton();

        minimal.add(toggleButton);
        minimal.add(idDeliveryLabel);
        final DeliveryView that = this;
        minimal.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               

                    //that.minimal.setBackground(MINIMAL_BG_OVER_COLOR);
                    that.minimal.setBorder(BorderFactory.createLineBorder(Color.gray, 1, false));
                    repaintall();
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                

                    //that.minimal.setBackground(MINIMAL_BG_COLOR);
                    that.minimal.setBorder(null);
                    repaintall();
                
            }
        });
        toggleButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggle(e);
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

        minimal.validate();
        minimal.repaint();
        return minimal;
    }

    public void toggle() {
        toggle(new MouseEvent(this, 0, 0, 0, 0, 0, 0, false, 0));
    }

    private void toggle(MouseEvent e) {
        extend.getActionMap().get("toggle")
                .actionPerformed(new ActionEvent(e, WIDTH, TOOL_TIP_TEXT_KEY));
        updateToggleButton();
        folded = !folded;


        validate();
        repaint();
    }

    private void updateToggleButton() {
        if (folded) {
            toggleButton.setText(ARROW_DOWN);
        } else {
            toggleButton.setText(ARROW_UP);
        }
        minimal.validate();
        minimal.repaint();
    }

    public void onEventSelect() {

        minimal.setBackground(MINIMAL_BG_SELECT_COLOR);
        repaintall();
        parent.setSelected(this);

    }

    public void select() {

        minimal.setBackground(MINIMAL_BG_SELECT_COLOR);
        repaintall();
        parent.setSelected(this);

        parent.fireChangeEvent();

    }

    public void unselect() {
        if (!extend.isCollapsed()) {

            minimal.setBackground(MINIMAL_BG_UNCOLLAPSED_COLOR);
            repaintall();
        } else {

            minimal.setBackground(MINIMAL_BG_COLOR);
            repaintall();
        }
    }

    public Delivery getDelivery() {
        return delivery;
    }
}
