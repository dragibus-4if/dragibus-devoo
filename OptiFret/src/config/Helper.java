package config;

/**
 * Classe utilitaire permettant de convertir facilement les attributs d'une
 * entrée.
 * 
 * @author Jean-Marie
 */
public class Helper {

    Entry entry;

    /**
     * Constructeur prenant l'entrée avec laquelle il faut travailler.
     * 
     * @param entry 
     * @throws NullPointerException si l'entrée passée est null
     */
    public Helper(Entry entry) {
        if (entry == null) {
            throw new NullPointerException("'entry' ne doit pas être null");
        }
        this.entry = entry;
    }

    /**
     * Accesseur en lecture de l'entrée courante.
     * 
     * @return l'entrée courante
     */
    public Entry getEntry() {
        return entry;
    }

    /**
     * Accesseur en écriture de l'entrée courante.
     * 
     * @param entry 
     * @throws NullPointerException si l'entrée passée est null
     */
    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    /**
     * Récupérer une String.
     * 
     * @param attributeName
     * @return l'attribut demandé
     * @throws MissingAttributeException 
     */
    public String getString(String attributeName) throws MissingAttributeException {
        return entry.getValue(attributeName);
    }

    /**
     * Récupérer une String en prenant une valeur par défaut.
     * 
     * @param attributeName
     * @param defaultValue
     * @return l'attribut demandé
     */
    public String getString(String attributeName, String defaultValue) {
        try {
            return entry.getValue(attributeName);
        } catch (MissingAttributeException e) {
            return defaultValue;
        }
    }

    /**
     * Récupérer un int en prenant une valeur par défaut.
     * 
     * @param attributeName
     * @param defaultValue
     * @return l'attribut demandé sous forme d'int
     */
    public int getInteger(String attributeName, int defaultValue) {
        try {
            String str = entry.getValue(attributeName);
            return Integer.parseInt(str);
        } catch (NumberFormatException | MissingAttributeException e) {
        }
        return defaultValue;
    }

    /**
     * Récupérer un double en prenant une valeur par défaut.
     * 
     * @param attributeName
     * @param defaultValue
     * @return l'attribut demandé sous forme de double
     */
    public double getDouble(String attributeName, double defaultValue) {
        try {
            String str = entry.getValue(attributeName);
            return Double.parseDouble(str);
        } catch (NumberFormatException | MissingAttributeException e) {
        }
        return defaultValue;
    }
}
