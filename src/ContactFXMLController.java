/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.fxml.Initializable;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class ContactFXMLController implements Initializable {
    //list for Contact objects
    ObservableList<Contacts> contact = FXCollections.observableArrayList();
    
    //Connection
    private Connection connection;
    private Statement statement;
    private PreparedStatement pst;
    ResultSet rst;
    
    //other variables
    private String firstN;
    private String lastN;
    private int contactN;
    private String stringInsert;

    //date object

    LocalDate currentDate;
    Date sqlDate;

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
    @FXML
    private TableView<Contacts> contactTable;
    @FXML
    private TextField cIDTF;

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createDatabaseConnection();
        loadTableRecord();
    }    
    
    @FXML
    private void contactTableClicked(MouseEvent event) {
       Contacts contact = contactTable.getSelectionModel().getSelectedItem();

       if (contact != null) {
           int contactID = contact.getID();
           cIDTF.setText(String.valueOf(contactID));

           // Retrieve and display current week and days worked from the database
           try {

               String selectQuery = "SELECT first_name, last_name, contact_no FROM Contacts WHERE employeeID = ?";
               pst = connection.prepareStatement(selectQuery);
               pst.setInt(1, contactID);

               rst = pst.executeQuery();

               if (rst.next()) {
                   firstN = rst.getString("first_name");
                   lastN = rst.getString("last_name");
                   contactN = rst.getInt("contact_no");
                   fNameTF.setText(firstN);
                   lNameTF.setText(lastN);
                   contactTF.setText(String.valueOf(contactN));
                }   

               pst.close();
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
            
    }
    
    private void loadTableRecord(){
        contact.clear();

        String contactTableView = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts";

        try {
            statement = connection.createStatement();
            rst = statement.executeQuery(contactTableView);

            while (rst.next()) {
                Integer queryID = rst.getInt("ID");
                String queryLName = rst.getString("last_name");
                String queryFName = rst.getString("first_name");
                Integer queryContact = rst.getInt("contact_no");
                Date queryDateCreated = rst.getDate("date_created");
                Date queryDateModified = rst.getDate("date_modified");

                contact.add(new Contacts(queryID, queryLName, queryFName, queryContact, queryDateCreated, queryDateModified));
            }

            personIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            contactNoColumn.setCellValueFactory(new PropertyValueFactory<>("contact_no"));
            dateAddedColumn.setCellValueFactory(new PropertyValueFactory<>("date_created"));
            dateModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("date_modified"));

            contactTable.setItems(contact);

        } catch (SQLException e) {
            
            e.printStackTrace();
        }
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
    private void addAction(ActionEvent event) {
        createDatabaseConnection();
        firstN = fNameTF.getText();
        lastN = lNameTF.getText();
        String sContact = contactTF.getText();
        contactN = Integer.parseInt(sContact);

        currentDate = LocalDate.now();
        sqlDate = Date.valueOf(currentDate);

        if(firstN.equals("") || lastN.equals("") || sContact.equals("")){
            JOptionPane.showMessageDialog(null,"Fill up all info");
        } 
        else{
            try{
                stringInsert = "INSERT INTO Contacts (first_name, last_name, contact_no, date_created) VALUES (?,?,?,?)";
                pst = connection.prepareStatement(stringInsert);
                pst.setString(1, firstN);
                pst.setString(2, lastN);
                pst.setInt(3, contactN);
                pst.setDate(4, sqlDate);

                pst.executeUpdate();
                pst.close();
                connection.close();

                fNameTF.setText("");
                lNameTF.setText("");
                contactTF.setText("");

                JOptionPane.showMessageDialog(null, "Added Successfully");
            } catch(Exception erlein){
                erlein.printStackTrace();
            }
        }
    }

    @FXML
    private void updateAction(ActionEvent event) {
        firstN = lNameTF.getText();
        lastN = fNameTF.getText();
        String sContact = contactTF.getText();
        contactN = Integer.parseInt(sContact);
        LocalDate modifiedDate = LocalDate.now();
        sqlDate = Date.valueOf(modifiedDate);
        String conID = contactTF.getText();
        if(conID.equals("")){
            JOptionPane.showMessageDialog(null,"Select an item from the table.");
        }
        else{
            int cID = Integer.parseInt(conID);
            try{
                pst = connection.prepareStatement("UPDATE Contacts SET first_name = ?, last_name = ?, contact_no = ?,  date_modified = ? WHERE ID = ?");

                pst.setString(1, firstN);
                pst.setString(2, lastN);
                pst.setInt(3, contactN);
                pst.setDate(4, sqlDate);
                pst.setInt(5, cID);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "Record updated hihi");
                lNameTF.setText("");
                fNameTF.setText("");
                contactTF.setText("");
                cIDTF.setText("");

                loadTableRecord();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }

    @FXML
    private void deleteAction(ActionEvent event) {
        Contacts contact = contactTable.getSelectionModel().getSelectedItem();
    
        if (contact != null) {
            int cID = contact.getID();
        
            try {
            
                PreparedStatement pst = connection.prepareStatement("DELETE FROM Contacts WHERE ID = ?");
                pst.setInt(1, cID);
            
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record deleted successfully.");
                pst.close();
                connection.close();

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No table item selected.");
        }
    }
}
