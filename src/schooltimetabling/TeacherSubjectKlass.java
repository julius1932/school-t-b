/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
@Table(name = "TeacherSubjectKlass", uniqueConstraints = @UniqueConstraint(columnNames = {"subject", "klass"}))
public class TeacherSubjectKlass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "subject")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "klass")
    private Klass klass;
    @ManyToOne
    @JoinColumn(name = "teacher")
    private Teacher teacher;

    public TeacherSubjectKlass() {

    }

    public TeacherSubjectKlass(Teacher teacher, Subject sub, Klass klas) {
        this.teacher = teacher;
        this.subject = sub;
        this.klass = klas;
    }

    /**
     * @return the teacher
     */
    public Teacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher the teacher to set
     */
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return this.teacher.getSurname() + " , " + this.getSubject().getCode() + " , " + this.getKlass();
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

    public static TeacherSubjectKlass getTeacherSubjectKlass(Subject sub, Klass klas) {
        try (Session session = HibernateCentre.getHibernateSession()) {
            TeacherSubjectKlass thy = (TeacherSubjectKlass) session.createQuery(" from TeacherSubjectKlass where subject ='" + sub.getCode() + "'" + " and klass =" + klas.getId()).uniqueResult();
            return thy;
        }
    }

    public static boolean isInTeacherSubjectKlass(Subject sub, Klass klas) {
        try (Session session = HibernateCentre.getHibernateSession()) {
            TeacherSubjectKlass thy = (TeacherSubjectKlass) session.createQuery(" from TeacherSubjectKlass where subject ='" + sub.getCode() + "'" + " and klass =" + klas.getId()).uniqueResult();
            if (thy == null) {
                return false;
            }
            return true;
        }
    }

    public static List<TeacherSubjectKlass> getTeacherSubjectKlass() {
        try (Session session = HibernateCentre.getHibernateSession()) {
            List<TeacherSubjectKlass> thy = session.createQuery(" from TeacherSubjectKlass ").list();
            return thy;
        }
    }

    public boolean isSharedSubject() {
        try (Session session = HibernateCentre.getHibernateSession()) {
            String query = "From SharedSubjects ss where ss.trSubKlas=" + this.getId();
            SharedSubjects ss = (SharedSubjects) session.createQuery(query).uniqueResult();
            if (ss == null) {
                return false;
            }
            return true;
        }
    }

    public List<TeacherSubjectKlass> getSharedSubject() {
        List<TeacherSubjectKlass> shared = new ArrayList<>();
        shared.add(this);
        try (Session session = HibernateCentre.getHibernateSession()) {
            String query = "From SharedSubjects ss where ss.trSubKlas=" + this.getId();
            SharedSubjects ss = (SharedSubjects) session.createQuery(query).uniqueResult();
            if (ss == null) {
                return shared;
            }
            return session.createQuery("select trSubKlas From SharedSubjects ss where ss.ref='" + ss.getRef() + "'").list();
        }
    }

    public boolean isCombinedSubject() {
        try (Session session = HibernateCentre.getHibernateSession()) {
            String query = "From CombinedSubjects sc where sc.trSubKlas=" + this.getId();
            CombinedSubjects ss = (CombinedSubjects) session.createQuery(query).uniqueResult();
            if (ss == null) {
                return false;
            }
            return true;
        }
    }

    public List<TeacherSubjectKlass> getCombinedSubject() {
        List<TeacherSubjectKlass> shared = new ArrayList<>();
        shared.add(this);
        try (Session session = HibernateCentre.getHibernateSession()) {
            String query = "From CombinedSubjects sc where sc.trSubKlas=" + this.getId();
            CombinedSubjects ss = (CombinedSubjects) session.createQuery(query).uniqueResult();
            if (ss == null) {
                return shared;
            }
            return session.createQuery(" select trSubKlas From CombinedSubjects cs where cs.ref='" + ss.getRef() + "'").list();
        }
    }

    public boolean isInList(List<TeacherSubjectKlass> effectiveSubKlasTr) {
        for (TeacherSubjectKlass subTrKl : effectiveSubKlasTr) {
            if (Objects.equals(this.getId(), subTrKl.getId())) {
                return true;
            }
        }
        return false;
    }
}
