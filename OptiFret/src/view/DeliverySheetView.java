package view;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 *
 * @author jmcomets
 */
public class DeliverySheetView extends JFrame {

    private JMenuBar bar = new JMenuBar();
    private JMenu menuFile, menuEdit;
    private JMenuItem loadRound;
    private JMenuItem loadMap;
    private JMenuItem exportRound;
    private JMenuItem undo;
    private JMenuItem redo;
    private DeliverySheetFrame deliverySheetFrame;
    private DeliveryCollapsiblePane test;
    
    public DeliverySheetView() {
        super("Optifret");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        Container cont = getContentPane();
        setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
        deliverySheetFrame = new DeliverySheetFrame();
        add(deliverySheetFrame, BorderLayout.CENTER);
        
        //TEST TEST TEST
        JScrollBar contentTest = new JScrollBar();
        test = new DeliveryCollapsiblePane();
        //contentTest.add(test);
        //contentTest.setVisible(true);
        test.setVisible(true);
        add(test, BorderLayout.CENTER);
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
        redo = new JMenuItem("Redo");
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

}
