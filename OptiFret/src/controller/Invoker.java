package controller;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 *
 * @author jmcomets
 */
class Invoker {
    private final Stack<Command> history;
    private final Stack<Command> redoneHistory;

    public Invoker() {
        this.history = new Stack<>();
        this.redoneHistory = new Stack<>();
    }

    /**
     * Permet d'exécuter une commande et de l'ajouter à l'historique.
     *
     * On gère ici les effets de bords éventuels : - dépassement de la taille de
     * l'historique (TODO) - vidage de l'historique des "redo"
     *
     * @param cmd
     */
    protected void executeCommand(Command cmd, boolean clearRedoHistory) {
        // Exécuter la commande et l'ajouter à l'historique
        cmd.execute();
        history.add(cmd);
        
        // Empêcher de revenir en avant dans l'historique
        if (clearRedoHistory) {
            redoneHistory.clear();
        }
    }

    /**
     * Surcharge spécifiant la valeur par défaut de {@literal clearRedoHistory}.
     *
     * @param cmd
     */
    protected void executeCommand(Command cmd) {
        executeCommand(cmd, true);
    }

    /**
     * Permet d'annuler une commande et de l'ajouter à l'historique des "redo".
     *
     * On gère ici les effets de bords éventuels : - dépassement de la taille de
     * l'historique des "redo" (TODO)
     *
     * @param cmd
     */
    protected void undoCommand(Command cmd) {
        cmd.undo();
        redoneHistory.add(cmd);
    }

    /**
     * Permet d'annuler la dernière commande et de l'ajouter à l'historique des
     * "redo".
     *
     * @throws EmptyStackException
     */
    protected void undoLastCommand() throws EmptyStackException {
        undoCommand(history.pop());
    }

    /**
     * Permet de refaire la dernière commande annulée et de l'ajouter à
     * l'historique des "undo".
     *
     * @throws EmptyStackException
     */
    protected void redoLastCommand() throws EmptyStackException {
        executeCommand(redoneHistory.pop(), false);
    }

    /**
     * Vide à la fois l'historique des "undo" et l'historique des "redo".
     */
    protected void clearAllHistory() {
        history.clear();
        redoneHistory.clear();
    }

    protected Stack<Command> getHistory() {
        return history;
    }

    protected Stack<Command> getRedoneHistory() {
        return redoneHistory;
    }
    
}
