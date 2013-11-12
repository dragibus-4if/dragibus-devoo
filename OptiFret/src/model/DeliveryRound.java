package model;

import java.util.List;

/**
 *
 * @author jmcomets
 */
class DeliveryRound {

    private List<Delivery> deliveries;
    private RoadPath path;

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public RoadPath getPath() {
        return path;
    }
}
