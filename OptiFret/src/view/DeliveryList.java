package view;

import javax.swing.DefaultListModel;

public class DeliveryList {
    private DefaultListModel notPlanned ;

    
    private DefaultListModel planned ;
    
    public DeliveryList() {
            notPlanned= new DefaultListModel();
            planned= new DefaultListModel();
    }
    public DefaultListModel getNotPlanned() {
            return notPlanned;
    }

        public DefaultListModel getPlanned() {
            return planned;
    }
}