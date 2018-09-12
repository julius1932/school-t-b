/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Junta
 */
public class Kubirira {

    private int duration;

    public Kubirira(int duration) {
        this.duration = duration;
    }

    public static List<Kubirira> getDataToDisplay(List<Integer> preferedDuration) {
        System.out.println(preferedDuration.size());
        List<Kubirira> kubaz = new ArrayList<>();
        for (int k : preferedDuration) {
             kubaz.add(new  Kubirira(k));
        }
        return kubaz;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
