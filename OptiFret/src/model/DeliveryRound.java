package model;

import java.util.ArrayList;
import java.util.List;

public class DeliveryRound {

    private List<Delivery> deliveries = new ArrayList<Delivery>();
    private List<RoadNode> path;

    public DeliveryRound() {
        // TODO - implement DeliveryRound.DeliveryRound
        throw new UnsupportedOperationException();
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    public List<RoadNode> getPath() {
        return path;
    }

    public void setPath(List<RoadNode> path) {
        this.path = path;
    }

}
