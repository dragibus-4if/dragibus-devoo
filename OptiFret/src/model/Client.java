package model;

/**
 * Structure de données représentant le client d'une livraison.
 *
 * <p>
 * Le chargement des livraisons via XML nous permet de définir la notion de
 * {@code Client}, correspondant aux informations personnelles de la personne
 * livrée au cours d'une livraison.</p>
 *
 * @author Jean-Marie
 */
public class Client {

    /**
     * ID du client
     */
    private Long id;
    private String phoneNum;
    private String address;
    private String name;

    public static final long DEFAULT_ID = 0;

    /**
     * Constructeur permettant de fournir toutes les informations.
     *
     * @param id
     * @param phoneNum
     * @param address
     * @param name
     */
    public Client(Long id, String phoneNum, String address, String name) {
        this.id = id;
        this.phoneNum = phoneNum;
        this.address = address;
        this.name = name;
    }

    /**
     * Constructeur par défaut mettant l'ID du client à {@code DEFAULT_ID}.
     */
    public Client() {
        this(DEFAULT_ID);
    }

    /**
     * Constructeur mettant l'ID du client à la valeur spécifiée.
     *
     * @param id
     */
    public Client(Long id) {
        if (id == null) {
            throw new NullPointerException("L'id d'un Client ne peut pas être null");
        }
        this.id = id;
    }

    /**
     * Surcharge prenant un type primitif {@code long}.
     *
     * @param id
     */
    public Client(long id) {
        this(new Long(id));
    }

    /**
     * Accesseur en lecture de l'id du client.
     *
     * @return l'id du client
     */
    public Long getId() {
        return id;
    }

    /**
     * Accesseur en écriture de l'id du client.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Accesseur en lecture du numéro de téléphone du client.
     *
     * @return le numéro de téléphone du client
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * Accesseur en écriture du numéro de téléphone du client.
     *
     * @param phoneNum
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * Accesseur en lecture de l'adresse du client.
     *
     * @return l'adresse du client
     */
    public String getAddress() {
        return address;
    }

    /**
     * Accesseur en écriture de l'adresse du client.
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Accesseur en lecture du nom du client.
     *
     * @return le nom du client
     */
    public String getName() {
        return name;
    }

    /**
     * Accesseur en écriture du nom du client.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Redéfinition de toString() pour un client.
     * 
     * @return la représentation String du client
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client ")
                .append(getId())
                .append(" ")
                .append(getName())
                .append(", phone : ")
                .append(getPhoneNum())
                .append(", adresse : ")
                .append(getAddress());
        return sb.toString();
    }

}
