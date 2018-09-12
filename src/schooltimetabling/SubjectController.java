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

public class SubjectController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab addSubTab;
    @FXML
    private JFXTextField code;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField subSearch;
    @FXML
    private Label label;

    @FXML
    private TableView table;
    @FXML
    private TableColumn<Subject, String> codeColumn;
    @FXML
    private TableColumn<Subject, String> nameColumn;
    private Subject subToEdit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Validation.validateTextField(code);
        Validation.validateTextField(name);
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        final ContextMenu menu = new ContextMenu();
        final MenuItem editTr = new MenuItem("Edit Subject");
        editTr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                subToEdit = (Subject) table.getSelectionModel().getSelectedItem();
                name.setText(subToEdit.getName());
                code.setText(subToEdit.getCode());
                label.setText("Editing .....   " + subToEdit);
                code.setEditable(false);
                tabPane.getSelectionModel().select(addSubTab);
            }
        });
        final MenuItem delTr = new MenuItem("Delete  Subject");
        delTr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Subject sbjct = (Subject) table.getSelectionModel().getSelectedItem();
                if (DialogManager.showConformationDialoge("Do you want to delete " + sbjct + " ?")) {
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        session.beginTransaction();
                        session.delete(sbjct);
                        session.getTransaction().commit();
                        table.getItems().remove(sbjct);
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
        table.setItems(getSubjects());
    }

    @FXML
    void clearFields(ActionEvent event) {
        name.clear();
        code.clear();
        label.setText("");
        subToEdit = null;
        code.setEditable(true);
    }

    @FXML
    void saveSubjects(ActionEvent event) {
        if (!name.validate() || !code.validate()) {
            return;
        }
        String nam = name.getText();
        String cod = code.getText();
        Subject sub;
        if (subToEdit == null) {
            sub = new Subject(cod, nam);
        } else {
            sub = subToEdit;
            sub.setName(nam);
        }

        if (!sub.isValid()) {
            System.out.println("subject not valid");
            DialogManager.showErrorMassageDialoge("subject not valid");
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            session.beginTransaction();
            session.saveOrUpdate(sub);
            session.getTransaction().commit();
            session.close();
            clearFields(event);
            if (subToEdit == null) {
                table.getItems().add(sub);
            }else{
                code.setEditable(true);
            }
            table.refresh();
            subToEdit = null;
            label.setText("");
            DialogManager.showDialog(MainPageController.stackPane, "Data saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Subject> getSubjects() {
        return Subject.getAllSubjects();
    }

    @FXML
    public void searchSubsActionButton(ActionEvent event) {
        String sub = subSearch.getText().trim();
        if (sub.isEmpty()) {
            table.setItems(getSubjects());
        } else {
            try (Session session = HibernateCentre.getHibernateSession();) {
                String sqlQuery = "from Subject where name like  '%" + sub + "%' order by name";
                ObservableList<Subject> subzs = FXCollections.observableArrayList(session.createQuery(sqlQuery).list());
                table.setItems(subzs);
            }
        }
    }

}
