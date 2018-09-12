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
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class TimeTableController implements Initializable {

    public static TimeTable timeTable = new TimeTable();

    @FXML
    private TableView masterTableV;
    @FXML
    private TableView klassTable;
    @FXML
    private TableView trTable;
    @FXML
    private JFXComboBox dayMasterComboBox;
    @FXML
    private JFXComboBox klasTimCombobox;

    @FXML
    private JFXComboBox trTimComboBox;
    @FXML
    private TableView discripanceTable;

    @FXML
    private TableColumn disKlass;
    @FXML
    private JFXComboBox disKlassComboBox;
    @FXML
    private TableColumn disSub;

    @FXML
    private TableColumn disWeekly;

    @FXML
    private TableColumn disLoged;
    @FXML
    private TableView freeTable;

    @FXML
    private TableColumn freeDay;

    @FXML
    private TableColumn freePeriod;
    @FXML
    private JFXCheckBox allSubsCheckBox;
    @FXML
    private JFXCheckBox wholeSckCheckBox;
    @FXML
    private TableView savedTimeTables;
    @FXML
    private TableColumn colmnRef;
    @FXML
    private TableColumn colmnRefDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeMasterTimeTableTemplate();
        makeKlassTimeTableTemplate();
        makeTrTimeTableTemplate();
        discripancyTable();
        freeTable();
        ObservableList<Klass> allKlasses = Klass.getAllKlasses();
        klasTimCombobox.getItems().setAll(allKlasses);
        disKlassComboBox.getItems().setAll(allKlasses);

        trTimComboBox.getItems().setAll(Teacher.getAllTeachers());
        for (int i = 1; i <= TimeTable.getCycleSize(); i++) {
            dayMasterComboBox.getItems().add(i);
        }
        klassTable.getSelectionModel().setCellSelectionEnabled(true);
    }

    private void freeTable() {
        freeDay.setCellValueFactory(new PropertyValueFactory<>("dy"));
        freePeriod.setCellValueFactory(new PropertyValueFactory<>("periodi"));
    }

    @FXML
    public void showSavedTimeTables(ActionEvent event) {
        colmnRef.setCellValueFactory(new PropertyValueFactory<>("id"));
        colmnRefDate.setCellValueFactory(new PropertyValueFactory<>("refDaf"));
        try (Session session = HibernateCentre.getHibernateSession();) {
            List<TimeTableSaved> list = session.createQuery("from TimeTableSaved").list();
            savedTimeTables.getItems().setAll(list);
        }
        final ContextMenu menu = new ContextMenu();
        final MenuItem showSavedTmTable = new MenuItem("Load Saved TimeTable ");
        showSavedTmTable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TimeTableSaved ref = (TimeTableSaved) savedTimeTables.getSelectionModel().getSelectedItem();
                if (ref != null) {
                    timeTable = new TimeTable();
                    timeTable.makeTimeTableFromDb(ref);
                    klassTable.refresh();
                    masterTableV.refresh();
                    trTable.refresh();
                    discripanceTable.refresh();
                    freeTable.refresh();
                } else {
                    return;
                }

            }
        });
        final MenuItem deleteTmTable = new MenuItem("Delete TimeTable ");
        deleteTmTable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TimeTableSaved ref = (TimeTableSaved) savedTimeTables.getSelectionModel().getSelectedItem();
                if (ref != null) {
                    if (JOptionPane.showConfirmDialog(null, "delete", "delete", JOptionPane.YES_OPTION) == JOptionPane.YES_OPTION) {
                        try (Session session = HibernateCentre.getHibernateSession();) {
                            session.beginTransaction();
                            Query query = session.createQuery("delete from Slot where ref=" + ref.getId());
                            int result = query.executeUpdate();
                            session.delete(ref);
                            session.getTransaction().commit();
                        }
                    }
                } else {
                    return;
                }

            }
        });
        showSavedTmTable.disableProperty().bind(Bindings.isEmpty(savedTimeTables.getSelectionModel().getSelectedItems()));
        deleteTmTable.disableProperty().bind(Bindings.isEmpty(savedTimeTables.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll(showSavedTmTable, deleteTmTable);
        savedTimeTables.setContextMenu(menu);

    }

    private void discripancyTable() {
        disKlass.setCellValueFactory(new PropertyValueFactory<>("klass"));
        disSub.setCellValueFactory(new PropertyValueFactory<>("subject"));
        disWeekly.setCellValueFactory(new PropertyValueFactory<>("numPeriods"));
        disLoged.setCellValueFactory(new PropertyValueFactory<>("numLessonLoged"));

        final ContextMenu menu = new ContextMenu();
        final MenuItem showPossiblePeriods = new MenuItem("Show possible periods");
        showPossiblePeriods.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Subject_constrainsOnTheFly ex = (Subject_constrainsOnTheFly) discripanceTable.getSelectionModel().getSelectedItem();
                    //titleLabel.setText(ex.getKlass() + " " + ex.getSubject() + " Prefered Duration");
                    //  durationTable.getItems().clear();
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        TeacherSubjectKlass trSubKlas = (TeacherSubjectKlass) session.createQuery("from TeacherSubjectKlass  where klass=" + ex.getKlass().getId() + " and subject ='" + ex.getSubject().getCode() + "'").uniqueResult();
                        List<TeacherSubjectKlass> effectiveSubKlasTr = timeTable.getEffectiveSubKlasTr(trSubKlas);
                        showPossible(effectiveSubKlasTr);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        final MenuItem showPossibleAndFreePeriods = new MenuItem("Show possible and free periods");
        showPossibleAndFreePeriods.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Subject_constrainsOnTheFly ex = (Subject_constrainsOnTheFly) discripanceTable.getSelectionModel().getSelectedItem();
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        TeacherSubjectKlass trSubKlas = (TeacherSubjectKlass) session.createQuery("from TeacherSubjectKlass  where klass=" + ex.getKlass().getId() + " and subject ='" + ex.getSubject().getCode() + "'").uniqueResult();
                        List<TeacherSubjectKlass> effectiveSubKlasTr = timeTable.getEffectiveSubKlasTr(trSubKlas);
                        showFreePossible(effectiveSubKlasTr);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        final MenuItem showEffectivePossibleAndFreePeriods = new MenuItem("Show EFECTIVE possible and free periods");
        showEffectivePossibleAndFreePeriods.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Subject_constrainsOnTheFly ex = (Subject_constrainsOnTheFly) discripanceTable.getSelectionModel().getSelectedItem();
                    try (Session session = HibernateCentre.getHibernateSession();) {
                        TeacherSubjectKlass trSubKlas = (TeacherSubjectKlass) session.createQuery("from TeacherSubjectKlass  where klass=" + ex.getKlass().getId() + " and subject ='" + ex.getSubject().getCode() + "'").uniqueResult();
                        List<TeacherSubjectKlass> effectiveSubKlasTr = timeTable.getEffectiveSubKlasTr(trSubKlas);
                        showEffectiveFreePossible(effectiveSubKlasTr);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        final MenuItem insert = new MenuItem("Insert into  a free slot ");
        insert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Subject_constrainsOnTheFly subcon = (Subject_constrainsOnTheFly) discripanceTable.getSelectionModel().getSelectedItem();
                if (subcon.getDifference() <= 0) {
                    System.out.println("Difference is zero ");
                    return;
                }
                insertDiscripanceDialog(subcon);
            }
        });
        insert.disableProperty().bind(Bindings.isEmpty(discripanceTable.getSelectionModel().getSelectedItems()));
        showPossiblePeriods.disableProperty().bind(Bindings.isEmpty(discripanceTable.getSelectionModel().getSelectedItems()));
        showPossibleAndFreePeriods.disableProperty().bind(Bindings.isEmpty(discripanceTable.getSelectionModel().getSelectedItems()));
        showEffectivePossibleAndFreePeriods.disableProperty().bind(Bindings.isEmpty(discripanceTable.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll(insert, showPossiblePeriods, showPossibleAndFreePeriods, showEffectivePossibleAndFreePeriods);
        discripanceTable.setContextMenu(menu);
    }

    private void showEffectiveFreePossible(List<TeacherSubjectKlass> effectiveSubKlasTr) {
        String gg = "" + effectiveSubKlasTr.size();
        List<Integer> daysWithoutSub = timeTable.getDaysWithoutSub(effectiveSubKlasTr);
        for (int day : daysWithoutSub) {
            ArrayList<Integer> possiblePeriods = timeTable.getPossibleAndFreePeriods(day, effectiveSubKlasTr);
            gg += "==================== DAY " + day + " ====================\n";
            gg += possiblePeriods + "\n";
        }
        if (gg.isEmpty()) {
            gg = "no possible periods";
        }
        System.out.println(gg);
        DialogManager.showDialog(MainPageController.stackPane, "Effective Possible Free Periods ", gg);
    }

    private void showTrs(List<TeacherSubjectKlass> effectiveSubKlasTr) {
        String gg = "";
        gg = effectiveSubKlasTr.stream().map((trSub) -> trSub + "  \n").reduce(gg, String::concat);
        if (gg.isEmpty()) {
            gg = "";
        }
        System.out.println(gg);
        DialogManager.showDialog(MainPageController.stackPane, "TEACHER SUBJECT CLASS  ", gg);
    }

    private void showFreePossible(List<TeacherSubjectKlass> effectiveSubKlasTr) {
        String gg = "";
        for (int day = 1; day <= TimeTable.getCycleSize(); day++) {
            ArrayList<Integer> possiblePeriods = timeTable.getPossibleAndFreePeriods(day, effectiveSubKlasTr);
            gg += "==================== DAY " + day + " ====================\n";
            gg += possiblePeriods + "\n";
        }
        if (gg.isEmpty()) {
            gg = "no possible periods";
        }
        System.out.println(gg);
        DialogManager.showDialog(MainPageController.stackPane, "Possible Free Periods ", gg);
    }

    private void showPossible(List<TeacherSubjectKlass> effectiveSubKlasTr) {
        String gg = "";
        for (int day = 1; day <= TimeTable.getCycleSize(); day++) {

            ArrayList<Integer> possiblePeriods = timeTable.getPossiblePeriods(day, effectiveSubKlasTr);
            gg += "==================== DAY " + day + " ====================\n";
            gg += possiblePeriods + "\n";
        }
        if (gg.isEmpty()) {
            gg = "no possible periods";
        }
        System.out.println(gg);
        DialogManager.showDialog(MainPageController.stackPane, "Possible Periods ", gg);
    }

    private void insertDiscripanceDialog(Subject_constrainsOnTheFly subCon) {
        if (subCon.getDifference() <= 0) {
            return;
        }
        TeacherSubjectKlass pullSubKlasTr = subCon.pullSubKlasTr();
        List<TeacherSubjectKlass> effectiveSubKlasTr = timeTable.getEffectiveSubKlasTr(pullSubKlasTr);
        Stage newStage = new Stage();
        newStage.setResizable(false);
        Pane rty = new Pane();
        rty.setMinSize(500, 600);
        JFXComboBox dayCombo = new JFXComboBox();
        dayCombo.setPromptText("Day");
        for (int i = 1; i <= TimeTable.getCycleSize(); i++) {
            dayCombo.getItems().add(i);
        }
        dayCombo.setLabelFloat(true);
        dayCombo.setMinSize(300, 25);
        dayCombo.setLayoutX(100);
        dayCombo.setLayoutY(50);
        Label label = new Label("Period");
        label.setLayoutX(5);
        label.setLayoutY(100);
        rty.getChildren().add(label);
        // JFXComboBox periodCombo = new JFXComboBox();
        // periodCombo.setPromptText("Period");
        //periodCombo.setLabelFloat(true);
        // periodCombo.setMinSize(300, 25);
        //periodCombo.setLayoutX(100);
        //periodCombo.setLayoutY(100);
        HashMap<JFXCheckBox, Integer> effectivePeriodsCheckBoxs = new HashMap<>();
        dayCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //periodCombo.getItems().clear();
                for (Map.Entry<JFXCheckBox, Integer> entry : effectivePeriodsCheckBoxs.entrySet()) {
                    JFXCheckBox jfxCheckBox = entry.getKey();
                    rty.getChildren().remove(jfxCheckBox);
                }
                if (dayCombo.getSelectionModel().getSelectedItem() == null) {
                    return;
                }
                int dayyi = (int) dayCombo.getSelectionModel().getSelectedItem();
                //periodCombo.getItems().setAll(timeTable.getPossibleAndFreePeriods(dayyi, effectiveSubKlasTr));
                ArrayList<Integer> efPossibleAndFreePeriods = timeTable.getPossibleAndFreePeriods(dayyi, effectiveSubKlasTr);
                int x = 100;
                int y = 100;
                int count = 0;
                for (int efP : efPossibleAndFreePeriods) {
                    if (count != 1 && count % 6 == 1) {
                        x = 100;
                        y += 30;
                    }
                    JFXCheckBox jfxCheckBox = new JFXCheckBox(efP + "");

                    jfxCheckBox.setLayoutX(x);
                    jfxCheckBox.setLayoutY(y);
                    rty.getChildren().add(jfxCheckBox);
                    effectivePeriodsCheckBoxs.put(jfxCheckBox, efP);
                    x += 60;
                    Separator sa = new Separator(Orientation.VERTICAL);
                    sa.setPrefHeight(30);
                    sa.setLayoutX(x);
                    sa.setLayoutY(y);
                    rty.getChildren().add(sa);
                }
            }
        });
        Button bbtn = new Button();
        bbtn.setLayoutX(100);
        bbtn.setLayoutY(230);
        bbtn.setText("Save");
        bbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (dayCombo.getSelectionModel().getSelectedItem() == null) {
                    System.out.println(" select day and period please");
                    return;
                }
                ObservableList<Integer> selectedPeriods = FXCollections.observableArrayList();
                effectivePeriodsCheckBoxs.entrySet().stream().forEach((entry) -> {
                    JFXCheckBox jfxCheckBox = entry.getKey();
                    if (jfxCheckBox.isSelected()) {
                        selectedPeriods.add(entry.getValue());
                    }
                });

                int difference = subCon.getDifference();
                if (difference < 0) {
                    return;
                }
                if (difference < selectedPeriods.size()) {
                    return;
                }
                int dyy = (int) dayCombo.getSelectionModel().getSelectedItem();
                //Slot otherSlt = timeTable.findSlot(dyy, prd, subCon.getKlass());
                for (int prd : selectedPeriods) {
                    ArrayList<Integer> validBlockTolog = new ArrayList<>();
                    validBlockTolog.add(prd);
                    List<TeacherSubjectKlass> sharedSubject = pullSubKlasTr.getSharedSubject();
                    for (TeacherSubjectKlass subKlasTr : sharedSubject) {
                        ArrayList<Slot> listSlots = timeTable.findSlots(dyy, validBlockTolog, subKlasTr.getKlass());
                        timeTable.insertSlots(listSlots, subKlasTr.getCombinedSubject(), dyy);
                    }
                }

                klassTable.refresh();
                discripanceTable.refresh();
                newStage.close();
            }
        });
        Button bbcl = new Button();
        bbcl.setLayoutX(300);
        bbcl.setLayoutY(230);
        bbcl.setText("Cancel");
        bbcl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newStage.close();
            }
        });

        rty.getChildren().add(dayCombo);
        rty.getChildren().add(bbcl);
        rty.getChildren().add(bbtn);
        //rty.getChildren().add(periodCombo);
        Scene dialogScene = new Scene(rty, 500, 600);
        newStage.setScene(dialogScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle("Insert into slot ");
        newStage.showAndWait();
    }

    @FXML
    public void showClashes(ActionEvent event) {
        DialogManager.showDialog(MainPageController.stackPane, "clashes window ", timeTable.showClashes());
    }

    @FXML
    public void showBreakLunchOverlaps(ActionEvent event) {
        DialogManager.showDialog(MainPageController.stackPane, "Break and lunch  overlaps", timeTable.showBreakLunchOverlap());
    }

    @FXML
    public void showAnomalise(ActionEvent event) {
        DialogManager.showDialog(MainPageController.stackPane, "Anomalise", timeTable.showAnomalise());
    }

    public void fillFreeTable(Klass klas) {
        if (klas == null) {
            return;
        }
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            timeTable.makeTimeTable();
        }
        MasterTimeTableDisplay.setType(2);
        ObservableList<Slot> freeSlots = FXCollections.observableArrayList();
        HashMap<Integer, ArrayList<Slot>> schoolTimetable = timeTable.getSchoolTimetable();
        for (Map.Entry<Integer, ArrayList<Slot>> entry : schoolTimetable.entrySet()) {
            ArrayList<Slot> wholeSchoolSingleDay = entry.getValue();
            for (Slot singleKlassSingleDay : wholeSchoolSingleDay) {
                if (!Objects.equals(singleKlassSingleDay.getKlass().getId(), klas.getId())) {
                    continue;
                }
                Slot curr = singleKlassSingleDay;
                do {
                    if (curr.isFree()) {
                        freeSlots.add(curr);
                    }
                    if (curr.hasNext()) {
                        curr = curr.getNext();
                    } else {
                        break;
                    }
                } while (true);
            }
        }
        freeTable.setItems(freeSlots);
    }

    @FXML
    public void fillDiscripancyTable(ActionEvent event) {
        discripanceTable.getItems().clear();
        freeTable.getItems().clear();
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            return;
        }
        boolean selected = allSubsCheckBox.isSelected();
        boolean allKlasz = wholeSckCheckBox.isSelected();
        Klass klas = (Klass) disKlassComboBox.getSelectionModel().getSelectedItem();

        ObservableList<Subject_constrainsOnTheFly> subCons = FXCollections.observableArrayList();
        HashMap<String, Subject_constrainsOnTheFly> subject_constrains = timeTable.getSubject_constrains();
        for (Map.Entry<String, Subject_constrainsOnTheFly> entry : subject_constrains.entrySet()) {
            Subject_constrainsOnTheFly subCon = entry.getValue();
            if (!allKlasz && klas != null) {
                if (!Objects.equals(subCon.getKlass().getId(), klas.getId())) {
                    continue;
                }
                if (selected) {
                    if (subCon.getNumPeriods() != subCon.getNumLessonLoged()) {
                        subCons.add(subCon);
                    }
                } else {
                    subCons.add(subCon);
                }
            } else {//all klasses
                if (subCon.getNumPeriods() != subCon.getNumLessonLoged()) {
                    subCons.add(subCon);
                }
            }
        }
        discripanceTable.setItems(subCons);
        fillFreeTable(klas);
    }

    @FXML
    public void printShortages(ActionEvent event) {
        ObservableList<Subject_constrainsOnTheFly> subCons = discripanceTable.getItems();
        ArrayList<ShortageLog> myList = new ArrayList<>();
        if (subCons.isEmpty()) {
            DialogManager.showDialog("Table is empty");
            return;
        }
        for (Subject_constrainsOnTheFly subCon : subCons) {
            myList.add(new ShortageLog(subCon));
        }
        HashMap map = new HashMap();
        map.put("ReportTitle", School.getTheSchl().toUpperCase());
        new ReportsFilling().fillShortages(myList, map);
    }

    private void makeMasterTimeTableTemplate() {
        List<TableColumn> tableColumns = new ArrayList<>();
        TableColumn tC = new TableColumn("");
        tC.setCellValueFactory(new PropertyValueFactory<>("slot0"));
        tableColumns.add(tC);
        for (int i = 1; i <= TimeTable.getNumPeriods(); i++) {
            tC = new TableColumn("" + i);
            tC.setCellValueFactory(new PropertyValueFactory<>("slot" + i));
            tableColumns.add(tC);
            if (i == TimeTable.getPeriodsB4Brk()) {
                tC = new TableColumn("BREAK");
                tC.setCellValueFactory(new PropertyValueFactory<>("brkSlot"));
                tableColumns.add(tC);
            }
            if (i == TimeTable.getPeriodsB4Lnch()) {
                tC = new TableColumn("LAUNCH");
                tC.setCellValueFactory(new PropertyValueFactory<>("LanchSlot"));
                tableColumns.add(tC);
            }
        }
        masterTableV.getColumns().setAll(tableColumns);
    }

    private void makeTrTimeTableTemplate() {
        List<TableColumn> tableColumns = new ArrayList<>();
        TableColumn tC = new TableColumn("");
        tC.setCellValueFactory(new PropertyValueFactory<>("slt0"));
        tableColumns.add(tC);
        for (int i = 1; i <= TimeTable.getNumPeriods(); i++) {
            tC = new TableColumn("" + i);
            tC.setCellValueFactory(new PropertyValueFactory<>("slt" + i));
            tableColumns.add(tC);
            if (i == TimeTable.getPeriodsB4Brk()) {
                tC = new TableColumn("BREAK");

                tC.setCellValueFactory(new PropertyValueFactory<>("brkSlt"));
                tableColumns.add(tC);
            }
            if (i == TimeTable.getPeriodsB4Lnch()) {
                tC = new TableColumn("LAUNCH");
                tC.setCellValueFactory(new PropertyValueFactory<>("LanchSlt"));
                tableColumns.add(tC);
            }
        }
        trTable.getColumns().setAll(tableColumns);
    }

    private void makeKlassTimeTableTemplate() {
        List<TableColumn> tableColumns = new ArrayList<>();
        TableColumn tC = new TableColumn("");
        tC.setCellValueFactory(new PropertyValueFactory<>("slot0"));
        tableColumns.add(tC);
        for (int i = 1; i <= TimeTable.getNumPeriods(); i++) {
            tC = new TableColumn("" + i);
            tC.setCellValueFactory(new PropertyValueFactory<>("slot" + i));
            tableColumns.add(tC);
            if (i == TimeTable.getPeriodsB4Brk()) {
                tC = new TableColumn("BREAK");

                tC.setCellValueFactory(new PropertyValueFactory<>("brkSlot"));
                tableColumns.add(tC);
            }
            if (i == TimeTable.getPeriodsB4Lnch()) {
                tC = new TableColumn("LAUNCH");
                tC.setCellValueFactory(new PropertyValueFactory<>("LanchSlot"));
                tableColumns.add(tC);
            }
        }
        klassTable.getColumns().setAll(tableColumns);
        final ContextMenu menu = new ContextMenu();
        final MenuItem exchange = new MenuItem("Exchange ");
        exchange.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Slot slt = getSelectedSlot();
                System.out.println(slt);
                if (slt == null) {
                    return;
                }
                showExchangeDialog(slt);
            }
        });
        final MenuItem effectiveWithout = new MenuItem("Show effective slots  on days without");
        effectiveWithout.setOnAction((ActionEvent event) -> {
            Slot slt = getSelectedSlot();
            System.out.println(slt);
            if (slt == null) {
                return;
            }
            List<TeacherSubjectKlass> effectiveSubKlasTr = slt.getTrSubKlasList();
            if (effectiveSubKlasTr.isEmpty()) {
                return;
            }
            showEffectiveFreePossible(effectiveSubKlasTr);
        });
        final MenuItem possible = new MenuItem("Show possible slots");
        possible.setOnAction((ActionEvent event) -> {
            Slot slt = getSelectedSlot();
            System.out.println(slt);
            if (slt == null) {
                return;
            }
            List<TeacherSubjectKlass> effectiveSubKlasTr = slt.getTrSubKlasList();
            if (effectiveSubKlasTr.isEmpty()) {
                return;
            }
            showPossible(effectiveSubKlasTr);
        });
        final MenuItem freeNpossible = new MenuItem("Show free and possible slots");
        freeNpossible.setOnAction((ActionEvent event) -> {
            Slot slt = getSelectedSlot();
            System.out.println(slt);
            if (slt == null) {
                return;
            }
            List<TeacherSubjectKlass> effectiveSubKlasTr = slt.getTrSubKlasList();
            if (effectiveSubKlasTr.isEmpty()) {
                return;
            }
            showFreePossible(effectiveSubKlasTr);
        });
        final MenuItem trs = new MenuItem("Show Teachers ");
        trs.setOnAction((ActionEvent event) -> {
            Slot slt = getSelectedSlot();
            System.out.println(slt);
            if (slt == null) {
                return;
            }
            List<TeacherSubjectKlass> effectiveSubKlasTr = slt.getTrSubKlasList();
            if (effectiveSubKlasTr.isEmpty()) {
                return;
            }
            showTrs(effectiveSubKlasTr);
        });
        final MenuItem empty = new MenuItem("Delete/empty slot");
        empty.setOnAction((ActionEvent event) -> {
            Slot slt = getSelectedSlot();
            System.out.println(slt);
            if (slt == null) {
                return;
            }
            if (slt.isFree()) {
                return;
            }
            int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Delete/empty selected slot", "Delete Conformation", JOptionPane.YES_NO_OPTION);
            if (showConfirmDialog == JOptionPane.YES_OPTION) {
                emptySelectSlot(slt);
            }
        });

        empty.disableProperty().bind(Bindings.isEmpty(klassTable.getSelectionModel().getSelectedItems()));
        exchange.disableProperty().bind(Bindings.isEmpty(klassTable.getSelectionModel().getSelectedItems()));
        effectiveWithout.disableProperty().bind(Bindings.isEmpty(klassTable.getSelectionModel().getSelectedItems()));
        possible.disableProperty().bind(Bindings.isEmpty(klassTable.getSelectionModel().getSelectedItems()));
        freeNpossible.disableProperty().bind(Bindings.isEmpty(klassTable.getSelectionModel().getSelectedItems()));
        trs.disableProperty().bind(Bindings.isEmpty(klassTable.getSelectionModel().getSelectedItems()));
        menu.getItems().addAll(exchange, effectiveWithout, possible, freeNpossible, trs, empty);
        klassTable.setContextMenu(menu);
    }

    private void emptySelectSlot(Slot slt) {
        List<TeacherSubjectKlass> trSubKlasList = slt.getTrSubKlasList();
        List<TeacherSubjectKlass> theEffTrSubKlasList = new ArrayList<>();
        for (TeacherSubjectKlass trSbK : trSubKlasList) {
            List<TeacherSubjectKlass> effectiveSubKlasTr = timeTable.getEffectiveSubKlasTr(trSbK);
            if (theEffTrSubKlasList.size() < effectiveSubKlasTr.size()) {
                theEffTrSubKlasList = effectiveSubKlasTr;
            }
        }
        for (TeacherSubjectKlass trSbK : theEffTrSubKlasList) {
            Slot findSlot = timeTable.findSlot(slt.getDy(), slt.getPeriodi(), trSbK.getKlass());
            Subject_constrainsOnTheFly subCon = timeTable.getSubject_constrains().get(trSbK.getKlass().getId() + "" + trSbK.getSubject().getCode());
            subCon.setNumLessonLoged(subCon.getNumLessonLoged() - 1);
            findSlot.setTrSubKlasList(new ArrayList<>());
            this.discripanceTable.refresh();
            klassTable.refresh();
        }
        slt.saveSlot();
    }

    private Slot getSelectedSlot() {
        TablePosition pos = (TablePosition) klassTable.getSelectionModel().getSelectedCells().get(0);
        String text = pos.getTableColumn().getText().trim();
        int colmn;
        try {
            colmn = Integer.parseInt(text);
        } catch (Exception ee) {
            System.out.println(" do not select break or lanch ");
            return null;
        }
        if (colmn < 0) {
            return null;
        }
        Slot slt = ((MasterTimeTableDisplay) klassTable.getSelectionModel().getSelectedItem()).getSlot(colmn);
        if (slt == null) {
            return null;
        }
        if (slt.isSportsPeriod()) {
            System.out.println(" can select sport period ");
            return null;
        }
        return slt;
    }

    private void showExchangeDialog(Slot slt) {
        if (slt.isSportsPeriod() || slt.containsSharedORCombined()) {
            return;
        }
        int y = 0;
        Stage newStage = new Stage();
        newStage.setResizable(false);
        Pane rty = new Pane();
        rty.setMinSize(500, 600);
        JFXComboBox dayCombo = new JFXComboBox();
        dayCombo.setPromptText("Day");
        for (int i = 1; i <= TimeTable.getCycleSize(); i++) {
            dayCombo.getItems().add(i);
        }
        dayCombo.setLabelFloat(true);
        dayCombo.setMinSize(300, 25);
        dayCombo.setLayoutX(100);
        dayCombo.setLayoutY(50);
        Label lbl2 = new Label();
        lbl2.setLayoutX(150);
        lbl2.setLayoutY(100);
        lbl2.setText("Period");
        HashMap<JFXCheckBox, Integer> allPeriods = new HashMap<>();
        JFXComboBox periodCombo = new JFXComboBox();
        periodCombo.setPromptText("Period");
        ToggleGroup group = new ToggleGroup();
        int x = 100;
        y = 140;
        for (int i = 1; i <= TimeTable.getNumPeriods(); i++) {
            JFXRadioButton jfxRadioB = new JFXRadioButton(i + "");
            jfxRadioB.setUserData(i);
            jfxRadioB.setToggleGroup(group);
            if (i != 1 && i % 5 == 1) {
                x = 100;
                y += 50;
            }
            jfxRadioB.setLayoutX(x);
            jfxRadioB.setLayoutY(y);
            x += 60;
            Separator sa = new Separator(Orientation.VERTICAL);
            sa.setPrefHeight(30);
            sa.setLayoutX(x);
            sa.setLayoutY(y);
            rty.getChildren().add(jfxRadioB);
            rty.getChildren().add(sa);
            periodCombo.getItems().add(i);
        }
        //periodCombo.setLabelFloat(true);
        //periodCombo.setMinSize(300, 25);
        //periodCombo.setLayoutX(100);
        //periodCombo.setLayoutY(100);
        y += 50;
        Button bbtn = new Button();
        bbtn.setLayoutX(100);
        bbtn.setLayoutY(y);
        bbtn.setText("Save");
        bbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //int prd = (int) periodCombo.getSelectionModel().getSelectedItem();
                int dyy = (int) dayCombo.getSelectionModel().getSelectedItem();
                int prd = (int) group.getSelectedToggle().getUserData();
                System.out.println(prd + "     PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPp  ");
                Slot otherSlt = timeTable.findSlot(dyy, prd, slt.getKlass());
                if (otherSlt.isSportsPeriod()) {
                    System.out.println(" Sports period");
                    return;
                }
                if (otherSlt.containsSharedORCombined()) {
                    System.out.println(" can not move shared or combined subject");
                    return;
                }
                if (slt.exchangeSlot(otherSlt)) {
                    slt.saveSlot();
                    otherSlt.saveSlot();
                    System.out.println("exchanged");
                    klassTable.refresh();

                    newStage.close();
                } else {
                    System.out.println("falied to exchange");
                }
            }
        });
        Button bbcl = new Button();
        bbcl.setLayoutX(300);
        bbcl.setLayoutY(y);
        bbcl.setText("Cancel");
        bbcl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newStage.close();
            }
        });

        rty.getChildren().add(dayCombo);
        rty.getChildren().add(bbcl);
        rty.getChildren().add(bbtn);
        // rty.getChildren().add(periodCombo);
        rty.getChildren().add(lbl2);
        Scene dialogScene = new Scene(rty, 500, 600);
        newStage.setScene(dialogScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle("Exchange slots ");
        newStage.showAndWait();
    }

    private void makeTimeTableTemplate1() {
        List<JFXTreeTableColumn> tableColumns = new ArrayList<>();
        JFXTreeTableColumn tC = new JFXTreeTableColumn("");
        tC.setCellValueFactory(new PropertyValueFactory<>("slot0"));
        tableColumns.add(tC);
        for (int i = 1; i <= TimeTable.getNumPeriods(); i++) {
            tC = new JFXTreeTableColumn("" + i);
            tC.setCellValueFactory(new PropertyValueFactory<>("slot" + i));
            tableColumns.add(tC);
            if (i == TimeTable.getPeriodsB4Brk()) {
                tC = new JFXTreeTableColumn("BREAK");
                tC.setCellValueFactory(new PropertyValueFactory<>("brkSlot"));
                tableColumns.add(tC);
            }
            if (i == TimeTable.getPeriodsB4Lnch()) {
                tC = new JFXTreeTableColumn("LAUNCH");
                tC.setCellValueFactory(new PropertyValueFactory<>("LanchSlot"));
                tableColumns.add(tC);
            }
        }
        // masterTable.getColumns().setAll(tableColumns);
    }

    @FXML
    public void viewMasterReportTimeTable(ActionEvent event) {
        if (dayMasterComboBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        int theDay = (int) dayMasterComboBox.getSelectionModel().getSelectedItem();
        List<TrTimeTableDisplay> myList = this.getMasterTimeTableDataForReport(theDay);
        HashMap map = new HashMap();
        map.put("ReportTitle", "DAY " + theDay);
        new ReportsFilling().fillMasterTimeTable(myList, map);
    }

    @FXML
    void downloadMasterReportTimeTable(ActionEvent event) {
        if (dayMasterComboBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        int theDay = (int) dayMasterComboBox.getSelectionModel().getSelectedItem();
        List<TrTimeTableDisplay> myList = this.getMasterTimeTableDataForReport(theDay);
        HashMap map = new HashMap();
        map.put("ReportTitle", "DAY " + theDay);
        new ReportsFilling().exportRtfMaster(myList, map);
    }

    @FXML
    public void downloadTrReportTimeTable(ActionEvent event) {
        Teacher theTr = (Teacher) trTimComboBox.getSelectionModel().getSelectedItem();
        List<TrTimeTableDisplay> myList = this.getTrTimeTableDataForReport(theTr);
        HashMap map = new HashMap();
        map.put("ReportTitle", theTr.toString());
        new ReportsFilling().exportRtfTr(myList, map);
    }

    @FXML
    public void viewTrReportTimeTable(ActionEvent event) {
        Teacher theTr = (Teacher) trTimComboBox.getSelectionModel().getSelectedItem();
        List<TrTimeTableDisplay> myList = this.getTrTimeTableDataForReport(theTr);
        HashMap map = new HashMap();
        map.put("ReportTitle", theTr.toString());
        new ReportsFilling().fillKlassTimeTable(myList, map);
    }

    @FXML
    public void downloadWholeSchoolTimeTable(ActionEvent event) {
        ObservableList<Klass> allKlasses = Klass.getAllKlasses();
        allKlasses.stream().filter((klas) -> !(klas == null)).forEach((klas) -> {
            List<TrTimeTableDisplay> myList = this.getKlassTimeTableDataForReport(klas);
            HashMap map = new HashMap();
            map.put("ReportTitle", klas.toString());
            new ReportsFilling().exportRtfKlass(myList, map);
        });
        ObservableList<Teacher> allTeachers = Teacher.getAllTeachers();
        allTeachers.stream().filter((theTr) -> !(theTr == null)).forEach((theTr) -> {
            List<TrTimeTableDisplay> myList = this.getTrTimeTableDataForReport(theTr);
            HashMap map = new HashMap();
            map.put("ReportTitle", theTr.toString());
            new ReportsFilling().exportRtfTr(myList, map);
        });
        for (int theDay = 1; theDay <= TimeTable.getCycleSize(); theDay++) {
            List<TrTimeTableDisplay> myList = this.getMasterTimeTableDataForReport(theDay);
            HashMap map = new HashMap();
            map.put("ReportTitle", "DAY " + theDay);
            new ReportsFilling().exportRtfMaster(myList, map);
        }
    }

    @FXML
    public void downloadKlassReportTimeTable(ActionEvent event) {
        Klass klas = (Klass) klasTimCombobox.getSelectionModel().getSelectedItem();
        if (klas == null) {
            return;
        }
        List<TrTimeTableDisplay> myList = this.getKlassTimeTableDataForReport(klas);
        HashMap map = new HashMap();
        map.put("ReportTitle", klas.toString());
        new ReportsFilling().exportRtfKlass(myList, map);
    }

    @FXML
    public void viewKlassReportTimeTable(ActionEvent event) {
        Klass klas = (Klass) klasTimCombobox.getSelectionModel().getSelectedItem();
        if (klas == null) {
            return;
        }
        List<TrTimeTableDisplay> myList = this.getKlassTimeTableDataForReport(klas);
        HashMap map = new HashMap();
        map.put("ReportTitle", klas.toString());
        new ReportsFilling().fillKlassTimeTable(myList, map);
    }

    public List<TrTimeTableDisplay> getKlassTimeTableDataForReport(Klass klas) {
        if (klas == null) {
            return FXCollections.observableArrayList();
        }
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            timeTable.makeTimeTable();
        }
        ObservableList<TrTimeTableDisplay> trTimeTableDisplays = FXCollections.observableArrayList();
        HashMap<Integer, ArrayList<Slot>> schoolTimetable = timeTable.getSchoolTimetable();
        for (Map.Entry<Integer, ArrayList<Slot>> entry : schoolTimetable.entrySet()) {
            ArrayList<Slot> wholeSchoolSingleDay = entry.getValue();
            for (Slot singleKlassSingleDay : wholeSchoolSingleDay) {
                if (!Objects.equals(singleKlassSingleDay.getKlass().getId(), klas.getId())) {
                    continue;
                }
                TrTimeTableDisplay mtd = new TrTimeTableDisplay();
                Slot curr = singleKlassSingleDay;
                mtd.setSlt(curr.getPeriodi(), curr.toStringKlass());
                if (singleKlassSingleDay.hasPrev()) {
                    while (curr.hasPrev()) {
                        curr = curr.getPrev();
                        mtd.setSlt(curr.getPeriodi(), curr.toStringKlass());
                    }
                }
                if (singleKlassSingleDay.hasNext()) {
                    curr = singleKlassSingleDay;
                    while (curr.hasNext()) {
                        curr = curr.getNext();
                        mtd.setSlt(curr.getPeriodi(), curr.toStringKlass());
                    }
                }
                if (MainPageController.inludeAssembly) {
                    mtd.setSltAR("A/Reg");
                }
                trTimeTableDisplays.add(mtd);
            }
        }
        return trTimeTableDisplays;
    }

    public List<TrTimeTableDisplay> getMasterTimeTableDataForReport(int theDay) {
        if (theDay > TimeTable.getCycleSize() || theDay <= 0) {
            return FXCollections.observableArrayList();
        }
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            timeTable.makeTimeTable();
        }
        HashMap<Integer, ArrayList<Slot>> schoolTimetable = timeTable.getSchoolTimetable();
        ArrayList<Slot> wholeSchoolSingleDay = schoolTimetable.get(theDay);
        ObservableList<TrTimeTableDisplay> trTimeTableDisplays = FXCollections.observableArrayList();
        for (Slot singleKlassSingleDay : wholeSchoolSingleDay) {
            TrTimeTableDisplay mtd = new TrTimeTableDisplay();
            Slot curr = singleKlassSingleDay;
            String cnts = curr.toStringKlass();
            if (curr.getPeriodi() == 0) {
                cnts = curr.getKlass().toString();
            }
            mtd.setSlt(curr.getPeriodi(), cnts);
            if (singleKlassSingleDay.hasPrev()) {
                while (curr.hasPrev()) {
                    curr = curr.getPrev();
                    cnts = curr.toStringKlass();
                    if (curr.getPeriodi() == 0) {
                        cnts = curr.getKlass().toString();
                    }
                    mtd.setSlt(curr.getPeriodi(), cnts);
                }
            }
            if (singleKlassSingleDay.hasNext()) {
                curr = singleKlassSingleDay;
                while (curr.hasNext()) {
                    curr = curr.getNext();
                    mtd.setSlt(curr.getPeriodi(), curr.toStringKlass());
                }
            }
            if (MainPageController.inludeAssembly) {
                mtd.setSltAR("A/Reg");
            }
            trTimeTableDisplays.add(mtd);
        }
        return trTimeTableDisplays;
    }

    public List<MasterTimeTableDisplay> getKlassTimeTableData(Klass klas) {
        if (klas == null) {
            return FXCollections.observableArrayList();
        }
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            timeTable.makeTimeTable();
        }
        MasterTimeTableDisplay.setType(2);
        ObservableList<MasterTimeTableDisplay> masterTimeTableDisplays = FXCollections.observableArrayList();
        HashMap<Integer, ArrayList<Slot>> schoolTimetable = timeTable.getSchoolTimetable();
        for (Map.Entry<Integer, ArrayList<Slot>> entry : schoolTimetable.entrySet()) {
            ArrayList<Slot> wholeSchoolSingleDay = entry.getValue();
            for (Slot singleKlassSingleDay : wholeSchoolSingleDay) {
                if (Objects.equals(singleKlassSingleDay.getKlass().getId(), klas.getId())) {
                    MasterTimeTableDisplay mtd = new MasterTimeTableDisplay();
                    mtd.setSlots(singleKlassSingleDay);
                    masterTimeTableDisplays.add(mtd);
                    break;
                }
            }
        }
        return masterTimeTableDisplays;
    }

    @FXML
    public void fillMasterTimeTablePerDay(ActionEvent event) {
        int theDay = (int) dayMasterComboBox.getSelectionModel().getSelectedItem();
        if (theDay < 1 || theDay > TimeTable.getCycleSize()) {
            return;
        }
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            timeTable.makeTimeTable();
        }
        MasterTimeTableDisplay.setType(1);
        ObservableList<MasterTimeTableDisplay> masterTimeTableDisplays = FXCollections.observableArrayList();
        HashMap<Integer, ArrayList<Slot>> schoolTimetable = timeTable.getSchoolTimetable();
        ArrayList<Slot> wholeSchoolSingleDay = schoolTimetable.get(theDay);
        for (Slot singleKlassSingleDay : wholeSchoolSingleDay) {
            MasterTimeTableDisplay mtd = new MasterTimeTableDisplay();
            mtd.setSlots(singleKlassSingleDay);
            masterTimeTableDisplays.add(mtd);
        }

        masterTableV.setItems(masterTimeTableDisplays);
    }

    @FXML
    public void fillKlassTimeTablePerDay(ActionEvent event) {
        Klass klas = (Klass) klasTimCombobox.getSelectionModel().getSelectedItem();
        if (klas == null) {
            return;
        }
        klassTable.setItems(FXCollections.observableArrayList(getKlassTimeTableData(klas)));
    }

    public List<TrTimeTableDisplay> getTrTimeTableDataForReport(Teacher theTr) {
        if (theTr == null) {
            return new ArrayList<>();

        }
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            return new ArrayList<>();
        }
        ObservableList<TrTimeTableDisplay> trTimeTableDisplays = FXCollections.observableArrayList();
        HashMap<Integer, ArrayList<Slot>> schoolTimetable = timeTable.getSchoolTimetable();
        for (Map.Entry<Integer, ArrayList<Slot>> entry : schoolTimetable.entrySet()) {
            ArrayList<Slot> wholeSchoolSingleDay = entry.getValue();
            Slot singleKlassSingleDay = wholeSchoolSingleDay.get(0);
            while (singleKlassSingleDay.hasPrev()) {
                singleKlassSingleDay = singleKlassSingleDay.getPrev();
            }
            while (singleKlassSingleDay.hasUp()) {
                singleKlassSingleDay = singleKlassSingleDay.getUp();
            }
            TrTimeTableDisplay trDisplay = new TrTimeTableDisplay();
            if (singleKlassSingleDay.getPeriodi() == 0) {
                trDisplay.setSlt(singleKlassSingleDay.getPeriodi(), "DAY " + singleKlassSingleDay.getDy());
            }
            while (singleKlassSingleDay.hasNext()) {
                singleKlassSingleDay = singleKlassSingleDay.getNext();
                Slot curr = singleKlassSingleDay;
                String subKls = "";
                do {
                    List<TeacherSubjectKlass> trSubKlasList = curr.getTrSubKlasList();
                    for (TeacherSubjectKlass tsKsSb : trSubKlasList) {
                        if (tsKsSb.getTeacher().getId() == theTr.getId()) {
                            subKls += tsKsSb.getKlass() + " " + tsKsSb.getSubject().getCode() + "\n";
                        }
                    }
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                } while (true);
                trDisplay.setSlt(curr.getPeriodi(), subKls);
            }
            if (MainPageController.inludeAssembly) {
                trDisplay.setSltAR("A/Reg");
            }
            trTimeTableDisplays.add(trDisplay);
        }
        return trTimeTableDisplays;
    }

    @FXML
    public void fillTrTimeTable(ActionEvent event) {
        Teacher theTr = (Teacher) trTimComboBox.getSelectionModel().getSelectedItem();
        trTable.setItems(FXCollections.observableArrayList(this.getTrTimeTableDataForReport(theTr)));
    }

    @FXML
    public void saveTimeTable(ActionEvent event) {
        if (timeTable.getSchoolTimetable() == null || timeTable.getSchoolTimetable().isEmpty()) {
            return;
        }
        timeTable.saveTimeTableToDB();
    }

    @FXML
    public void generateTimeTable(ActionEvent event) {
        for (int i = 0; i < 1; i++) {
            TimeTable temp = new TimeTable();
            temp.makeTimeTable();
            if (i == 0) {
                timeTable = temp;
            } else {
                if (temp.getTotalDiscripancy() < timeTable.getTotalDiscripancy()) {
                    timeTable = temp;
                }
            }
            System.out.println(i + " ROUNDS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            if (timeTable.getTotalDiscripancy() <= 10) {
                break;
            }
        }
    }

    @FXML
    public void compileJXML(ActionEvent event) {
        new JXMLCREATOR().compileJXML();
    }

    @FXML
    public void createAndCompileJXML(ActionEvent event) {
        JXMLCREATOR.makeJXMLTempate();
        JXMLCREATOR.makeJXMLTempateMaster();
        new JXMLCREATOR().compileJXML();
    }

    @FXML
    public void fillDiscp(ActionEvent event) {
        timeTable.fillDiscripance();
    }

    @FXML
    public void backTrack(ActionEvent event) {
        timeTable.backTracking();
    }
}
