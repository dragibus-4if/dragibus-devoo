/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import junit.framework.TestCase;
import model.Client;
import model.Delivery;
import model.DeliverySheet;
import model.RoadNetwork;
import model.RoadNode;
import model.RoadSection;
import model.TimeSlot;

public class DeliverySheetTest extends TestCase {

    public void testFile() {
        // Si le filename est null, la fonction retourne null.
        try {
            try {
                DeliverySheet.loadFromXML(null);
            } catch (IOException ex) {
            }
            fail("Chargement d'un reader null");
        } catch (NullPointerException e) {
        }

        try {
            // Si c'est un dossier, la fonction retourne Null
            DeliverySheet.loadFromXML(new FileReader("/"));
            fail("Chargement d'un dossier");
        } catch (IOException ex) {
        }

        try {
            // Si le fichier n'est pas lisible, la fonction retourne Null
            // Normalement le fichier /root ne sont pas lisibles
            DeliverySheet.loadFromXML(new FileReader("/root"));
            fail("Chargement d'un fichier illisible");
        } catch (IOException ex) {
        }
    }

    public void testXMLSyntax() {
        try {
            // Erreur s'il manque une fermeture de balise
            DeliverySheet.loadFromXML(new StringReader("<root>"));
            fail("Balise manquante");
        } catch (IOException ex) {
        }

        try {
            // Erreur s'il y a une fermeture de balise manquante
            DeliverySheet.loadFromXML(new StringReader("<root></balise></root>"));
            fail("Balise manquante fermée");
        } catch (IOException ex) {
        }

        // Il n'y a pas tout les cas sur la syntaxe XML. La bibliothèque
        // utilisée doit pouvoir détecter les erreurs. Nous l'utilisons et ces
        // quelques tests permettent de montrer qu'on capte que la bibliothèque
        // a détecté une erreur.
    }

    public void testXMLSemantic() {
        // Erreur si la balise racine est un élément quelconque
        String s = "<root></root>";
        assertFalse("root".equals(DeliverySheet.ROOT_ELEM));
        try {
            DeliverySheet.loadFromXML(new StringReader(s));
            fail("Erreur quand le noeud racine est quelconque");
        } catch (IOException ex) {
        }

        // Erreur si la balise racine ne contient aucun élément
        s = "<" + DeliverySheet.ROOT_ELEM + "></" + DeliverySheet.ROOT_ELEM + ">";
        try {
            DeliverySheet.loadFromXML(new StringReader(s));
            fail("Erreur quand la racine ne contient aucun élément");
        } catch (IOException ex) {
        }

        // Pas d'erreur si la balise racine ne contient que l'entrepôt
        s = "<" + DeliverySheet.ROOT_ELEM + "><" + DeliverySheet.WAREHOUSE_NAME
                + " " + DeliverySheet.WAREHOUSE_ADDRESS + "=\"1\"/></"
                + DeliverySheet.ROOT_ELEM + ">";
        try {
            DeliverySheet.loadFromXML(new StringReader(s));
        } catch (IOException ex) {
            fail("Erreur quand la racine ne contient que l'entrepôt");
        }

        // Erreur si le document contient un élément non défini
        s = "<" + DeliverySheet.ROOT_ELEM + "><" + DeliverySheet.WAREHOUSE_NAME
                + " /><autre></autre></" + DeliverySheet.ROOT_ELEM + ">";
        try {
            DeliverySheet.loadFromXML(new StringReader(s));
            fail("Erreur quand le document contient un élément non défini");
        } catch (IOException ex) {
        }
    }

    public void testExportEmpty() {
        DeliverySheet sheet = new DeliverySheet();

        // Test avec une sheet vide
        String result = "";
        StringWriter sw = new StringWriter();
        try {
            try {
                sheet.export(sw);
            } catch (IOException ex) {
            }
        } catch (NullPointerException e) {
        }

    }

    public void testExportNull() {
        DeliverySheet sheet = new DeliverySheet();
        try {
            try {
                sheet.export(null);
            } catch (IOException ex) {
            }
            fail("Appel de export avec un paramètre null");
        } catch (NullPointerException e) {
        }
    }

    public void testExportWithoutSection() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        path.add(new RoadNode(1));
        path.add(new RoadNode(2));
        path.add(new RoadNode(3));
        path.add(path.get(0));
        path.get(0).addNeighbor(new RoadSection(path.get(0), path.get(1), 1, 1, "R1"));
        path.get(1).addNeighbor(new RoadSection(path.get(1), path.get(2), 1, 1, "R2"));
        path.get(3).addNeighbor(new RoadSection(path.get(3), path.get(0), 1, 1, "R4"));

        DeliverySheet sheet = new DeliverySheet();
        Delivery d1 = new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client());
        Delivery d2 = new Delivery(new Long(1), new Long(3),
                new TimeSlot(new Date(), new Long(0)), new Client());
        List<Delivery> ld = new ArrayList<>();
        ld.add(d1);
        ld.add(d2);
        sheet.setDeliveries(ld);

        RoadNetwork rn = new RoadNetwork();
        rn.setRoot(path.get(0));
        sheet.setRoadNetwork(rn);

        try {
            try {
                sheet.export(new StringWriter());
            } catch (IOException ex) {
            }
            fail("Pas de section pour continuer le chemin.");
        } catch (RuntimeException e) {
        }
    }

    public void testExportNoPath() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        DeliverySheet sheet = new DeliverySheet();

        RoadNetwork rn = new RoadNetwork();
        rn.setRoot(path.get(0));
        sheet.setRoadNetwork(rn);

        String result = "";
        StringWriter sw = new StringWriter();
        try {
            try {
                sheet.export(sw);
            } catch (IOException ex) {
            }
        } catch (NullPointerException e) {
        }
    }
}
