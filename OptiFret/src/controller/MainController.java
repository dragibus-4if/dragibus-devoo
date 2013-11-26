package controller;

import config.Entry;
import config.Helper;
import config.Manager;
import config.MissingAttributeException;
import config.MissingEntryException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import model.Delivery;
import model.DeliverySheet;
import model.RoadNetwork;
import model.RoadNode;
import view.CreateDeliveryDialog;
import view.DeliveryMap;
import view.Listener;
import view.MainFrame;
import view.MyChangeEvent;
import view.DeliveryList;
import view.DeliveryView;
import view.NodeView;

public class MainController extends Invoker implements Listener {

    private RoadNetwork roadNetwork;
    private DeliverySheet deliverySheet;
    private MainFrame mainFrame;

    public MainController(MainFrame mainFrame) {
        super();
        if (mainFrame == null) {
            throw new NullPointerException("'view' ne doit pas être nul");
        }
        this.mainFrame = mainFrame;
        setupNewView();
    }

    public void run() {
        String startupConfigEntryName = "startup";
        try {
            configureStartup(Manager.getInstance().registerEntry(startupConfigEntryName));
        } catch (MissingEntryException e) {
            System.out.println("Aucune configuration trouvée pour " + startupConfigEntryName);
        }
        mainFrame.setVisible(true);
    }

    private void addDelivery(long address) {
        long id = 0; // TODO
        CreateDeliveryDialog cdd = new CreateDeliveryDialog(mainFrame, id, address);
        cdd.setVisible(true);

        // Validation préalable: livraison crée si formulaire validé
        final Delivery delivery = cdd.getDelivery();
        if (delivery == null) {
            return;
        }

        executeCommand(new Command("Ajouter la livraison d'id " + id) {

            @Override
            public void execute() {
                // recuperer la liste de livraisons et ajouter la nouvelle liv
                deliverySheet.addDelivery(delivery);

                // ajouter la nouvelle liste a la fenetre et mettre a jour
                mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
                mainFrame.getExportRound().setEnabled(true);

                // recalculer le chemin et mettre a jour le plan
                mainFrame.getDeliveryMap().updateNetwork(deliverySheet.getDeliveryRound());
                mainFrame.repaint();
            }

            @Override
            public void undo() {
                // TODO supprimer la livraison
                deliverySheet.getDeliveries().remove(delivery);

                // TODO - ajouter fonctionnalite
                if (deliverySheet.getDeliveries() == null) {
                    mainFrame.getDeliveryList().setDeliveries(new ArrayList<Delivery>());
                    mainFrame.getExportRound().setEnabled(false);
                } else {
                    mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
                }

                mainFrame.repaint();
            }
        });
    }

    private void deleteDelivery(long deliveryId) {
        final Delivery delivery = deliverySheet.findDeliveryById(deliveryId);
        if (delivery == null) {
            throw new IllegalArgumentException("Livraison d'id '" + deliveryId + "' indéfinie");
        }

        executeCommand(new Command("Supprimer la livraison d'id " + delivery.getId()) {

            @Override
            public void execute() {
                List<Delivery> deliveries = deliverySheet.getDeliveries();
                deliverySheet.getDeliveries().remove(delivery);

                mainFrame.getDeliveryList().setDeliveries(deliveries);
                mainFrame.getExportRound().setEnabled(deliveries.isEmpty());
                mainFrame.getDeliveryMap().updateNetwork(deliverySheet.getDeliveryRound());
                mainFrame.repaint();
            }

            @Override
            public void undo() {
                deliverySheet.getDeliveries().add(delivery);
            }
        });

    }

    private void configureStartup(Entry entry) {
        Helper helper = new Helper(entry);
        try {
            String rnFilename = helper.getString("load-road-network");
            roadNetwork = RoadNetwork.loadFromXML(new FileReader(rnFilename));
            mainFrame.getLoadRound().setEnabled(true);
            mainFrame.getDeliveryMap().updateNetwork(roadNetwork.getNodes());
            try {
                String dsFilename = helper.getString("load-delivery-sheet");
                deliverySheet = DeliverySheet.loadFromXML(new FileReader(dsFilename));
                mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
                mainFrame.getExportRound().setEnabled(true);
            } catch (MissingAttributeException ex) {
                System.out.println("Aucune configuration trouvée pour les demandes de livraison");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        } catch (MissingAttributeException ex) {
            System.out.println("Aucune configuration trouvée pour la carte");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
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
                                    deliverySheet.getDeliveries());
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

                // Vérification comme quoi toutes les livraisons sont présentes sur la carte
                for (Delivery delivery : loadedDeliverySheet.getDeliveries()) {
                    if (roadNetwork.getNodeById(delivery.getAddress()) == null) {
                        throw new IOException("Addresse '" + delivery.getAddress()
                                + "' indéfinie,\nchargement annulé.");
                    }
                }

                executeCommand(new Command(fc.getDialogTitle()) {
                    private DeliverySheet currentDeliverySheet;

                    @Override
                    public void execute() {
                        // sauvegarder l'état courant de la liste de livraisons
                        currentDeliverySheet = deliverySheet;

                        deliverySheet = loadedDeliverySheet;
                        mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
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
                            mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
                        }
                        mainFrame.repaint();
                    }
                });

                // TODO enlever
                List<RoadNode> path = roadNetwork.makeRoute(deliverySheet.getDeliveries());
                mainFrame.getDeliveryMap().updateDeliveryNodes(path);
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

    private void setupNewView() {
        // Historique
        clearAllHistory();
        mainFrame.getUndo().setEnabled(false);
        mainFrame.getRedo().setEnabled(false);
        mainFrame.getLoadRound().setEnabled(false);
        mainFrame.getExportRound().setEnabled(false);

        // Boutons pour ajouter/supprimer une livraison
        mainFrame.getAddDeliveryButton().setEnabled(false);
        mainFrame.getDelDeliveryButton().setEnabled(false);

        // Listeners
        setupViewListeners();
    }

    private void setupViewListeners() {
        // sélectionner un noeud/une livraison
        mainFrame.getDeliveryMap().addListener(this);
        mainFrame.getDeliveryList().addListener(this);

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
            return;
        }

        WeakReference<NodeView> selectedNodeRef = map.getSelectedNode();
        if (selectedNodeRef == null) {
            return;
        }

        NodeView selectedNode = selectedNodeRef.get();
        if (selectedNode == null) {
            mainFrame.getDeliveryList().setSelectionById(-1);
            mainFrame.getAddDeliveryButton().setEnabled(false);
        } else {
            mainFrame.getDeliveryList().setSelectionById(selectedNode.getAddress());
            if (selectedNode.getMode() == NodeView.MODE.DELIVERY) {
                mainFrame.getDelDeliveryButton().setEnabled(true);
                mainFrame.getAddDeliveryButton().setEnabled(false);
            } else {
                mainFrame.getDelDeliveryButton().setEnabled(false);
                mainFrame.getAddDeliveryButton().setEnabled(true);
            }
        }
    }

    private void onListDeliverySelected(DeliveryList deliveryList) {
        if (deliveryList == null) {
            return;
        }

        DeliveryView selectedDeliveryView = deliveryList.getSelected();
        if (selectedDeliveryView == null) {
            mainFrame.getDeliveryMap().setSelectedNodeById(-1);
            mainFrame.getDelDeliveryButton().setEnabled(false);
            return;
        }

        Delivery selectedDelivery = selectedDeliveryView.getDelivery();
        if (selectedDelivery == null) {
            mainFrame.getDeliveryMap().setSelectedNodeById(-1);
            mainFrame.getDelDeliveryButton().setEnabled(false);
        } else {
            mainFrame.getDeliveryMap().setSelectedNodeById(selectedDelivery.getAddress());
            mainFrame.getAddDeliveryButton().setEnabled(false);
            mainFrame.getDelDeliveryButton().setEnabled(true);
        }
    }

    @Override
    protected void executeCommand(Command cmd, boolean clearRedoHistory) {
        super.executeCommand(cmd, clearRedoHistory);
        JMenuItem undo = mainFrame.getUndo();
        undo.setEnabled(true);
        undo.setText(MainFrame.UNDO_TOOLTIP + " \"" + cmd.getName() + '"');
        if (clearRedoHistory) {
            mainFrame.getRedo().setText(MainFrame.REDO_TOOLTIP);
        }
    }

    @Override
    protected void undoLastCommand() throws EmptyStackException {
        Command cmd = getHistory().peek();
        super.undoLastCommand();
        JMenuItem redo = mainFrame.getRedo();
        JMenuItem undo = mainFrame.getUndo();
        redo.setEnabled(true);
        redo.setText(MainFrame.REDO_TOOLTIP + " \"" + cmd.getName() + '"');
        if (getHistory().size() == 0) {
            undo.setEnabled(false);
            undo.setText(MainFrame.UNDO_TOOLTIP);
        } else {
            Command cmd2 = getHistory().peek();
            undo.setText(MainFrame.UNDO_TOOLTIP + " \"" + cmd2.getName() + '"');
        }
    }

    @Override
    protected void redoLastCommand() throws EmptyStackException {
        super.redoLastCommand();
        JMenuItem redo = mainFrame.getRedo();
        if (getRedoneHistory().size() == 0) {
            redo.setEnabled(false);
            redo.setText(MainFrame.REDO_TOOLTIP);
        } else {
            Command cmd = getRedoneHistory().peek();
            redo.setText(MainFrame.REDO_TOOLTIP + " \"" + cmd.getName() + '"');
        }
    }
}
