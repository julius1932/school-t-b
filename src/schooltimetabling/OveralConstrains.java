/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.Session;

/**
 *
 * @author Junta
 */
@Entity
public class OveralConstrains implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cycleSize;
    private int numPeriods;

    private int periodsB4Brk;
    private int periodsB4Lnch;
    private int periodsAfterLnch;

    private int lesssonDuration;
    private int brkDuration;
    private int lnchDuration;

    private LocalTime startTime;
    private LocalTime brkTime;
    private LocalTime lnchTime;
    @OneToMany
    private List<SportsDay> sportsDays;

    public OveralConstrains() {
        sportsDays = new ArrayList<>();
    }

    public boolean validateConstrains() {
        for (SportsDay spD : this.getSportsDays()) {
            if (spD.getDy() > cycleSize) {
                return false;
            }
        }

        if (this.periodsB4Brk >= this.periodsB4Lnch) {
            return false;
        }
        if (this.numPeriods != this.periodsB4Lnch + this.periodsAfterLnch) {
            return false;
        }
        return true;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the cycleSize
     */
    public int getCycleSize() {
        return cycleSize;
    }

    /**
     * @return the numPeriods
     */
    public int getNumPeriods() {
        return numPeriods;
    }

    /**
     * @return the periodsB4Brk
     */
    public int getPeriodsB4Brk() {
        return periodsB4Brk;
    }

    /**
     * @return the periodsB4Lnch
     */
    public int getPeriodsB4Lnch() {
        return periodsB4Lnch;
    }

    /**
     * @return the periodsAfterLnch
     */
    public int getPeriodsAfterLnch() {
        return periodsAfterLnch;
    }

    /**
     * @return the lesssonDuration
     */
    public int getLesssonDuration() {
        return lesssonDuration;
    }

    /**
     * @return the brkDuration
     */
    public int getBrkDuration() {
        return brkDuration;
    }

    /**
     * @return the lnchDuration
     */
    public int getLnchDuration() {
        return lnchDuration;
    }

    /**
     * @return the startTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return the brkTime
     */
    public LocalTime getBrkTime() {
        return brkTime;
    }

    /**
     * @return the lnchTime
     */
    public LocalTime getLnchTime() {
        return lnchTime;
    }

    /**
     * @return the sportsDays
     */
    public List<SportsDay> getSportsDays() {
        return sportsDays;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param cycleSize the cycleSize to set
     */
    public void setCycleSize(int cycleSize) {
        this.cycleSize = cycleSize;
    }

    /**
     * @param numPeriods the numPeriods to set
     */
    public void setNumPeriods(int numPeriods) {
        this.numPeriods = numPeriods;
    }

    /**
     * @param periodsB4Brk the periodsB4Brk to set
     */
    public void setPeriodsB4Brk(int periodsB4Brk) {
        this.periodsB4Brk = periodsB4Brk;
    }

    /**
     * @param periodsB4Lnch the periodsB4Lnch to set
     */
    public void setPeriodsB4Lnch(int periodsB4Lnch) {
        this.periodsB4Lnch = periodsB4Lnch;
    }

    /**
     * @param periodsAfterLnch the periodsAfterLnch to set
     */
    public void setPeriodsAfterLnch(int periodsAfterLnch) {
        this.periodsAfterLnch = periodsAfterLnch;
    }

    /**
     * @param lesssonDuration the lesssonDuration to set
     */
    public void setLesssonDuration(int lesssonDuration) {
        this.lesssonDuration = lesssonDuration;
    }

    /**
     * @param brkDuration the brkDuration to set
     */
    public void setBrkDuration(int brkDuration) {
        this.brkDuration = brkDuration;
    }

    /**
     * @param lnchDuration the lnchDuration to set
     */
    public void setLnchDuration(int lnchDuration) {
        this.lnchDuration = lnchDuration;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @param brkTime the brkTime to set
     */
    public void setBrkTime(LocalTime brkTime) {
        this.brkTime = brkTime;
    }

    /**
     * @param lnchTime the lnchTime to set
     */
    public void setLnchTime(LocalTime lnchTime) {
        this.lnchTime = lnchTime;
    }

    /**
     * @param sportsDays the sportsDays to set
     */
    public void setSportsDays(List<SportsDay> sportsDays) {
        this.sportsDays = sportsDays;
    }

    public static OveralConstrains getOveralConstrains() {
        try (Session session = HibernateCentre.getHibernateSession();) {
            ObservableList< OveralConstrains> overalConstrains = FXCollections.observableArrayList(session.createQuery("from  OveralConstrains").list());
            if (!overalConstrains.isEmpty()) {
                overalConstrains.get(0).getSportsDays();
                return overalConstrains.get(0);
            }
            return null;
        }
    }
}
