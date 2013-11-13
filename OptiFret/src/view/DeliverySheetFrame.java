package view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

/**
 *
 * @author jmcomets
 */
public class DeliverySheetFrame extends JFrame {

    private DefaultListModel notPlanned = new DefaultListModel();
    private DefaultListModel planned = new DefaultListModel();
    private Canvas canvas = new Canvas();

    public DeliverySheetFrame() {
        super("Optifret");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setDefaultLookAndFeelDecorated(false);
        Container cont = getContentPane();
        setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
        add(makeBothDeliveryLists(), BorderLayout.WEST);
        add(makeDeliveryMap(), BorderLayout.EAST);
        
        // TODO remove this test data
        planned.addElement("Serge le Lama");
        planned.addElement("Petit papa Noël");
        planned.addElement("Justine Bi-beurre");
        notPlanned.addElement("Toto");
        notPlanned.addElement("Martine");
    }

    private Component makeBothDeliveryLists() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(150, getHeight()));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        int padding = 3;
        gbc.insets.set(padding, padding, padding, padding);
        gbc.weighty = 0.7;
        panel.add(makeDeliveryList(planned), gbc);
        gbc.weighty = 0.3;
        panel.add(makeDeliveryList(notPlanned), gbc);
        return panel;
    }

    private JList makeDeliveryList(ListModel model) {
        JList list = new JList(model);
        list.setBorder(BorderFactory.createEtchedBorder());
        return list;
    }

    private Component makeDeliveryMap() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(650, getHeight()));
        panel.add(canvas);
        return panel;
    }
}
