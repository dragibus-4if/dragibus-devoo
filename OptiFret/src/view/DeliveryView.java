package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    private static final Color MINIMAL_BG_COLOR = Color.white;
    private static final Color MINIMAL_BG_OVER_COLOR = new Color(240, 240, 240);
    private static final Color MINIMAL_BG_SELECT_COLOR = new Color(180, 180, 180);
    private static final Color MINIMAL_BG_UNCOLLAPSED_COLOR = new Color(200, 200, 200);
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
            String clientName = (client.getPhoneNum() != null) ? client.getPhoneNum() : unspecified;
            String clientPhoneNum = (client.getPhoneNum() != null) ? client.getPhoneNum() : unspecified;
            String clientAddress = (client.getPhoneNum() != null) ? client.getPhoneNum() : unspecified;
            clientPhoneNumLabel.setText("Tél. client : " + clientPhoneNum);
            clientNameLabel.setText("Client : " + clientName);
            clientAddressLabel.setText("Adresse : " + clientAddress);
        }

        add(makeMinimal());
        add(makeExtend());
        validate();
    }

    private JXCollapsiblePane makeExtend() {
        extend.setLayout(new GridLayout(3, 1));
        extend.add(clientNameLabel);
        extend.add(clientAddressLabel);
        extend.add(clientPhoneNumLabel);
        extend.getContentPane().setBackground(MINIMAL_BG_OVER_COLOR);
        return extend;
    }

    private JPanel makeMinimal() {
        minimal.setBackground(MINIMAL_BG_COLOR);
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
                if (extend.isCollapsed() && parent.getSelected() != that) {
                    that.minimal.setBackground(MINIMAL_BG_OVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (extend.isCollapsed() && parent.getSelected() != that) {
                    that.minimal.setBackground(MINIMAL_BG_COLOR);
                }
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
        if (!extend.isCollapsed() && parent.getSelected() != this) {
            minimal.setBackground(MINIMAL_BG_UNCOLLAPSED_COLOR);
        }
    }

    private void updateToggleButton() {
        if (folded) {
            toggleButton.setText(ARROW_DOWN);
        } else {
            toggleButton.setText(ARROW_UP);
        }
    }

    public void onEventSelect() {
        minimal.setBackground(MINIMAL_BG_SELECT_COLOR);

        parent.setSelected(this);

    }

    public void select() {
        minimal.setBackground(MINIMAL_BG_SELECT_COLOR);

        parent.setSelected(this);

        parent.fireChangeEvent();

    }

    public void unselect() {
        if (!extend.isCollapsed()) {
            minimal.setBackground(MINIMAL_BG_UNCOLLAPSED_COLOR);
        } else {
            minimal.setBackground(MINIMAL_BG_COLOR);
        }
    }

    public Delivery getDelivery() {
        return delivery;
    }
}
