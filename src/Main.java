
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Main extends Application{
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("contactFXML.fxml"));
            Parent root = loader.load();
            
            // Set the fixed width and height of the Stage
            primaryStage.setWidth(630);
            primaryStage.setHeight(450);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Contacts");
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
        
        
    }
}
