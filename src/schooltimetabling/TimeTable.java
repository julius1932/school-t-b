/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
public class TimeTable {

    private Slot slot;
    private static int cycleSize;
    private static int numPeriods;

    private static int periodsB4Brk;
    private static int periodsB4Lnch;
    private static int periodsAfterLnch;

    private static int lesssonDuration;
    private static int brkDuration;
    private static int lnchDuration;

    private static LocalTime startTime;
    private static LocalTime brkTime;
    private static LocalTime lnchTime;

    private static List<SportsDay> sportsDays = new ArrayList<>();
    private HashMap<Integer, ArrayList<Slot>> schoolTimetable = new HashMap<>();
    private HashMap<String, Subject_constrainsOnTheFly> subject_constrains = new HashMap<>();
    private HashMap<Klass, ArrayList<Integer>> klassDays = new HashMap<>();
    private HashMap<String, Integer> klassDaySubLoged = new HashMap<>();

    static {
        fillOveralConstrains();
    }

    public static void fillOveralConstrains() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList< OveralConstrains> overalConstrains = FXCollections.observableArrayList(session.createQuery("from  OveralConstrains").list());
            if (!overalConstrains.isEmpty()) {
                OveralConstrains overalCon = overalConstrains.get(0);
                if (overalCon == null) {
                    return;
                }
                TimeTable.setCycleSize(overalCon.getCycleSize());
                TimeTable.setNumPeriods(overalCon.getNumPeriods());
                TimeTable.setPeriodsB4Brk(overalCon.getPeriodsB4Brk());
                TimeTable.setPeriodsB4Lnch(overalCon.getPeriodsB4Lnch());

                TimeTable.setStartTime(overalCon.getStartTime());
                TimeTable.setBrkTime(overalCon.getBrkTime());
                TimeTable.setLnchTime(overalCon.getLnchTime());

                TimeTable.setLesssonDuration(overalCon.getLesssonDuration());
                TimeTable.setBrkDuration(overalCon.getBrkDuration());
                TimeTable.setLnchDuration(overalCon.getLnchDuration());
                //TimeTable.setLesssonDuration(overalCon.getLesssonDuration());

                List<SportsDay> sportsDays1 = overalCon.getSportsDays();
                sportsDays = new ArrayList<>();
                for (SportsDay sp : sportsDays1) {
                    SportsDay dd = new SportsDay(sp.getDy());
                    dd.setId(dd.getId());
                    sportsDays.add(dd);
                }

            }
        }
    }

    public TimeTable() {

    }

    public TimeTable(Slot slot) {
        this.slot = slot;
    }

    public ObservableList<Klass> getKlasses() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<Klass> klasses = FXCollections.observableArrayList();
            List<Klass> list = session.createQuery("from Klass").list();
            klasses.addAll(list);
            return klasses;
        }

    }

    public ObservableList<TeacherSubjectKlass> getTeacherSubjectKlass(Klass klass) {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<TeacherSubjectKlass> teacherSubjectKlass = FXCollections.observableArrayList();

            List<TeacherSubjectKlass> list = session.createQuery("from TeacherSubjectKlass where klass=" + klass.getId()).list();
            teacherSubjectKlass.addAll(list);
            return teacherSubjectKlass;
        }
    }

    private void addToSubKlassLessonsLogged(List<TeacherSubjectKlass> list) {
        if (list == null) {
            return;
        }
        for (TeacherSubjectKlass trSubKlas : list) {
            Subject_constrainsOnTheFly subCon = getSubject_constrains().get(trSubKlas.getKlass().getId() + trSubKlas.getSubject().getCode());
            subCon.addToWeeklyNumLessonLoged(1);
        }
    }

    public void makeTimeTableFromDb(TimeTableSaved ref) {
        this.logSubjectConstrains();
        ObservableList<Klass> klasses = this.getKlasses();
        if (klasses == null || klasses.isEmpty()) {
            return;
        }
        this.setSlot(new Slot());
        HashMap<Integer, ArrayList<Slot>> wholeSchoolTimetable = new HashMap<>();
        int cycleSize1 = TimeTable.getCycleSize();
        System.out.println(" cycleSize1  " + cycleSize1);
        for (int day = 1; day <= cycleSize1; day++) {
            System.out.println(" day  " + day);
            ArrayList<Slot> singleDayWholeSchoolTimeTable = new ArrayList();
            for (Klass klas : klasses) {
                Slot singleDayKlassTimeTable = new Slot(0, klas);
                Slot curr = singleDayKlassTimeTable;
                curr.setDy(day);
                while (curr.getPeriodi() < numPeriods) {
                    curr.setSlot(ref);
                    addToSubKlassLessonsLogged(curr.getTrSubKlasList());
                    curr.setNext(new Slot(curr.getPeriodi() + 1, klas, day));
                    Slot next = curr.getNext();
                    next.setPrev(curr);
                    curr = next;
                }
                curr.setSlot(ref);
                addToSubKlassLessonsLogged(curr.getTrSubKlasList());
                singleDayWholeSchoolTimeTable.add(singleDayKlassTimeTable);
                //System.out.println(" singleDayKlassTimeTable  "+singleDayKlassTimeTable.getKlas());
            }
            wholeSchoolTimetable.put(day, singleDayWholeSchoolTimeTable);
        }
        this.setSchoolTimetable(wholeSchoolTimetable);
        this.linkUpDown();
    }

    public void makeTimeTable() {
        ObservableList<Klass> klasses = this.getKlasses();
        if (klasses == null || klasses.isEmpty()) {
            return;
        }
        this.setSlot(new Slot());
        HashMap<Integer, ArrayList<Slot>> wholeSchoolTimetable = new HashMap<>();
        int cycleSize1 = TimeTable.getCycleSize();
        System.out.println(" cycleSize1  " + cycleSize1);
        for (int day = 1; day <= cycleSize1; day++) {
            System.out.println(" day  " + day);
            ArrayList<Slot> singleDayWholeSchoolTimeTable = new ArrayList();
            for (Klass klas : klasses) {
                Slot singleDayKlassTimeTable = new Slot(0, klas);
                Slot curr = singleDayKlassTimeTable;
                while (curr.getPeriodi() < numPeriods) {
                    curr.setDy(day);
                    curr.setNext(new Slot(curr.getPeriodi() + 1, klas, day));
                    Slot next = curr.getNext();
                    next.setPrev(curr);
                    curr = next;
                }
                curr.setDy(day);
                singleDayWholeSchoolTimeTable.add(singleDayKlassTimeTable);
                //System.out.println(" singleDayKlassTimeTable  "+singleDayKlassTimeTable.getKlas());
            }
            wholeSchoolTimetable.put(day, singleDayWholeSchoolTimeTable);
        }
        this.setSchoolTimetable(wholeSchoolTimetable);
        this.linkUpDown();
        this.revisedFillTimeTable();
        // this.fillDiscripance();
        //this.backTracking();
        this.display();
        //this.backracking();
    }

    public void saveTimeTableToDB() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            session.beginTransaction();
            boolean noRef = false;
            TimeTableSaved reff;
            TimeTableSaved newRef = new TimeTableSaved();
            ObservableList<Klass> allKlasses = Klass.getAllKlasses();
            if (!allKlasses.isEmpty()) {
                Slot sltt = this.findSlot(1, 1, allKlasses.get(0));
                reff = sltt.getRef();
                if (reff != null) {
                    TimeTableSaved getRf = session.get(TimeTableSaved.class, reff.getId());
                    if (getRf == null) {
                        session.saveOrUpdate(reff);
                    }
                } else {
                    session.saveOrUpdate(newRef);
                    noRef = true;
                }
            } else {
                return;
            }

            for (Map.Entry<Integer, ArrayList<Slot>> entry : this.getSchoolTimetable().entrySet()) {
                ArrayList<Slot> singleDayTimeTable = entry.getValue();
                for (Slot dayKlasTimT : singleDayTimeTable) {
                    Slot curr = dayKlasTimT;
                    do {
                        if (noRef) {
                            curr.setRef(newRef);
                        } else {
                            curr.setRef(reff);
                        }
                        System.out.println(curr.getTrSubKlasList().size());

                        session.saveOrUpdate(curr);

                        if (curr.hasNext()) {
                            curr = curr.getNext();
                        } else {
                            break;
                        }
                    } while (true);
                    System.out.println("");
                }

            }
            session.getTransaction().commit();
        }
        System.out.println("Time table Saved");
    }

    private void display() {
        for (Map.Entry<Integer, ArrayList<Slot>> entry : this.getSchoolTimetable().entrySet()) {
            ArrayList<Slot> singleDayTimeTable = entry.getValue();
            System.out.println(" DAY " + entry.getKey());
            for (Slot dayKlasTimT : singleDayTimeTable) {
                Slot curr = dayKlasTimT;
                do {
                    System.out.print(curr.toStringCdis());
                    if (curr.hasNext()) {
                        curr = curr.getNext();
                    } else {
                        break;
                    }
                } while (true);
                System.out.println("");
            }
        }
    }

    private void linkUpDown() {
        for (Map.Entry<Integer, ArrayList<Slot>> entry : this.getSchoolTimetable().entrySet()) {
            ArrayList<Slot> singleDayTimeTable = entry.getValue();
            //System.out.println(curr.getKlas());
            for (int pri = 0; pri <= TimeTable.numPeriods; pri++) {
                Slot curr = singleDayTimeTable.get(0);
                while (curr.getPeriodi() != pri && curr.hasNext()) {
                    curr = curr.getNext();
                }
                for (int i = 0; i < singleDayTimeTable.size(); i++) {
                    if (i != 0) {
                        Slot temp = singleDayTimeTable.get(i - 1);
                        while (curr.getPeriodi() != temp.getPeriodi() && temp.hasNext()) {
                            temp = temp.getNext();
                        }
                        if (curr.getPeriodi() == temp.getPeriodi()) {
                            curr.setUp(temp);
                        }
                    }
                    if (i != singleDayTimeTable.size() - 1) {
                        Slot temp = singleDayTimeTable.get(i + 1);
                        while (curr.getPeriodi() != temp.getPeriodi() && temp.hasNext()) {
                            temp = temp.getNext();
                        }
                        if (curr.getPeriodi() == temp.getPeriodi()) {
                            curr.setDown(temp);
                        }
                    }
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public List<TeacherSubjectKlass> getEffectiveSubKlasTr(TeacherSubjectKlass trSubKlas) {
        List<TeacherSubjectKlass> effectiveTrSubKlas = new ArrayList<>();
        List<TeacherSubjectKlass> sharedSubject = trSubKlas.getSharedSubject();
        for (TeacherSubjectKlass shared : sharedSubject) {
            effectiveTrSubKlas.addAll(shared.getCombinedSubject());
        }
        return effectiveTrSubKlas;
    }

    public void fillDiscripance() {
        for (int pi = 0; pi < 10; pi++) {
            HashMap<String, Subject_constrainsOnTheFly> subject_constrns = this.getSubject_constrains();
            for (Map.Entry<String, Subject_constrainsOnTheFly> entry : subject_constrns.entrySet()) {
                Subject_constrainsOnTheFly subCon = entry.getValue();

                if (subCon.getNumLessonLoged() < subCon.getNumPeriods()) {
                    TeacherSubjectKlass pullSubKlasTr = subCon.pullSubKlasTr();
                    List<TeacherSubjectKlass> effectiveSubKlasTr = this.getEffectiveSubKlasTr(pullSubKlasTr);
                    List<Integer> daysWithoutSub = this.getDaysWithoutSub(effectiveSubKlasTr);
                    for (int day : daysWithoutSub) {
                        if (!(subCon.getNumLessonLoged() < subCon.getNumPeriods())) {
                            break;
                        }
                        ArrayList<PeriodsFilling> arrLis = new ArrayList<>();
                        boolean isLoggedToday = false;
                        for (TeacherSubjectKlass efTrSubKlas : effectiveSubKlasTr) {
                            this.getKlassDaySubLoged().remove(day + "" + efTrSubKlas.getKlass().getId() + efTrSubKlas.getSubject().getCode(), 0);
                            if (this.getKlassDaySubLoged().get(day + "" + efTrSubKlas.getKlass().getId() + efTrSubKlas.getSubject().getCode()) == null) {
                                this.logSubjectConstrains(efTrSubKlas.getKlass(), efTrSubKlas.getSubject());
                            } else {
                                System.out.println(subCon + "   " + day + "    =====================================================================================================================");
                                isLoggedToday = true;
                            }
                        }
                        if (isLoggedToday) {
                            continue;
                        }
                        //System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        ArrayList<Integer> possibleAndFreePeriods = this.getPossibleAndFreePeriods(day, effectiveSubKlasTr);
                        //.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        PeriodsFilling pf = new PeriodsFilling(pullSubKlasTr, possibleAndFreePeriods);
                        arrLis.add(pf);
                        PeriodsFilling.sortPeriodsFilling(arrLis);
                        this.fillingIntoSlots(day, arrLis);
                    }
                }
            }
        }
    }

    public List<Integer> getDaysWithoutSub(List<TeacherSubjectKlass> effectiveSubKlasTr) {
        List<Integer> daysWithOut = new ArrayList<>();
        for (int i = 1; i <= TimeTable.getCycleSize(); i++) {
            boolean subBooked = false;
            for (TeacherSubjectKlass subTrKlas : effectiveSubKlasTr) {
                Slot findSlot = this.findSlot(i, 1, subTrKlas.getKlass());
                if (findSlot.hasThisSubjectOnThisDay(subTrKlas.getSubject())) {
                    subBooked = true;
                    break;
                }
            }
            if (!subBooked) {
                daysWithOut.add(i);
            }
        }
        return daysWithOut;
    }

    public boolean hasShakedToAnotherDay(Slot clashingSlot) {
        if (clashingSlot.containsSharedORCombinedOrDouble()) {
            return false;
        }
        List<Integer> daysWithoutSub = this.getDaysWithoutSub(clashingSlot.getTrSubKlasList());
        for (int dayWithOut : daysWithoutSub) {
            Slot curr = this.findSlot(dayWithOut, 1, clashingSlot.getKlass());
            do {
                if (!curr.containsSharedORCombinedOrDouble()) {
                    if (curr.exchangeSlot(clashingSlot)) {
                        return true;
                    }
                }
                if (curr.hasNext()) {
                    curr = curr.getNext();
                } else {
                    break;
                }
            } while (true);
        }
        return true;
    }

    public boolean hasShakedWithInSameDay(Slot clashingSlot) {
        if (clashingSlot.containsSharedORCombinedOrDouble()) {
            return false;
        }
        for (int i = 0; i < 2; i++) {
            do {
                Slot curr = clashingSlot;
                if (i == 0) {
                    if (curr.hasNext()) {
                        curr = curr.getNext();
                    } else {
                        break;
                    }
                }
                if (i == 1) {
                    if (curr.hasPrev()) {
                        curr = curr.getNext();
                    } else {
                        break;
                    }
                }
                if (!curr.containsSharedORCombinedOrDouble()) {
                    if (curr.exchangeSlot(clashingSlot)) {
                        return true;
                    }
                }
            } while (true);
        }
        return false;
    }

    public void makePossibleFree(int day, ArrayList<Integer> possiblePeriods, List<TeacherSubjectKlass> effectiveSubKlasTr, Subject_constrainsOnTheFly subCon) {
        System.out.println("make Possible freee PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        int diff1 = subCon.getNumPeriods() - subCon.getNumLessonLoged();
        if (effectiveSubKlasTr.size() > 1 || effectiveSubKlasTr.isEmpty() || diff1 <= 0 || possiblePeriods.isEmpty()) {
            return;
        }
        Klass klass = effectiveSubKlasTr.get(0).getKlass();
        ArrayList<Slot> findSlots = this.findSlots(day, possiblePeriods, klass);
        for (Slot pSlt : findSlots) {
            ArrayList<Integer> freePeriods = this.getFreePeriods(day, effectiveSubKlasTr);
            if (pSlt.containsSharedORCombinedOrDouble() || freePeriods.isEmpty()) {
                continue;
            }
            ArrayList<Slot> freeSlots = this.findSlots(day, freePeriods, klass);
            for (Slot fSlt : freeSlots) {
                if (fSlt.containsSharedORCombinedOrDouble()) {
                    continue;
                }
                if (fSlt.exchangeSlot(pSlt)) {
                    ArrayList<Integer> possibleAndFreePeriods = this.getPossibleAndFreePeriods(day, effectiveSubKlasTr);
                    if (!possibleAndFreePeriods.isEmpty()) {
                        System.out.println(possibleAndFreePeriods + "        UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
                        PeriodsFilling pf = new PeriodsFilling(effectiveSubKlasTr.get(0), possibleAndFreePeriods);
                        ArrayList<PeriodsFilling> arrLis = new ArrayList<>();
                        arrLis.add(pf);
                        this.fillingIntoSlots(day, arrLis);
                        List<Integer> daysWithoutSub = this.getDaysWithoutSub(effectiveSubKlasTr);
                        if (daysWithoutSub.contains(day)) {
                            return;
                        }
                    }
                }
            }
        }

    }

    public void makeFreePossible(int day, ArrayList<Integer> freePeriods, List<TeacherSubjectKlass> effectiveSubKlasTr, Subject_constrainsOnTheFly subCon) {
        int diff1 = subCon.getNumPeriods() - subCon.getNumLessonLoged();
        if (effectiveSubKlasTr.size() > 1 || effectiveSubKlasTr.isEmpty() || diff1 <= 0) {
            return;
        }
        Klass klass = effectiveSubKlasTr.get(0).getKlass();
        ArrayList<Slot> findSlots = this.findSlots(day, freePeriods, klass);
        for (Slot freeSlt : findSlots) {
            if (!freeSlt.isFree()) {
                continue;
            }
            Slot clashingSlot = freeSlt.getClashingSlot(effectiveSubKlasTr);

            if (clashingSlot == null) {
                System.out.println("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            } else {
                System.out.println(clashingSlot.toStringTr() + " DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                if (clashingSlot.containsSharedORCombinedOrDouble()) {
                    continue;
                }
                if (this.hasShakedWithInSameDay(clashingSlot)) {
                } else {
                    if (this.hasShakedToAnotherDay(clashingSlot)) {
                    }
                }
            }
        }
        ArrayList<Integer> possibleAndFreePeriods = this.getPossibleAndFreePeriods(day, effectiveSubKlasTr);
        if (!possibleAndFreePeriods.isEmpty()) {
            System.out.println(possibleAndFreePeriods + "        UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
            PeriodsFilling pf = new PeriodsFilling(effectiveSubKlasTr.get(0), possibleAndFreePeriods);
            ArrayList<PeriodsFilling> arrLis = new ArrayList<>();
            arrLis.add(pf);
            this.fillingIntoSlots(day, arrLis);
        }
    }

    public boolean isFreeOrPossiblePeriodsSizeGood(int sizeOfFreeOrPossiblePeriods, Subject_constrainsOnTheFly subCon) {
        List<Integer> preferedDuration = subCon.getPreferedDuration();
        int diff = subCon.getNumPeriods() - subCon.getNumLessonLoged();
        if (preferedDuration.size() == 1) {
            if (preferedDuration.get(0) <= sizeOfFreeOrPossiblePeriods) {
                return true;
            }
            return false;
        }
        Collections.sort(preferedDuration);
        for (int i = 1; i <= 0; i--) {
            int pDrtn = preferedDuration.get(i);
            if (i == 1 && subCon.isGreatest(pDrtn) && !subCon.isGreatestLoged() && diff == pDrtn) {
                if (pDrtn <= sizeOfFreeOrPossiblePeriods) {
                    return true;
                }
                return false;
            }
            if (i == 0) {
                if (pDrtn <= sizeOfFreeOrPossiblePeriods) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getTotalDiscripancy() {
        int totalDis = 0;
        HashMap<String, Subject_constrainsOnTheFly> subject_constrns = this.getSubject_constrains();
        totalDis = subject_constrns.entrySet().stream().map((entry) -> entry.getValue()).map((subCon) -> subCon.getDifference()).reduce(totalDis, Integer::sum);
        return totalDis;
    }

    public void backTracking() {
        HashMap<String, Subject_constrainsOnTheFly> subject_constrns = this.getSubject_constrains();
        for (Map.Entry<String, Subject_constrainsOnTheFly> entry : subject_constrns.entrySet()) {
            Subject_constrainsOnTheFly subCon = entry.getValue();
            if (subCon.getNumLessonLoged() < subCon.getNumPeriods()) {
                System.out.println("back tracking make free possible " + subCon.toString());
                TeacherSubjectKlass pullSubKlasTr = subCon.pullSubKlasTr();
                List<TeacherSubjectKlass> effectiveSubKlasTr = this.getEffectiveSubKlasTr(pullSubKlasTr);
                if (effectiveSubKlasTr.size() > 1) {
                    continue;
                }
                List<Integer> daysWithoutSub = this.getDaysWithoutSub(effectiveSubKlasTr);
                for (int day : daysWithoutSub) {
                    int diff = subCon.getNumPeriods() - subCon.getNumLessonLoged();
                    if (diff <= 0) {
                        break;
                    }
                    ArrayList<Integer> possiblePeriods = this.getPossiblePeriods(day, effectiveSubKlasTr);
                    this.makePossibleFree(day, possiblePeriods, effectiveSubKlasTr, subCon);
                    if (!this.getDaysWithoutSub(effectiveSubKlasTr).contains(day)) {
                        continue;
                    }
                    diff = subCon.getNumPeriods() - subCon.getNumLessonLoged();
                    ArrayList<Integer> freePeriods = this.getFreePeriods(day, effectiveSubKlasTr);
                    if (diff > 0 && this.isFreeOrPossiblePeriodsSizeGood(freePeriods.size(), subCon)) {
                        this.makeFreePossible(day, freePeriods, effectiveSubKlasTr, subCon);
                    }
                }
            }
        }
    }

    public void revisedFillTimeTable() {
        this.setKlassDays();
        for (int k = 0; k < 2; k++) {
            for (Map.Entry<Klass, ArrayList<Integer>> entry : this.getKlassDays().entrySet()) {
                Klass klass = entry.getKey();
                ArrayList<Integer> days = entry.getValue();
                Collections.shuffle(days);
                for (int day : days) {
                    HashMap<String, Integer> promisingMap = new HashMap<>();
                    ArrayList<PeriodsFilling> arrLis = new ArrayList<>();
                    ObservableList<TeacherSubjectKlass> allTrSubKlas = this.getTeacherSubjectKlass(klass);
                    if (allTrSubKlas != null && !allTrSubKlas.isEmpty()) {
                        Collections.shuffle(allTrSubKlas);
                        for (TeacherSubjectKlass trSubKlas : allTrSubKlas) {
                            List<TeacherSubjectKlass> effectiveTrSubKlasList = this.getEffectiveSubKlasTr(trSubKlas);
                            if (k == 0 && effectiveTrSubKlasList.size() <= 1) {// giving preference to shared and combined
                                this.logSubjectConstrains(trSubKlas.getKlass(), trSubKlas.getSubject());
                                Subject_constrainsOnTheFly theSubCon = subject_constrains.get(trSubKlas.getKlass().getId() + trSubKlas.getSubject().getCode());
                                System.out.println(theSubCon + "    WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
                                if (!theSubCon.hasBlockOf4()) { //giving preference to those with blocks
                                    continue;
                                }
                            }
                            boolean isLoggedToday = false;
                            for (TeacherSubjectKlass efTrSubKlas : effectiveTrSubKlasList) {
                                if (!this.getKlassDaySubLoged().containsKey(day + "" + efTrSubKlas.getKlass().getId() + efTrSubKlas.getSubject().getCode())) {
                                    if (promisingMap.containsKey(day + "" + efTrSubKlas.getKlass().getId() + efTrSubKlas.getSubject().getCode())) {
                                        isLoggedToday = true;
                                    } else {
                                        promisingMap.put(day + "" + efTrSubKlas.getKlass().getId() + efTrSubKlas.getSubject().getCode(), 0);
                                    }
                                    this.logSubjectConstrains(efTrSubKlas.getKlass(), efTrSubKlas.getSubject());
                                } else {
                                    isLoggedToday = true;
                                }
                            }
                            if (isLoggedToday) {
                                continue;
                            }
                            ArrayList<Integer> possibleAndFreePeriods = this.getPossibleAndFreePeriods(day, effectiveTrSubKlasList);
                            System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                            PeriodsFilling pf = new PeriodsFilling(trSubKlas, possibleAndFreePeriods);
                            arrLis.add(pf);
                        }
                    }
                    PeriodsFilling.sortPeriodsFilling(arrLis);
                    this.fillingIntoSlots(day, arrLis);
                }
            }
        }
    }

    public String showBreakLunchOverlap() {
        if (this.getSchoolTimetable() == null || this.getSchoolTimetable().isEmpty()) {
            return " Time table not initialised";
        }
        String kk = "";
        HashMap<Integer, ArrayList<Slot>> schoolTmetable = this.getSchoolTimetable();
        for (Map.Entry<Integer, ArrayList<Slot>> entry : schoolTmetable.entrySet()) {
            ArrayList<Slot> wholeSchoolSingleDay = entry.getValue();
            Slot slt = wholeSchoolSingleDay.get(0);
            while (slt.hasPrev()) {
                slt = slt.getPrev();
            }
            while (slt.hasUp()) {
                slt = slt.getUp();
            }
            while (slt.getPeriodi() != TimeTable.getPeriodsB4Brk() && slt.hasNext()) {
                slt = slt.getNext();
            }
            Slot curr = slt;
            if (slt.getPeriodi() == TimeTable.getPeriodsB4Brk()) {
                do {
                    if (!curr.isFree() && curr.hasNext() && !curr.getNext().isFree()) {
                        List<TeacherSubjectKlass> trSubKlasList = curr.getTrSubKlasList();
                        List<TeacherSubjectKlass> trSubKlasList1 = curr.getNext().getTrSubKlasList();
                        if (trSubKlasList1.size() == trSubKlasList.size() && !trSubKlasList1.isEmpty()) {
                            if (curr.toStringKlass().equals(curr.getNext().toStringKlass())) {
                                kk += " BREAK OVERLAP : " + curr.getKlass() + "   DAY " + curr.getDy() + "\n";
                            }
                        }
                    }
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                } while (true);
            }
            while (slt.getPeriodi() != TimeTable.getPeriodsB4Lnch() && slt.hasNext()) {
                slt = slt.getNext();
            }
            curr = slt;
            if (slt.getPeriodi() == TimeTable.getPeriodsB4Lnch()) {
                do {
                    if (!curr.isFree() && curr.hasNext() && !curr.getNext().isFree()) {
                        List<TeacherSubjectKlass> trSubKlasList = curr.getTrSubKlasList();
                        List<TeacherSubjectKlass> trSubKlasList1 = curr.getNext().getTrSubKlasList();
                        if (trSubKlasList1.size() == trSubKlasList.size() && !trSubKlasList1.isEmpty()) {
                            if (curr.toStringKlass().equals(curr.getNext().toStringKlass())) {
                                kk += " LUNCH OVERLAP : " + curr.getKlass() + "   DAY " + curr.getDy() + "\n";
                            }
                        }
                    }
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                } while (true);
            }
        }
        if (kk.isEmpty()) {
            kk = "MAKOROKOTO NO BREAK OR LUNCH OVERLAP";
        }
        return kk;
    }

    public String showAnomalise() {
        if (this.getSchoolTimetable() == null || this.getSchoolTimetable().isEmpty()) {
            return " Time table not initialised";
        }
        String kk = "";
        HashMap<Integer, ArrayList<Slot>> schoolTmetable = this.getSchoolTimetable();
        for (Map.Entry<Integer, ArrayList<Slot>> entry : schoolTmetable.entrySet()) {
            ArrayList<Slot> wholeSchoolSingleDay = entry.getValue();
            for (Slot klassSingleDay : wholeSchoolSingleDay) {
                HashMap<String, Integer> subDay = new HashMap<>();
                Slot curr = klassSingleDay;
                while (curr.hasPrev()) {
                    curr = curr.getPrev();
                }
                System.out.println();
                do {
                    System.out.println(curr.getKlass()+"  "+curr.getDy()+" "+curr.getPeriodi());
                    //if (curr.isFree() || curr.isSportsPeriod()) {
                      //  continue;
                    //}
                    List<TeacherSubjectKlass> trSubKlasList = curr.getTrSubKlasList();
                    for (TeacherSubjectKlass trSubKlas : trSubKlasList) {
                        String subCode = trSubKlas.getSubject().getCode().trim();
                        if (subDay.containsKey(subCode)) {
                            int prevPos = subDay.get(subCode);
                            if (curr.getPeriodi() - prevPos != 1) {
                                kk = "Anomaly in " + curr.getKlass() + "    " + subCode + "  DAY " + curr.getDy() + "  PERIODS  " + prevPos + " AND " + curr.getPeriodi();
                            } else {
                                subDay.put(subCode, curr.getPeriodi());
                            }
                        } else {
                            subDay.put(subCode, curr.getPeriodi());
                        }
                    }
                    if (curr.hasNext()) {
                        curr = curr.getNext();
                    } else {
                        break;
                    }
                } while (true);

            }
        } 
        if (kk.isEmpty()) {
            kk = "MAKOROKOTO NO ANOMALISE";
        }
        return kk;

    }

    public String showClashes() {
        if (this.getSchoolTimetable() == null || this.getSchoolTimetable().isEmpty()) {
            return " Time table not initialised";
        }
        String kk = "";
        HashMap<Integer, ArrayList<Slot>> schoolTmetable = this.getSchoolTimetable();
        for (Map.Entry<Integer, ArrayList<Slot>> entry : schoolTmetable.entrySet()) {
            ArrayList<Slot> wholeSchoolSingleDay = entry.getValue();
            Slot slt = wholeSchoolSingleDay.get(0);
            while (slt.hasPrev()) {
                slt = slt.getPrev();
            }
            while (slt.hasUp()) {
                slt = slt.getUp();
            }
            while (slt.hasNext()) {
                HashMap<Long, TeacherSubjectKlass> ourMap = new HashMap<>();
                slt = slt.getNext();
                Slot curr = slt;
                //System.out.println("========================================================="+slt.getDy());
                do {
                    if (!curr.getTrSubKlasList().isEmpty()) {
                        List<TeacherSubjectKlass> trSubKlasList = curr.getTrSubKlasList();
                        for (TeacherSubjectKlass subKlasTr : trSubKlasList) {
                            if (ourMap.containsKey(subKlasTr.getTeacher().getId())) {
                                // System.out.println("=============================ourMap.containsKey(subKlasTr.getTeacher()=========================================");
                                TeacherSubjectKlass old = ourMap.get(subKlasTr.getTeacher().getId());
                                List<TeacherSubjectKlass> effectiveSubKlasTr = this.getEffectiveSubKlasTr(old);
                                if (!subKlasTr.isInList(effectiveSubKlasTr)) {
                                    //System.out.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
                                    kk += subKlasTr.toString() + " Clash at Day " + curr.getDy() + "  period " + curr.getPeriodi() + " with " + old.toString() + "\n";
                                }
                            } else {
                                ourMap.put(subKlasTr.getTeacher().getId(), subKlasTr);
                            }
                        }
                    }
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                } while (true);
            }

        }
        if (kk.isEmpty()) {
            kk = "MAKOROKOTO NO CLASHES";
        }
        return kk;
    }

    private void fillingIntoSlots(int day, ArrayList<PeriodsFilling> arrLis) {
        //System.out.println("_________________________________________ fillingIntoSlots UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU entry____________________________________________________________");
        ArrayList<Integer> loggedPeriods = new ArrayList<>();
        for (int cou = 0; cou < arrLis.size(); cou++) {
            PeriodsFilling arrLi = arrLis.get(cou);
            ArrayList<Integer> pp = arrLi.getPossiblePeriods();
            Collections.sort(loggedPeriods);
            for (int i = 0; i < pp.size(); i++) {
                int pp1 = pp.get(i);
                if (Collections.binarySearch(loggedPeriods, pp1) >= 0) {// position occupied
                    pp.remove(i);//delete ele
                    i--;
                }
            }
            TeacherSubjectKlass rootTrSubKlas = arrLi.getRootTrSubKlas();
            ArrayList<Integer> validBlockTolog = getvalidBlockTolog(day, rootTrSubKlas, pp);
            if (validBlockTolog.isEmpty()) {
                System.out.println(rootTrSubKlas + "               valid block to log is empty ==================================================================");
                continue;
            }
            List<TeacherSubjectKlass> sharedSubject = rootTrSubKlas.getSharedSubject();
            // this.getEffectiveSubKlasTr(rootTrSubKlas);
            System.out.println(rootTrSubKlas + "  " + validBlockTolog + " LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL ");
            for (TeacherSubjectKlass subKlasTr : sharedSubject) {
                ArrayList<Slot> listSlots = this.findSlots(day, validBlockTolog, subKlasTr.getKlass());
                this.insertSlots(listSlots, subKlasTr.getCombinedSubject(), day);
            }
            validBlockTolog.stream().forEach((vbt) -> {
                loggedPeriods.add(vbt);
            });
        }
        //System.out.println("_______________________________ fillingIntoSlots EXIT______________________________________________________________________");
    }

    public void insertSlots(ArrayList<Slot> slots, List<TeacherSubjectKlass> trSubKlasList, int day) {
        // System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<insertSlot ENTRY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (Slot slt : slots) {
            slt.setTrSubKlasList(trSubKlasList);
            slt.saveSlot();
        }
        for (TeacherSubjectKlass trSubKlas : trSubKlasList) {
            this.logSubjectConstrains(trSubKlas.getKlass(), trSubKlas.getSubject());
            Subject_constrainsOnTheFly subCon = getSubject_constrains().get(trSubKlas.getKlass().getId() + trSubKlas.getSubject().getCode());
            subCon.addToWeeklyNumLessonLoged(slots.size());
            this.getKlassDaySubLoged().put(day + "" + trSubKlas.getKlass().getId() + trSubKlas.getSubject().getCode(), 0);
            if (subCon.isGreatest(slots.size()) && !subCon.isGreatestLoged()) {
                subCon.setGreatestLoged(true);
            }
        }
        //System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< insertSlot eXIT>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    public ArrayList<Slot> findSlots(int day, ArrayList<Integer> periods, Klass klass) {
        int blokSize = periods.size();
        // System.out.println(" =======================  Entry findSlot ==========================================================");
        ArrayList<Slot> slts = new ArrayList<>();
        int i = 0;
        Slot slt = this.findSlot(day, periods.get(i), klass);
        do {
            if (slt.getPeriodi() == periods.get(i)) {
                slts.add(slt);
            }
            if (slt.hasNext() && i < blokSize - 1) {
                slt = slt.getNext();
                i++;
            } else {
                break;
            }
        } while (true);
        //System.out.println(" ======================= EXIT findSlot ==========================================================");
        return slts;
    }

    public Slot findSlot(int day, int period, Klass klass) {
        ArrayList<Slot> dayTimeTable = this.getSchoolTimetable().get(day);
        for (Slot slt : dayTimeTable) {
            if (!Objects.equals(slt.getKlass().getId(), klass.getId())) {
                continue;
            }
            do {
                if (slt.getPeriodi() == period) {
                    return slt;
                }
                if (slt.hasNext()) {
                    slt = slt.getNext();
                } else {
                    break;
                }
            } while (true);
        }
        return null;
    }
//findSlot(int day, int period, Klass klass)

    private ArrayList<Integer> getvalidBlockTolog(int day, TeacherSubjectKlass rootTrSubKlas, ArrayList<Integer> pp) {
        Subject_constrainsOnTheFly sbc = subject_constrains.get(rootTrSubKlas.getKlass().getId() + rootTrSubKlas.getSubject().getCode());
        if (sbc.getNumLessonLoged() < sbc.getNumPeriods()) {
            System.out.println(sbc.toString());
            List<Integer> preferedDuration = sbc.getPreferedDuration();
            int diff = sbc.getNumPeriods() - sbc.getNumLessonLoged();
            for (int i = preferedDuration.size() - 1; i >= 0; i--) {
                if (sbc.isGreatest(preferedDuration.get(i)) && sbc.isGreatestLoged() || diff < preferedDuration.get(i)) {
                    continue;
                }
                ArrayList<Integer> validBlock = this.getValidBlock(day, rootTrSubKlas.getKlass(), preferedDuration.get(i), pp);
                if (validBlock.size() == preferedDuration.get(i)) {
                    return validBlock;
                }
            }
        }
        return new ArrayList<>();
    }

    private ArrayList<Integer> getValidBlock(int day, Klass klass, int blockSize, ArrayList<Integer> pps) {
        for (int pp : pps) {
            Slot startSlt = this.findSlot(day, pp, klass);
            if (startSlt == null) {
                continue;
            }
            ArrayList<Integer> block = this.getBlock(startSlt, pps, blockSize);
            if (containsBreakLunchOverlap(block)) {
                continue;
            }
            if (block.size() == blockSize) {
                return block;
            }
        }
        return new ArrayList<>();
    }

    public boolean containsBreakLunchOverlap(ArrayList<Integer> block) {
        Collections.sort(block);
        for (int i = 0; i < block.size(); i++) {
            if (block.get(i) == TimeTable.getPeriodsB4Brk() || block.get(i) == TimeTable.getPeriodsB4Lnch()) {
                if (i != block.size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Integer> getBlock(Slot startSlt, ArrayList<Integer> pp1, int blockSize) {
        ArrayList<Integer> lis = new ArrayList<>();
        if (startSlt.getPeriodi() == 0 || !startSlt.getTrSubKlasList().isEmpty()) {
            return lis;
        }
        ArrayList<Integer> pp = new ArrayList<>();
        pp1.stream().forEach((pp11) -> {
            pp.add(pp11);
        });
        Collections.sort(pp);
        if (startSlt.getPeriodi() != 0) {
            lis.add(startSlt.getPeriodi());
            Slot curr = startSlt;
            while (curr.hasNext() && lis.size() < blockSize) {
                curr = curr.getNext();
                boolean isInPossiblePrds = Collections.binarySearch(pp, curr.getPeriodi()) >= 0;
                if (curr.getPeriodi() != 0 && isInPossiblePrds && curr.getTrSubKlasList().isEmpty()) {
                    if (this.hasBreakORLanchOvelap(lis.get(lis.size() - 1), curr.getPeriodi())) {
                        lis = new ArrayList<>();
                        break;
                    }
                    lis.add(curr.getPeriodi());
                } else {
                    lis = new ArrayList<>();
                    break;
                }
            }
        }
        Collections.sort(lis);
        return lis;

    }

    public boolean hasBreakORLanchOvelap(int pr1, int pr2) {
        int lower = pr1;
        int higher = pr2;
        if (pr1 > pr2) {
            lower = pr2;
            higher = pr1;
        }
        if (lower == TimeTable.getPeriodsB4Brk() || lower == TimeTable.getPeriodsAfterLnch()) {
            if (higher - lower == 1) { // not necessary
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getPossiblePeriods(int day, List<TeacherSubjectKlass> effectiveTrSubKlas) {
        // System.out.println("getPossiblePeriods =====================================entry ");
        ArrayList<Integer> lis = new ArrayList<>();
        ArrayList<Slot> singleDayTimeTable = this.getSchoolTimetable().get(day);
        Slot slt = singleDayTimeTable.get(0);
        do {
            if (slt.hasNext()) {
                slt = slt.getNext();
            }
            if (!slt.isClashing(effectiveTrSubKlas)) {
                lis.add(slt.getPeriodi());
            }
            if (!slt.hasNext()) {
                break;
            }
            System.out.println(slt.getDy() + " " + slt.getPeriodi());
        } while (true);
        Collections.sort(lis);
        //System.out.println("getPossiblePeriods XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        return lis;
    }

    public ArrayList<Integer> getFreePeriods(int day, List<TeacherSubjectKlass> effectiveTrSubKlas) {
        ArrayList<Integer> lis = new ArrayList<>();
        if (effectiveTrSubKlas.size() > 1 || effectiveTrSubKlas.isEmpty()) {
            return lis;
        }
        Klass klas = effectiveTrSubKlas.get(0).getKlass();
        Slot slt = this.findSlot(day, 1, klas);
        if (slt == null) {
            return lis;
        }
        do {
            if (slt.isFree()) {
                lis.add(slt.getPeriodi());
            }
            if (slt.hasNext()) {
                slt = slt.getNext();
            } else {
                return lis;
            }
        } while (true);
    }

    public ArrayList<Integer> getPossibleAndFreePeriods(int day, List<TeacherSubjectKlass> effectiveTrSubKlas) {
        ArrayList<Integer> lis = new ArrayList<>();
        ArrayList<Slot> singleDayTimeTable = this.getSchoolTimetable().get(day);
        Slot slt = singleDayTimeTable.get(0);
        do {
            if (slt.hasNext()) {
                slt = slt.getNext();
            }
            if (!slt.isClashingOrOccupied(effectiveTrSubKlas)) {
                lis.add(slt.getPeriodi());
            }
            if (!slt.hasNext()) {
                break;
            }
            //System.out.println("getPossibleAndFreePeriods MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
        } while (true);
        Collections.sort(lis);
        return lis;
    }

    private void logSubjectConstrains(List<TeacherSubjectKlass> effectiveTrSubKlas) {
        for (TeacherSubjectKlass trSubKlas : effectiveTrSubKlas) {
            this.logSubjectConstrains(trSubKlas.getKlass(), trSubKlas.getSubject());
        }
    }

    private void logSubjectConstrains() {
        List<TeacherSubjectKlass> teacherSubjectKlass = TeacherSubjectKlass.getTeacherSubjectKlass();
        this.logSubjectConstrains(teacherSubjectKlass);
    }

    private void logSubjectConstrains(Klass klass, Subject sub) {
        // System.out.println("IN logSubjectConstrains HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        if (!getSubject_constrains().containsKey(klass.getId() + "" + sub.getCode())) {
            String qry = "from SubjectConstrainEntity where klass=" + klass.getId() + " and subject = '" + sub.getCode() + "'";
            try (Session session = HibernateCentre.getHibernateSession();) {
                //System.out.println(qry);
                SubjectConstrainEntity subConE = (SubjectConstrainEntity) session.createQuery(qry).uniqueResult();
                if (subConE == null) {
                    //System.out.println(klass + " " + sub + " not logged  hence given default values");
                    subConE = new SubjectConstrainEntity(sub, klass, 6);
                }
                if (subConE.getPreferedDuration() == null || subConE.getPreferedDuration().isEmpty()) {
                    subConE.setPreferedDuration(new ArrayList<>(Arrays.asList(1, 2)));
                    session.saveOrUpdate(subConE);
                }
                List<Integer> preferedDuration = subConE.getPreferedDuration();
                Subject_constrainsOnTheFly subConOnFly = new Subject_constrainsOnTheFly(subConE, preferedDuration, 0);
                getSubject_constrains().put(klass.getId() + sub.getCode(), subConOnFly);
            }

        }
        // System.out.println("OUT logSubjectConstrains JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");
    }

    public void setKlassDays() {
        ObservableList<Klass> klasses = this.getKlasses();
        if (klasses == null || klasses.isEmpty()) {
            return;
        }
        Collections.shuffle(klasses);
        Collections.shuffle(klasses);
        klasses.stream().forEach((klas) -> {
            ArrayList<Integer> days = new ArrayList<>();
            for (int day = 1; day <= TimeTable.getCycleSize(); day++) {
                days.add(day);
            }
            Collections.shuffle(days);

            klassDays.put(klas, days);
        });
    }

    public HashMap<Klass, ArrayList<Integer>> getKlassDays() {
        return klassDays;
    }

    public void setSchoolTimetable(HashMap<Integer, ArrayList<Slot>> schoolTimetable) {
        this.schoolTimetable = schoolTimetable;
    }

    /**
     * @return the cycleSize
     */
    public static int getCycleSize() {
        return cycleSize;
    }

    /**
     * @return the numPeriods
     */
    public static int getNumPeriods() {
        return numPeriods;
    }

    /**
     * @return the periodsB4Brk
     */
    public static int getPeriodsB4Brk() {
        return periodsB4Brk;
    }

    /**
     * @return the periodsB4Lnch
     */
    public static int getPeriodsB4Lnch() {
        return periodsB4Lnch;
    }

    /**
     * @return the periodsAfterLnch
     */
    public static int getPeriodsAfterLnch() {
        return periodsAfterLnch;
    }

    /**
     * @return the lesssonDuration
     */
    public static int getLesssonDuration() {
        return lesssonDuration;
    }

    /**
     * @return the brkDuration
     */
    public static int getBrkDuration() {
        return brkDuration;
    }

    /**
     * @return the lnchDuration
     */
    public static int getLnchDuration() {
        return lnchDuration;
    }

    /**
     * @return the startTime
     */
    public static LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return the brkTime
     */
    public static LocalTime getBrkTime() {
        return brkTime;
    }

    /**
     * @return the lnchTime
     */
    public static LocalTime getLnchTime() {
        return lnchTime;
    }

    /**
     * @return the sportsDays
     */
    public static List<SportsDay> getSportsDays() {
        return sportsDays;
    }

    /**
     * @param aCycleSize the cycleSize to set
     */
    public static void setCycleSize(int aCycleSize) {
        cycleSize = aCycleSize;
    }

    /**
     * @param aNumPeriods the numPeriods to set
     */
    public static void setNumPeriods(int aNumPeriods) {
        numPeriods = aNumPeriods;
    }

    /**
     * @param aPeriodsB4Brk the periodsB4Brk to set
     */
    public static void setPeriodsB4Brk(int aPeriodsB4Brk) {
        periodsB4Brk = aPeriodsB4Brk;
    }

    /**
     * @param aPeriodsB4Lnch the periodsB4Lnch to set
     */
    public static void setPeriodsB4Lnch(int aPeriodsB4Lnch) {
        periodsB4Lnch = aPeriodsB4Lnch;
    }

    /**
     * @param aPeriodsAfterLnch the periodsAfterLnch to set
     */
    public static void setPeriodsAfterLnch(int aPeriodsAfterLnch) {
        periodsAfterLnch = aPeriodsAfterLnch;
    }

    /**
     * @param aLesssonDuration the lesssonDuration to set
     */
    public static void setLesssonDuration(int aLesssonDuration) {
        lesssonDuration = aLesssonDuration;
    }

    /**
     * @param aBrkDuration the brkDuration to set
     */
    public static void setBrkDuration(int aBrkDuration) {
        brkDuration = aBrkDuration;
    }

    /**
     * @param aLnchDuration the lnchDuration to set
     */
    public static void setLnchDuration(int aLnchDuration) {
        lnchDuration = aLnchDuration;
    }

    /**
     * @param aStartTime the startTime to set
     */
    public static void setStartTime(LocalTime aStartTime) {
        startTime = aStartTime;
    }

    /**
     * @param aBrkTime the brkTime to set
     */
    public static void setBrkTime(LocalTime aBrkTime) {
        brkTime = aBrkTime;
    }

    /**
     * @param aLnchTime the lnchTime to set
     */
    public static void setLnchTime(LocalTime aLnchTime) {
        lnchTime = aLnchTime;
    }

    /**
     * @param aSportsDays the sportsDays to set
     */
    public static void setSportsDays(List<SportsDay> aSportsDays) {
        sportsDays = aSportsDays;
    }

    /**
     * @return the slot
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * @param slot the slot to set
     */
    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    /**
     * @return the schoolTimetable
     */
    public HashMap<Integer, ArrayList<Slot>> getSchoolTimetable() {
        return schoolTimetable;
    }

    /**
     * @return the subject_constrains
     */
    public HashMap<String, Subject_constrainsOnTheFly> getSubject_constrains() {
        return subject_constrains;
    }

    /**
     * @param subject_constrains the subject_constrains to set
     */
    public void setSubject_constrains(HashMap<String, Subject_constrainsOnTheFly> subject_constrains) {
        this.subject_constrains = subject_constrains;
    }

    /**
     * @return the klassDaySubLoged
     */
    public HashMap<String, Integer> getKlassDaySubLoged() {
        return klassDaySubLoged;
    }

    /**
     * @param klassDaySubLoged the klassDaySubLoged to set
     */
    public void setKlassDaySubLoged(HashMap<String, Integer> klassDaySubLoged) {
        this.klassDaySubLoged = klassDaySubLoged;
    }

}
