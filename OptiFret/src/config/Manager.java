package config;

/**
 *
 * @author jmcomets
 */
public class Manager {
    private static Manager instance = null;
    private Loader loader;
    
    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }
    
    private Manager() {
        loader = null;
    }

    public Loader getLoader() {
        return loader;
    }

    public void setLoader(Loader loader) {
        if (loader == null) {
            throw new NullPointerException("'loader' ne doit pas être null");
        }
        this.loader = loader;
    }
    
    public Entry registerEntry(String name) throws MissingEntryException {
        if (loader == null) {
            throw new NullPointerException("'loader' ne doit pas être null");
        } else if (loader.isLoaded() == false) {
            loader.load();
        }
        return loader.getEntry(name);
    }
}
