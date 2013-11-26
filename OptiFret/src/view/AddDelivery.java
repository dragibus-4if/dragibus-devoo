/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import model.Client;
import model.Delivery;
import model.TimeSlot;


/**
 *Cette fenetre a pour but de permettre à l'utilisateur de créer une livraison
 * 
 * Une livraison est constituée d'un identifiant, d'une addresse, d'un client 
 * et d'une plage horaire
 * 
 * L'on considère l'addresse connue ( ce formulaire est appellé en cliquant
 * sur un point de la carte pour y ajouter une livraison )
 * 
 * L'on considère les clients également connus ( la fenetre permet à l'utilisateur
 * le client correspondant dans une liste)
 * 
 * La fenetre permet à l'utilisateur de définir une plage horaire.
 * 
 * L'identifiant de la livraison crée n'est pas défini
 * 
 * 
 * La fenetre lancera un evenement PropertyChangeEvent lors de la validation de 
 * l'utilisateur, qui contiendra l'objet Delivery créé dans l'attribut new Value
 * de l'évènement.
 */
public class AddDelivery extends javax.swing.JFrame {
    
    private Long address;
    private Calendar gCalendar;
    private Collection<Client> clients;
    private Delivery delivery;
    

    /**
     * 
     * 
     * Creates new form AddDelivery
     */
    public AddDelivery( Long address, Collection<Client> clients ) {
        this.address = address;
        
        String [] ids = TimeZone.getAvailableIDs(1 * 60 * 60 * 1000);
        SimpleTimeZone pdt = new SimpleTimeZone(1 * 60 * 60 * 1000, ids[0]);
        pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        Calendar calendarStart = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendarStart.setTime(trialTime);
        this.gCalendar = calendarStart;
        this.clients = clients;
        
        
        initComponents();
    }
    
    public String dateToStringFr( Date d){
        gCalendar.setTime ( d );
        String res = new String ( );
        
        res += gCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.FRANCE );
        res += ", ";
        res += gCalendar.get(Calendar.DAY_OF_MONTH);
        res += " " + gCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.FRANCE );
        res += " " + gCalendar.get( Calendar.YEAR);
        
        return res;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        dateEndTextField = new javax.swing.JTextField();
        labelDateFieldEnd = new javax.swing.JLabel();
        labelDateFieldStart = new javax.swing.JLabel();
        dateStartTextField = new javax.swing.JTextField();
        hourStartTextField = new javax.swing.JTextField();
        hourEndTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DefaultListModel<Client> listModel = new DefaultListModel<Client>();
        Iterator<Client> it = this.clients.iterator();
        while ( it.hasNext()){
            listModel.addElement(it.next());
        }
        listeClients = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        validationButton = new javax.swing.JButton();
        calendarStartButton = new org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton();
        calendarEndButton = new org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton();
        timeButtonBegin = new org.jbundle.thin.base.screen.jcalendarbutton.JTimeButton();
        timeButtonEnd = new org.jbundle.thin.base.screen.jcalendarbutton.JTimeButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dateEndTextField.setEditable(false);

        labelDateFieldEnd.setText("Date fin");

        labelDateFieldStart.setText("Date début");

        dateStartTextField.setEditable(false);

        hourStartTextField.setEditable(false);

        hourEndTextField.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Création d'une nouvelle livraison");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Plage horaire :");

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setText(" ");

        listeClients.setModel(listModel);
        listeClients.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(listeClients);
        if ( listeClients.getModel().getSize()>=1){
            listeClients.setSelectedIndex(0);
        }

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Client :");

        cancelButton.setText("Annuler");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        validationButton.setText("Valider");
        validationButton.setEnabled(false);
        validationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validationButtonActionPerformed(evt);
            }
        });

        calendarStartButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calendarStartButtonPropertyChange(evt);
            }
        });

        calendarEndButton.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calendarEndButtonPropertyChange(evt);
            }
        });

        timeButtonBegin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                timeButtonBeginPropertyChange(evt);
            }
        });

        timeButtonEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                timeButtonEndPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(293, 293, 293))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelDateFieldEnd)
                                .addComponent(labelDateFieldStart)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(152, 152, 152)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(hourStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hourEndTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(dateEndTextField)
                                        .addComponent(dateStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(calendarStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(calendarEndButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timeButtonBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timeButtonEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(334, 334, 334)
                .addComponent(validationButton)
                .addGap(65, 65, 65)
                .addComponent(cancelButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelDateFieldStart)
                        .addGap(53, 53, 53)
                        .addComponent(labelDateFieldEnd))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(calendarStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hourStartTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeButtonBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateEndTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(calendarEndButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hourEndTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeButtonEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel)
                .addGap(68, 68, 68)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(validationButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        calendarStartButton.setTargetDate(new Date());
        calendarEndButton.setTargetDate(new Date());
        timeButtonBegin.setTargetDate(new Date());
        timeButtonEnd.setTargetDate(new Date(new Date().getTime()+60000));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calendarEndButtonPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_calendarEndButtonPropertyChange
       Object o = evt.getNewValue();
       Date selectedDate;
       if ( !evt.getPropertyName().equals("ancestor") && evt.getNewValue() instanceof Date ){
            selectedDate = (Date) o;
            dateEndTextField.setText( dateToStringFr( selectedDate ) ) ;
                }
        else{
           selectedDate = new Date();
            dateEndTextField.setText( dateToStringFr( new Date() ) ) ;
        }
        
        Date startDate;
        if ( calendarStartButton.getTargetDate() != null ){
            startDate = calendarStartButton.getTargetDate();
        }
        else{
            startDate = new Date();
        }
        Date startTime;
        if ( timeButtonBegin.getTargetDate() != null ){
            startTime = timeButtonBegin.getTargetDate();
        }
        else{
            startTime = new Date();
        }
         Date endTime;
        if ( timeButtonEnd.getTargetDate() != null ){
            endTime = timeButtonEnd.getTargetDate();
        }
        else{
            endTime = new Date(new Date().getTime()+60000);
        }
        if ( ! isValidTimeSlot( startDate, startTime, selectedDate, endTime) ){
            errorLabel.setText("Erreur : la date de fin de la plage horaire doit être postérieure à sa date de début");
            this.validationButton.setEnabled(false);
        }
        else{
            errorLabel.setText("");
            this.validationButton.setEnabled(true);
        }
    }//GEN-LAST:event_calendarEndButtonPropertyChange

    private void calendarStartButtonPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_calendarStartButtonPropertyChange
       
        Object o = evt.getNewValue();
        Date selectedDate;
        if ( !evt.getPropertyName().equals("ancestor") && evt.getNewValue() instanceof Date ){
            selectedDate = (Date) o;
            dateStartTextField.setText( dateToStringFr( selectedDate ) ) ;
                }
        else{
            selectedDate = new Date();
            dateStartTextField.setText( dateToStringFr( new Date( ) ) ) ;
        }
        
        Date endDate;
        if ( calendarEndButton.getTargetDate() != null ){
            endDate = calendarEndButton.getTargetDate();
        }
        else{
            endDate = new Date();
        }
        Date startTime;
        if ( timeButtonBegin.getTargetDate() != null ){
            startTime = timeButtonBegin.getTargetDate();
        }
        else{
            startTime = new Date();
        }
         Date endTime;
        if ( timeButtonEnd.getTargetDate() != null ){
            endTime = timeButtonEnd.getTargetDate();
        }
        else{
            endTime = new Date(new Date().getTime()+60000);
        }
        
        if ( ! isValidTimeSlot( selectedDate, startTime, endDate, endTime ) ){
            errorLabel.setText("Erreur : la date de fin de la plage horaire doit être postérieure à sa date de début");
            this.validationButton.setEnabled(false);
        }
        else{
            errorLabel.setText("");
            this.validationButton.setEnabled(true);
        }
        
        
    }//GEN-LAST:event_calendarStartButtonPropertyChange

    private void timeButtonBeginPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_timeButtonBeginPropertyChange

        
        Object o = evt.getNewValue();
        if ( !evt.getPropertyName().equals("ancestor") && evt.getNewValue() instanceof Date ){
            Date time = (Date) o;
            
            this.gCalendar.setTime( time );
            String displayedTime = "";
            displayedTime += this.gCalendar.get(Calendar.HOUR_OF_DAY);
            displayedTime += "h";
            displayedTime += this.gCalendar.get(Calendar.MINUTE);
            hourStartTextField.setText ( displayedTime );
            
            Date endDate;
            if ( calendarEndButton.getTargetDate() != null ){
                endDate = calendarEndButton.getTargetDate();
            }
            else{
                endDate = new Date();
            }
            Date startDate;
            if ( calendarStartButton.getTargetDate() != null ){
                startDate = calendarStartButton.getTargetDate();
            }
            else{
                startDate = new Date();
            }
            Date endTime;
            if ( timeButtonEnd.getTargetDate() != null ){
                endTime = timeButtonEnd.getTargetDate();
            }
            else{
                endTime = new Date(new Date().getTime()+60000);
            }
            
            if ( ! isValidTimeSlot( startDate, time, endDate, endTime) ){
                errorLabel.setText("Erreur : la date de fin de la plage horaire doit être postérieure à sa date de début");
                this.validationButton.setEnabled(false);
            }
            else{
                errorLabel.setText("");
                this.validationButton.setEnabled(true);
            }
        }
        else{
            Date time = new Date( );
            
            this.gCalendar.setTime( time );
            String displayedTime = "";
            displayedTime += this.gCalendar.get(Calendar.HOUR_OF_DAY);
            displayedTime += "h";
            if (this.gCalendar.get(Calendar.MINUTE) <10 ){
                displayedTime += "0";
            }
            displayedTime += this.gCalendar.get(Calendar.MINUTE);
            hourStartTextField.setText ( displayedTime );
        }
    }//GEN-LAST:event_timeButtonBeginPropertyChange

    private void timeButtonEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_timeButtonEndPropertyChange
       Object o = evt.getNewValue();
        if ( !evt.getPropertyName().equals("ancestor") && evt.getNewValue() instanceof Date ){
            Date time = (Date) o;
            
            this.gCalendar.setTime( time );
            String displayedTime = "";
            displayedTime += this.gCalendar.get(Calendar.HOUR_OF_DAY);
            displayedTime += "h";
            displayedTime += this.gCalendar.get(Calendar.MINUTE);
            hourEndTextField.setText ( displayedTime );
            
            Date endDate;
            if ( calendarEndButton.getTargetDate() != null ){
                endDate = calendarEndButton.getTargetDate();
            }
            else{
                endDate = new Date();
            }
            Date startDate;
            if ( calendarStartButton.getTargetDate() != null ){
                startDate = calendarStartButton.getTargetDate();
            }
            else{
                startDate = new Date();
            }
            Date startTime;
            if ( timeButtonBegin.getTargetDate() != null ){
                startTime = timeButtonBegin.getTargetDate();
            }
            else{
                startTime = new Date();
            }
            
            if ( ! isValidTimeSlot( startDate, startTime, endDate, time) ){
                errorLabel.setText("Erreur : la date de fin de la plage horaire doit être postérieure à sa date de début");
                this.validationButton.setEnabled(false);
            }
            else{
                errorLabel.setText("");
                this.validationButton.setEnabled(true);
            }
        }
        else{
            Date time = new Date(new Date ( ).getTime()+60000);
            
            this.gCalendar.setTime( time );
            String displayedTime = "";
            displayedTime += this.gCalendar.get(Calendar.HOUR_OF_DAY);
            displayedTime += "h";
            if (this.gCalendar.get(Calendar.MINUTE) <10 ){
                displayedTime += "0";
            }
            displayedTime += this.gCalendar.get(Calendar.MINUTE);

            hourEndTextField.setText ( displayedTime );
        }
    }//GEN-LAST:event_timeButtonEndPropertyChange

    private void validationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validationButtonActionPerformed
         Object c = listeClients.getSelectedValue();
        Client client;
        if ( c instanceof Client ){
            client = (Client)c;
        }
        else{
            client = new Client();
        }
        TimeSlot timeSlot = getTimeSlot();
        Delivery del = new Delivery(address);
        del.setClient( client );
        del.setAddress( address );
        del.setTimeSlot( timeSlot );
        this.delivery = del;
        this.firePropertyChange("delivery", null, del);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_validationButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
       this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    public Delivery getDelivery(){
        return this.delivery;
    }
    
    private Date getEndDate( Date endDate, Date endTime ){
        String [] ids = TimeZone.getAvailableIDs(1 * 60 * 60 * 1000);
        SimpleTimeZone pdt = new SimpleTimeZone(1 * 60 * 60 * 1000, ids[0]);
        pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        
        Calendar calendarEnd = new GregorianCalendar(pdt);
       
        calendarEnd.setTime(endDate);
        Calendar calendarTimeEnd = new GregorianCalendar(pdt);
        
        calendarTimeEnd.setTime(endTime);
        calendarEnd.set( Calendar.HOUR_OF_DAY , calendarTimeEnd.get( Calendar.HOUR_OF_DAY));
        calendarEnd.set( Calendar.MINUTE , calendarTimeEnd.get( Calendar.MINUTE));
        return ( calendarEnd.getTime());
    }
    
    private Date getStartDate( Date startDate, Date startTime ){
        String [] ids = TimeZone.getAvailableIDs(1 * 60 * 60 * 1000);
        SimpleTimeZone pdt = new SimpleTimeZone(1 * 60 * 60 * 1000, ids[0]);
        pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        
        Calendar calendarStart = new GregorianCalendar(pdt);
        
        calendarStart.setTime(startDate);
        Calendar calendarTimeStart = new GregorianCalendar(pdt);
       
        calendarTimeStart.setTime(startTime);
        calendarStart.set( Calendar.HOUR_OF_DAY , calendarTimeStart.get( Calendar.HOUR_OF_DAY));
        calendarStart.set( Calendar.MINUTE , calendarTimeStart.get( Calendar.MINUTE));
        return calendarStart.getTime();
    }
    
    private boolean isValidTimeSlot( Date startDate, Date startTime, Date endDate, Date endTime ){
        
        return ( getEndDate( endDate, endTime ).after( getStartDate( startDate, startTime )));
        
    }
    
   /** it is supposed that the current dates are valid ( ie the end date is after the before date)
     * 
     */
    private TimeSlot getTimeSlot(){
        return new TimeSlot( getStartDate(calendarStartButton.getTargetDate(), 
                timeButtonBegin.getTargetDate() ) , 
                getEndDate( calendarEndButton.getTargetDate(), timeButtonEnd.getTargetDate())
                .getTime() - getStartDate(calendarStartButton.getTargetDate(), 
                timeButtonBegin.getTargetDate()).getTime() );

    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Collection<Client> clients = new ArrayList<Client>();
                Client c1 = new Client (new Long(1), "0642132568", "42 rue des Charettes", "Henri Paul");
                Client c2 = new Client (new Long(2), "0642148568", "43 rue des Tracteurs", "Jean Pierre");
                Client c3 = new Client (new Long(3), "0642786168", "21 rue des Brouettes", "Michel Vincent");
                clients.add(c1);
                clients.add(c2);
                clients.add(c3);
                new AddDelivery(new Long(4), clients).setVisible(true);
            }
        });
    } 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton calendarEndButton;
    private org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton calendarStartButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dateEndTextField;
    private javax.swing.JTextField dateStartTextField;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JTextField hourEndTextField;
    private javax.swing.JTextField hourStartTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelDateFieldEnd;
    private javax.swing.JLabel labelDateFieldStart;
    private javax.swing.JList listeClients;
    private org.jbundle.thin.base.screen.jcalendarbutton.JTimeButton timeButtonBegin;
    private org.jbundle.thin.base.screen.jcalendarbutton.JTimeButton timeButtonEnd;
    private javax.swing.JButton validationButton;
    // End of variables declaration//GEN-END:variables
}
