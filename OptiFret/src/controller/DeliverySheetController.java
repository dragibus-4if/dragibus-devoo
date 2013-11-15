package controller;

import java.util.EmptyStackException;
import java.util.Stack;
import model.DeliverySheetModel;
import model.RoadNetwork;
import view.DeliverySheetView;

public class DeliverySheetController {

    private Stack<DeliverySheetCommand> history = new Stack<DeliverySheetCommand>();
    private Stack<DeliverySheetCommand> redoneHistory = new Stack<DeliverySheetCommand>();
    private RoadNetwork roadNetwork;
    private DeliverySheetModel deliverySheetModel;
    private DeliverySheetView deliverySheetView;

    public DeliverySheetController() {
        // TODO - implement DeliverySheet.loadRoadNetwork
        throw new UnsupportedOperationException();
    }

    public void loadRoadNetwork() {
        // TODO - implement DeliverySheet.loadRoadNetwork
        throw new UnsupportedOperationException();
    }

    public void loadDeliverySheet() {
        // TODO - implement DeliverySheet.loadDeliverySheet
        throw new UnsupportedOperationException();
    }

    /**
     * Permet d'exécuter une commande et de l'ajouter à l'historique.
     *
     * On gère ici les effets de bords éventuels : - dépassement de la taille de
     * l'historique (TODO) - vidage de l'historique des "redo"
     *
     * @param cmd
     */
    private void executeCommand(DeliverySheetCommand cmd) {
        cmd.execute();
        history.add(cmd);
        redoneHistory.clear();
    }

    /**
     * Permet d'annuler une commande et de l'ajouter à l'historique des "redo".
     *
     * On gère ici les effets de bords éventuels : - dépassement de la taille de
     * l'historique des "redo" (TODO)
     *
     * @param cmd
     */
    private void undoCommand(DeliverySheetCommand cmd) {
        cmd.undo();
        redoneHistory.add(cmd);
    }

    /**
     * Permet d'annuler la dernière commande et de l'ajouter à l'historique des
     * "redo".
     * @throws EmptyStackException s'il n'y a aucune commande à annuler.
     */
    private void undoLastCommand() throws EmptyStackException {
        undoCommand(history.pop());
    }

}
