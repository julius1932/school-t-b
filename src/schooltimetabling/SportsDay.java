/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
@Entity
@Table(name = "SportsDay", uniqueConstraints = @UniqueConstraint(columnNames = {"dy"}))
public class SportsDay implements Serializable {

    private int dy;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SportsDay() {

    }

    public SportsDay(int day) {
        this.dy = day;
    }

    /**
     * @return the day
     */
    public int getDy() {
        return dy;
    }

    /**
     * @param day the day to set
     */
    public void setDy(int day) {
        this.dy = day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static SportsDay getSportsDay(int day) {
        try (Session session = HibernateCentre.getHibernateSession();) {
            SportsDay sportsDay = (SportsDay) session.createQuery("from SportsDay where dy=" + day).uniqueResult();
            if (sportsDay == null) {
                sportsDay = new SportsDay(day);
            }
            session.saveOrUpdate(sportsDay);
            return sportsDay;
        }
    }
}
