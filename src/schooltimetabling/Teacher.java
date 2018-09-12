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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
@Entity

public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String name;
    private String surname;
    private String phone;

    public Teacher() {
    }

    public Teacher(String title, String name, String surname) {
        this.name = name;
        this.title = title;
        this.surname = surname;
       
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param ecNo the id to set
     */
    public void setId(Long ecNo) {
        this.id = ecNo;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return this.surname+" "+this.name;
        //return this.title +" "+this.surname+" "+this.name;
    }

    public boolean isTeachrtValid() {

        if (this.getSurname().trim().isEmpty()) {
            return false;
        }
        if (this.getName().trim().isEmpty()) {
            return false;
        }
        
        if (this.getTitle().trim().isEmpty()) {
            return false;
        }
        if (this.getTitle() == null) {
            return false;
        }
        return true;
    }
    
     public static ObservableList<Teacher> getAllTeachers() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<Teacher> trss = FXCollections.observableArrayList(session.createQuery("from Teacher order by  surname").list());
            return trss;
        }
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

 
   
}
