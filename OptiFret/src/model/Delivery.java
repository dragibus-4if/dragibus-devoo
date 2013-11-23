package model;

public class Delivery {

    private Long id;
    private Long address;
    private TimeSlot timeSlot;
    private Client client;

    public Delivery() {
    }

    public Delivery(long id) {
        this(new Long(id));
    }

    public Delivery(Long id) {
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
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Delivery ")
                .append(this.getId())
                .append(" for \n")
                .append(this.getClient())
                .append("\nto adresse ")
                .append(this.getAddress());
        return sb.toString();

    }
}
