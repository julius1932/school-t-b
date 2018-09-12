/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schooltimetabling;

/**
 *
 * @author cherutombo
 */
public class ShortageLog {

    private String numLessonLoged;
    private String subject;
    private String klass;
    private String numPeriods;
    
    public ShortageLog(Subject_constrainsOnTheFly subCon){
        this.numLessonLoged=subCon.getNumLessonLoged()+"";
        this.numPeriods=subCon.getNumPeriods()+"";
        this.subject=subCon.getSubject().getCode();
        this.klass=subCon.getKlass().toString();
    }

    /**
     * @return the numLessonLoged
     */
    public String getNumLessonLoged() {
        return numLessonLoged;
    }

    /**
     * @param numLessonLoged the numLessonLoged to set
     */
    public void setNumLessonLoged(String numLessonLoged) {
        this.numLessonLoged = numLessonLoged;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the klass
     */
    public String getKlass() {
        return klass;
    }

    /**
     * @param klass the klass to set
     */
    public void setKlass(String klass) {
        this.klass = klass;
    }

    /**
     * @return the numPeriods
     */
    public String getNumPeriods() {
        return numPeriods;
    }

    /**
     * @param numPeriods the numPeriods to set
     */
    public void setNumPeriods(String numPeriods) {
        this.numPeriods = numPeriods;
    }

}
