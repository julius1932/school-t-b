/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
public class SchoolTimeTabling extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Session hibernateSession = HibernateCentre.getHibernateSession();
        hibernateSession.close();
        Parent root = FXMLLoader.load(getClass().getResource("FXML/MainPage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            HibernateCentre.killSessionFactory();    
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
