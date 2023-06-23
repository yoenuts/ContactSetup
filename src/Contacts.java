
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class Contacts {
    private int cID;
    private String fName;
    private String lName;
    private int contactNo;
    private Date dateCreated;
    private Date dateModified;
    
    
    
    public Contacts(){
        
    }
    
    public Contacts(int cID, String fName, String lName, int contactNo, Date dateCreated){
        this.cID = cID;
        this.fName = fName;
        this.lName = lName;
        this.contactNo = contactNo;
        this.dateCreated = dateCreated;
        
    }
    
    public Contacts(int cID, String fName, String lName, int contactNo, Date dateCreated, Date dateModified){
        this.cID = cID;
        this.fName = fName;
        this.lName = lName;
        this.contactNo = contactNo;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        
    }
    
    public int getID(){
        return cID;
    }
    
}
