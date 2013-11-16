package model;

import java.io.File;

public class DeliverySheetModel {

    private DeliveryRound deliveryRound = new DeliveryRound();
    private DeliveryEmployee deliveryEmployee = new DeliveryEmployee();

    public static DeliverySheetModel loadFromXML(File file) {
        // TODO - implement DeliverySheetModel.loadFromXML
        throw new UnsupportedOperationException();
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
