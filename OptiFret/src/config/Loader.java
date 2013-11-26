package config;

import java.io.IOException;

/**
 *
 * @author jmcomets
 */
public abstract class Loader {

    private static final String DEFAULT_CONFIG_FILENAME = "config.ini";
    private String filename;

    private boolean loaded;

    public Loader() {
        filename = DEFAULT_CONFIG_FILENAME;
        loaded = false;
    }

    public Loader(String filename) {
        this.filename = filename;
        loaded = false;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        if (filename == null) {
            throw new NullPointerException("configFilename ne peut pas être nul");
        }
        this.filename = filename;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void load() {
        try {
            doLoad();
            loaded = false;
        } catch (IOException e) {
            System.err.println("Loading failed...");
        }
    }

    public abstract void doLoad() throws IOException;

    public abstract Entry getEntry(String name) throws MissingEntryException;
}
