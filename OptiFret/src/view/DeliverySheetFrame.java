package view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    private JMenuBar bar = new JMenuBar();
    private JMenu menuFile, menuEdit;
    private JMenuItem loadRound, loadMap, exportRound, undo, redo;
    private JFileChooser fc = new JFileChooser();
    

    public DeliverySheetFrame() {
        super("Optifret");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        Container cont = getContentPane();
        setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
        add(makeBothDeliveryLists(), BorderLayout.WEST);
        add(makeDeliveryMap(), BorderLayout.EAST);
        makeMenu();
        setJMenuBar(bar);
        
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
    
    
    private void makeMenu() {
        menuFile = new JMenu("Fichier");      
        loadRound = new JMenuItem("Charger une tournée");
        loadMap = new JMenuItem("Charger un plan");
        exportRound = new JMenuItem("Exporter une tournée");
        menuFile.add(loadMap);
        menuFile.add(loadRound);
        menuFile.add(exportRound);
        loadMap.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int ret = fc.showOpenDialog(null);
                if(ret == JFileChooser.APPROVE_OPTION)
                {
                    //loadNetwork(fc.getSelectedFile());
                }
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
        bar.add(menuFile);
               
        menuEdit = new JMenu("Edition");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");
        menuEdit.add(undo);
        menuEdit.add(redo);
        bar.add(menuEdit);
        
        
        
        
    }
}
