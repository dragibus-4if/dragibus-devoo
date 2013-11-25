package config;

/**
 *
 * @author jmcomets
 */
public class Helper {

    Entry entry;

    public Helper(Entry entry) {
        if (entry == null) {
            throw new NullPointerException("'entry' ne doit pas être null");
        }
        this.entry = entry;
    }

    public String getString(String attributeName, String defaultValue) {
        try {
            return entry.getValue(attributeName);
        } catch (MissingAttributeException e) {
            return defaultValue;
        }
    }

    public double getInteger(String attributeName, int defaultValue) {
        try {
            String str = entry.getValue(attributeName);
            return Integer.parseInt(str);
        } catch (NumberFormatException | MissingAttributeException e) {
        }
        return defaultValue;
    }

    public double getDouble(String attributeName, double defaultValue) {
        try {
            String str = entry.getValue(attributeName);
            return Double.parseDouble(str);
        } catch (NumberFormatException | MissingAttributeException e) {
        }
        return defaultValue;
    }
}
