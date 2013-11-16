package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListModel;

public class DeliverySheetView extends JPanel {

	MapCanvas mapCanvas;
	DeliveryList deliveryList;
        
        
      
	public DeliverySheetView getPlannedList() {
		// TODO - implement DeliverySheetView.getPlannedList
		throw new UnsupportedOperationException();
	}

	public DeliveryList getNotPlannedList() {
		// TODO - implement DeliverySheetView.getNotPlannedList
		throw new UnsupportedOperationException();
	}

	
    private JMenu menuFile, menuEdit;
    private JMenuItem loadRound, loadMap, exportRound, undo, redo;
    private JFileChooser fc = new JFileChooser();

    public DeliverySheetView() {
        
        setSize(800, 600);
        Container cont = this.getParent();
        setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
        add(makeBothDeliveryLists(), BorderLayout.WEST);
        add(makeDeliveryMap(), BorderLayout.EAST);        
        // TODO remove this test data
        deliveryList.getPlanned().addElement("Serge le Lama");
        deliveryList.getPlanned().addElement("Petit papa Noël");
        deliveryList.getPlanned().addElement("Justine Bi-beurre");
        deliveryList.getNotPlanned().addElement("Toto");
        deliveryList.getNotPlanned().addElement("Martine");
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
        panel.add(makeDeliveryList(deliveryList.getPlanned()), gbc);
        gbc.weighty = 0.3;
        panel.add(makeDeliveryList(deliveryList.getNotPlanned()), gbc);
        
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
        panel.add(mapCanvas);
        return panel;
    }
    
    
  
}