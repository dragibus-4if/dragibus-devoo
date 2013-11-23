package model;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public class DeliverySheetModel {

    private DeliveryRound deliveryRound = new DeliveryRound();
    private DeliveryEmployee deliveryEmployee = new DeliveryEmployee();

    public static DeliverySheetModel loadFromXML(Reader input) {
        // TODO - implement DeliverySheetModel.loadFromXML
        throw new UnsupportedOperationException();
    }

    public void export(Writer output) throws IOException {
        if(output == null)
            throw new NullPointerException();
        
        List<Delivery> delv = deliveryRound.getDeliveries(); 
        List<RoadNode> path = deliveryRound.getPath();
        
        if(path == null)
            return;

        int index_delv = 0;
        RoadNode old = null;
        
        String bufferRoad = "";      //Buffer de toute la route à effectuer
        
        for(RoadNode liv : path) {
            if(old == null) {
                old = liv;
                continue;
            }
            Iterator secI = old.getSections().iterator();
            RoadSection rs = null;
            while(secI.hasNext()) {
                rs = (RoadSection)secI.next();
                if(rs.getRoadNodeEnd() == liv) {
                    bufferRoad += "Prendre la rue ";
                    bufferRoad += rs.getRoadName(); 
                    bufferRoad += "\n";
                    break;
                }
            }
            if(rs == null)
                throw new RuntimeException();
            if(index_delv < delv.size() && liv.getId().equals(
                    delv.get(index_delv).getAddress())) {
                
                output.write("Prochaine livraison : ");
                output.write(rs.getRoadName() + "\n");
                output.write(bufferRoad);
                output.write("Arrivée à la livraison : ");
                output.write(rs.getRoadName());
                output.write("\n\n***\n\n");
                bufferRoad = "";
                index_delv++;
            }
            else if (index_delv == delv.size()) {
                output.write(bufferRoad);
                bufferRoad = "";
            }
            old = liv;
        }
        if(index_delv != delv.size()) { 
            // On est pas passé par toutes les livraisons
            throw new RuntimeException();
        }
        output.flush();
    }
    
    public DeliverySheetModel() {
    }
    
    public DeliveryRound getDeliveryRound() {
        return this.deliveryRound;
    }

    public DeliveryEmployee getDeliveryEmployee() {
        return deliveryEmployee;
    }

    public void setDeliveryEmployee(DeliveryEmployee deliveryEmployee) {
        if(deliveryEmployee == null)
            throw new NullPointerException();
        this.deliveryEmployee = deliveryEmployee;
    }
}
