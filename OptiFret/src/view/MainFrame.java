/*
 */
package view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import model.RoadNode;
import model.RoadSection;

/**
 *
 * @author jmcomets
 */
public class MainFrame extends JFrame {

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
