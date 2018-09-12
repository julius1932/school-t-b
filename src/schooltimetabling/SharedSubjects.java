/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javafx.collections.ObservableList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
@Entity
@Table(name = "SharedSubjects", uniqueConstraints = @UniqueConstraint(columnNames = {"trSubKlas"}))
public class SharedSubjects implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "trSubKlas")
    private TeacherSubjectKlass trSubKlas;
    private String ref; // shared sub share same ref

    public SharedSubjects() {

    }

    public SharedSubjects(TeacherSubjectKlass teacherSubjectKlass, String ref) {
        this.trSubKlas = teacherSubjectKlass;
        this.ref = ref;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the trSubKlas
     */
    public TeacherSubjectKlass getTrSubKlas() {
        return trSubKlas;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param teacherSubjectKlass the trSubKlas to set
     */
    public void setTrSubKlas(TeacherSubjectKlass teacherSubjectKlass) {
        this.trSubKlas = teacherSubjectKlass;
    }

    /**
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    public static String generateUniqRef() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            for (int i = 0; i < 100; i++) {
                String uniqueID = UUID.randomUUID().toString();
                List list = session.createQuery("from SharedSubjects where ref ='" + uniqueID + "'").list();
                if (list.isEmpty()) {
                    return uniqueID;
                }
            }
        }
        return "";
    }

    public static void saveCombinedSubject(ObservableList<TeacherSubjectKlass> selectedItems) {
        try (Session session = HibernateCentre.getHibernateSession();) {
            String ref = SharedSubjects.generateUniqRef();
            session.beginTransaction();
            for (TeacherSubjectKlass teacherSubjectKlass : selectedItems) {
                SharedSubjects shared = (SharedSubjects) session.createQuery("from SharedSubjects where trSubKlas ='" + teacherSubjectKlass.getId() + "'").uniqueResult();
                if (shared == null) {
                    shared = new SharedSubjects();
                    shared.setTrSubKlas(teacherSubjectKlass);
                }
                shared.setRef(ref);
                session.saveOrUpdate(shared);
            }
            session.getTransaction().commit();
            session.close();
        }
    }

    public static boolean validateSharedSubjects(ObservableList<TeacherSubjectKlass> selectedItems) {
        SubjectConstrainEntity pulledSubjectConstrainEntity1 = SubjectConstrainEntity.pullSubjectConstrainEntity(selectedItems.get(0));
        if (pulledSubjectConstrainEntity1 == null) {
            System.out.println("no  subcon for " + selectedItems.get(0));
            return false;
        }
        for (TeacherSubjectKlass subKls : selectedItems) {
            SubjectConstrainEntity pulledSubjectConstrainEntity = SubjectConstrainEntity.pullSubjectConstrainEntity(subKls);
            if (pulledSubjectConstrainEntity == null) {
                System.out.println("no  subcon for " + subKls);
                return false;
            }
            if (!pulledSubjectConstrainEntity1.hasSameConstrains(pulledSubjectConstrainEntity)) {
                return false;
            }
        }
        return true;
    }
}
