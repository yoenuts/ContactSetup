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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ContactFXMLController implements Initializable {
    //list for Contact objects
    private ObservableList<Contacts> contact = FXCollections.observableArrayList();
    
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
    private LocalDate currentDate;
    private Date sqlDate;
    private Date dateFilter;
    
    
    private String queryStatement;
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
    @FXML
    private TextField searchTF;
    @FXML
    private Button clearB;
    @FXML
    private ComboBox<String> categoryBox;
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> dateCategory;

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //create connection
        createDatabaseConnection();
        //refresh table
        loadTableRecord();

        //populate combo box in lambda
        
        categoryBox.setOnMouseClicked(e -> {
            // Clear existing items in the positionComboBox
            categoryBox.getItems().clear();

            // Retrieve data from the contacts table
            ObservableList<String> contact = FXCollections.observableArrayList("Last Name", "First Name");
            categoryBox.setItems(contact);
        });
        
        dateCategory.setOnMouseClicked(e -> {
           dateCategory.getItems().clear();
           
            ObservableList<String> dates = FXCollections.observableArrayList("Before", "After");
            dateCategory.setItems(dates);
        });
        

    }    
    
    @FXML
    private void contactTableClicked(MouseEvent event) {
       Contacts contact = contactTable.getSelectionModel().getSelectedItem();

       if (contact != null) {
           int contactID = contact.getID();
           cIDTF.setText(String.valueOf(contactID));

           // Retrieve and display current week and days worked from the database
           try {

               String selectQuery = "SELECT first_name, last_name, contact_no FROM Contacts WHERE ID = ?";
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
               
               
               
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
            
    }
    
    private void loadTableRecord(){
        contact.clear();

        try {
            statement = connection.createStatement();
            String defaultQuery = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts";
            rst = statement.executeQuery(defaultQuery);

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
                

                fNameTF.setText("");
                lNameTF.setText("");
                contactTF.setText("");

                JOptionPane.showMessageDialog(null, "Added Successfully");
                loadTableRecord();
                pst.close();
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
        
        String conID = cIDTF.getText();
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
            
                pst = connection.prepareStatement("DELETE FROM Contacts WHERE ID = ?");
                pst.setInt(1, cID);
            
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Record deleted successfully.");
                lNameTF.setText("");
                fNameTF.setText("");
                contactTF.setText("");
                cIDTF.setText("");
               
                loadTableRecord();
                
                
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No table item selected.");
        }
    }


    
    @FXML
    private void clearAction(ActionEvent event) {
        clear();
    }
    
    //introducing, varargs (...)
    private void executeSearchQuery(String query, String... searchResult){
        contact.clear();
        try{
            pst = connection.prepareStatement(query);
            for (int i = 0; i < searchResult.length; i++) {
                if(){ // check kung date data type tas parse
                    Date date1 = new Date.parse(searchResult[i]);
                    pst.setDate(i + 1, date1);
                }
                else{
                    pst.setString(i + 1, searchResult[i]);
                }
            }
            rst = pst.executeQuery();
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
            contactTable.refresh();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void categoryAction(ActionEvent event) {
        System.out.println("Clicked");
    }

    private void clear(){
        lNameTF.setText("");
        fNameTF.setText("");
        contactTF.setText("");
        cIDTF.setText("");
        searchTF.setText("");
        categoryBox.getSelectionModel().clearSelection();
        dateCategory.getSelectionModel().clearSelection();
        loadTableRecord();
    }
    
    //Filter search
    private void searchB(ActionEvent event) {
        /*
        1.) Search with date only 
        2.) Search using last name and first name
        3.) Search date with category (Before and After)
        4.) Search date with first/last name LIKE
        */

        //get values from datepicker
        LocalDate local = datePicker.getValue();
        dateFilter = Date.valueOf(local);
        
        String categoryValue = categoryBox.getValue();
        
        //get date category from combo box
        String dCategory = dateCategory.getValue();
        System.out.println(dateFilter);
        
        //get contact category from combo box
        String comboValue = categoryBox.getValue();
        
        //get search text field for contents
        String searchQuery = searchTF.getText();
        //if text field is empty then user wants to clear all selected object fields
        if(searchQuery.equals("")){
            clear();
            return;
        }
        
        //check if both date picker and date category not empty
        if(dateFilter != null && dCategory != null){
            queryStatement = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts WHERE ";
            // if there is a date category
            if(!dCategory.equals("") && comboValue != null && !(searchQuery.equals(""))){
                //check kung may laman 
                if(dCategory.equals("Before")){
                    queryStatement += "date_created < ?";
                }
                else if(dCategory.equals("After")){
                    queryStatement += "date_created > ?";
                }
                else{
                    queryStatement += "date_created = ?";
                }

               //check if user is looking for something more specific
                if(comboValue.equals("Last Name")){
                    if((searchQuery.charAt(0) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%' && searchQuery.charAt(0) == '%')){
                        //char letter = searchQuery.charAt(0);
                        queryStatement += " AND last_name LIKE = ?";
                    }
                    else{
                       queryStatement += " AND last_name = ?"; 
                    }
                }
                else if(comboValue.equals("First Name")){
                    if((searchQuery.charAt(0) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%' && searchQuery.charAt(0) == '%')){
                        //char letter = searchQuery.charAt(0);
                        queryStatement += " AND first_name LIKE = ?";
                    }
                    else{
                       queryStatement += " AND first_name = ?"; 
                    }
                }
                
                executeSearchQuery(queryStatement, dCategory, comboValue, searchQuery);
            }
        }
        else if(dateFilter != null && dCategory == null){ //if there is date but no category
            queryStatement = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts WHERE date_created = ?";
            //check if combo box and search box has value next to it
            if(comboValue != null && !(searchQuery.equals(""))){
                
                if(comboValue.equals("Last Name")){
                    if((searchQuery.charAt(0) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%' && searchQuery.charAt(0) == '%')){
                        //char letter = searchQuery.charAt(0);
                        queryStatement += " AND last_name LIKE = ?";
                    }
                    else{
                       queryStatement += " AND last_name = ?"; 
                    }
                }
                else if(comboValue.equals("First Name")){
                    if((searchQuery.charAt(0) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%' && searchQuery.charAt(0) == '%')){
                        //char letter = searchQuery.charAt(0);
                        queryStatement += " AND first_name LIKE = ?";
                    }
                    else{
                       queryStatement += " AND first_name = ?"; 
                    }
                }
                executeSearchQuery(queryStatement, dCategory,searchQuery);
              
            }
        }
        else{
            //usual search name, last name
            if(comboValue.equals("First Name")){
                queryStatement = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts WHERE first_name = ?";
                //check for %
                if((searchQuery.charAt(0) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%' && searchQuery.charAt(0) == '%')){
                    //char letter = searchQuery.charAt(0);
                    queryStatement = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts WHERE first_name LIKE ?";
                
                }
            }
            else if(comboValue.equals("Last Name")){
                queryStatement = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts WHERE first_name = ?";
                //check for %
                if((searchQuery.charAt(0) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%') || (searchQuery.charAt(searchQuery.length() - 1) == '%' && searchQuery.charAt(0) == '%')){
                    //char letter = searchQuery.charAt(0);
                    queryStatement = "SELECT DISTINCT ID, first_name, last_name, contact_no, date_created, date_modified FROM Contacts WHERE first_name LIKE ?";
                }
            }
            
            executeSearchQuery(queryStatement,searchQuery);
        }          
    }

    @FXML
    private void dateCategoryAction(ActionEvent event) {
        System.out.println("I was clicked!");
    }
    

}
