package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListModel;
import model.RoadNode;
import model.RoadSection;

public class DeliverySheetView extends JPanel {

    MapCanvas mapCanvas;
    DeliveryList deliveryList;
              
    public DefaultListModel getPlannedList() {
            return deliveryList.getPlanned();
    }

    public DefaultListModel getNotPlannedList() {
            return deliveryList.getNotPlanned();
    }
    
    private JMenu menuFile, menuEdit;
    private JMenuItem loadRound, loadMap, exportRound, undo, redo;
    private JFileChooser fc = new JFileChooser();

    public DeliverySheetView() {
        
        setSize(800, 600);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
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
        deliveryList=new DeliveryList();
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

       //Test Method 
    private List<RoadNode> generateTestNetwork(){
        List<RoadNode> temp=new ArrayList<>();
      //  for(int i = 0;i<10;i++){
            RoadNode rnTemp=new RoadNode(1);
            rnTemp.setX(10);
            rnTemp.setY(10);
            RoadNode rnTemp2=new RoadNode(2);
            rnTemp2.setX(60);
            rnTemp2.setY(60);
            RoadSection sec=new RoadSection(rnTemp, rnTemp2, 1, 10);
       
            RoadNode rnTemp3=new RoadNode(3);
            rnTemp3.setX(0);
            rnTemp3.setY(20);
            RoadNode rnTemp4=new RoadNode(4);
            rnTemp4.setX(20);
            rnTemp4.setY(60);
            RoadSection sec2=new RoadSection(rnTemp3, rnTemp4, 1, 10);
            RoadSection sec3=new RoadSection(rnTemp2, rnTemp4, 1, 10);
            RoadSection sec4=new RoadSection(rnTemp4, rnTemp2, 1, 10);

            rnTemp.addNeighbor(sec);
            rnTemp2.addNeighbor(sec3);
            rnTemp3.addNeighbor(sec2);
            rnTemp4.addNeighbor(sec4);

            temp.add(rnTemp);
            temp.add(rnTemp2);
            temp.add(rnTemp3);
            temp.add(rnTemp4);
            
            
      //  }
        return temp;
    }
    
    private Component makeDeliveryMap() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(650, getHeight()));
        mapCanvas=new MapCanvas(generateTestNetwork());
        mapCanvas.setBounds(0,0,650,getHeight());
        panel.add(mapCanvas);
        return panel;
    }
    
    
  
}