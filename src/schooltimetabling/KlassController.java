/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

/**
 *
 * @author Junta
 */
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.hibernate.Session;

public class KlassController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField division;

    @FXML
    private JFXComboBox level;

    @FXML
    private TableView<Klass> table;
    @FXML
    private TableColumn<Klass, Integer> levelColumn;

    @FXML
    private TableColumn<Klass, String> divColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 1; i < 7; i++) {
            level.getItems().add(i);
        }
        Validation.validateTextField(division);
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        divColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        table.setItems(getKlasses());
    }

    @FXML
    void clearFields(ActionEvent event) {

    }

    @FXML
    void saveSubjects(ActionEvent event) {
        int levl = (int) level.getValue();
        String divs = division.getText();
        Klass klas = new Klass(levl, divs);

        try (Session session = HibernateCentre.getHibernateSession();) {
            session.beginTransaction();
            session.save(klas);
            session.getTransaction().commit();
            session.close();
            clearFields(event);
            table.getItems().add(klas);
            DialogManager.showDialog(MainPageController.stackPane, "Data saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Klass> getKlasses() {
        ObservableList<Klass> klasses = FXCollections.observableArrayList();
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<Klass> list = session.createQuery("from Klass").list();
            klasses.addAll(list);
            return klasses;
        }
    }
}
