package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.KeyStroke;

/**
 *
 * @author jmcomets
 */
public class MainFrame extends JFrame {

    private JMenuBar bar;
    private JMenu menuFile, menuEdit;
    private JMenuItem loadRound;
    private JMenuItem loadMap;
    private JMenuItem exportRound;
    private JMenuItem undo;
    private JMenuItem redo;
    private DeliveryMap deliveryMap;
    private DeliveryList deliveryList;

    public MainFrame() {
        super("Optifret");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        setJMenuBar(makeMenu());
        add(makeDeliveryMap(), BorderLayout.CENTER);
        add(makeDeliveryList(), BorderLayout.WEST);
        deliveryList.setPreferredSize(new Dimension(deliveryList.getPreferredSize().width, deliveryMap.getMaxY() + 20));

    }

    private JMenuBar makeMenu() {
        bar = new JMenuBar();
        menuFile = new JMenu("Fichier");
        loadRound = new JMenuItem("Charger une tournée");
        loadMap = new JMenuItem("Charger un plan");
        exportRound = new JMenuItem("Exporter une tournée");
        menuFile.add(loadMap);
        menuFile.add(loadRound);
        menuFile.add(exportRound);
        bar.add(menuFile);

        menuEdit = new JMenu("Edition");
        undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK));
        redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        menuEdit.add(undo);
        menuEdit.add(redo);
        bar.add(menuEdit);
        return bar;
    }

    private Component makeDeliveryMap() {
        deliveryMap = new DeliveryMap();
        deliveryMap.setPreferredSize(new Dimension(600, getHeight()));
        deliveryMap.setVisible(true);
        return deliveryMap;
    }

    private Component makeDeliveryList() {
        deliveryList = new DeliveryList();
        deliveryList.setPreferredSize(new Dimension(200, getHeight()));
        return deliveryList;
    }

    public JMenuItem getLoadRound() {
        return loadRound;
    }

    public JMenuItem getLoadMap() {
        return loadMap;
    }

    public JMenuItem getExportRound() {
        return exportRound;
    }

    public JMenuItem getUndo() {
        return undo;
    }

    public JMenuItem getRedo() {
        return redo;
    }

    public DeliveryMap getDeliveryMap() {
        return deliveryMap;
    }

    public DeliveryList getDeliveryList() {
        return deliveryList;
    }

    public void showErrorMessage(String msg) {
        showMessageDialog(null, msg);
    }
}
