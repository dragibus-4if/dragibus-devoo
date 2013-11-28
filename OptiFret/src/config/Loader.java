package config;

import java.io.IOException;

/**
 * Loader de configuration, gérant l'accés spécifique aux entrées de la
 * configuration et l'interfaçage pour le chargement.
 *
 * @author Jean-Marie
 */
public abstract class Loader {

    private boolean loaded = false;

    /**
     * Accesseur de l'état de chargement de la configuration
     * 
     * @return si la configuration a été chargée
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Chargement explicite de la configuration.
     */
    public void load() {
        try {
            doLoad();
            loaded = false;
        } catch (IOException e) {
            System.err.println("Loading failed...");
        }
    }

    /**
     * Méthode sensée réellement faire le chargement de la configuration en
     * gérant une erreur éventuelle par une IOException.
     *
     * @throws IOException
     */
    public abstract void doLoad() throws IOException;

    /**
     * Accesseur récupérant une entrée nommé.
     * 
     * @param name
     * @return l'entrée associée au nom passé
     * @throws MissingEntryException si l'entrée demandée n'existe pas
     */
    public abstract Entry getEntry(String name) throws MissingEntryException;
}
