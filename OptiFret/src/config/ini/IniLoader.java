package config.ini;

import config.Entry;
import config.Loader;
import config.MissingEntryException;
import java.io.FileReader;
import java.io.IOException;
import org.ini4j.Ini;

/**
 * Spécification du Loader pour une configuration sous forme de fichiers INI.
 */
public class IniLoader extends Loader {

    private static final String DEFAULT_FILENAME = "config.ini";
    private String filename;
    private final Ini ini = new Ini();

    /**
     * Constructeur par défaut, utilisant le {@code DEFAULT_FILENAME}.
     */
    public IniLoader() {
        this(DEFAULT_FILENAME);
    }

    /**
     * Constructeur prenant le nom du fichier à charger.
     * 
     * @param filename 
     */
    public IniLoader(String filename) {
        this.filename = filename;
    }

    @Override
    public void doLoad() throws IOException {
        ini.load(new FileReader(filename));
    }

    @Override
    public Entry getEntry(String name) throws MissingEntryException {
        Ini.Section section = ini.get(name);
        if (section == null) {
            throw new MissingEntryException();
        }
        return new IniEntry(section);
    }

}
