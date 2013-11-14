/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.util.Date;

/**
 *
 * @author Sylvain
 */
public class Delivery {
    private long id;
    private String content;
    private int number;
    private Date date;
    private long idClient;
    private String nameClient;

    public boolean isValid(){
        if(id!=0 && !content.isEmpty() && number!=0 && date!=null  && idClient!=0 && !nameClient.isEmpty()){
            return true;
        }
        return true;
    }
    public Delivery(Long id){
        this.id=id;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContenu() {
        return content;
    }

    public void setContenu(String contenu) {
        this.content = contenu;
    }

    public int getNumero() {
        return number;
    }

    public void setNumero(int numero) {
        this.number = numero;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nameClient;
    }

    public void setNomClient(String nomClient) {
        this.nameClient = nomClient;
    }
   
            
}
