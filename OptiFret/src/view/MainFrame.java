package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.KeyStroke;
import model.Client;
import model.Delivery;
import model.RoadNode;
import model.RoadSection;
import model.TimeSlot;

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
        deliveryList.setPreferredSize(new Dimension(deliveryList.getPreferredSize().width, deliveryMap.getMaxY()+20));

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
       // deliveryMap.updateNetwork(temp);
       // deliveryMap.updateDeliveryNodes(temp);
        deliveryMap.setPreferredSize(new Dimension(600, getHeight()));
        deliveryMap.setVisible(true);
        return deliveryMap;
    }

    private Component makeDeliveryList() {
        deliveryList = new DeliveryList();
        deliveryList.setDeliveries(generateDeliveries());
        deliveryList.setPreferredSize(new Dimension(200, getHeight()));
        return deliveryList;
    }

    // TODO remove this method
    private List<RoadNode> generateTestNetwork() {
        List<RoadNode> temp = new ArrayList<>();
        RoadNode rnTemp = new RoadNode(1);
        rnTemp.setX(10);
        rnTemp.setY(10);
        RoadNode rnTemp2 = new RoadNode(2);
        rnTemp2.setX(60);
        rnTemp2.setY(60);
        RoadSection sec = new RoadSection(rnTemp, rnTemp2, 1, 10);
        RoadSection sec2 = new RoadSection(rnTemp, rnTemp2, 1, 10);
        RoadSection sec3 = new RoadSection(rnTemp, rnTemp2, 1, 10);


//
//        RoadNode rnTemp3 = new RoadNode(3);
//        rnTemp3.setX(0);
//        rnTemp3.setY(20);
//        RoadNode rnTemp4 = new RoadNode(4);
//        rnTemp4.setX(20);
//        rnTemp4.setY(60);
//        RoadSection sec2 = new RoadSection(rnTemp3, rnTemp4, 1, 10);
//        RoadSection sec3 = new RoadSection(rnTemp2, rnTemp4, 1, 10);
//        RoadSection sec4 = new RoadSection(rnTemp4, rnTemp2, 1, 10);
//
        rnTemp.addNeighbor(sec);
        rnTemp.addNeighbor(sec2);
        rnTemp.addNeighbor(sec3);

//        rnTemp2.addNeighbor(sec3);
//        rnTemp3.addNeighbor(sec2);
//        rnTemp4.addNeighbor(sec4);
//
        temp.add(rnTemp);
        temp.add(rnTemp2);
//        temp.add(rnTemp3);
//        temp.add(rnTemp4);
        return temp;
    }

    // TODO remove this method
    private List<Delivery> generateDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int t = i / 24;
            deliveries.add(new Delivery((long) i, (long) (Math.random() * 100),
                    new TimeSlot(new Date(t), (long) 3600000),
                    new Client((long) i, "truc", "machin", "bidule")));
        }
        return deliveries;
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
