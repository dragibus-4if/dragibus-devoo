/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import junit.framework.TestCase;
import model.Client;
import model.Delivery;
import model.DeliveryEmployee;
import model.DeliverySheet;
import model.RoadNode;
import model.TimeSlot;

public class DeliverySheetModelTest extends TestCase {

    public void testFile() {
        try {
            assertNull(DeliverySheet.loadFromXML(null));
            throw new IOException();
        } catch(NullPointerException e) {
        } catch (IOException e) {
            fail("loadFromXML(null) doit causer une NullPointerException");
        }

        try {
            assertNull(DeliverySheet.loadFromXML(new FileReader("/")));
            fail("loadFromXML(dossier) doit causer une exception");
        } catch (IOException e) {
        }

        try {
            // Si le fichier n'est pas lisible, la fonction retourne Null
            // Normalement le fichier /root ne sont pas lisibles
            assertNull(DeliverySheet.loadFromXML(new FileReader("/root")));
        } catch (IOException e) {
        }
    }

    public void testXMLSyntax() {
        try {
            // Test d'une fermeture de balise manquante
            assertNull(DeliverySheet.loadFromXML(new StringReader("<root>")));
        } catch (IOException e) {
        }
        
        try {
            // Test d'une ouverture de balise manquante
            assertNull(DeliverySheet.loadFromXML(new StringReader("<root></balise></root>")));
            
            // Il n'y a pas tout les cas sur la syntaxe XML. La bibliothèque
            // utilisée doit pouvoir détecter les erreurs. Nous l'utilisons et ces
            // quelques tests permettent de montrer qu'on capte que la bibliothèque
            // a détecté une erreur.
        } catch (IOException e) {
        }
    }

    public void testXMLSemantic() {
        // Si la balise racine est un élément quelconque (différent de ce qui
        // est attendu), la fonction cause une exception
        String s1 = "<root></root>";
        try {
            assertNull(DeliverySheet.loadFromXML(new StringReader(s1)));
            fail("Syntaxe invalide");
        } catch (IOException ex) {
        }

        // Si le document contient un élément non défini, la fonction renvoie
        // null.
        String s2 = "<road_network><autre></autre></road_network>";
        try {
            assertNull(DeliverySheet.loadFromXML(new StringReader(s2)));
            fail("Syntaxe invalide");
        } catch (IOException ex) {
        }

        // Si c'est la bonne balise racine, la fonction renvoie quelque chose de non
        // null.
        String s3 = "<road_network></road_network>";
        DeliverySheet rn = null;
        try {
            rn = DeliverySheet.loadFromXML(new StringReader(s3));
        } catch (IOException ex) {
        }
        assertNotNull(rn);
        assertNotNull(rn.getDeliveryEmployee());
        assertNotNull(rn.getDeliveryRound());
    }

    public void testSetDeliveryEmployee() {
        DeliverySheet sheet = new DeliverySheet();
        // Test avec un paramètre null
        try {
            sheet.setDeliveryEmployee(null);
            fail("Set un employé avec un null");
        } catch (NullPointerException e) {
        }

        // Vérifie l'égalité entre le getter/setter
        DeliveryEmployee e = new DeliveryEmployee();
        sheet.setDeliveryEmployee(e);
        assertSame(sheet.getDeliveryEmployee(), e);
    }

    public void testExportEmpty() {
        DeliverySheet sheet = new DeliverySheet();

        // Test avec une sheet vide
        String result = "";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
            fail("Appel de export ne donne pas un string");
        }
        assertEquals(result, sw.toString());
    }

    public void testExportNull() {
        DeliverySheet sheet = new DeliverySheet();
        try {
            sheet.export(null);
            fail("Exception IO inattendue");
        } catch (NullPointerException | IOException e) {
        }
    }

    public void testExportBasic() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        path.add(new RoadNode(1));
        path.add(new RoadNode(2));
        path.add(new RoadNode(3));
        path.add(path.get(0));
        path.get(0).addNeighbor(path.get(1), 1, 1, "R1");
        path.get(1).addNeighbor(path.get(2), 1, 1, "R2");
        path.get(2).addNeighbor(path.get(3), 1, 1, "R3");
        path.get(3).addNeighbor(path.get(0), 1, 1, "R4");

        DeliverySheet sheet = new DeliverySheet();
        sheet.getDeliveryRound().setPath(path);
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client(0)));
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(1), new Long(3),
                new TimeSlot(new Date(), new Long(0)), new Client(1)));

        String result = "Prendre la rue R1\n";
        result += "Arrivée à la livraison : R1\n\n***\n\n";
        result += "Prendre la rue R2\n";
        result += "Prendre la rue R3\n";
        result += "Arrivée à la livraison : R3\n\n***\n\n";
        result += "Prendre la rue R4\n";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
            fail("Exception IO inattendue");
        }
        assertEquals(result, sw.toString());
    }

    public void testExportWithoutSection() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        path.add(new RoadNode(1));
        path.add(new RoadNode(2));
        path.add(new RoadNode(3));
        path.add(path.get(0));
        path.get(0).addNeighbor(path.get(1), 1, 1, "R1");
        path.get(1).addNeighbor(path.get(2), 1, 1, "R2");
        path.get(3).addNeighbor(path.get(0), 1, 1, "R4");

        DeliverySheet sheet = new DeliverySheet();
        sheet.getDeliveryRound().setPath(path);
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client(0)));
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(1), new Long(3),
                new TimeSlot(new Date(), new Long(0)), new Client(1)));

        try {
            try {
                sheet.export(new StringWriter());
            } catch (IOException ex) {
                fail("Exception IO inattendue");
            }
            fail("Pas de section pour continuer le chemin.");
        } catch (RuntimeException e) {
        }
    }

    public void testExportCross() {
        RoadNode n0 = new RoadNode(0);
        RoadNode n1 = new RoadNode(1);
        RoadNode n2 = new RoadNode(2);
        RoadNode n3 = new RoadNode(3);
        RoadNode n4 = new RoadNode(4);
        n0.addNeighbor(n1, 1, 1, "R1");
        n1.addNeighbor(n2, 1, 1, "R2");
        n2.addNeighbor(n1, 1, 1, "-R2");
        n1.addNeighbor(n3, 1, 1, "R3");
        n3.addNeighbor(n1, 1, 1, "-R3");
        n1.addNeighbor(n4, 1, 1, "R4");
        n4.addNeighbor(n1, 1, 1, "-R4");
        n4.addNeighbor(n0, 1, 1, "R5");
        List<RoadNode> path = new ArrayList<>();
        path.add(n0);
        path.add(n1);
        path.add(n2);
        path.add(n1);
        path.add(n3);
        path.add(n1);
        path.add(n4);
        path.add(n0);

        DeliverySheet sheet = new DeliverySheet();
        sheet.getDeliveryRound().setPath(path);
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client(0)));
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(1), new Long(2),
                new TimeSlot(new Date(), new Long(0)), new Client(1)));
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(3), new Long(4),
                new TimeSlot(new Date(), new Long(0)), new Client(2)));

        String result = "Prendre la rue R1\n";
        result += "Arrivée à la livraison : R1\n\n***\n\n";
        result += "Prendre la rue R2\n";
        result += "Arrivée à la livraison : R2\n\n***\n\n";
        result += "Prendre la rue -R2\n";
        result += "Prendre la rue R3\n";
        result += "Prendre la rue -R3\n";
        result += "Prendre la rue R4\n";
        result += "Arrivée à la livraison : R4\n\n***\n\n";
        result += "Prendre la rue R5\n";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
            fail("Exception IO inattendue");
        }
        assertEquals(result, sw.toString());
    }

    public void testExportDeliveryOutPath() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        path.add(path.get(0));
        path.get(0).addNeighbor(path.get(0), 1, 1, "R");

        DeliverySheet sheet = new DeliverySheet();
        sheet.getDeliveryRound().setPath(path);
        sheet.getDeliveryRound().addDelivery(new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client(0)));

        try {
            try {
                sheet.export(new StringWriter());
            } catch (IOException ex) {
                fail("Exception IO inattendue");
            }
            fail("Livraison en dehors du chemin");
        } catch (RuntimeException e) {
        }
    }

    public void testExportWithoutDelivery() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        path.add(path.get(0));
        path.get(0).addNeighbor(path.get(0), 1, 1, "R");

        DeliverySheet sheet = new DeliverySheet();
        sheet.getDeliveryRound().setPath(path);

        String result = "Prendre la rue R\n";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
            fail("Exception IO inattendue");
        }
        assertEquals(result, sw.toString());
    }

    public void testExportEmptyPath() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        DeliverySheet sheet = new DeliverySheet();
        sheet.getDeliveryRound().setPath(path);

        String result = "";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
            fail("Exception IO inattendue");
        }
        assertEquals(result, sw.toString());
    }

    public void testExportNoPath() {
        // Création d'un chemin basique
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        DeliverySheet sheet = new DeliverySheet();
        sheet.getDeliveryRound().setPath(path);

        String result = "";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
            fail("Exception IO inattendue");
        }
        assertEquals(result, sw.toString());
    }
}
