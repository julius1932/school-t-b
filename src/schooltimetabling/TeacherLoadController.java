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
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;

public class TeacherLoadController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXComboBox teacher;

    @FXML
    private JFXComboBox subject;

    @FXML
    private TableView<TeacherSubjectKlass> table;

    @FXML
    private TableColumn klassColumn;

    @FXML
    private TableColumn subjectColumn;
    @FXML
    private AnchorPane giveloadAcrchPane;
    @FXML
    private TableColumn teacherColumn;
    @FXML
    private JFXComboBox searchKeyCombo;

    @FXML
    private JFXComboBox searchComboValue;
    private HashMap<JFXCheckBox, Klass> allKlassesCheckBoxs = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacher.getItems().setAll(getTeachers());
        subject.getItems().setAll(getSubjects());
        initCheckBoxs();
        ObservableList<String> options = FXCollections.observableArrayList("Class", "Subject", "Teacher");
        searchKeyCombo.getItems().addAll(options);
        klassColumn.setCellValueFactory(new PropertyValueFactory<>("klass"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        
        final ContextMenu menu = new ContextMenu();
        final MenuItem delet = new MenuItem("Delete");
        delet.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    TeacherSubjectKlass sbtr = (TeacherSubjectKlass) table.getSelectionModel().getSelectedItem();

                    try (Session session = HibernateCentre.getHibernateSession();) {
                        session.beginTransaction();
                        session.delete(sbtr);
                        session.getTransaction().commit();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        final MenuItem changeTr = new MenuItem("Change Teacher");
        changeTr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TeacherSubjectKlass trSubKlas = (TeacherSubjectKlass) table.getSelectionModel().getSelectedItem();
                if (trSubKlas == null) {
                    System.out.println("null ");
                    return;
                }
                changeTeacherDialog(trSubKlas);
            }
        });
        changeTr.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        delet.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll(changeTr, delet);
        table.setContextMenu(menu);
    }

    @FXML
    void searchKeyOnAction(ActionEvent event) {
        String searchKey = searchKeyCombo.getValue().toString().toLowerCase().trim();
        switch (searchKey) {
            case "class":
                searchComboValue.getItems().setAll(Klass.getAllKlasses());
                break;
            case "subject":
                searchComboValue.getItems().setAll(Subject.getAllSubjects());
                break;
            case "teacher":
                searchComboValue.getItems().setAll(Teacher.getAllTeachers());
                break;
            default:
                searchComboValue.getItems().clear();
                break;
        }
    }
   public void showSubLoad(Subject sub){
       String qry = "from TeacherSubjectKlass where  subject = '" + sub.getCode() + "' order by klass";
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<TeacherSubjectKlass> trs = FXCollections.observableArrayList();
            List<TeacherSubjectKlass> list = session.createQuery(qry).list();
            table.getItems().setAll(list);
        }
   }
    @FXML
    void showButtonOnAction(ActionEvent event) {
        if (searchKeyCombo.getValue() == null && searchComboValue.getValue() == null) {
            DialogManager.showErrorMassageDialoge("Fill in all fields");
            return;
        }
        String searchKey = searchKeyCombo.getValue().toString().toLowerCase().trim();
        if (searchKey.equals("class")) {
            searchKey = "klass";
        }
        Object object = searchComboValue.getValue();
        String searchValue = "";
        switch (searchKey) {
            case "klass":
                searchValue = ((Klass) object).getId().toString();
                break;
            case "subject":
                searchValue = ((Subject) object).getCode();
                break;
            case "teacher":
                searchValue = ((Teacher) object).getId().toString();
                break;
            default:
                return;
        }
        String qry = "from TeacherSubjectKlass where " + searchKey + " = '" + searchValue + "'";
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<TeacherSubjectKlass> trs = FXCollections.observableArrayList();
            List<TeacherSubjectKlass> list = session.createQuery(qry).list();
            table.getItems().setAll(list);
        }
    }

    private void initCheckBoxs() {
        long level = 1;
        allKlassesCheckBoxs = new HashMap<>();
        ObservableList<Klass> allKlasses = Klass.getAllKlasses();
        int x = 235;
        int y = 239;
        for (Klass klas : allKlasses) {
            if (level != klas.getLevel()) {
                level = klas.getLevel();
                x = 235;
                y += 30;
            }
            JFXCheckBox jfxCheckBox = new JFXCheckBox(klas.toString());
            
            jfxCheckBox.setLayoutX(x);
            jfxCheckBox.setLayoutY(y);
            giveloadAcrchPane.getChildren().add(jfxCheckBox);
            allKlassesCheckBoxs.put(jfxCheckBox, klas);
            x += 60;
            Separator sa=new Separator(Orientation.VERTICAL);
            sa.setPrefHeight(30);
            sa.setLayoutX(x);
            sa.setLayoutY(y);
            giveloadAcrchPane.getChildren().add(sa);
        }
    }

    private void changeTeacherDialog(TeacherSubjectKlass trSubKlas) {
        if (trSubKlas == null) {
            return;
        }

        Stage newStage = new Stage();
        newStage.setResizable(false);
        Pane rty = new Pane();
        rty.setMinSize(500, 200);
        JFXComboBox newTrCombo = new JFXComboBox();
        newTrCombo.setPromptText("Teacher");
        newTrCombo.getItems().setAll(this.getTeachers());
        newTrCombo.setLabelFloat(true);
        newTrCombo.setMinSize(300, 25);
        newTrCombo.setLayoutX(100);
        newTrCombo.setLayoutY(50);
        Button bbtn = new Button();
        bbtn.setLayoutX(100);
        bbtn.setLayoutY(150);
        bbtn.setText("Save");
        bbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Teacher newTr = (Teacher) newTrCombo.getSelectionModel().getSelectedItem();
                if (newTr == null) {
                    System.out.println(" newTr is null");
                    return;
                }
                trSubKlas.setTeacher(newTr);
                try (Session session = HibernateCentre.getHibernateSession();) {
                    session.beginTransaction();
                    session.update(trSubKlas);
                    session.getTransaction().commit();
                }
                table.refresh();
                newStage.close();
            }
        });
        Button bbcl = new Button();
        bbcl.setLayoutX(300);
        bbcl.setLayoutY(150);
        bbcl.setText("Cancel");
        bbcl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newStage.close();
            }
        });

        rty.getChildren().add(newTrCombo);
        rty.getChildren().add(bbcl);
        rty.getChildren().add(bbtn);

        Scene dialogScene = new Scene(rty, 500, 200);
        newStage.setScene(dialogScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle("Change Teacher for : " + trSubKlas);
        newStage.showAndWait();
    }

    @FXML
    void printAlloctions(ActionEvent event) {
        ObservableList<TeacherSubjectKlass> items = table.getItems();
        if(items.isEmpty()){
           return;
        }
         if (searchKeyCombo.getValue() == null && searchComboValue.getValue() == null) {
            DialogManager.showErrorMassageDialoge("Fill in all fields");
            return;
        }
        String theSubject=searchComboValue.getValue().toString();
        List<ClassAllocations> mylist= new ArrayList<>();
        items.stream().forEach((trSb) -> {
            mylist.add(new ClassAllocations( trSb));
        });
        HashMap<String, String> map = new HashMap<>();
        map.put("theSubject", theSubject);
      new ReportsFilling().fillClassAllocations( mylist,map);
    }

    private ObservableList<Teacher> getTeachers() {
        return Teacher.getAllTeachers();
    }

    private ObservableList<Subject> getSubjects() {
        return Subject.getAllSubjects();
    }

     

    @FXML
    void clearFields(ActionEvent event) {
        for (Map.Entry<JFXCheckBox, Klass> entry : allKlassesCheckBoxs.entrySet()) {
            entry.getKey().setSelected(false);
        }
    }

    @FXML
    void saveTeacherLoad(ActionEvent event) {
        Teacher tr = (Teacher) teacher.getValue();
        Subject sub = (Subject) subject.getValue();
        Boolean saved = false;
        try (Session session = HibernateCentre.getHibernateSession();) {
            for (Map.Entry<JFXCheckBox, Klass> entry : allKlassesCheckBoxs.entrySet()) {
                JFXCheckBox checkBx = entry.getKey();
                Klass klas = entry.getValue();
                if (checkBx.isSelected()) {
                    TeacherSubjectKlass trSb = TeacherSubjectKlass.getTeacherSubjectKlass(sub, klas);
                    if (trSb == null) {
                        trSb = new TeacherSubjectKlass(tr, sub, klas);
                    } else {
                        trSb.setTeacher(tr);
                    }
                    session.getTransaction().begin();
                    session.saveOrUpdate(trSb);
                    session.getTransaction().commit();
                    showSubLoad(sub);
                    saved = true;
                }
            }
        } catch (Exception ee) {
            DialogManager.showErrorMassageDialoge(ee.getMessage());
        } finally {
            if (saved) {
                clearFields(event);
                DialogManager.showDialog(MainPageController.stackPane, "Data saved");
            }
        }
    }

}
