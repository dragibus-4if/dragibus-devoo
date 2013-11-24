package model;

/**
 * Structure de données représentant un employé de l'entreprise de transport.
 *
 * <p>
 * Correspond actuellement à un simple ID, mais pourrait contenir plus
 * d'informations à l'avenir (nom de l'employé, numéro de téléphone,
 * etc...).</p>
 *
 * @author Jean-Marie
 */
public class DeliveryEmployee {

    /**
     * ID de l'employé
     */
    private Long id;
    public static final long DEFAULT_ID = 0;

    /**
     * Constructeur par défaut mettant l'id à {@code DEFAULT_ID}.
     */
    public DeliveryEmployee() {
        this(DEFAULT_ID);
    }

    /**
     * Constructeur prenant l'id de l'employé.
     *
     * @param id
     */
    public DeliveryEmployee(Long id) {
        this.id = id;
    }

    /**
     * Surcharge prenant un type primitif {@code long}.
     *
     * @param id
     */
    public DeliveryEmployee(long id) {
        this(new Long(id));
    }

    /**
     * Accesseur en lecture pour l'id de l'employé.
     *
     * @return l'id de l'employé
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Accesseur en écriture pour l'id de l'employé.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

}
