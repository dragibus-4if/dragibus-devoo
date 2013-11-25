package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 *
 * @author jmcomets
 */
public class MainFrame extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenu menuEdit;
    private JMenuItem loadRound;
    private JMenuItem loadMap;
    private JMenuItem exportRound;
    private JMenuItem undo;
    private JMenuItem redo;
    private JButton addDeliveryButton;
    private JButton delDeliveryButton;
    private DeliveryMap deliveryMap;
    private DeliveryList deliveryList;
    
    private static final int DELIVERY_MAP_WIDTH = 600;
    private static final int DELIVERY_LIST_WIDTH = 200;
    
    public static final String REDO_TOOLTIP = "Refaire";
    public static final String UNDO_TOOLTIP = "Annuler";
    public static final String MENU_EDIT_TOOLTIP = "Edition";
    public static final String EXPORT_ROUND_TOOLTIP = "Exporter une tournée";
    public static final String LOAD_MAP_TOOLTIP = "Charger un plan";
    public static final String MENU_FILE_TOOLTIP = "Fichier";
    public static final String LOAD_ROUND_TOOLTIP = "Charger une tournée";
    public static final String ADD_DELIVERY_TOOLTIP = "Ajouter";
    public static final String DEL_DELIVERY_TOOLTIP = "Supprimer";

    public MainFrame() {
        super("Optifret");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, DELIVERY_MAP_WIDTH);
        setLayout(new BorderLayout());
        setJMenuBar(makeMenu());
        add(makeDeliveryMap(), BorderLayout.CENTER);
        add(makeDeliveryList(), BorderLayout.LINE_START);
    }

    private JMenuBar makeMenu() {
        menuBar = new JMenuBar();
        menuFile = new JMenu(MENU_FILE_TOOLTIP);
        loadRound = new JMenuItem(LOAD_ROUND_TOOLTIP);
        loadMap = new JMenuItem(LOAD_MAP_TOOLTIP);
        exportRound = new JMenuItem(EXPORT_ROUND_TOOLTIP);
        menuFile.add(loadMap);
        menuFile.add(loadRound);
        menuFile.add(exportRound);
        menuBar.add(menuFile);

        menuEdit = new JMenu(MENU_EDIT_TOOLTIP);
        undo = new JMenuItem(UNDO_TOOLTIP);
        undo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK));
        redo = new JMenuItem(REDO_TOOLTIP);
        redo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        menuEdit.add(undo);
        menuEdit.add(redo);
        menuBar.add(menuEdit);
        return menuBar;
    }

    private Component makeDeliveryMap() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        deliveryMap = new DeliveryMap();
        deliveryMap.setPreferredSize(new Dimension(DELIVERY_MAP_WIDTH, getHeight()));
        addDeliveryButton = new JButton(ADD_DELIVERY_TOOLTIP);
        addDeliveryButton.setPreferredSize(new Dimension(100, 70));
        delDeliveryButton = new JButton(DEL_DELIVERY_TOOLTIP);
        addDeliveryButton.setPreferredSize(addDeliveryButton.getPreferredSize());
        panel.add(deliveryMap, gbc);
        panel.add(addDeliveryButton, gbc);
        panel.add(delDeliveryButton, gbc);
        return panel;
    }

    private Component makeDeliveryList() {
        deliveryList = new DeliveryList();
        deliveryList.setPreferredSize(new Dimension(DELIVERY_LIST_WIDTH, deliveryMap.getMaxY()));
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

    public JButton getAddDeliveryButton() {
        return addDeliveryButton;
    }

    public void showErrorMessage(String msg) {
        showMessageDialog(null, msg);
    }
}
