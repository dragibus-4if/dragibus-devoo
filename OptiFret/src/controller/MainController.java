package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.swing.JFileChooser;
import model.Delivery;
import model.DeliveryRound;
import model.DeliverySheet;
import model.RoadNetwork;
import model.RoadNode;
import view.DeliveryMap;
import view.Listener;
import view.MainFrame;
import view.MyChangeEvent;
import view.DeliveryList;

public class MainController implements Listener {

    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoneHistory = new Stack<>();
    private RoadNetwork roadNetwork;
    private DeliverySheet deliverySheet;
    private MainFrame mainFrame;

    public MainController(MainFrame frame) {
        if (frame == null) {
            throw new NullPointerException("'view' ne doit pas être nul");
        }
        this.mainFrame = frame;
        setupNewView();
        setupListeners();
    }

    private void setupListeners() {
        mainFrame.getDeliveryMap().addListener(this);
        mainFrame.getDeliveryList().addListener(this);
    }

    private void loadRoadNetwork() {
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(MainFrame.LOAD_MAP_TOOLTIP);
        if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                final RoadNetwork loadedNetwork = RoadNetwork.loadFromXML(new FileReader(fc.getSelectedFile()));
                executeCommand(new Command() {
                    // instance de RoadNetwork pour stocker l'etat courant
                    private RoadNetwork currentNetwork;
                    // instance de DeliverySheet pour stocker l'etat courant
                    private DeliverySheet currentDeliverySheet;

                    @Override
                    public void execute() {
                        // recuperer l'etat courant du network et de la listLivs
                        currentNetwork = roadNetwork;
                        currentDeliverySheet = deliverySheet;

                        roadNetwork = loadedNetwork;
                        mainFrame.getLoadRound().setEnabled(true);
                        mainFrame.getDeliveryMap()
                                .updateNetwork(roadNetwork.getNodes());
                        mainFrame.getDeliveryList()
                                .setDeliveries(new ArrayList<Delivery>());
                        mainFrame.pack();
                        mainFrame.repaint();
                    }

                    @Override
                    public void undo() {

                        roadNetwork = currentNetwork;
                        deliverySheet = currentDeliverySheet;

                        // verifier si un reseau a deja ete charge
                        if (roadNetwork == null) {
                            mainFrame.getLoadRound().setEnabled(false);
                            mainFrame.getDeliveryMap().updateNetwork(new ArrayList<RoadNode>());
                        } else {
                            mainFrame.getDeliveryMap().updateNetwork(
                                    roadNetwork.getNodes());
                        }

                        // verifier si une liste de livraisons a deja ete charge
                        if (deliverySheet == null) {
                            mainFrame.getDeliveryList().setDeliveries(null);
                        } else {
                            mainFrame.getDeliveryList().setDeliveries(
                                    deliverySheet
                                    .getDeliveryRound()
                                    .getDeliveries());
                        }
                        mainFrame.repaint();
                    }
                });
            } catch (IOException e) {
                mainFrame.showErrorMessage(e.getMessage());
            }
        }
    }

    private void loadDeliverySheet() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(MainFrame.LOAD_ROUND_TOOLTIP);
        if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                final DeliverySheet loadedDeliverySheet = DeliverySheet
                        .loadFromXML(new FileReader(fc.getSelectedFile()));

                executeCommand(new Command() {
                    private DeliverySheet currentDeliverySheet;

                    @Override
                    public void execute() {
                        // sauvegarder l'état courant de la liste de livraisons
                        currentDeliverySheet = deliverySheet;

                        deliverySheet = loadedDeliverySheet;
                        DeliveryRound dr = deliverySheet.getDeliveryRound();
                        mainFrame.getDeliveryList().setDeliveries(dr.getDeliveries());
                        mainFrame.getExportRound().setEnabled(true);
                        mainFrame.repaint();
                    }

                    @Override
                    public void undo() {
                        deliverySheet = currentDeliverySheet;

                        // verifier si un DeliverySheet a deja ete charge
                        if (deliverySheet == null) {
                            mainFrame.getDeliveryList().setDeliveries(new ArrayList<Delivery>());
                            mainFrame.getExportRound().setEnabled(false);
                        } else {
                            DeliveryRound dr = deliverySheet.getDeliveryRound();
                            mainFrame.getDeliveryList().setDeliveries(dr.getDeliveries());
                        }
                        mainFrame.repaint();
                    }
                });

                mainFrame.getDeliveryMap().updateDeliveryNodes(roadNetwork.getNodes());
            } catch (IOException e) {
                mainFrame.showErrorMessage(e.getMessage());
            }
        }
    }

    private void exportRound() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(MainFrame.EXPORT_ROUND_TOOLTIP);
        if (fc.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                deliverySheet.export(new FileWriter(fc.getSelectedFile()));
            } catch (IOException e) {
                mainFrame.showErrorMessage(e.getMessage());
            }
        }
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
    private void executeCommand(Command cmd) {
        cmd.execute();
        mainFrame.getUndo().setEnabled(true);
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
    private void undoCommand(Command cmd) {
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
        //loadRoadNetwork();
        //loadDeliverySheet();
    }

    private void setupViewListeners() {
        // "charger la carte"
        mainFrame.getLoadMap().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRoadNetwork();
            }
        });

        // "charger des demandes de livraison"
        mainFrame.getLoadRound().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDeliverySheet();
            }
        });

        // "exporter l'itinéraire"
        mainFrame.getExportRound().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportRound();
            }
        });

        // "undo"
        mainFrame.getUndo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoLastCommand();
            }
        });

        // "redo"
        mainFrame.getRedo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoLastCommand();
            }
        });
    }

    @Override
    public void changeEventReceived(MyChangeEvent evt) {
        if (evt.getSource() instanceof DeliveryMap) {
            onMapNodeSelected(((DeliveryMap) (evt.getSource())));
        } else if (evt.getSource() instanceof DeliveryList) {
            onListDeliverySelected(((DeliveryList) (evt.getSource())));
        }
    }

    public void onMapNodeSelected(DeliveryMap map) {
        if (map == null) {
            System.err.println("map null");
            return;
        }
        if (map.getSelectedNode() == null) {
            System.err.println("map.getSelectedNode() null");
            
            return;
        }
         if (map.getSelectedNode().get()== null) {
            System.err.println("map.getSelectedNode().get() null");
            mainFrame.getDeliveryList().setSelectionById(-1);
        }
         else{
        mainFrame.getDeliveryList().setSelectionById(map.getSelectedNode().get().getAddress());
    }}

    private void onListDeliverySelected(DeliveryList deliveryList) {
        if (deliveryList == null) {
            System.err.println("deliveryList null");
            return;
        }
        if (deliveryList.getSelected() == null) {
            mainFrame.getDeliveryMap().setSelectedNodeById(-1l);
            System.err.println("deliveryList.getSelected() null");
            return;
        }
        else{
        mainFrame.getDeliveryMap().setSelectedNodeById(deliveryList.getSelected().getDelivery().getAddress());    
        }
         if (deliveryList.getSelected().getDelivery() == null) {
            System.err.println("deliveryList.getSelected.getDelivery null");
            return;
        }
        
    }

    private interface Command {

        void execute();

        void undo();
    }
}
