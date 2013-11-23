package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.swing.JFileChooser;
import model.Delivery;
import model.DeliveryRound;
import model.DeliverySheet;
import model.RoadNetwork;
import view.MainFrame;

public class MainController {

    private Stack<DeliverySheetCommand> history = new Stack<>();
    private Stack<DeliverySheetCommand> redoneHistory = new Stack<>();
    private RoadNetwork roadNetwork;
    private DeliverySheet deliverySheetModel;
    private MainFrame mainFrame;

    public MainController(MainFrame frame) {
        if (frame == null) {
            throw new NullPointerException("'view' ne doit pas être nul");
        }
        this.mainFrame = frame;
        setupNewView();
    }

    private void loadRoadNetwork() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                roadNetwork = RoadNetwork.loadFromXML(new FileReader(fc.getSelectedFile()));
                mainFrame.getLoadRound().setEnabled(true);
                mainFrame.getDeliveryMap().clearMap();
                mainFrame.getDeliveryMap().updateNetwork(roadNetwork.getNodes());
                mainFrame.getDeliveryList().setDeliveries(new ArrayList<Delivery>());
                mainFrame.repaint();
            } catch (IOException e) {
                mainFrame.showErrorMessage(e.getMessage());
            }
        }
    }

    private void loadDeliverySheet() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                deliverySheetModel = DeliverySheet.loadFromXML(new FileReader(fc.getSelectedFile()));
                DeliveryRound dr = deliverySheetModel.getDeliveryRound();
                mainFrame.getDeliveryList().setDeliveries(dr.getDeliveries());
                mainFrame.getExportRound().setEnabled(true);             
                //mainFrame.getDeliveryMap().updateDeliveryNodes(roadNetwork.makeRoute(dr.getPath()));
            } catch (IOException e) {
                mainFrame.showErrorMessage(e.getMessage());
            }
        }
    }

    private void exportRound() {
        // TODO save as dialog
    }

    /**
     * Permet d'annuler la dernière commande et de l'ajouter à l'historique des
     * "redo".
     *
     * @throws EmptyStackException
     */
    private void undoLastCommand() throws EmptyStackException {
        undoCommand(history.pop());
        mainFrame.getRedo().setEnabled(true);
        if (history.size() == 0) {
            mainFrame.getUndo().setEnabled(false);
        }
    }

    /**
     * Permet de refaire la dernière commande annulée et de l'ajouter à
     * l'historique des "undo".
     *
     * @throws EmptyStackException
     */
    private void redoLastCommand() throws EmptyStackException {
        executeCommand(redoneHistory.pop());
        mainFrame.getUndo().setEnabled(true);
        if (redoneHistory.size() == 0) {
            mainFrame.getRedo().setEnabled(false);
        }
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

    private void setupNewView() {
        // Historique
        history.clear();
        mainFrame.getUndo().setEnabled(false);
        mainFrame.getRedo().setEnabled(false);
        mainFrame.getLoadRound().setEnabled(false);
        mainFrame.getExportRound().setEnabled(false);
        
        // Listeners
        setupViewListeners();
        loadRoadNetwork();
        loadDeliverySheet();
    }

    private void setupViewListeners() {
        // "charger la carte"
        mainFrame.getLoadMap().addMouseListener(new MenuItemClickListener() {

            @Override
            public void click() {
                loadRoadNetwork();
            }
        });

        // "charger des demandes de livraison"
        mainFrame.getLoadRound().addMouseListener(new MenuItemClickListener() {

            @Override
            public void click() {
                loadDeliverySheet();
            }
        });

        // "exporter l'itinéraire"
        mainFrame.getExportRound().addMouseListener(new MenuItemClickListener() {

            @Override
            public void click() {
                exportRound();
            }
        });

        // "undo"
        mainFrame.getUndo().addMouseListener(new MenuItemClickListener() {

            @Override
            public void click() {
                undoLastCommand();
            }
        });

        // "redo"
        mainFrame.getRedo().addMouseListener(new MenuItemClickListener() {

            @Override
            public void click() {
                redoLastCommand();
            }
        });
    }

    private abstract class MenuItemClickListener implements MouseListener {

        /**
         * Méthode abstraite correspondant au click sur un item du menu
         */
        public abstract void click();

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            click();
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

    }

}
