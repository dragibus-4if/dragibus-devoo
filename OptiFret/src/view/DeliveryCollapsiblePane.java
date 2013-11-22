/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Delivery;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.VerticalLayout;
/**
 *
 * @author Quentin
 */
public class DeliveryCollapsiblePane extends JPanel{
    
private JPanel minimal;
private JXCollapsiblePane extend;

    public DeliveryCollapsiblePane() {
                
        //creation de minimal
        
        //DEBUG
        JLabel deliveryLabel = new JLabel("Livraison : "+"nomLivraison");
        JLabel idLabel = new JLabel("ID : "+"idLivraison");
        //DEBUG
        
        minimal = new JPanel();
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        minimal.add(deliveryLabel);
        minimal.add(idLabel);
        minimal.setBackground(Color.red);
        minimal.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                extend.getActionMap().get("toggle").actionPerformed(new ActionEvent(e, WIDTH, TOOL_TIP_TEXT_KEY));
                
                System.out.println("youhou");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        //creation de extend
        //DEBUG
        JLabel id = new JLabel("ID : "+"123456789");
        JLabel id1 = new JLabel("ID : "+"123456789");
        JLabel id2 = new JLabel("ID : "+"123456789");
        //DEBUG
        
        extend = new JXCollapsiblePane();
        extend.setLayout(new BorderLayout());
        extend.add(id, BorderLayout.PAGE_END);
        extend.add(id1, BorderLayout.PAGE_START);
        extend.add(id2, BorderLayout.CENTER);
        
        //creation du panel principal
        setLayout(new VerticalLayout());
        add(minimal);
        add(extend);
        this.validate();
        
        
    }

    public DeliveryCollapsiblePane(Delivery aDelivery) {
        
        JLabel deliveryLabel = new JLabel("Livraison : "+aDelivery.getId());
        
        
        minimal = new JPanel();
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        minimal.add(deliveryLabel);
        
        minimal.setBackground(Color.red);
        minimal.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                extend.getActionMap().get("toggle").actionPerformed(new ActionEvent(e, WIDTH, TOOL_TIP_TEXT_KEY));
                
                System.out.println("youhou");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void makeMinimal(Delivery aDelivery){
        
    }
    
    
    
}
