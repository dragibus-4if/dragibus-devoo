package model;

import java.io.File;

public class DeliverySheetModel {

    private DeliveryRound deliveryRound = new DeliveryRound();
    private DeliveryEmployee deliveryEmployee;

    public static DeliverySheetModel loadFromXML(File file) {
        // TODO - implement DeliverySheetModel.loadFromXML
        throw new UnsupportedOperationException();
    }

    public DeliverySheetModel() {
        // TODO - implement DeliverySheetModel.DeliverySheetModel
        throw new UnsupportedOperationException();
    }

    public DeliveryEmployee getDeliveryEmployee() {
        return deliveryEmployee;
    }

    public void setDeliveryEmployee(DeliveryEmployee deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
    }
}
