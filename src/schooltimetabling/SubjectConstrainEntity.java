/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
@Entity
@Table(name = "SubjectConstrainEntity", uniqueConstraints = @UniqueConstraint(columnNames = {"klass", "subject"}))
public class SubjectConstrainEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @ManyToOne
    @JoinColumn(name = "subject")
    protected Subject subject;
    @ManyToOne
    @JoinColumn(name = "klass")
    protected Klass klass;
    protected int numPeriods;
    @ElementCollection
    protected List<Integer> preferedDuration;

    public SubjectConstrainEntity() {
    }

    public SubjectConstrainEntity(Subject sub, Klass klas, int numP) {
        this.subject = sub;
        this.klass = klas;
        this.numPeriods = numP;
        this.preferedDuration=new ArrayList<>();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the subject
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * @return the klass
     */
    public Klass getKlass() {
        return klass;
    }

    /**
     * @return the numPeriods
     */
    public int getNumPeriods() {
        return numPeriods;
    }

    /**
     * @return the preferedDuration
     */
    public List<Integer> getPreferedDuration() {
        return preferedDuration;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * @param klass the klass to set
     */
    public void setKlass(Klass klass) {
        this.klass = klass;
    }

    /**
     * @param numPeriods the numPeriods to set
     */
    public void setNumPeriods(int numPeriods) {
        this.numPeriods = numPeriods;
    }

    /**
     * @param preferedDuration the preferedDuration to set
     */
    public void setPreferedDuration(List<Integer> preferedDuration) {
        this.preferedDuration = preferedDuration;
    }

    public static void saveSubjectConstrainEntity(SubjectConstrainEntity subCon) {
        SubjectConstrainEntity subContrain = subCon;
        try (Session session = HibernateCentre.getHibernateSession();) {
            SubjectConstrainEntity sbCn = (SubjectConstrainEntity) session.createQuery("from SubjectConstrainEntity where klass =" + subCon.getKlass().getId() + " and subject='" + subCon.getSubject().getCode() + "'").uniqueResult();
            if (sbCn == null) {
                if (!TeacherSubjectKlass.isInTeacherSubjectKlass(subCon.getSubject(), subCon.getKlass())) {
                    return;
                }
            } else {
                subContrain = sbCn;
                subContrain.setNumPeriods(subCon.getNumPeriods());
                subContrain.setPreferedDuration(subCon.getPreferedDuration());
            }
            session.beginTransaction();
            session.save(subContrain);
            session.getTransaction().commit();
        }
    }
     public static SubjectConstrainEntity pullSubjectConstrainEntity(TeacherSubjectKlass sbKlsTr) {
        return pullSubjectConstrainEntity(sbKlsTr.getSubject(),sbKlsTr.getKlass());
     }
    public static SubjectConstrainEntity pullSubjectConstrainEntity(Subject sub, Klass klas) {
        try (Session session = HibernateCentre.getHibernateSession();) {
            SubjectConstrainEntity dd = (SubjectConstrainEntity) session.createQuery("from SubjectConstrainEntity where klass =" + klas.getId() + " and subject='" + sub.getCode() + "'").uniqueResult();
            if (dd != null) {
                dd.getPreferedDuration();
            }
            return dd;
        }
    }

    public boolean hasSameConstrains(SubjectConstrainEntity subCon) {
        if (this.getNumPeriods() != subCon.getNumPeriods()) {
            System.out.println("Num preiods not equal");
            return false;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            SubjectConstrainEntity dd = (SubjectConstrainEntity) session.createQuery("from SubjectConstrainEntity where id =" + this.getId()).uniqueResult();
            SubjectConstrainEntity dd2 = (SubjectConstrainEntity) session.createQuery("from SubjectConstrainEntity where id =" + subCon.getId()).uniqueResult();
            if (dd != null && dd2 != null) {
                List<Integer> pullPreferedDuration = dd.getPreferedDuration();
                List<Integer> pullPreferedDuration1 = dd2.getPreferedDuration();
                if (pullPreferedDuration.isEmpty() || pullPreferedDuration1.isEmpty()) {
                    System.out.println("PreferedDuration empty");
                    return false;
                }
                if (pullPreferedDuration.size() != pullPreferedDuration1.size()) {
                    System.out.println(pullPreferedDuration.size() + " != " + pullPreferedDuration1.size());
                    System.out.println("PreferedDuration size not the same");
                    return false;
                }
                Collections.sort(pullPreferedDuration);
                Collections.sort(pullPreferedDuration1);
                for (int i = 0; i < pullPreferedDuration.size(); i++) {
                    if (pullPreferedDuration.get(i) != pullPreferedDuration1.get(i)) {
                        System.out.println("PreferedDuration  not the same in looop");
                        return false;
                    }
                }
                
            } else {
                System.out.println("null null");
                return false;
            }

        }

        return true;
    }

}
