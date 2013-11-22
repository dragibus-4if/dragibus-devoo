/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.FlowLayout;
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
private Delivery delivery;
private JLabel idDeliveryLabel;
private JLabel clientAdressLabel;
private JLabel clientPhoneNumLabel;
private JLabel clientNameLabel;


    public DeliveryCollapsiblePane(Delivery aDelivery) {
        delivery = aDelivery;
        
        makeMinimal(aDelivery);
        makeExtend(aDelivery);
        
        setLayout(new VerticalLayout());
        add(makeMinimal(aDelivery));
        add(makeExtend(aDelivery));
        validate();
    }

    private JXCollapsiblePane makeExtend(Delivery aDelivery){
        extend = new JXCollapsiblePane();
        extend.setLayout(new FlowLayout());
        
        clientPhoneNumLabel = new JLabel("Téléphone du client : "+aDelivery.getClient().getPhoneNum());
        clientNameLabel = new JLabel("Client : "+aDelivery.getClient().getName());
        clientAdressLabel = new JLabel("Adresse :"+aDelivery.getClient().getAddress());
        
        extend.add(clientAdressLabel);
        extend.add(clientNameLabel);
        extend.add(clientPhoneNumLabel);
        
        return extend;
    }
    private JPanel makeMinimal(Delivery aDelivery){
                
        idDeliveryLabel = new JLabel("Livraison : "+aDelivery.getId());
        
        
        minimal = new JPanel();
        minimal.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        minimal.add(idDeliveryLabel);
        return minimal;
    }
    
    
    
}
