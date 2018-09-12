/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
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
@Table(name = "CombinedSubjects", uniqueConstraints = @UniqueConstraint(columnNames = {"trSubKlas"}))
public class CombinedSubjects implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "trSubKlas")
    private TeacherSubjectKlass trSubKlas;
    private String ref; // Combined sub share same ref

    public CombinedSubjects() {

    }

    public CombinedSubjects(TeacherSubjectKlass teacherSubjectKlass, String ref) {
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
                List list = session.createQuery("from CombinedSubjects where ref ='" + uniqueID + "'").list();
                if (list.isEmpty()) {
                    return uniqueID;
                }
            }
        }
        return "";
    }

    public static CombinedSubjects getCombinedSubject(TeacherSubjectKlass teacherSubjectKlass) {
        try (Session session = HibernateCentre.getHibernateSession();) {
            CombinedSubjects combined = (CombinedSubjects) session.createQuery("from CombinedSubjects where trSubKlas ='" + teacherSubjectKlass.getId() + "'").uniqueResult();
            return combined;
        }
    }

    public static void saveCombinedSubject(TeacherSubjectKlass teacherSubjectKlass, String ref) {
        try (Session session = HibernateCentre.getHibernateSession();) {
            CombinedSubjects combined = (CombinedSubjects) session.createQuery("from CombinedSubjects where trSubKlas ='" + teacherSubjectKlass.getId() + "'").uniqueResult();
            if (combined == null) {
                combined = new CombinedSubjects();
                combined.setTrSubKlas(teacherSubjectKlass);
            }
            combined.setRef(ref);
            session.beginTransaction();
            session.saveOrUpdate(combined);
            session.getTransaction().commit();
            session.close();
        }
    }
}
