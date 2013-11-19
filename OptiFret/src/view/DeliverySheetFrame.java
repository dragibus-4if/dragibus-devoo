package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author jmcomets
 */
public class DeliverySheetFrame extends JFrame {

  
    private JMenuBar bar = new JMenuBar();
    private JMenu menuFile, menuEdit;
    private JMenuItem loadRound, loadMap, exportRound, undo, redo;
    private JFileChooser fc = new JFileChooser();
    private DeliverySheetView deliverySheetView;
    public DeliverySheetFrame() {
        super("Optifret");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        Container cont = getContentPane();
        deliverySheetView=new DeliverySheetView();
        setLayout(new BoxLayout(cont, BoxLayout.X_AXIS));
        add(deliverySheetView, BorderLayout.CENTER);
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
        loadMap.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int ret = fc.showOpenDialog(null);
                if(ret == JFileChooser.APPROVE_OPTION)
                {
                    
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
