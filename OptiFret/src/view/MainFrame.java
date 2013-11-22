/*
 */
package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import model.RoadNode;
import model.RoadSection;

/**
 *
 * @author jmcomets
 */
public class MainFrame extends JFrame {

    private JMenuBar bar = new JMenuBar();
    private JMenu menuFile, menuEdit;
    private JMenuItem loadRound;
    private JMenuItem loadMap;
    private JMenuItem exportRound;
    private JMenuItem undo;
    private JMenuItem redo;
    private DeliveryMap deliveryMap;
    private DeliveryCollapsiblePane deliveryCollapsiblePane;

    public MainFrame() {
        super("Optifret");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        Container cont = getContentPane();
        setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
        deliveryMap = new DeliveryMap();
        add(deliveryMap, BorderLayout.EAST);

        //TEST TEST TEST
        JScrollBar scrollbar = new JScrollBar();
        deliveryCollapsiblePane = new DeliveryCollapsiblePane();
        //contentTest.add(test);
        //contentTest.setVisible(true);
        deliveryCollapsiblePane.setVisible(true);
        add(deliveryCollapsiblePane, BorderLayout.CENTER);
        //contentTest.add(test, BoxLayout.LINE_AXIS);
        makeMenu();
        setJMenuBar(bar);
    }

    private void makeMenu() {
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

    private List<RoadNode> generateTestNetwork() {
        List<RoadNode> temp = new ArrayList<>();
        RoadNode rnTemp = new RoadNode(1);
        rnTemp.setX(10);
        rnTemp.setY(10);
        RoadNode rnTemp2 = new RoadNode(2);
        rnTemp2.setX(60);
        rnTemp2.setY(60);
        RoadSection sec = new RoadSection(rnTemp, rnTemp2, 1, 10);

        RoadNode rnTemp3 = new RoadNode(3);
        rnTemp3.setX(0);
        rnTemp3.setY(20);
        RoadNode rnTemp4 = new RoadNode(4);
        rnTemp4.setX(20);
        rnTemp4.setY(60);
        RoadSection sec2 = new RoadSection(rnTemp3, rnTemp4, 1, 10);
        RoadSection sec3 = new RoadSection(rnTemp2, rnTemp4, 1, 10);
        RoadSection sec4 = new RoadSection(rnTemp4, rnTemp2, 1, 10);

        rnTemp.addNeighbor(sec);
        rnTemp2.addNeighbor(sec3);
        rnTemp3.addNeighbor(sec2);
        rnTemp4.addNeighbor(sec4);

        temp.add(rnTemp);
        temp.add(rnTemp2);
        temp.add(rnTemp3);
        temp.add(rnTemp4);
        return temp;
    }

    private Component makeDeliveryMap() {
        /*JPanel panel = new JPanel();
         panel.setPreferredSize(new Dimension(650, getHeight()));
         mapCanvas = new MapCanvas(generateTestNetwork());
         mapCanvas.setBounds(0, 0, 650, getHeight());
         panel.add(mapCanvas);
         return panel;*/
        return null;
    }
}
