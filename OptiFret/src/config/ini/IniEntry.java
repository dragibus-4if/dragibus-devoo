package config.ini;

import config.Entry;
import config.MissingAttributeException;
import org.ini4j.Ini;

/**
 *
 * @author jmcomets
 */
public class IniEntry implements Entry {
    
    Ini.Section section;

    public IniEntry(Ini.Section section) {
        if (section == null) {
            throw new NullPointerException("'section' ne doit pas être null");
        }
        this.section = section;
    }

    @Override
    public String getValue(String name) throws MissingAttributeException {
        String value = section.get(name);
        if (value == null) {
            throw new MissingAttributeException();
        }
        return value;
    }

}
