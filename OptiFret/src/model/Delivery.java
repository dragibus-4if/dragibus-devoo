package model;

public class Delivery {

    private Long id;
    private Long address;
    private TimeSlot timeSlot;
    private Client client;

    public Delivery(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("'id' cannot be null");
        }
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getAddress() {
        return address;
    }

    public void setAddress(Long address) {
        this.address = address;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
