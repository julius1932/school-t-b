/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
@Entity
@Table(name = "Slot", uniqueConstraints = @UniqueConstraint(columnNames = {"dy","ref", "klass", "periodi"}))
public class Slot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        private int dy;
    private int periodi;
    @ManyToOne
    @JoinColumn(name = "klass")
    private Klass klass;
    @ManyToMany
    private List<TeacherSubjectKlass> trSubKlasList;
    @Transient
    private Slot prev;
    @Transient
    private Slot next;
    @Transient
    private Slot down;
    @Transient
    private Slot up;
    @ManyToOne
    @JoinColumn(name = "ref")
    private TimeTableSaved ref;

    public Slot() {
    }

    public Slot(int period, Klass klass) {
        this.periodi = period;
        this.klass = klass;
        trSubKlasList = new ArrayList<>();
    }

    public Slot(int period, Klass klass, int day) {
        this(period, klass);
        this.dy = day;
        trSubKlasList = new ArrayList<>();
    }

    public void setSlot(TimeTableSaved reff) {      
        try (Session session = HibernateCentre.getHibernateSession();) {
            String qry="from Slot where klass="+this.getKlass().getId()+" and dy="+this.getDy()+" and periodi="+this.getPeriodi()+" and ref="+reff.getId();
            Slot slt=(Slot) session.createQuery(qry).uniqueResult();
            if(slt==null){
               return; 
            }
            this.setRef(reff);
            List<TeacherSubjectKlass> trSubKlassZ = slt.getTrSubKlasList();
            System.out.println((trSubKlassZ==null)+"  ============================================= ");
            if(trSubKlassZ!=null && !trSubKlassZ.isEmpty()){
                
              this.setTrSubKlasList(trSubKlassZ);
            }
            this.setId(slt.getId());
        }
    }

    public boolean hasPrev() {
        return this.prev != null;
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public boolean hasDown() {
        return this.down != null;
    }

    public boolean hasUp() {
        return this.up != null;
    }

    /**
     * @return the periodi
     */
    public int getPeriodi() {
        return periodi;
    }

    /**
     * @param periodi the periodi to set
     */
    public void setPeriodi(int periodi) {
        this.periodi = periodi;
    }

    /**
     * @return the prev
     */
    public Slot getPrev() {
        return prev;
    }

    /**
     * @return the next
     */
    public Slot getNext() {
        return next;
    }

    /**
     * @return the down
     */
    public Slot getDown() {
        return down;
    }

    /**
     * @return the up
     */
    public Slot getUp() {
        return up;
    }

    /**
     * @param prev the prev to set
     */
    public void setPrev(Slot prev) {
        this.prev = prev;
    }

    /**
     * @param next the next to set
     */
    public void setNext(Slot next) {
        this.next = next;
    }

    /**
     * @param down the down to set
     */
    public void setDown(Slot down) {
        this.down = down;
    }

    /**
     * @param up the up to set
     */
    public void setUp(Slot up) {
        this.up = up;
    }

    /**
     * @return the dy
     */
    public int getDy() {
        return dy;
    }

    /**
     * @param dy the dy to set
     */
    public void setDy(int dy) {
        this.dy = dy;
    }

    /**
     * @return the trSubKlasList
     */
    public List<TeacherSubjectKlass> getTrSubKlasList() {
        return trSubKlasList;
    }

    /**
     * @param trSubKlasList the trSubKlasList to set
     */
    public void setTrSubKlasList(List<TeacherSubjectKlass> trSubKlasList) {
        this.trSubKlasList = trSubKlasList;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String toStringKlass() {
        if (this.getPeriodi() == 0) {
            return "DAY " + this.getDy();
        }
        if (this.isSportsPeriod()) {
            return "SPORTS";
        }
        String st = "";
        int k = 0;
        for (TeacherSubjectKlass trSbKls : this.getTrSubKlasList()) {
            st += trSbKls.getSubject().getCode() + " ";
            if (k < this.getTrSubKlasList().size() - 1) {
                st += ",";
            }
            k++;
        }
        if(st.trim().isEmpty()){
            st="STUDY";
        }    
        return st;
    }

    public String toStringTr() {
        String st = "";
        for (TeacherSubjectKlass trSbKls : this.getTrSubKlasList()) {
            st += trSbKls.getSubject().getCode() + " " + trSbKls.getKlass().toString();
        }
        return st;
    }

    @Override
    public String toString() {
        if (this.getPeriodi() == 0) {
            if (MasterTimeTableDisplay.getType() == 1) {
                return this.getKlass().toString() + " ";
            } else if (MasterTimeTableDisplay.getType() == 2) {
                return "DAY " + this.getDy();
            }
            return "" + this.getKlass().toString();
        } else {
            if (!this.getTrSubKlasList().isEmpty()) {
                return this.toStringKlass();
            } else {

                if (this.isSportsPeriod()) {
//                   System.out.println(TimeTable.getSportsDays()+" iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
                    return "sports";
                }
                return " ";//+this.getKlass()+" "+this.getPeriodi();
            }
        }

    }

    public boolean hasThisSubjectOnThisDay(Subject sub) {
        Slot curr = this;
        if (curr.getPeriodi() == 0) {
            curr = curr.getNext();
        }
        do {
            List<TeacherSubjectKlass> trSubKlasList1 = curr.getTrSubKlasList();
            for (TeacherSubjectKlass subKlTr : trSubKlasList1) {
                if (subKlTr.getSubject().getCode().equals(sub.getCode())) {
                    return true;
                }
            }
            if (curr.hasNext()) {
                curr = curr.getNext();
            } else {
                break;
            }
        } while (true);
        return false;
    }

    public boolean isSportsPeriod() {
        if (this.isSportsDay()) {
            if (this.getPeriodi() > TimeTable.getPeriodsB4Lnch()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSportsDay() {
        for (SportsDay srpD : TimeTable.getSportsDays()) {
            if (srpD.getDy() == this.getDy()) {
                return true;
            }
        }
        return false;
    }

    public String toStringCdis() {
        if (this.getPeriodi() == 0) {
            return this.getKlass().toString() + " ";
        } else {
            if (!this.getTrSubKlasList().isEmpty()) {
                return this.getPeriodi() + " " + this.toStringKlass();
            } else {
                return this.getPeriodi() + " ";
            }
        }

    }

    public Slot getClashingSlot(List<TeacherSubjectKlass> trSubKlasToInsert) {
        // System.out.println("isClashing EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        List<Long> arrTrsToPut = new ArrayList<>();
        for (TeacherSubjectKlass ele : trSubKlasToInsert) {
            arrTrsToPut.add(ele.getTeacher().getId());
        }
        Collections.sort(arrTrsToPut);
        for (int i = 0; i < 2; i++) {
            Slot curr = this;
            do {
                List<TeacherSubjectKlass> list = curr.getTrSubKlasList();
                if (list != null && !list.isEmpty()) {
                    for (TeacherSubjectKlass ele : list) {
                        if (Collections.binarySearch(arrTrsToPut, ele.getTeacher().getId()) >= 0) {
                            // System.out.println("isClashing HXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
                            return curr;
                        }
                    }
                }
                if (i == 0) {
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                } else {
                    if (curr.hasUp()) {
                        curr = curr.getUp();
                    } else {
                        break;
                    }
                }
            } while (true);
        }
        //System.out.println("isClashing XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
        return null;
    }

    public boolean exchangeSlot(Slot slt2) {
        Slot slt1 = this;
        boolean test1 = false;
        if (!slt1.isFree()) {
            if (!slt2.isClashing(slt1.getTrSubKlasList())) {
                test1 = true;
            }
        } else {
            test1 = true;
        }
        boolean test2 = false;
        if (!slt2.isFree()) {
            if (!slt1.isClashing(slt2.getTrSubKlasList())) {
                test2 = true;
            }
        } else {
            test2 = true;
        }
        if (test1 && test2) {
            List<TeacherSubjectKlass> temp = slt1.getTrSubKlasList();
            slt1.setTrSubKlasList(slt2.getTrSubKlasList());
            slt2.setTrSubKlasList(temp);
            return true;
        } else {
            return false;
        }
    }
    public void saveSlot(){
        try (Session session = HibernateCentre.getHibernateSession();) {
            if(this.getRef()==null){
                return;
            }
            TimeTableSaved rf = session.get(TimeTableSaved.class, this.getRef().getId());
            if (rf != null) {
                session.saveOrUpdate(this);
            }
        }
    }
    public boolean containsSharedORCombined() {
        Slot slt = this;
        return slt.getTrSubKlasList().size() > 1;
    }

    public boolean containsSharedORCombinedOrDouble() {
        Slot slt = this;
        if (slt.getTrSubKlasList().size() > 1) {
            return true;
        }
        if (slt.getTrSubKlasList().isEmpty()) {
            return false;
        }
        TeacherSubjectKlass sbCl = slt.getTrSubKlasList().get(0);
        if (sbCl.getCombinedSubject().size() > 1 || sbCl.getSharedSubject().size() > 1) {
            return true;
        }
        Subject subject = sbCl.getSubject();
        for (int i = 0; i < 2; i++) {
            List<TeacherSubjectKlass> trSubKlasList1 = null;
            if (i == 0 && slt.hasNext()) {
                trSubKlasList1 = slt.getNext().getTrSubKlasList();
            }
            if (i == 1 && slt.hasPrev()) {
                trSubKlasList1 = slt.getPrev().getTrSubKlasList();
            }
            if (trSubKlasList1 != null && !trSubKlasList1.isEmpty()) {
                for (TeacherSubjectKlass trSubK : trSubKlasList1) {
                    if (trSubK.getSubject().getCode().equals(subject.getCode())) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public boolean isClashing(List<TeacherSubjectKlass> trSubKlasToInsert) {
        // System.out.println("isClashing EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        if (this.isSportsPeriod()) {
            return true;
        }
        List<Long> arrTrsToPut = new ArrayList<>();
        for (TeacherSubjectKlass ele : trSubKlasToInsert) {
            arrTrsToPut.add(ele.getTeacher().getId());
        }
        Collections.sort(arrTrsToPut);
        for (int i = 0; i < 2; i++) {
            Slot curr = this;
            do {
                List<TeacherSubjectKlass> list = curr.getTrSubKlasList();
                if (list != null && !list.isEmpty()) {
                    for (TeacherSubjectKlass ele : list) {
                        if (Collections.binarySearch(arrTrsToPut, ele.getTeacher().getId()) >= 0) {
                            // System.out.println("isClashing HXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
                            return true;
                        }
                    }
                }
                if (i == 0) {
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                } else {
                    if (curr.hasUp()) {
                        curr = curr.getUp();
                    } else {
                        break;
                    }
                }
            } while (true);
        }
        //System.out.println("isClashing XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ");
        return false;
    }

    public boolean isClashingOrOccupied(List<TeacherSubjectKlass> trSubKlasToInsert) {
        if (this.isSportsPeriod()) {
            return true;
        }
        HashMap<Long, Integer> arrTrsToPut = new HashMap<>();
        HashMap<Long, Integer> klassesToPut = new HashMap<>();
        for (TeacherSubjectKlass ele : trSubKlasToInsert) {
            arrTrsToPut.put(ele.getTeacher().getId(), 0);
            klassesToPut.put(ele.getKlass().getId(), 0);
        }
        for (int i = 0; i < 2; i++) {
            Slot curr = this;
            do {

                List<TeacherSubjectKlass> list = curr.getTrSubKlasList();
                if (list != null && !list.isEmpty()) {
                    if (klassesToPut.get(curr.getKlass().getId()) != null) {
                        if (!curr.isFree()) {
                            return true;
                        }
                    }
                    for (TeacherSubjectKlass ele : list) {
                        if (arrTrsToPut.get(ele.getTeacher().getId()) != null) {
                            return true;
                        }
                    }
                }
                if (i == 0) {
                    if (curr.hasDown()) {
                        curr = curr.getDown();
                    } else {
                        break;
                    }
                } else {
                    if (curr.hasUp()) {
                        curr = curr.getUp();
                    } else {
                        break;
                    }
                }
            } while (true);
        }
        return false;
    }

    public boolean isFree() {
        if (this.getPeriodi() == 0) {
            return false;
        }
        if (this.isSportsPeriod()) {
            return false;
        }
        if (this.getTrSubKlasList() == null || this.getTrSubKlasList().isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Teacher> getTrs() {
        List<Teacher> trs = new ArrayList<>();
        for (TeacherSubjectKlass trSbKls : this.getTrSubKlasList()) {
            trs.add(trSbKls.getTeacher());
        }
        return trs;
    }

    /**
     * @return the klass
     */
    public Klass getKlass() {
        return klass;
    }

    /**
     * @param klass the klass to set
     */
    public void setKlass(Klass klass) {
        this.klass = klass;
    }

    /**
     * @return the ref
     */
    public TimeTableSaved getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(TimeTableSaved ref) {
        this.ref = ref;
    }

}
