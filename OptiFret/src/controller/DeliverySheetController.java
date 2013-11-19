package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.swing.JFileChooser;
import model.DeliverySheetModel;
import model.RoadNetwork;
import view.DeliverySheetView;

public class DeliverySheetController {

    private Stack<DeliverySheetCommand> history = new Stack<>();
    private Stack<DeliverySheetCommand> redoneHistory = new Stack<>();
    private RoadNetwork roadNetwork = new RoadNetwork();
    private DeliverySheetModel deliverySheetModel;
    private DeliverySheetView deliverySheetView;

    public DeliverySheetController() {
    }

    public void loadRoadNetwork() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(deliverySheetView) == JFileChooser.APPROVE_OPTION) {
            roadNetwork.loadFromXML(fc.getSelectedFile());
        }
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
     *
     * @throws EmptyStackException s'il n'y a aucune commande à annuler.
     */
    private void undoLastCommand() throws EmptyStackException {
        undoCommand(history.pop());
    }

    private void setupViewListeners() {
        deliverySheetView.getLoadMap().addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                loadRoadNetwork();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public DeliverySheetModel getDeliverySheetModel() {
        return deliverySheetModel;
    }

    public DeliverySheetView getDeliverySheetView() {
        return deliverySheetView;
    }

    public void setDeliverySheetModel(DeliverySheetModel model) {
        if (model == null) {
            throw new NullPointerException("'model' ne doit pas être nul");
        }
        this.deliverySheetModel = model;
    }

    public void setDeliverySheetView(DeliverySheetView view) {
        if (view == null) {
            throw new NullPointerException("'view' ne doit pas être nul");
        }
        this.deliverySheetView = view;
        setupViewListeners();
    }

}
