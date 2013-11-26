package config.ini;

import config.Entry;
import config.Loader;
import config.MissingEntryException;
import java.io.FileReader;
import java.io.IOException;
import org.ini4j.Ini;

/**
 *
 * @author jmcomets
 */
public class IniLoader extends Loader {

    private final Ini ini = new Ini();

    @Override
    public void doLoad() throws IOException {
        ini.load(new FileReader(getFilename()));
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
