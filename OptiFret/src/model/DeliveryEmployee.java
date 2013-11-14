package model;

public class DeliveryEmployee {
    private Long id;

    public DeliveryEmployee() {
        this.id = new Long(-1);
    }

    public DeliveryEmployee(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
