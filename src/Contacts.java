
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class Contacts {
    private int ID;
    private String first_name;
    private String last_name;
    private int contact_no;
    private Date date_created;
    private Date date_modified;
    
    
    
    public Contacts(){
        
    }
    
    public Contacts(int ID, String first_name, String last_name, int contact_no, Date date_created){
        this.ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact_no = contact_no;
        this.date_created = date_created;
        
    }
    
    public Contacts(int ID, String first_name, String last_name, int contact_no, Date date_created, Date date_modified){
        this.ID = ID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact_no = contact_no;
        this.date_created = date_created;
        this.date_modified = date_modified;
    }
    
    public int getID(){
        return ID;
    }
    
    public String getFirst_name(){
        return first_name;
    }
    
    public String getLast_name(){
        return last_name;
    }
    
    public int getContact_no(){
        return contact_no;
    }
    
    public Date getDate_created(){
        return date_created;
    }
    
    public Date getDate_modified(){
        return date_modified;
    }
    
    public void setID(int ID){
        this.ID = ID;
    }
    
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }
    
    public void setLast_name(String last_name){
        this.last_name = last_name;
    }
    
    public void setContact_no(int contact_no){
        this.contact_no = contact_no;
    }
    
    public void setDate_created(Date date_created){
        this.date_created = date_created;
    }
    
    public void setDate_modified(Date date_modified){
        this.date_modified = date_modified;
    }
    
    
}
