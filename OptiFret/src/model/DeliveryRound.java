package model;

import java.util.ArrayList;
import java.util.List;

public class DeliveryRound {

    private List<Delivery> deliveries = new ArrayList<>();
    private List<RoadNode> path;

    public DeliveryRound() {
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
