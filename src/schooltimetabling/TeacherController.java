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
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;

public class TeacherController implements Initializable {

    @FXML
    private JFXTextField surnameSearch;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab addTeacherTab;
    @FXML
    private JFXTextField initial;

    @FXML
    private JFXComboBox<String> title;

    @FXML
    private JFXTextField surname;

    @FXML
    private TableView<Teacher> table;

    @FXML
    private TableColumn<Teacher, String> titleColumn;

    @FXML
    private TableColumn<Teacher, String> surnameColumn;

    @FXML
    private TableColumn<Teacher, String> initialColumn;
    @FXML
    private Label label;

    private static Teacher trToEdit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options = FXCollections.observableArrayList("Mr", "Ms", "Mrs", "Miss", "Dr");
        title.getItems().addAll(options);
        Validation.validateTextField(surname);
        Validation.validateTextField(initial);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        initialColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        final ContextMenu menu = new ContextMenu();
        final MenuItem editTr = new MenuItem("Edit Teacher");
        editTr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                trToEdit = (Teacher) table.getSelectionModel().getSelectedItem();
                initial.setText(trToEdit.getName());
                surname.setText(trToEdit.getSurname());
                title.setValue(trToEdit.getTitle());
                label.setText("Editing .....   " + trToEdit);
                tabPane.getSelectionModel().select(addTeacherTab);
            }
        });
        final MenuItem delTr = new MenuItem("Delete  Teacher");
        delTr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Teacher tr = (Teacher) table.getSelectionModel().getSelectedItem();
                if (DialogManager.showConformationDialoge("Do you want to delete " + tr + " ?")) {
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        session.beginTransaction();
                        session.delete(tr);
                        session.getTransaction().commit();
                        table.getItems().remove(tr);
                        DialogManager.showMassageDialoge("Deleted");
                    } catch (Exception e) {
                        DialogManager.showErrorMassageDialoge("Could not delete .   " + e.getLocalizedMessage());
                    }
                }
            }
        });
        editTr.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        delTr.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll(editTr, delTr);
        table.setContextMenu(menu);
        table.setItems(getTeachers());
    }

    public ObservableList<Teacher> getTeachers() {
        return Teacher.getAllTeachers();
    }

    @FXML
    public void clearFields(ActionEvent event) {
        this.surname.clear();
        this.initial.clear();
    }

    @FXML
    public void saveTeacher(ActionEvent event) {

        if (!initial.validate() || !surname.validate()) {
            return;
        }
        String name = initial.getText();
        String surnam = surname.getText();
        String titl = title.getValue();
        Teacher tr;
        if (trToEdit == null) {
            tr = new Teacher(titl, name, surnam);
        } else {
            tr = trToEdit;
            tr.setName(name);
            tr.setSurname(surnam);
            tr.setTitle(titl);
        }

        try (Session session = HibernateCentre.getHibernateSession();) {
            session.beginTransaction();
            session.saveOrUpdate(tr);
            session.getTransaction().commit();
            session.close();
            if (trToEdit == null) {
                table.getItems().add(tr);
            }
            table.refresh();
            trToEdit = null;
            label.setText("");
            DialogManager.showDialog(MainPageController.stackPane, "Data saved");

            clearFields(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void searchTrsActionButton(ActionEvent event) {
        String surnm = surnameSearch.getText().trim();
        if (surnm.isEmpty()) {
            table.setItems(getTeachers());
        } else {
            try (Session session = HibernateCentre.getHibernateSession();) {
                String sqlQuery = "from Teacher where surname like  '%" + surnm + "%' order by  surname";
                ObservableList<Teacher> trzs = FXCollections.observableArrayList(session.createQuery(sqlQuery).list());
                table.setItems(trzs);
            }
        }
    }

}
