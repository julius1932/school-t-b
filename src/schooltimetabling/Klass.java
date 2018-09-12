package schooltimetabling;

import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.Session;

/**
 * Created by Junta on 2/24/2017.
 */
@Entity
@Table(name = "klass", uniqueConstraints = @UniqueConstraint(columnNames = {"level", "division"}))
public class Klass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int level;
    private String division;
    
    public Klass() {
    }

    public Klass(int levl, String division) {
        this.level = levl;
        this.division = division;

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Override
    public String toString() {
        return "" + this.level + this.division;
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

    public static ObservableList<Klass> getAllLevel() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<Klass> klasses = FXCollections.observableArrayList(session.createQuery("from Klass ").list());
            return klasses;
        }
    }

   public static ObservableList<Klass> getAllKlasses() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<Klass> klasses = FXCollections.observableArrayList(session.createQuery("from Klass order by level,division").list());
            return klasses;
        }
    }
    public static ObservableList<Klass> getKlasses(ObservableList<Klass> klazzz) {
        String qry = "";
        int i = 0;
        for (Klass klas : klazzz) {
            if (i == 0) {
                qry += " Where ";
            }
            qry += " level = " + klas.level;
            if (i != klazzz.size() - 1) {
                qry += " or ";
            }
            i++;
        }
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList<Klass> klasses = FXCollections.observableArrayList(session.createQuery("from Klass " + qry).list());
            return klasses;
        }
    }

    
}
