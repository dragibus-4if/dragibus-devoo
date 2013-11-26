package config;

/**
 *
 * @author jmcomets
 */
public interface Entry {

    /**
     * Accéder à la valeur d'un attribut nommé.
     *
     * @param name
     * @return la valeur de l'attribut
     * @throws config.MissingAttributeException si l'attribut n'existe pas
     */
    String getValue(String name) throws MissingAttributeException;
}
