package view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.WindowConstants;
import model.Client;
import model.Delivery;
import model.TimeSlot;

/**
 *
 * @author jmcomets
 */
public class CreateDeliveryDialog extends JDialog {
    
    private final long id;
    private final long address;
    private Delivery delivery;
    
    private final JSpinner clientId;
    private final JTextField clientName;
    private final JTextField clientPhoneNum;
    private final JButton createButton;
    private final JButton cancelButton;
    private final JSpinner timeSlotBegin;
    private final JSpinner timeSlotEnd;
    
    public CreateDeliveryDialog(Frame parent, long id, long address) {
        super(parent, true);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Créer une Livraison");
        setResizable(false);
        
        this.address = address;
        delivery = null;

        this.id = id;
        timeSlotBegin = new JSpinner();
        timeSlotEnd = new JSpinner();
        clientId = new JSpinner();
        clientName = new JTextField();
        clientPhoneNum = new JTextField();
        createButton = new JButton("Créer");
        cancelButton = new JButton("Annuler");

        setupConstraints();
        setupLayout();
        setupListeners();
    }

    private void setupLayout() {
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Panel créneau horaire
        JPanel timeSlotPanel = new JPanel();
        timeSlotPanel.setBorder(BorderFactory.createTitledBorder("Créneau Horaire"));
        timeSlotPanel.setLayout(new GridLayout(2, 2, 10, 5));
        // début
        timeSlotPanel.add(new JLabel("Début"));
        timeSlotPanel.add(timeSlotBegin);
        // début
        timeSlotPanel.add(new JLabel("Fin"));
        timeSlotPanel.add(timeSlotEnd);
        // layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
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
        gbc.gridy = 2;
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
        // Créneau horaire
        timeSlotBegin.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
        timeSlotBegin.setEditor(new JSpinner.DateEditor(timeSlotBegin, "HH:mm"));
        timeSlotEnd.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
        timeSlotEnd.setEditor(new JSpinner.DateEditor(timeSlotEnd, "HH:mm"));
    }

    private void setupListeners() {
        final CreateDeliveryDialog that = this;
        
        // créer
        createButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    delivery = validateDelivery();
                    setVisible(false);
                } catch (ValidationError ex) {
                    String message = ex.getMessage();
                    JOptionPane.showMessageDialog(that, "Erreur de validation",
                            message, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // annuler
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                that.setVisible(false);
                that.dispatchEvent(new WindowEvent(that, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    private Delivery validateDelivery() throws ValidationError {
        // Créneau horaire
        TimeSlot timeSlot;
        try {
            Date begin = ((SpinnerDateModel) timeSlotBegin.getModel()).getDate();
            Date end = ((SpinnerDateModel) timeSlotEnd.getModel()).getDate();
            begin.setDate(new Date().getDate());
            begin.setMonth(new Date().getMonth());
            begin.setYear(new Date().getYear());
            end.setDate(new Date().getDate());
            end.setMonth(new Date().getMonth());
            end.setYear(new Date().getYear());
            timeSlot = new TimeSlot(begin, end);
        } catch (IllegalArgumentException e) {
            throw new ValidationError("Créneau horaire invalide");
        }
        
        // Client
        Client client = new Client(((Number) clientId.getModel().getValue()).longValue());
        client.setName(clientName.getText());
        client.setPhoneNum(clientPhoneNum.getText());
        
        // Livraison
        Delivery retDelivery = new Delivery(id);
        retDelivery.setAddress(address);
        retDelivery.setClient(client);
        retDelivery.setTimeSlot(timeSlot);
        return retDelivery;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    private static class ValidationError extends Exception {
        
        public ValidationError(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        CreateDeliveryDialog cdd = new CreateDeliveryDialog(null, 5, 5);
        cdd.setVisible(true);
        System.out.println("delivery = " + cdd.getDelivery());
    }
}
