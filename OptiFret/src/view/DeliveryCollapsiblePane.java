/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
        JLabel deliveryLabel = new JLabel("Livraison : "+"nomLivraison");
        JLabel idLabel = new JLabel("ID : "+"idLivraison");
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
        JLabel id = new JLabel("ID : "+"123456789");
        JLabel id1 = new JLabel("ID : "+"123456789");
        JLabel id2 = new JLabel("ID : "+"123456789");
        
        extend = new JXCollapsiblePane();
        extend.setLayout(new BorderLayout());
        extend.add(id, BorderLayout.PAGE_END);
        extend.add(id1, BorderLayout.PAGE_START);
        extend.add(id2, BorderLayout.CENTER);
        
        id.setBackground(Color.green);
        id1.setBackground(Color.green);
        id2.setBackground(Color.green);
        //creation du panel principal
        setLayout(new VerticalLayout());
        add(minimal);
        add(extend);
        setBackground(Color.BLUE);
        setSize(getPreferredSize());
        
    }


    
    
    
    
}
