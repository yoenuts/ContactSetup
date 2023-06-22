/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class ContactFXMLController implements Initializable {
    ObservableList<Contacts> contact = FXCollections.observableArrayList();
    
    private Connection connection;
    private Statement statement;
    private PreparedStatement pst;
    
    @FXML
    private TextField fNameTF;
    @FXML
    private TextField lNameTF;
    @FXML
    private TextField contactTF;
    @FXML
    private TableColumn<Contacts, Integer> personIDColumn;
    @FXML
    private TableColumn<Contacts, String> firstNameColumn;
    @FXML
    private TableColumn<Contacts, String> lastNameColumn;
    @FXML
    private TableColumn<Contacts, Integer> contactNoColumn;
    @FXML
    private TableColumn<Contacts, Date> dateAddedColumn;
    @FXML
    private TableColumn<Contacts, Date> dateModifiedColumn;
    @FXML
    private Button addB;
    @FXML
    private Button updateB;
    @FXML
    private Button deleteB;

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createDatabaseConnection();
        loadTableRecord();
    }    
    
    
    
    void loadTableRecord(){
        
    }
    
    void createDatabaseConnection(){
        
        try{
            DatabaseConnection dbConnection = new DatabaseConnection();
            connection = dbConnection.getDBConnection();
        } catch(Exception erlein){
            erlein.printStackTrace();
        }

    }

    @FXML
    private void addbutton(ActionEvent event) {
    }
    
}
