package model;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public class DeliverySheetModel {
    
    
    //Quelques const pour définir les angles de "tout droit", "a droite"...
    private final double coneFront;
    private final double coneBack;

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
        RoadSection oldRs = null;
        
        String bufferRoad = "";      //Buffer de toute la route à effectuer
        
        for(RoadNode liv : path) {
            if(old == null) {
                old = liv;
                continue;
            }
            Iterator secI = old.getSections().iterator();
            RoadSection rs = null;
            
            int gauche = 0;
            int droite = 0;
            
            while(secI.hasNext()) {
                rs = (RoadSection)secI.next();
                if(rs.getRoadNodeEnd() == liv) {
                    
                    //Première rue à prendre
                    if ( oldRs == null ) { 
                        bufferRoad += "Prendre la rue ";
                        bufferRoad += rs.getRoadName(); 
                        bufferRoad += "\n\n";
                    }
                    //Calcul du "Prenez à gauche/droite, sur"
                    else {
                        //Calcul de la longeur
                        bufferRoad += "Dans ";
                        bufferRoad += (int) rs.getLength();
                        bufferRoad += " mètres : \n";
                        
                        int v1X = oldRs.getRoadNodeEnd().getX() - 
                                oldRs.getRoadNodeBegin().getX();
                        int v1Y = oldRs.getRoadNodeEnd().getY() - 
                                oldRs.getRoadNodeBegin().getY();
                        int v2X = rs.getRoadNodeEnd().getX() - 
                                rs.getRoadNodeBegin().getX();
                        int v2Y = rs.getRoadNodeEnd().getY() - 
                                rs.getRoadNodeBegin().getY();
                        
                        double angle1 = Math.atan2(v1Y,v1X);
                        double angle2 = Math.atan2(v2Y,v2X);
                        double angle = angle2 - angle1;
                        
                        if ( angle < -coneFront && angle > -coneBack) {
                            bufferRoad += "Prenez à gauche ";
                        }
                        else if (angle > coneFront && angle < coneBack) {
                            bufferRoad += "Prenez à droite ";
                        }
                        else if (angle >= coneBack  ||
                                angle <= -coneBack) {
                            bufferRoad += "Faites demi-tour ";
                        }
                        else {
                            bufferRoad += "Prenez tout droit ";
                        }
                           
                        System.out.println(rs.getRoadName() + angle);
                        
                        bufferRoad += "sur la rue ";
                        bufferRoad += rs.getRoadName(); 
                        bufferRoad += "\n\n";
                    }
                    
                    oldRs = rs;
                    break;
                }
            }
            if(rs == null)
                throw new RuntimeException();
            if(index_delv < delv.size() && liv.getId().equals(
                    delv.get(index_delv).getAddress())) {
                
                output.write("Prochaine livraison : ");
                output.write(rs.getRoadName() + "\n\n");
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
        this.coneBack = Math.PI*(1.0-1.0/20.0);
        this.coneFront = Math.PI/20.0;
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
