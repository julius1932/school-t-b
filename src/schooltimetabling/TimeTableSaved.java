/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author cherutombo
 */
@Entity
@Table(name = "TimeTableSaved", uniqueConstraints = @UniqueConstraint(columnNames = {"refDaf"}))
public class TimeTableSaved implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime refDaf;
    
    public TimeTableSaved(){
        this.refDaf=LocalDateTime.now();
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

    /**
     * @return the refDaf
     */
    public LocalDateTime getRefDaf() {
        return refDaf;
    }

    /**
     * @param refDaf the refDaf to set
     */
    public void setRefDaf(LocalDateTime refDaf) {
        this.refDaf = refDaf;
    }
    
}
