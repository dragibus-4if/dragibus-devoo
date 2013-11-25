package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
import view.NodeView;

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
                executeCommand(new Command(fc.getDialogTitle()) {
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

                executeCommand(new Command(fc.getDialogTitle()) {
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
                File file = fc.getSelectedFile();
                if (file.exists()) {
                    if (JOptionPane.showConfirmDialog(mainFrame,
                            "Le fichier '" + file.getName() + "' existe déjà.\n"
                            + "L'écraser ?", MainFrame.EXPORT_ROUND_TOOLTIP,
                            JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                deliverySheet.export(new FileWriter(file));
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
        Command cmd = history.pop();
        undoCommand(cmd);
        JMenuItem redo = mainFrame.getRedo();
        JMenuItem undo = mainFrame.getUndo();
        redo.setEnabled(true);
        redo.setText(MainFrame.REDO_TOOLTIP + " \"" + cmd.getName() + '"');
        if (history.size() == 0) {
            undo.setEnabled(false);
            undo.setText(MainFrame.UNDO_TOOLTIP);
        } else {
            Command cmd2 = history.peek();
            undo.setText(MainFrame.UNDO_TOOLTIP + " \"" + cmd2.getName() + '"');
        }
    }

    /**
     * Permet de refaire la dernière commande annulée et de l'ajouter à
     * l'historique des "undo".
     *
     * @throws EmptyStackException
     */
    private void redoLastCommand() throws EmptyStackException {
        executeCommand(redoneHistory.pop(), false);
        JMenuItem redo = mainFrame.getRedo();
        if (redoneHistory.size() == 0) {
            redo.setEnabled(false);
            redo.setText(MainFrame.REDO_TOOLTIP);
        } else {
            Command cmd2 = redoneHistory.peek();
            redo.setText(MainFrame.REDO_TOOLTIP + " \"" + cmd2.getName() + '"');
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
    private void executeCommand(Command cmd, boolean clearRedoHistory) {
        // Exécuter la commande et l'ajouter à l'historique
        cmd.execute();
        history.add(cmd);
        JMenuItem undo = mainFrame.getUndo();
        undo.setEnabled(true);
        undo.setText(MainFrame.UNDO_TOOLTIP + " \"" + cmd.getName() + '"');

        // Empêcher de revenir en avant dans l'historique
        if (clearRedoHistory) {
            redoneHistory.clear();
            mainFrame.getRedo().setText(MainFrame.REDO_TOOLTIP);
        }
    }

    /**
     * Surcharge spécifiant la valeur par défaut de {@literal clearRedoHistory}.
     *
     * @param cmd
     */
    private void executeCommand(Command cmd) {
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
            System.out.println("map null");
            return;
        }
        WeakReference<NodeView> selectedNodeRef = map.getSelectedNode();
        if (selectedNodeRef == null) {
            System.out.println("map.getSelectedNode() null");
            return;
        }
        NodeView selectedNode = selectedNodeRef.get();
        if (selectedNode == null) {
            System.out.println("map.getSelectedNode().get() null");
            mainFrame.getDeliveryList().setSelectionById(-1);
        } else {
            mainFrame.getDeliveryList().setSelectionById(selectedNode.getAddress());
        }
    }

    private void onListDeliverySelected(DeliveryList deliveryList) {
        if (deliveryList == null) {
            System.err.println("deliveryList null");
            return;
        }
        if (deliveryList.getSelected() == null) {
            mainFrame.getDeliveryMap().setSelectedNodeById(-1l);
            System.err.println("deliveryList.getSelected() null");
            return;
        } else {
            mainFrame.getDeliveryMap().setSelectedNodeById(deliveryList.getSelected().getDelivery().getAddress());
        }
        if (deliveryList.getSelected().getDelivery() == null) {
            System.err.println("deliveryList.getSelected.getDelivery null");
            return;
        }

    }

    public void run() {
        mainFrame.setVisible(true);
    }

    private abstract class Command {

        private String name;

        Command(String name) {
            this.name = name;
        }

        public abstract void execute();

        public abstract void undo();

        public String getName() {
            return name;
        }
    }
}
