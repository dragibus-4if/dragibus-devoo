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
 *  Vue correspondant à l'affichage d'une livraison 
 *  sous la forme d'un bandeau déroulant
 * @author Quentin
 */
public class DeliveryView extends JPanel {

    private static final Color MINIMAL_BG_COLOR = new Color(0, 0, 0, 0);
    private static final Color MINIMAL_BG_SELECT_COLOR = new Color(0, 0, 0, 40);
    private static final Color MINIMAL_BG_LATE_COLOR = new Color(215, 20, 20, 40);
    private static final Color MINIMAL_BG_LATE_SELECT_COLOR = new Color(215, 20, 20, 80);
    private final Delivery delivery;
    private final boolean deliveryLate;
    private final JPanel minimal;
    private final JXCollapsiblePane extend;
    private final JLabel timeSlotHour;
    private final JLabel predHour;
    private final JLabel idDeliveryLabel;
    private final JLabel clientAddressLabel;
    private final JLabel clientPhoneNumLabel;
    private final JLabel clientNameLabel;
    private JButton toggleButton;
    private final DeliveryList parent;
    private boolean folded;
    private static final String ARROW_UP = "\u2191";
    private static final String ARROW_DOWN = "\u2193";

    /**
     * Constructeur utilisant une Delivery 
     * et la Deliverylist parente
     * 
     * @param delivery
     * @param parent 
     */
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
        timeSlotHour = new JLabel();
        timeSlotHour.setText("Tranche : " + delivery.getTimeSlot().getBegin().getHours() + "h" + delivery.getTimeSlot().getBegin().getMinutes());
        predHour = new JLabel();
        predHour.setText("Horaire prévu " + delivery.getPredTimeSlot().getBegin().getHours()+"h"+delivery.getPredTimeSlot().getBegin().getMinutes());

        deliveryLate = delivery.isLate();

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
        //Listerners de changement de d'état du JXCollapsiblePane
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

    /**
     * Permet de valider et repeindre la Deliverylist contenant la DeliveryView
     */
    private void repaintall() {
        this.parent.validate();
        this.parent.repaint();

    }
    /**
     * Méthode permettant d'initialiser le JXcollapsiblePane extend
     * Cette méthode ne devrait pas etre appeller autrement que par le constructeur
     * 
     * @see DeliveryView
     * @return JXCollapsiblePane - renvoie le JXcollapsiblePane extend initialisé
     */
    private JXCollapsiblePane makeExtend() {
        extend.setLayout(new GridLayout(5, 1));
        extend.add(clientNameLabel);
        extend.add(clientAddressLabel);
        extend.add(clientPhoneNumLabel);
        extend.add(timeSlotHour);
        extend.add(predHour);

        //Met en place le Background
        extend.setBackground(MINIMAL_BG_COLOR);
        extend.getContentPane().setBackground(MINIMAL_BG_COLOR);
        for (Component c : extend.getComponents()) {
            c.setBackground(MINIMAL_BG_COLOR);
        }
        extend.setBorder(BorderFactory.createLineBorder(Color.black));
        extend.validate();
        extend.repaint();
        return extend;
    }

    /**
     * Méthode permettant d'initialiser le JPanel minimal
     * Cette méthode ne devrait pas etre appeller autrement que par le constructeur
     * 
     * @return Jpanel - renvoie le JPanel minimal initialisé
     */
    private JPanel makeMinimal() {
        minimal.setBackground(MINIMAL_BG_COLOR);
        minimal.setOpaque(true);
        if (deliveryLate) {
            minimal.setBackground(MINIMAL_BG_LATE_COLOR);
        } else {
            minimal.setBackground(MINIMAL_BG_COLOR);
        }
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));

        toggleButton = new JButton();
        updateToggleButton();

        minimal.add(toggleButton);

        minimal.add(toggleButton);
        minimal.add(idDeliveryLabel);
        final DeliveryView that = this;
        //MouseListerner
        minimal.addMouseListener(new MouseListener() {
            @Override
            //Selection par clic
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
            //Mise en surbrillance lorsque la souris est au dessus
            public void mouseEntered(MouseEvent e) {
                that.minimal.setBorder(BorderFactory.createLineBorder(Color.gray, 1, false));
                repaintall();
            }
            @Override
            public void mouseExited(MouseEvent e) {
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
    /**
     * Déclenche le depliement ou repliement du JXCollapsiblePane extend
     */
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
    /**
     * Met à jour l'affichage du bouton de déroulement en fonction de l'état
     * du JXCollapsiblePane
     */
    private void updateToggleButton() {
        if (folded) {
            toggleButton.setText(ARROW_DOWN);
        } else {
            toggleButton.setText(ARROW_UP);
        }
        minimal.validate();
        minimal.repaint();
    }

    /**
     * Permet de séléctionner la DeliveryView sans envoyé de notification de changement
     * d'état aux listeners de la deliveryList parent
     */
    public void onEventSelect() {
        if(deliveryLate){
            minimal.setBackground(MINIMAL_BG_LATE_SELECT_COLOR);
        }
        else{
        minimal.setBackground(MINIMAL_BG_SELECT_COLOR);
        }
        repaintall();
        parent.setSelected(this);

    }
    /**
     * Permet de séléctionner la DeliveryView dans la DeliveryList.
     * en envoyant une notification de changement d'état au Listerners de la DeliveryList parent
     */
    public void select() {
        if(deliveryLate){
            minimal.setBackground(MINIMAL_BG_LATE_SELECT_COLOR);
        }
        else{
            minimal.setBackground(MINIMAL_BG_SELECT_COLOR);
        }
        repaintall();
        parent.setSelected(this);

        parent.fireChangeEvent();
    }
    /**
     * Permet de déselectionner la DeliveryView
     */
    public void unselect() {
        if(deliveryLate){
            minimal.setBackground(MINIMAL_BG_LATE_COLOR);
        }
        else{
        minimal.setBackground(MINIMAL_BG_COLOR);
        }
        repaintall();
    }

    public Delivery getDelivery() {
        return delivery;
    }
}
