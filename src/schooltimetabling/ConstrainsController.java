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
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalTime;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;

public class ConstrainsController implements Initializable {

    @FXML
    private TableView overalTable;

    @FXML
    private TableColumn prds;

    @FXML
    private TableColumn b4brk;

    @FXML
    private TableColumn brkTime;

    @FXML
    private TableColumn brkLngth;

    @FXML
    private TableColumn b4lnch;

    @FXML
    private TableColumn lnchTime;

    @FXML
    private TableColumn lnch;

    @FXML
    private TableColumn aftaLnch;

    @FXML
    private TableColumn days;

    @FXML
    private TableColumn strtTime;

    @FXML
    private TableColumn lesnDration;

    @FXML
    private TableView sportsTable;

    @FXML
    private TableColumn sprtyDay;

    @FXML
    private JFXTextField numPeriodsPerDay;

    @FXML
    private JFXTextField periodsBeforeLuanch;

    @FXML
    private JFXTextField periodsAfterLuanch;

    @FXML
    private JFXTextField periodsBeforeBreak;

    @FXML
    private JFXTextField numDaysPerCycle;

    @FXML
    private JFXListView sportDays;

    @FXML
    private JFXTextField breakTime;

    @FXML
    private JFXTextField lessesonDuration;

    @FXML
    private JFXTextField breakDuration;

    @FXML
    private JFXTextField launchTime;

    @FXML
    private JFXTextField startTime;

    @FXML
    private JFXTextField luanchDuration;

    @FXML
    private JFXListView subjects;

    @FXML
    private JFXListView level;

    @FXML
    private JFXListView prefferedLesson;

    @FXML
    private JFXTextField subPeriods;

    @FXML
    private TableView overalSubjectTable;

    @FXML
    private TableColumn overalSubjectColmn;

    @FXML
    private TableColumn levelColmn;

    @FXML
    private TableColumn prdsColmn;

    @FXML
    private TableView durationTable;

    @FXML
    private TableColumn preferedPrdsColmn;

    @FXML
    private JFXComboBox subSearchKey;
    @FXML
    private JFXComboBox combanedKlassCombo;

    @FXML
    private JFXComboBox subShared;
    @FXML
    private JFXComboBox levelShared;
   
    @FXML
    private Label titleLabel;
    @FXML
    private JFXComboBox klassSharedSearch;
    @FXML
    private TableView sharedRootTable;

    @FXML
    private TableColumn sharedRootColumn;
    @FXML
    private TableView sharedTable;

    @FXML
    private TableColumn sharedTableColumn;
    @FXML
    private JFXComboBox combinedKlassSearch;
    @FXML
    private TableView combinedTableRoot;

    @FXML
    private TableColumn combinedColumnRoot;

    @FXML
    private TableView combinedTable;
    @FXML
    private AnchorPane combinedArchorPane;
    @FXML
    private AnchorPane sharedArchorPane;
    @FXML
    private TableColumn combinedColumn;
    private HashMap<JFXCheckBox, TeacherSubjectKlass> combinedCheckBoxs = new HashMap<>();
    private HashMap<JFXCheckBox, TeacherSubjectKlass> sharedCheckBoxs = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validateFields();
        initiList();
        initiTables();
        fillOveralConstrains();
        registerMenus();
    }

    private void initiTables() {
        prds.setCellValueFactory(new PropertyValueFactory<>("numPeriods"));
        b4brk.setCellValueFactory(new PropertyValueFactory<>("periodsB4Brk"));
        brkTime.setCellValueFactory(new PropertyValueFactory<>("brkTime"));

        brkLngth.setCellValueFactory(new PropertyValueFactory<>("brkDuration"));
        b4lnch.setCellValueFactory(new PropertyValueFactory<>("periodsB4Lnch"));
        lnchTime.setCellValueFactory(new PropertyValueFactory<>("lnchTime"));
        lnch.setCellValueFactory(new PropertyValueFactory<>("lnchDuration"));
        aftaLnch.setCellValueFactory(new PropertyValueFactory<>("periodsAfterLnch"));
        days.setCellValueFactory(new PropertyValueFactory<>("cycleSize"));
        strtTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        lesnDration.setCellValueFactory(new PropertyValueFactory<>("lesssonDuration"));

        sprtyDay.setCellValueFactory(new PropertyValueFactory<>("dy"));

        overalSubjectColmn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        levelColmn.setCellValueFactory(new PropertyValueFactory<>("klass"));
        prdsColmn.setCellValueFactory(new PropertyValueFactory<>("numPeriods"));

        preferedPrdsColmn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        sharedRootColumn.setCellValueFactory(new PropertyValueFactory<>("trSubKlas"));
        sharedTableColumn.setCellValueFactory(new PropertyValueFactory<>("trSubKlas"));
        combinedColumnRoot.setCellValueFactory(new PropertyValueFactory<>("trSubKlas"));
        combinedColumn.setCellValueFactory(new PropertyValueFactory<>("trSubKlas"));
        
        final ContextMenu menu = new ContextMenu();
        final MenuItem delet = new MenuItem("Delete");
        delet.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                try {
                    CombinedSubjects combinedSubject = (CombinedSubjects) combinedTable.getSelectionModel().getSelectedItem();
                    if(!DialogManager.showConformationDialoge("Do you want to remove "+combinedSubject.getTrSubKlas()+" from combined subjects")){
                       return; 
                    }
                   
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        session.beginTransaction();
                        session.delete(combinedSubject);
                        session.getTransaction().commit();
                        combinedTable.getItems().remove(combinedSubject);
                        DialogManager.showDialog("Deleted");
                    }
                } catch (Exception ex) {
                    DialogManager.showErrorMassageDialoge(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
        
        delet.disableProperty().bind(Bindings.isEmpty( combinedTable.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll( delet);
        combinedTable.setContextMenu(menu);
        
        final ContextMenu menu1 = new ContextMenu();
        final MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                try {
                    SharedSubjects sharedSubjects = (SharedSubjects) sharedTable.getSelectionModel().getSelectedItem();
                    if(!DialogManager.showConformationDialoge("Do you want to remove "+sharedSubjects.getTrSubKlas()+" from shared subjects")){
                       return; 
                    }
                   
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        session.beginTransaction();
                        session.delete(sharedSubjects);
                        session.getTransaction().commit();
                        sharedTable.getItems().remove(sharedSubjects);
                        DialogManager.showDialog("Deleted");
                    }
                } catch (Exception ex) {
                    DialogManager.showErrorMassageDialoge(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        
        
        delete.disableProperty().bind(Bindings.isEmpty( sharedTable.getSelectionModel().getSelectedItems()));
        menu1.getItems().addAll( delete);
        sharedTable.setContextMenu(menu1);
    }

    private void registerMenus() {
        final ContextMenu menu = new ContextMenu();
        final MenuItem showDurations = new MenuItem("Show prefered duration");
        showDurations.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SubjectConstrainEntity ex = (SubjectConstrainEntity) overalSubjectTable.getSelectionModel().getSelectedItem();
                    titleLabel.setText(ex.getKlass() + " " + ex.getSubject() + " Prefered Duration");
                    durationTable.getItems().clear();
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        SubjectConstrainEntity get = (SubjectConstrainEntity) session.createQuery("from SubjectConstrainEntity where id=" + ex.getId()).uniqueResult();
                        durationTable.getItems().setAll(Kubirira.getDataToDisplay(get.getPreferedDuration()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        showDurations.disableProperty().bind(Bindings.isEmpty(overalSubjectTable.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll(showDurations);
        overalSubjectTable.setContextMenu(menu);

        final ContextMenu sharedMenu = new ContextMenu();
        final MenuItem showShared = new MenuItem("Show Shared Subject");
        showShared.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SharedSubjects ex = (SharedSubjects) sharedRootTable.getSelectionModel().getSelectedItem();

                fillSharedSubjectTable(ex);
            }
        });
        showShared.disableProperty().bind(Bindings.isEmpty(sharedRootTable.getSelectionModel().getSelectedItems()));
        sharedMenu.getItems().addAll(showShared);
        sharedRootTable.setContextMenu(sharedMenu);

        final ContextMenu combinedSubMenu = new ContextMenu();
        final MenuItem showCombined = new MenuItem("Show Combined Subjects");
        showCombined.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CombinedSubjects ex = (CombinedSubjects) combinedTableRoot.getSelectionModel().getSelectedItem();
                fillCombinedSubjectTable(ex);
            }
        });
        showCombined.disableProperty().bind(Bindings.isEmpty(combinedTableRoot.getSelectionModel().getSelectedItems()));
        combinedSubMenu.getItems().addAll(showCombined);
        combinedTableRoot.setContextMenu(combinedSubMenu);
    }

    private void fillOveralConstrains() {
        overalTable.getItems().clear();
        sportsTable.getItems().clear();
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<OveralConstrains> list = session.createQuery("from OveralConstrains").list();
            if (list != null && !list.isEmpty()) {
                OveralConstrains overalCon = list.get(0);
                overalTable.getItems().add(overalCon);
                sportsTable.getItems().setAll(overalCon.getSportsDays());
            }

        }
    }

    public void fillSharedSubjectTable(SharedSubjects ex) {
        sharedTable.getItems().clear();
        // titleLabel.setText("Prefered Duration");
        if (ex == null) {
            System.out.println("oooooooooooooooooooooooooooooooo");
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<SubjectConstrainEntity> list = session.createQuery("from SharedSubjects where ref ='" + ex.getRef() + "'").list();
            if (!list.isEmpty()) {
                System.out.println("uuuuuuuuu");
                sharedTable.getItems().setAll(list);
            }
        }
    }

    public void fillCombinedSubjectTable(CombinedSubjects ex) {
        combinedTable.getItems().clear();
        // titleLabel.setText("Prefered Duration");
        if (ex == null) {
            System.out.println("oooooooooooooooooooooooooooooooo");
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<SubjectConstrainEntity> list = session.createQuery("from CombinedSubjects where ref ='" + ex.getRef() + "'").list();
            if (!list.isEmpty()) {
                System.out.println("uuuuuuuuu");
                combinedTable.getItems().setAll(list);
            }
        }
    }

    @FXML
    public void fillCombinedRootTable(ActionEvent event) {
        combinedTable.getItems().clear();
        combinedTableRoot.getItems().clear();
        // titleLabel.setText("Prefered Duration");
        Klass klas = (Klass) combinedKlassSearch.getSelectionModel().getSelectedItem();
        if (klas == null) {
            System.out.println("oooooooooooooooooooooooooooooooo");
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<SubjectConstrainEntity> list = session.createQuery("from CombinedSubjects where trSubKlas.klass =" + klas.getId() + " group by ref ").list();
            if (!list.isEmpty()) {
                System.out.println("uuuuuuuuu");
                combinedTableRoot.getItems().setAll(list);
            }
        }
    }

    @FXML
    public void fillSharedSubjectRoot(ActionEvent event) {
        sharedRootTable.getItems().clear();
        sharedTable.getItems().clear();
        // titleLabel.setText("Prefered Duration");
        Klass klas = (Klass) klassSharedSearch.getSelectionModel().getSelectedItem();
        if (klas == null) {
            System.out.println("oooooooooooooooooooooooooooooooo");
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<SubjectConstrainEntity> list = session.createQuery("from SharedSubjects where trSubKlas.klass =" + klas.getId()+ " ").list();
            if (!list.isEmpty()) {
                System.out.println("uuuuuuuuu");
                sharedRootTable.getItems().setAll(list);
            }
        }
    }

    @FXML
    public void fillSubjectConstrains(ActionEvent event) {
        overalSubjectTable.getItems().clear();
        durationTable.getItems().clear();
        titleLabel.setText("Prefered Duration");
        Subject sub = (Subject) subSearchKey.getSelectionModel().getSelectedItem();
        if (sub == null) {
            System.out.println("oooooooooooooooooooooooooooooooo");
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<SubjectConstrainEntity> list = session.createQuery("from SubjectConstrainEntity where subject ='" + sub.getCode() + "'").list();
            if (!list.isEmpty()) {
                System.out.println("uuuuuuuuu");
                overalSubjectTable.getItems().setAll(list);
            }
        }
    }

    @FXML
    void clearFields(ActionEvent event) {

    }

    @FXML
    void clearOveralFields(ActionEvent event) {

    }

    private void initiList() {
        sportDays.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        subjects.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        level.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        prefferedLesson.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sportDays.getItems().setAll(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6));
        prefferedLesson.getItems().setAll(FXCollections.observableArrayList(1, 2, 4));
        ObservableList<Subject> allSubjects = Subject.getAllSubjects();
        ObservableList<Klass> allLevel = Klass.getAllLevel();
        ObservableList<Klass> allKlasses = Klass.getAllKlasses();
        subjects.getItems().setAll(allSubjects);
        subSearchKey.getItems().setAll(allSubjects);
        level.getItems().setAll(allLevel);
        levelShared.getItems().setAll(allLevel);
        combanedKlassCombo.getItems().setAll(allKlasses);
        subShared.getItems().setAll(allSubjects);
        klassSharedSearch.getItems().setAll(allKlasses);
        combinedKlassSearch.getItems().setAll(allKlasses);

    }

    @FXML
    public void setSubTrKlasShared(ActionEvent event) {
        for (Map.Entry<JFXCheckBox, TeacherSubjectKlass> entry : sharedCheckBoxs.entrySet()) {
            JFXCheckBox jfxCheckBox = entry.getKey();
            sharedArchorPane.getChildren().remove(jfxCheckBox);
        }
        sharedCheckBoxs = new HashMap<>();
        Subject sub = (Subject) subShared.getSelectionModel().getSelectedItem();
        Klass klas = (Klass) levelShared.getSelectionModel().getSelectedItem();
        if (sub == null || klas == null) {
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession()) {
            String query = "From TeacherSubjectKlass  where klass.level=" + klas.getLevel() + " and subject ='" + sub.getCode() + "'";
            List<TeacherSubjectKlass> trSubKls = session.createQuery(query).list();
            int x = 172;
            int y = 172;
            int count = 0;
            for (TeacherSubjectKlass klasTrSub : trSubKls) {
                if (count == 2) {
                    x = 172;
                    y += 27;
                    count = 0;
                }
                JFXCheckBox jfxCheckBox = new JFXCheckBox(klasTrSub.toString());

                jfxCheckBox.setLayoutX(x);
                jfxCheckBox.setLayoutY(y);
                sharedArchorPane.getChildren().add(jfxCheckBox);
                sharedCheckBoxs.put(jfxCheckBox, klasTrSub);
                x += 247;
                count++;
            }
        }
    }

    @FXML
    public void setCombinedKlassList(ActionEvent event) {
        for (Map.Entry<JFXCheckBox, TeacherSubjectKlass> entry : combinedCheckBoxs.entrySet()) {
            JFXCheckBox jfxCheckBox = entry.getKey();
            combinedArchorPane.getChildren().remove(jfxCheckBox);
        }
        combinedCheckBoxs = new HashMap<>();

        Klass klas = (Klass) combanedKlassCombo.getSelectionModel().getSelectedItem();
        if (klas == null) {
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession()) {
            String query = "From TeacherSubjectKlass  where klass=" + klas.getId()+" order by subject";

            List<TeacherSubjectKlass> trSubKls = session.createQuery(query).list();

            int x = 155;
            int y = 143;
            int count = 0;
            for (TeacherSubjectKlass klasTrSub : trSubKls) {
                if (count == 2) {
                    x = 155;
                    y += 27;
                    count = 0;
                }
                JFXCheckBox jfxCheckBox = new JFXCheckBox(klasTrSub.toString());

                jfxCheckBox.setLayoutX(x);
                jfxCheckBox.setLayoutY(y);
                combinedArchorPane.getChildren().add(jfxCheckBox);
                combinedCheckBoxs.put(jfxCheckBox, klasTrSub);
                x += 247;
                count++;
            }
        }
    }

    @FXML
    public void saveSharedSubjects(ActionEvent event) {
        ObservableList<TeacherSubjectKlass> selectedItems = FXCollections.observableArrayList();
        sharedCheckBoxs.entrySet().stream().forEach((entry) -> {
            JFXCheckBox jfxCheckBox = entry.getKey();
            if (jfxCheckBox.isSelected()) {
                selectedItems.add(entry.getValue());
            }
        });
        if (selectedItems.size() <= 1) {
            System.out.println("list size less than two");
            return;
        }
        if (!SharedSubjects.validateSharedSubjects(selectedItems)) {
            System.out.println("Subject con not the same");
            return;
        }
        SharedSubjects.saveCombinedSubject(selectedItems);
        DialogManager.showDialog(MainPageController.stackPane, "Data saved");
        sharedCheckBoxs.entrySet().stream().forEach((entry) -> {
            entry.getKey().setSelected(false);
        });
    }

    @FXML
    public void saveCombinedSubjects(ActionEvent event) {
        ObservableList<TeacherSubjectKlass> selectedItems = FXCollections.observableArrayList();
        combinedCheckBoxs.entrySet().stream().forEach((entry) -> {
            JFXCheckBox jfxCheckBox = entry.getKey();
            if (jfxCheckBox.isSelected()) {
                selectedItems.add(entry.getValue());
            }
        });
        if (selectedItems.isEmpty() || selectedItems.size() < 2) {
            DialogManager.showErrorMassageDialoge("Combined subjects can not be less than 2");
            return;
        }
        TeacherSubjectKlass get = selectedItems.get(0);
        SubjectConstrainEntity subCon = SubjectConstrainEntity.pullSubjectConstrainEntity(get.getSubject(), get.getKlass());
        subCon.getPreferedDuration();
        System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
        for (TeacherSubjectKlass item : selectedItems) {
            SubjectConstrainEntity currSubCon = SubjectConstrainEntity.pullSubjectConstrainEntity(item.getSubject(), item.getKlass());
            if (currSubCon == null) {
                System.out.println("No constrains for sub");
                DialogManager.showErrorMassageDialoge("No constrains for sub " + item);
                return;
            }
            if (!subCon.hasSameConstrains(currSubCon)) {
                System.out.println("sub constrains must be the same");
                DialogManager.showErrorMassageDialoge("sub constrains must be the same " + item);
                return;
            }
        }
        System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        String uniqRef = CombinedSubjects.generateUniqRef();
        selectedItems.stream().forEach((item) -> {
            CombinedSubjects.saveCombinedSubject(item, uniqRef);
        });
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        DialogManager.showDialog(MainPageController.stackPane, "Data saved");
        combinedCheckBoxs.entrySet().stream().forEach((entry) -> {
            entry.getKey().setSelected(false);
        });
    }

    @FXML
    public void saveSubjectConstrains(ActionEvent event) {
        ObservableList<Subject> selectedSubjects = subjects.getSelectionModel().getSelectedItems();
        ObservableList<Klass> selectedLevels = level.getSelectionModel().getSelectedItems();
        ObservableList<Integer> selectedPrefferedLesson = prefferedLesson.getSelectionModel().getSelectedItems();
        if (!subPeriods.validate()) {
            return;
        }
        if (selectedSubjects.isEmpty() || selectedLevels.isEmpty() || selectedPrefferedLesson.isEmpty()) {
            System.out.println("eeeeeeeeeee");
            return;
        }
        ObservableList<Klass> klasses = Klass.getKlasses(selectedLevels);
        int numP = Integer.parseInt(subPeriods.getText());
        for (Subject sub : selectedSubjects) {
            klasses.stream().map((klas) -> new SubjectConstrainEntity(sub, klas, numP)).map((subCon) -> {
                subCon.setPreferedDuration(selectedPrefferedLesson);
                return subCon;
            }).forEach((subCon) -> {
                SubjectConstrainEntity.saveSubjectConstrainEntity(subCon);
            });
        }
        DialogManager.showDialog(MainPageController.stackPane, "Data saved");
    }

    @FXML
    public void saveOveralConstrains(ActionEvent event) {
        JFXTextField[] all = {
            numDaysPerCycle, numPeriodsPerDay, periodsBeforeBreak,
            periodsBeforeLuanch, periodsAfterLuanch, lessesonDuration,
            breakDuration, luanchDuration, startTime,
            breakTime, launchTime
        };
        boolean isValid = true;
        for (JFXTextField cntrol : all) {
            isValid = cntrol.validate() && isValid;
        }
        if (!isValid) {
            return;
        }
        int cycleSize = Integer.parseInt(numDaysPerCycle.getText());
        int numPeriods = Integer.parseInt(numPeriodsPerDay.getText());
        int periodsB4Brk = Integer.parseInt(periodsBeforeBreak.getText());

        int periodsB4Lnch = Integer.parseInt(periodsBeforeLuanch.getText());
        int periodsAfterLnch = Integer.parseInt(periodsAfterLuanch.getText());
        int lesssonDuration = Integer.parseInt(lessesonDuration.getText());

        int brkDuration = Integer.parseInt(breakDuration.getText());
        int lnchDuration = Integer.parseInt(luanchDuration.getText());

        LocalTime startTim = LocalTime.parse(startTime.getText().trim());
        LocalTime brkTime = LocalTime.parse(breakTime.getText().trim());
        LocalTime lnchTime = LocalTime.parse(launchTime.getText().trim());
        ObservableList<Integer> selectedDays = sportDays.getSelectionModel().getSelectedItems();
        OveralConstrains overCon = new OveralConstrains();
        overCon.setBrkDuration(brkDuration);
        overCon.setBrkTime(brkTime);
        overCon.setCycleSize(cycleSize);
        overCon.setLesssonDuration(lesssonDuration);
        overCon.setLnchDuration(lnchDuration);
        overCon.setLnchTime(lnchTime);
        overCon.setNumPeriods(numPeriods);
        overCon.setPeriodsAfterLnch(periodsAfterLnch);
        overCon.setPeriodsB4Brk(periodsB4Brk);
        overCon.setStartTime(startTim);
        overCon.setPeriodsB4Lnch(periodsB4Lnch);

        for (int day : selectedDays) {
            if (day > cycleSize) {
                continue;
            }
            SportsDay sportsDay = SportsDay.getSportsDay(day);
            overCon.getSportsDays().add(sportsDay);
        }
        if (!overCon.validateConstrains()) {
            System.out.println("ERROR CONSTRAIN NOT VALID ");
            return;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            String stringQuery = "DELETE FROM OveralConstrains";
            session.beginTransaction();
            session.createQuery(stringQuery).executeUpdate();
            session.getTransaction().commit();
            session.beginTransaction();
            session.save(overCon);
            session.getTransaction().commit();
            overalTable.getItems().setAll(overCon);
            sportsTable.getItems().setAll(overCon.getSportsDays());
            DialogManager.showDialog(MainPageController.stackPane, "Data saved");
        }

    }

    private void validateFields() {
        Validation.validateTextFieldNumberOnly(numDaysPerCycle);

        Validation.validateTextFieldNumberOnly(numPeriodsPerDay);
        Validation.validateTextFieldNumberOnly(periodsBeforeBreak);

        Validation.validateTextFieldNumberOnly(periodsBeforeLuanch);
        Validation.validateTextFieldNumberOnly(periodsAfterLuanch);

        Validation.validateTextFieldNumberOnly(lessesonDuration);
        Validation.validateTextFieldNumberOnly(breakDuration);
        Validation.validateTextFieldNumberOnly(luanchDuration);

        Validation.validateTextFieldTime(startTime);
        Validation.validateTextFieldTime(breakTime);
        Validation.validateTextFieldTime(launchTime);

        // subject constrains
        Validation.validateTextFieldNumberOnly(subPeriods);

    }
}
