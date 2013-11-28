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
import java.io.Reader;
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
        try {
            configureStartup(Manager.getInstance().registerEntry("startup"));
        } catch (MissingEntryException e) {
            System.out.println("Aucune configuration trouvée pour le MainController");
        }
        mainFrame.setVisible(true);
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
                deliverySheet = doloadDeliverySheet(new FileReader(dsFilename));
                deliverySheet.setRoadNetwork(roadNetwork);
                calculRoute();
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

    private void addDelivery(long address) {
        if (deliverySheet == null) {
            throw new NullPointerException();
        }
        // Calcul du max des id
        long id = 0;
        for (Delivery d : deliverySheet.getDeliveries()) {
            if (id < d.getId()) {
                id = d.getId();
            }
        }
        // ...incrémenté de 1
        id++;

        // Formulaire de création de la livraison
        CreateDeliveryDialog cdd = new CreateDeliveryDialog(mainFrame, id, address);
        cdd.setVisible(true);

        // Validation préalable: livraison créée si formulaire validé
        final Delivery delivery = cdd.getDelivery();
        if (delivery == null) {
            return;
        }

        executeCommand(new Command("Ajouter la livraison d'id " + id) {
            @Override
            public void execute() {
                // recuperer la liste de livraisons et ajouter la nouvelle liv
                deliverySheet.addDelivery(delivery);
                calculRoute();

                // ajouter la nouvelle liste a la fenetre et mettre a jour
                mainFrame.getExportRound().setEnabled(true);

                // recalculer le chemin et mettre a jour le plan
                //mainFrame.getDeliveryMap().updateNetwork(deliverySheet.getDeliveryRound());
                mainFrame.repaint();
            }

            @Override
            public void undo() {
                deliverySheet.getDeliveries().remove(delivery);
                calculRoute();

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
                calculRoute();

                mainFrame.getExportRound().setEnabled(deliveries.isEmpty());
                //mainFrame.getDeliveryMap().updateNetwork(deliverySheet.getDeliveryRound());
                mainFrame.repaint();
            }

            @Override
            public void undo() {
                deliverySheet.getDeliveries().add(delivery);
                calculRoute();
                mainFrame.repaint();
            }
        });
    }

    private void calculRoute() {
        if (roadNetwork == null) {
            throw new NullPointerException();
        }
        if (deliverySheet == null) {
            throw new NullPointerException();
        }
        if (roadNetwork.makeRoute(deliverySheet)) {
            deliverySheet.setDelivery(roadNetwork.getSortedDeliveries());
            updateDeliveryMap(deliverySheet);
            deliverySheet.createPredTimeSlot();
            mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
        } else {
            // Pas de solution
            // TODO afficher un message
            updateDeliveryMap(deliverySheet);
            mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
        }
    }

    private void updateDeliveryMap() {
        mainFrame.getDeliveryMap().clearNodeViewMode();
        mainFrame.getDeliveryMap().updateDeliveryNodesPath(new ArrayList<RoadNode>());
        mainFrame.getDeliveryMap().updateDeliveryNodes(new ArrayList<Delivery>());
    }

    private void updateDeliveryMap(List<RoadNode> path, List<Delivery> deliveries, Long wharehouseAdress, boolean wholePath) {
        if (path == null || deliveries == null) {
            updateDeliveryMap();
            return;
        }
        mainFrame.getDeliveryMap().clearNodeViewMode();
        if (!wholePath) {
            mainFrame.getDeliveryMap().clearArrowColors();
        }
        mainFrame.getDeliveryMap().updateDeliveryNodesPath(path);
        mainFrame.getDeliveryMap().updateDeliveryNodes(deliveries);
        mainFrame.getDeliveryMap().updateWhareHouse(wharehouseAdress);
        if (wholePath) {
            mainFrame.getDeliveryMap().updateTimeSlots(deliveries, path);
        }
    }

    private void updateDeliveryMap(DeliverySheet sheet) {
        if (sheet == null) {
            updateDeliveryMap();
        } else {
            updateDeliveryMap(sheet.getDeliveryRound(), sheet.getDeliveries(), sheet.getWarehouseAddress(), true);
        }
    }

    private void updateDeliveryMap(Delivery del) {
        if (del == null || deliverySheet == null) {
            updateDeliveryMap();
            return;
        }
        List<RoadNode> path = deliverySheet.getDeliveryRound(del);
        List<Delivery> ls = new ArrayList<>();
        ls.add(del);
        int index = deliverySheet.getDeliveries().indexOf(del);
        if (index != -1 && index + 1 < deliverySheet.getDeliveries().size()) {
            ls.add(deliverySheet.getDeliveries().get(index + 1));
        }
        if (deliverySheet.getDeliveries().get(0) == del) {
            path.addAll(0, deliverySheet.getWarehouseRound());
        }
        updateDeliveryMap(path, ls, -1l, false);
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
                        deliverySheet = null;
                        mainFrame.getLoadRound().setEnabled(true);
                        mainFrame.getDeliveryMap().updateNetwork(roadNetwork.getNodes());
                        mainFrame.getDeliveryList().setDeliveries(new ArrayList<Delivery>());
                        mainFrame.getExportRound().setEnabled(false);
                        mainFrame.getAddDeliveryButton().setEnabled(false);
                        mainFrame.getDelDeliveryButton().setEnabled(false);
                        mainFrame.repaint();
                    }

                    @Override
                    public void undo() {
                        roadNetwork = currentNetwork;
                        deliverySheet = currentDeliverySheet;

                        if (deliverySheet != null) {
                            deliverySheet.setRoadNetwork(roadNetwork);
                            calculRoute();
                        }

                        // verifier si un reseau a deja ete charge
                        if (roadNetwork == null) {
                            mainFrame.getLoadRound().setEnabled(false);
                            mainFrame.getDeliveryMap().updateNetwork(new ArrayList<RoadNode>());
                        } else {
                            mainFrame.getDeliveryMap().updateNetwork(roadNetwork.getNodes());
                        }

                        // verifier si une liste de livraisons a deja ete charge
                        if (deliverySheet == null) {
                            mainFrame.getDeliveryList().setDeliveries(new ArrayList<Delivery>());
                        } else {
                            mainFrame.getDeliveryList().setDeliveries(deliverySheet.getDeliveries());
                        }
                        mainFrame.repaint();
                    }
                });
            } catch (IOException e) {
                mainFrame.showErrorMessage(e.getMessage());
            }
        }
    }

    private DeliverySheet doloadDeliverySheet(Reader reader) throws IOException {
        DeliverySheet ds = DeliverySheet.loadFromXML(reader);
        // Vérification comme quoi toutes les livraisons sont présentes sur la carte
        List<Delivery> deliveries = ds.getDeliveries();
        List<Long> idList = new ArrayList<>(deliveries.size());
        for (Delivery delivery : deliveries) {
            idList.add(delivery.getId());
        }
        if (!roadNetwork.allValidNodes(idList)) {
            throw new IOException("Addresse indéfinie dans la tournée,\nchargement annulé.");
        }
        return ds;
    }

    private void loadDeliverySheet() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(MainFrame.LOAD_ROUND_TOOLTIP);
        if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                final DeliverySheet loadedDeliverySheet = doloadDeliverySheet(new FileReader(fc.getSelectedFile()));

                executeCommand(new Command(fc.getDialogTitle()) {
                    private DeliverySheet currentDeliverySheet;

                    @Override
                    public void execute() {
                        // sauvegarder l'état courant de la liste de livraisons
                        currentDeliverySheet = deliverySheet;

                        deliverySheet = loadedDeliverySheet;
                        deliverySheet.setRoadNetwork(roadNetwork);
                        calculRoute();
                        mainFrame.getExportRound().setEnabled(true);
                        mainFrame.getAddDeliveryButton().setEnabled(false);
                        mainFrame.getDelDeliveryButton().setEnabled(false);
                        mainFrame.repaint();
                    }

                    @Override
                    public void undo() {
                        deliverySheet = currentDeliverySheet;

                        // verifier si un DeliverySheet a deja ete charge
                        if (deliverySheet == null) {
                            mainFrame.getDeliveryList().setDeliveries(new ArrayList<Delivery>());
                            mainFrame.getExportRound().setEnabled(false);
                            mainFrame.getAddDeliveryButton().setEnabled(false);
                            mainFrame.getDelDeliveryButton().setEnabled(false);
                        } else {
                            deliverySheet.setRoadNetwork(roadNetwork);
                            calculRoute();
                        }
                        mainFrame.repaint();
                    }
                });
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
                FileWriter fw = new FileWriter(file, true);
                deliverySheet.export(fw);
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

        // "supprimer une livraison"
        mainFrame.getAddDeliveryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame.getDeliveryMap().getSelectedNode() != null) {
                    addDelivery(mainFrame.getDeliveryMap().getSelectedNode().get().getAddress());
                } else {
                    mainFrame.showErrorMessage("Impossible d'ajouter une livraison");
                }
            }
        });

        // "ajouter"
        mainFrame.getDelDeliveryButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame.getDeliveryList().getSelected() != null) {
                    deleteDelivery(mainFrame.getDeliveryList().getSelected().getDelivery().getId());
                } else {
                    mainFrame.showErrorMessage("Impossible de supprimer cette livraison");
                }
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
            mainFrame.getDelDeliveryButton().setEnabled(false);
            updateDeliveryMap(deliverySheet);

        } else {
            mainFrame.getDeliveryList().setSelectionById(selectedNode.getAddress());
            switch (selectedNode.getMode()) {
                case DELIVERY_NODE:
                    mainFrame.getDelDeliveryButton().setEnabled(true);
                    mainFrame.getAddDeliveryButton().setEnabled(false);
                    updateDeliveryMap(mainFrame.getDeliveryList().getSelected().getDelivery());
                    break;
                default:
                    mainFrame.getDelDeliveryButton().setEnabled(false);
                    mainFrame.getAddDeliveryButton().setEnabled(true);
                    updateDeliveryMap(deliverySheet);
                    break;
            }

            if (deliverySheet == null) {
                mainFrame.getDelDeliveryButton().setEnabled(false);
                mainFrame.getAddDeliveryButton().setEnabled(false);
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
            updateDeliveryMap(deliverySheet);
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
            updateDeliveryMap(selectedDelivery);
        }
        if (deliverySheet == null) {
            mainFrame.getDelDeliveryButton().setEnabled(false);
            mainFrame.getAddDeliveryButton().setEnabled(false);
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
