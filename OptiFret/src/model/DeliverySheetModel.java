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
        
        List<Delivery> delv = deliveryRound.getDeliveries(); 
        List<RoadNode> path = deliveryRound.getPath();

        int index_delv = 1;
        RoadNode old = null;
        
        for (RoadNode  liv : path) {
            if(old == null) {
                old = liv;
                continue;
            }
            //Prochaine livraison à effectuer
            Iterator secI = old.getSections().iterator();
            
            while (secI.hasNext()) {
                
                RoadSection rs = (RoadSection)secI.next();
                
                if (rs.getRoadNodeEnd() == liv) {
                    output.write("Prendre la rue ");
                    output.write(rs.getRoadName()
                            +"\n");
                    break;
                }
                
                
            }                
            
            if(liv.getId() == delv.get(index_delv).getAddress()) {
                output.write("\n\nArrivée à la livraison : \n");
                output.write(liv.getId().intValue() + ".");
                output.write(((RoadSection)secI.next()).getRoadName());

                //Passage au point de livraison suivant
                output.write("\n***\n");
                
                index_delv++;
            }
            
         old = liv;
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
        this.deliveryEmployee = deliveryEmployee;
    }
}
