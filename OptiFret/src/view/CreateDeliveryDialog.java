package view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author jmcomets
 */
public class CreateDeliveryDialog extends JDialog {
    private final JSpinner id;
    private final JComboBox address;
    private final JSpinner clientId;
    private final JTextField clientName;
    private final JTextField clientPhoneNum;
    private final JButton createButton;
    private final JButton cancelButton;
    private final JSpinner timeSlotHours;
    private final JSpinner timeSlotMinutes;

    public CreateDeliveryDialog(Frame parent, boolean modal) {
        super(parent, modal);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Créer une Livraison");
        setModal(true);
        setResizable(false);

        id = new JSpinner();
        address = new JComboBox();
        clientId = new JSpinner();
        clientName = new JTextField();
        clientPhoneNum = new JTextField();
        createButton = new JButton("Créer");
        cancelButton = new JButton("Annuler");
        timeSlotHours = new JSpinner();
        timeSlotMinutes = new JSpinner();
        
        setupConstraints();
        setupLayout();
    }
    
    private void setupLayout() {
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Panel livraison
        JPanel deliveryPanel = new JPanel();
        deliveryPanel.setBorder(BorderFactory.createTitledBorder("Livraison"));
        deliveryPanel.setLayout(new GridLayout(2, 2, 10, 5));
        // id
        JLabel idLabel = new JLabel("ID");
        deliveryPanel.add(idLabel);
        deliveryPanel.add(id);
        // adresse
        JLabel addressLabel = new JLabel("Adresse");
        deliveryPanel.add(addressLabel);
        deliveryPanel.add(address);
        // layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        getContentPane().add(deliveryPanel, gbc);
        
        // Panel créneau horaire
        JPanel timeSlotPanel = new JPanel();
        timeSlotPanel.setBorder(BorderFactory.createTitledBorder("Créneau Horaire"));
        timeSlotPanel.setLayout(new GridLayout(2, 2, 10, 5));
        // heures
        JLabel hoursLabel = new JLabel("Heures");
        timeSlotPanel.add(hoursLabel);
        timeSlotPanel.add(timeSlotHours);
        // minutes
        JLabel minutesLabel = new JLabel("Minutes");
        timeSlotPanel.add(minutesLabel);
        timeSlotPanel.add(timeSlotMinutes);
        // layout
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        getContentPane().add(timeSlotPanel, gbc);

        // Panel client
        JPanel clientPanel = new JPanel();
        clientPanel.setBorder(BorderFactory.createTitledBorder("Client"));
        clientPanel.setLayout(new GridLayout(3, 2, 10, 5));
        // id du client
        JLabel clientIdLabel = new JLabel("ID");
        clientPanel.add(clientIdLabel);
        clientPanel.add(clientId);
        // nom du client
        JLabel clientNameLabel = new JLabel("Nom");
        clientPanel.add(clientNameLabel);
        clientPanel.add(clientName);
        // téléphone
        JLabel clientPhoneNumLabel = new JLabel("Téléphone");
        clientPanel.add(clientPhoneNumLabel);
        clientPanel.add(clientPhoneNum);
        // layout
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        getContentPane().add(clientPanel, gbc);

        // Boutons
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 1;
        getContentPane().add(createButton, gbc);
        gbc.gridx = 2;
        getContentPane().add(cancelButton, gbc);

        pack();
    }

    private void setupConstraints() {
        // TODO
    }
    
    public static void main(String [] args) {
        new CreateDeliveryDialog(null, true).setVisible(true);
    }
}
