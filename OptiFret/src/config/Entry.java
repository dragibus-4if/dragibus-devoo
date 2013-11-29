package config;

/**
 * Interface d'une entrée, correspondant à une pseudo-map de String vers String,
 * permettant de structurer la configuration sous forme d'entrées nommées.
 *
 * @author Jean-Marie
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
