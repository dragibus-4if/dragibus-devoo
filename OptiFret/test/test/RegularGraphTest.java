/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.FileWriter;
import java.io.BufferedWriter;

import java.io.IOException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import model.RoadNetwork;
import model.RoadNode;
import model.RoadSection;
import tsp.RegularGraph;

/**
 *
 * @author julien
 */
public class RegularGraphTest extends TestCase{
    
    
    
    public void testLoadRoadFromNetwork (){
        
        //Création des infos nécessaires à un RegularGraph
        int nbVertices = 100000;
        int max = 1000000;
        int min = 0;
        int[][] cost = null;
        ArrayList<ArrayList<Integer>> succ = new ArrayList<>();
        Map<Integer, RoadNode> index2Node = new TreeMap<>();
        
        
        //On crée un RegularGraph "à la main" (HandMade)
        RegularGraph graphHM = new RegularGraph(nbVertices,max,min,cost,succ,
                index2Node);
        
        //On crée un RoadNetwork vide
        RoadNetwork net = new RoadNetwork(); 
        
        //Test du RegularGraph généré à partir d'un RoadNetwork nul
         try {
            RegularGraph.loadFromRoadNetwork(null);
            fail("Génération d'un RegularGraph à partir d'un RoadNetwork nul");
        } catch (NullPointerException e) {
        }
        
        //Test du RegularGraph créé avec un root nul (RoadNetwork)
         RoadNetwork netNR = new RoadNetwork();
         RegularGraph graphZero = new RegularGraph(0, 0, 0, new int[0][0], 
                 new ArrayList<ArrayList<Integer>>(), 
                 new TreeMap<Integer, RoadNode>());
         RegularGraph graphGZero = RegularGraph.loadFromRoadNetwork(netNR);
         assertEquals(graphZero.getNbVertices(),graphGZero.getNbVertices() );
         assertEquals(graphZero.getMaxArcCost(),graphGZero.getMaxArcCost());
         assertEquals(graphZero.getMinArcCost(),graphGZero.getMinArcCost());
         assertEquals(graphZero.getCost().length ,graphGZero.getCost().length);
         
         
         //Test de deux nodes pointant l'un vers l'autre : cas de base
         RoadNetwork netCB = new RoadNetwork();
         
         RoadNode node1 = new RoadNode(1);
         
         RoadNode node2 = new RoadNode(2);
         node1.addNeighbor(new RoadSection(node1, node2, 1, 88));
         node2.addNeighbor(new RoadSection(node2, node1, 1, 14));
         
         netCB.setRoot(node1);
         
            //Création du graph "à la main" de ce cas
         int[][] costCB = new int [2][2];
         costCB[0][0] = 0;
         costCB[0][1] = 88;
         costCB[1][0] = 14;
         costCB[1][1] = 0;
         ArrayList<ArrayList<Integer>> succCB = 
                 new ArrayList<>();
         ArrayList<Integer> e1 = new ArrayList<>();
         e1.add(1);
         ArrayList<Integer> e2 = new ArrayList<>();
         e2.add(0);
         succCB.add(e1);
         succCB.add(e2);
         Map<Integer, RoadNode> treeCB = new TreeMap<>();
         treeCB.put(1, node1);
         treeCB.put(2, node2);
         
         
         RegularGraph graphCB = new RegularGraph(2, 88, 14, costCB, succCB, 
                 treeCB);
         RegularGraph graphGCB = RegularGraph.loadFromRoadNetwork(netCB);
         assertEquals(graphCB.getNbVertices(),graphGCB.getNbVertices() );
         assertEquals(graphCB.getMaxArcCost(),graphGCB.getMaxArcCost());
         assertEquals(graphCB.getMinArcCost(),graphGCB.getMinArcCost());
         assertEquals(graphCB.getCost()[0][0],graphGCB.getCost()[0][0]);
         assertEquals(graphCB.getCost()[0][1],graphGCB.getCost()[0][1]);
         assertEquals(graphCB.getCost()[1][0],graphGCB.getCost()[1][0]);
         assertEquals(graphCB.getCost()[1][1],graphGCB.getCost()[1][1]);
         assertEquals(graphCB.getSucc(0).length,graphGCB.getSucc(0).length);
         assertEquals(graphCB.getSucc(1).length,graphGCB.getSucc(1).length);
         for(int i = 0 ; i < graphCB.getSucc(0).length ; i++) {
             assertEquals(graphCB.getSucc(0)[i], graphGCB.getSucc(0)[i]);
         }
         for(int i = 0 ; i < graphCB.getSucc(1).length ; i++) {
             assertEquals(graphCB.getSucc(1)[i], graphGCB.getSucc(1)[i]);
         }
    }
    
}
