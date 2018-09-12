/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
@Entity
@Table(name = "Subject", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Subject implements Serializable {

    @Id
    private String code;
    private String name;

    public Subject() {

    }

    public Subject(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public boolean isValid() {
        if (this.code.isEmpty() || this.name.isEmpty()) {
            return false;
        }
        if (this.code.length() > 8) {
            return false;
        }
        if (this.name.length() > 20) {
            return false;
        }
        return true;
    }

    public static ObservableList<Subject> getAllSubjects() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<Subject> subject = FXCollections.observableArrayList(session.createQuery("from Subject order by name").list());
            return subject;
        }
    }
}
