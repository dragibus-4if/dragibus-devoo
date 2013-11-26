package controller;

/**
 *
 * @author jmcomets
 */
abstract class Command {

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public abstract void execute();

    public abstract void undo();

    public String getName() {
        return name;
    }
}
