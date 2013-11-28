package config;

/**
 * Manager de configuration, gérant l'accés global à la configuration.
 *
 * <p>
 * Cette classe est un {@literal Singleton}, pouvant recevoir une instance de
 * {@literal Loader} pour définir la politique de chargement de la
 * configuration. Une {@literal Entry} peut être récupérée directement après
 * avoir mis à jour le {@code Loader}.</p>
 *
 * @author Jean-Marie
 */
public class Manager {

    private static Manager instance = null;
    private Loader loader;

    /**
     * Permet d'accéder à l'instance globale du
     *
     * @return
     */
    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    /**
     * Constructeur par défaut en privé (singleton).
     */
    private Manager() {
        loader = null;
    }

    /**
     * Accesseur en lecture du Loader
     *
     * @return le loader associé
     */
    public Loader getLoader() {
        return loader;
    }

    /**
     * Accesseur en écriture du loader.
     *
     * @param loader
     * @throws NullPointerException si le loader est null
     */
    public void setLoader(Loader loader) {
        if (loader == null) {
            throw new NullPointerException("'loader' ne doit pas être null");
        }
        this.loader = loader;
    }

    /**
     * Accesseur de l'{@code Entry} nommée.
     *
     * <p>
     * Charge aussi la configuration en demandant au {@code Loader} de charger
     * si ce n'est pas déjà fait.</p>
     *
     * @param name
     * @return l'entrée associée au nom passé
     * @throws MissingEntryException si aucune entrée n'est associée à ce nom
     * @throws NullPointerException si le nom passé est null
     */
    public Entry getEntry(String name) throws MissingEntryException {
        if (loader == null) {
            throw new NullPointerException("'loader' ne doit pas être null");
        } else if (loader.isLoaded() == false) {
            loader.load();
        }
        return loader.getEntry(name);
    }
}
