/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SchoolsController implements Initializable {

    @FXML
    private JFXTextField emal;

    @FXML
    private JFXTextField school;

    @FXML
    private TableView table;

    @FXML
    private TableColumn schoolColumn;

    @FXML
    private TableColumn emailColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emal"));
        schoolColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        final ContextMenu menu = new ContextMenu();
        final MenuItem commentNcourseWrkItem = new MenuItem("load school");
        commentNcourseWrkItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                School schl = (School) table.getSelectionModel().getSelectedItem();
                School.setTheSchl(schl.getName());
                HibernateCentre.initSessionFactory();
                TimeTableController.timeTable= new TimeTable();
                TimeTable.fillOveralConstrains();
            }
        });
        commentNcourseWrkItem.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll(commentNcourseWrkItem);
        table.setContextMenu(menu);
        this.showSchool(new ActionEvent());
    }
    
    @FXML
    public void clearFields(ActionEvent event) {

    }
    public void saveData(ActionEvent event) {
        String emial = emal.getText();
        String schll = school.getText();
        School.insertSingleRow(schll, emial);
    }
    @FXML
    public void showSchool(ActionEvent event) {
        System.out.println("uuuuuuuuuuuuuuuuuu");
        table.getItems().setAll(School.getDataFormDb());
    }

}
