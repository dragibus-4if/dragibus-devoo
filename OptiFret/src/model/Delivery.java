package model;

import java.util.Date;

/**
 * Modèle correspondant à une livraison
 *
 * <p>
 * Une livraison doit normalement être identifiée par son {@code id}, unique à
 * travers les livraisons de l'application. L'attribut {@code address} doit
 * correspondre à l'ID d'un {@code RoadNode}.</p>
 */
public class Delivery {

    private Long id;
    private Long address;
    private TimeSlot timeSlot;
    private Client client;

    /**
     * Constructeur par ID
     *
     * @param id
     * @throws NullPointerException si l'id passé est null
     */
    public Delivery(Long id) {
        if (id == null) {
            throw new NullPointerException("'id' ne doit pas être null");
        }
        this.id = id;
    }

    /**
     * Surcharge du constructeur par ID, ne lançant pas d'exception (en
     * théorie).
     *
     * @param id
     */
    public Delivery(long id) {
        this(new Long(id));
    }
    public boolean isLate(){
        if(timeSlot.getEnd().getTime()<(timeSlot.getBegin().getTime()+timeSlot.getDuration()))
        {
        return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * Constructeur par attributs, assignant tous les attributs d'un coup.
     *
     * @param id
     * @param address
     * @param timeSlot
     * @param client
     * @throws NullPointerException si l'id passé est null
     */
    public Delivery(Long id, Long address, TimeSlot timeSlot, Client client) {
        this(id);
        this.address = address;
        this.timeSlot = timeSlot;
        this.client = client;
    }

    /**
     * Accesseur en lecture de l'id de la livraison.
     *
     * @return l'id de la livraison
     */
    public Long getId() {
        return id;
    }

    /**
     * Accesseur en lecture de l'adresse de la livraison.
     *
     * @return l'adresse de la livraison
     */
    public Long getAddress() {
        return address;
    }

    /**
     * Accesseur en écriture de l'adresse de la livraison.
     *
     * @param address
     */
    public void setAddress(Long address) {
        this.address = address;
    }

    /**
     * Accesseur en lecture du créneau horaire de la livraison.
     *
     * @return le créneau horaire de la livraison
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Accesseur en écriture du créneau horaire de la livraison.
     *
     * @param timeSlot
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    /**
     * Accesseur en lecture du client de la livraison.
     *
     * @return le client de la livraison
     */
    public Client getClient() {
        return client;
    }

    /**
     * Accesseur en écriture du client de la livraison.
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Redéfinition de l'égalité entre deux livraisons, se fesant sur leur ID.
     *
     * @param obj
     * @return si les livraisons ont la même ID
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            return id.equals(((Delivery) obj).id);
        }
    }
    @Override
    public String toString() {
        return "Delivery{" + "id=" + id + ", address="
                + address + ", timeSlot=" + timeSlot
                + ", client=" + client + '}';
    }
}
