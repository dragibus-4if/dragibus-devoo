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
import model.DeliveryEmployee;
import model.DeliverySheet;
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

    public void testExportBasic() {
        // Création d'un chemin basique
        HashMap<Delivery, List<RoadNode>> map = new HashMap<>();
        List<RoadNode> path = new ArrayList<>();
        List<RoadNode> path1 = new ArrayList<>();
        List<RoadNode> path2 = new ArrayList<>();
        DeliverySheet sheet = new DeliverySheet();
        
        
        path.add(new RoadNode(0));
        path.add(new RoadNode(1));
        path.add(new RoadNode(2));
        path.add(new RoadNode(3));
        path.get(0).addNeighbor(new RoadSection(path.get(0), path.get(1), 1, 1, "R1"));
        path.get(1).addNeighbor(new RoadSection(path.get(1), path.get(2), 1, 1, "R2"));
        path.get(2).addNeighbor(new RoadSection(path.get(2), path.get(3), 1, 1, "R3"));
        path.get(3).addNeighbor(new RoadSection(path.get(3), path.get(0), 1, 1, "R4"));

        
        Delivery d1 = new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client());
        Delivery d2 = new Delivery(new Long(1), new Long(2),
                new TimeSlot(new Date(), new Long(0)), new Client());
        
        
        map.put(d1,path.subList(0, 1));
        map.put(d2,path.subList(2, 3));
        List<Delivery> dL = new ArrayList<>();
        dL.add(d1);
        dL.add(d2);
        sheet.setDelivery(dL);
        
        sheet.setDeliveryRound(map);
        

        String result = "Nouvelle Tournée\n\n\n**********************\n\n\n\n";
        result += "Prochaine livraison : R1\n\n";
        result += "Prendre la rue R1\n\n";
        result += "Arrivée à la livraison : R1\n\n***\n\n";
        result += "Prochaine livraison : R3\n\n";
        result += "Dans 1 mètres : \n";
        result += "Prenez tout droit sur la rue R2\n\n";
        result += "Dans 1 mètres : \n";
        result += "Prenez tout droit sur la rue R3\n\n";
        result += "Arrivée à la livraison : R3\n\n***\n\n";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
        }
        assertEquals(result, sw.toString());
    }

    public void testExportWithoutSection() {
        // Création d'un chemin basique
        HashMap<Delivery, List<RoadNode>> map = new HashMap<>();
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        path.add(new RoadNode(1));
        path.add(new RoadNode(2));
        path.add(new RoadNode(3));
        path.add(path.get(0));
        path.get(0).addNeighbor(new RoadSection(path.get(0), path.get(1), 1, 1, "R1"));
        path.get(1).addNeighbor(new RoadSection(path.get(1), path.get(2), 1, 1, "R2"));
        path.get(3).addNeighbor(new RoadSection(path.get(3), path.get(0), 1, 1, "R4"));

        map.put(new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client()), path);
        
        DeliverySheet sheet = new DeliverySheet();
        sheet.setDeliveryRound(map);
        Delivery d1 =new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client());
        Delivery d2 =new Delivery(new Long(1), new Long(3),
                new TimeSlot(new Date(), new Long(0)), new Client());
        List<Delivery> ld = new ArrayList<>();
        ld.add(d1);
        ld.add(d2);
        sheet.setDelivery(ld);
        

        try {
            try {
                sheet.export(new StringWriter());
            } catch (IOException ex) {
            }
            fail("Pas de section pour continuer le chemin.");
        } catch (RuntimeException e) {
        }
    }

    public void testExportCross() {
        RoadNode n0 = new RoadNode(0);
        n0.setX(10);
        n0.setY(10);
        RoadNode n1 = new RoadNode(1);
        n1.setX(15);
        n1.setY(25);
        RoadNode n2 = new RoadNode(2);
        n2.setX(12);
        n2.setY(5);
        RoadNode n3 = new RoadNode(3);
        n3.setX(53);
        n3.setY(9);
        RoadNode n4 = new RoadNode(4);
        n4.setX(60);
        n4.setY(25);
        n0.addNeighbor(new RoadSection(n0, n1, 1, 1, "R1"));
        n1.addNeighbor(new RoadSection(n1, n2, 1, 1, "R2"));
        n2.addNeighbor(new RoadSection(n2, n1, 1, 1, "-R2"));
        n1.addNeighbor(new RoadSection(n1, n3, 1, 1, "R3"));
        n3.addNeighbor(new RoadSection(n3, n1, 1, 1, "-R3"));
        n1.addNeighbor(new RoadSection(n1, n4, 1, 1, "R4"));
        n4.addNeighbor(new RoadSection(n4, n1, 1, 1, "-R4"));
        n4.addNeighbor(new RoadSection(n4, n0, 1, 1, "R5"));
        
        HashMap<Delivery, List<RoadNode>> map = new HashMap<>();
        List<RoadNode> path = new ArrayList<>();
        path.add(n0);
        path.add(n1);
        path.add(n2);
        path.add(n1);
        path.add(n3);
        path.add(n1);
        path.add(n4);
        
        

        DeliverySheet sheet = new DeliverySheet();
        sheet.setDeliveryRound(map);
        Delivery d1 = new Delivery(new Long(0), new Long(1),
                new TimeSlot(new Date(), new Long(0)), new Client());
        Delivery d2 = new Delivery(new Long(1), new Long(2),
                new TimeSlot(new Date(), new Long(0)), new Client());
        Delivery d3 = new Delivery(new Long(2), new Long(4),
                new TimeSlot(new Date(), new Long(0)), new Client());
        
        map.put(d1,path);
        map.put(d2,path);
        map.put(d3,path);
        
        List<Delivery> ld = new ArrayList<>();
        ld.add(d1);
        ld.add(d2);
        ld.add(d3);
        sheet.setDelivery(ld);

        String result = "Prochaine livraison : R1\n\n";
        result += "Prendre la rue R1\n\n";
        result += "Arrivée à la livraison : R1\n\n***\n\n";
        result += "Prochaine livraison : R2\n\n";
        result += "Dans 1 mètres : \n";
        result += "Prenez à gauche sur la rue R2\n\n";
        result += "Arrivée à la livraison : R2\n\n***\n\n";
        result += "Prochaine livraison : R4\n\n";
        result += "Dans 1 mètres : \n";
        result += "Faites demi-tour sur la rue -R2\n\n";
        result += "Dans 1 mètres : \n";
        result += "Prenez à gauche sur la rue R3\n\n";
        result += "Dans 1 mètres : \n";
        result += "Faites demi-tour sur la rue -R3\n\n";
        result += "Dans 1 mètres : \n";
        result += "Prenez à gauche sur la rue R4\n\n";
        result += "Arrivée à la livraison : R4\n\n***\n\n";
        result += "Dans 1 mètres : \n";
        result += "Prenez à gauche sur la rue R5\n\n";
        StringWriter sw = new StringWriter();
        try {
            sheet.export(sw);
        } catch (IOException ex) {
        }
        assertEquals(result, sw.toString());
    }


    public void testExportNoPath() {
        // Création d'un chemin basique
        HashMap<Delivery, List<RoadNode>> map = new HashMap<>();
        List<RoadNode> path = new ArrayList<>();
        path.add(new RoadNode(0));
        DeliverySheet sheet = new DeliverySheet();
        sheet.setDeliveryRound(map);

        String result = "";
        StringWriter sw = new StringWriter();
        try {    
            try {
                sheet.export(sw);
            } catch (IOException ex) {
            }
        } 
        catch (NullPointerException e) {
        }
    }
}
