/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import com.jfoenix.controls.JFXButton;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 *
 * @author Junta
 */
public class MainPageController implements Initializable {

    @FXML
    private ScrollPane mainScrollPane;

    public static StackPane stackPane;
    public static boolean inludeAssembly=false;
    @FXML
    private CheckMenuItem showAsRegCheckMenuItem;
    @FXML
    private StackPane skP;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            stackPane =skP;
            AnchorPane anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/schooling.fxml"));
            anchorPane.minWidthProperty().bind(mainScrollPane.widthProperty());
            anchorPane.minHeightProperty().bind(mainScrollPane.heightProperty());
            mainScrollPane.setContent(anchorPane);
        } catch (IOException ex) {
            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
    }
    @FXML
    public void showAsRegCheckMenuItemAction(ActionEvent event){
        inludeAssembly = showAsRegCheckMenuItem.isSelected();
    }
    @FXML
    public void changeContentOfMainPage(ActionEvent event) {
        System.out.println("oioioiioioioi");
        try {
            JFXButton node = (JFXButton) event.getSource();
            if (node.getAccessibleText() != null) {
                try {
                    AnchorPane anchorPane = null;
                    switch (node.getAccessibleText()) {
                        case "Subjects":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/Subjects.fxml"));
                            break;
                        case "Klasses":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/Klasses.fxml"));
                            break;
                        case "Teachers":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/Teachers.fxml"));
                            break;
                        case "Load":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/TeachersLoad.fxml"));
                            break;
                        case "Constrains":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/constrains.fxml"));
                            break;
                        case "Timetable":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/TimeTableDisplay.fxml"));
                            break;
                        case "Schools":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/schooling.fxml"));
                             break;
                        case "yearNew":
                            anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("FXML/newYear.fxml"));
                             break;
                            
                    }
                    if (anchorPane != null) {
                        anchorPane.minWidthProperty().bind(mainScrollPane.widthProperty());
                        anchorPane.minHeightProperty().bind(mainScrollPane.heightProperty());
                        mainScrollPane.setContent(anchorPane);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
